package sfiomn.legendarysurvivaloverhaul.client.events;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CalculatePlayerTurnEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundSourceEvent;
import net.neoforged.neoforge.client.event.sound.PlayStreamingSourceEvent;
import net.neoforged.neoforge.client.event.sound.SoundEngineLoadEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbConditions;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbPenalty;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbRegion;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.event.LimbConditionsChangedEvent;
import sfiomn.legendarysurvivaloverhaul.client.sounds.HearingLoss;
import sfiomn.legendarysurvivaloverhaul.client.sounds.LimbPenaltyLoopSound;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

/**
 * Client side effects of the {@link LimbPenalty limb penalties}: what the player hears, and the inputs he can't do anymore. <br>
 * Every hook starts with a bit test on {@link #conditions}, the limb conditions of the local player cached here,
 * so per frame and per tick hooks cost nothing while no penalty is active.
 * <p>
 * Visual feedback (motion blur, ...) is not implemented yet.
 */
@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientLimbPenaltyHooks
{
    private static final float EAR_RINGING_VOLUME = 0.15f;
    private static final float HEARTBEAT_VOLUME = 0.6f;
    private static final float HEARTBEAT_FAST_VOLUME = 0.8f;
    private static final float SHORT_BREATH_VOLUME = 0.6f;
    // Ticks after joining a world during which no sound is played for the penalties the player already has
    private static final int JOIN_GRACE_TICKS = 40;

    private static int conditions = LimbConditions.NONE;
    private static LimbPenaltyLoopSound earRingingSound;
    private static LimbPenaltyLoopSound heartbeatSound;

    // Lazy mouse: rotation the mouse gave that the camera hasn't followed yet
    private static final double LAZY_MOUSE_SNAP_ANGLE = 60.0;
    private static boolean lazyMouseActive;
    private static float lastYRot;
    private static float lastXRot;
    private static double pendingYRot;
    private static double pendingXRot;
    private static long lastFrameNanos;

    /**
     * @return the limb conditions of the local player
     */
    public static int getConditions()
    {
        return conditions;
    }

    public static boolean has(LimbPenalty penalty)
    {
        return LimbConditions.has(conditions, penalty);
    }

    /**
     * @return the volume to play a limb penalty sound at so it stays audible while the hearing is muffled
     */
    public static float compensateHearing(float volume)
    {
        return HearingLoss.compensate(volume);
    }

    @SubscribeEvent
    public static void onLimbConditionsChanged(LimbConditionsChangedEvent event)
    {
        Player player = event.getPlayer();
        if (!event.isClientSide() || player != Minecraft.getInstance().player)
            return;

        conditions = event.getNewConditions();
        HearingLoss.update(conditions, player.tickCount >= JOIN_GRACE_TICKS && event.worsened(LimbRegion.HEAD));

        if (has(LimbPenalty.EAR_RINGING) && isStopped(earRingingSound))
        {
            // PLACEHOLDER SOUND: limb_ear_ringing
            earRingingSound = new LimbPenaltyLoopSound(SoundRegistry.LIMB_EAR_RINGING.get(), LimbPenalty.EAR_RINGING, EAR_RINGING_VOLUME);
            Minecraft.getInstance().getSoundManager().play(earRingingSound);
        }

        if (has(LimbPenalty.CONSTANT_HEARTBEAT) && isStopped(heartbeatSound))
        {
            // PLACEHOLDER SOUND: limb_heartbeat
            heartbeatSound = new LimbPenaltyLoopSound(SoundRegistry.LIMB_HEARTBEAT.get(), LimbPenalty.CONSTANT_HEARTBEAT, HEARTBEAT_VOLUME);
            Minecraft.getInstance().getSoundManager().play(heartbeatSound);
        }

        // One time sounds, not played for the penalties the player already has when joining
        if (player.tickCount < JOIN_GRACE_TICKS)
            return;

        // The constant heartbeat takes over once the torso is broken
        if (has(LimbPenalty.HEARTBEAT_ON_BREAK) && !has(LimbPenalty.CONSTANT_HEARTBEAT) && event.worsened(LimbRegion.TORSO))
        {
            // PLACEHOLDER SOUND: limb_heartbeat_fast
            playOnce(SoundRegistry.LIMB_HEARTBEAT_FAST.get(), HEARTBEAT_FAST_VOLUME);
        }

        if (event.gained(LimbPenalty.SHORTNESS_OF_BREATH))
        {
            // PLACEHOLDER SOUND: limb_short_breath
            playOnce(SoundRegistry.LIMB_SHORT_BREATH.get(), SHORT_BREATH_VOLUME);
        }

        // TODO visual feedback: motion blur while LimbPenalty.MOTION_BLUR is active
    }

    private static boolean isStopped(LimbPenaltyLoopSound sound)
    {
        return sound == null || sound.isStopped() || !Minecraft.getInstance().getSoundManager().isActive(sound);
    }

    private static void playOnce(SoundEvent soundEvent, float volume)
    {
        Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(soundEvent.getLocation(), SoundSource.PLAYERS,
                compensateHearing(volume), 1.0f, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, true));
    }

    private static void reset()
    {
        conditions = LimbConditions.NONE;
        earRingingSound = null;
        heartbeatSound = null;
        lazyMouseActive = false;
        HearingLoss.reset();
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event)
    {
        reset();
    }

    @SubscribeEvent
    public static void onRespawn(ClientPlayerNetworkEvent.Clone event)
    {
        // The new player entity gets its limb conditions announced again when its limb data is synced
        reset();
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        HearingLoss.tick();
    }

    /**
     * Fractured / broken head: the sounds starting are muffled.
     */
    @SubscribeEvent
    public static void onSoundSourcePlay(PlaySoundSourceEvent event)
    {
        HearingLoss.onSourcePlay(event.getSound(), event.getChannel());
    }

    @SubscribeEvent
    public static void onStreamingSoundSourcePlay(PlayStreamingSourceEvent event)
    {
        HearingLoss.onSourcePlay(event.getSound(), event.getChannel());
    }

    @SubscribeEvent
    public static void onScreenClosing(ScreenEvent.Closing event)
    {
        // The sound options set the master volume back while they are changed
        if (HearingLoss.isActive())
            HearingLoss.applyMasterVolume();
    }

    /**
     * Broken head: the mouse is a bit less sensitive.
     */
    @SubscribeEvent
    public static void onPlayerTurn(CalculatePlayerTurnEvent event)
    {
        if (!has(LimbPenalty.LAZY_MOUSE))
            return;

        event.setMouseSensitivity(event.getMouseSensitivity() * Config.Baked.lazyMouseSensitivityMultiplier);
    }

    /**
     * Broken head: the camera lags a little behind the mouse. <br>
     * The vanilla cinematic camera can't be made lighter, so the rotation the mouse just gave the player (applied right before
     * this event) is held back and released smoothly over the next frames.
     */
    @SubscribeEvent
    public static void onRenderFrame(RenderFrameEvent.Pre event)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if (!has(LimbPenalty.LAZY_MOUSE) || player == null || Config.Baked.lazyMouseSmoothing <= 0)
        {
            lazyMouseActive = false;
            return;
        }

        long now = Util.getNanos();
        float yRot = player.getYRot();
        float xRot = player.getXRot();
        if (!lazyMouseActive)
        {
            lazyMouseActive = true;
            pendingYRot = 0;
            pendingXRot = 0;
            lastYRot = yRot;
            lastXRot = xRot;
            lastFrameNanos = now;
            return;
        }

        double frameTime = (now - lastFrameNanos) / 1.0e9;
        lastFrameNanos = now;
        pendingYRot += yRot - lastYRot;
        pendingXRot = Mth.clamp(lastXRot + pendingXRot + (xRot - lastXRot), -90.0, 90.0) - lastXRot;

        // Teleports and respawns set the rotation, they aren't smoothed
        if (Math.abs(pendingYRot) > LAZY_MOUSE_SNAP_ANGLE || Math.abs(pendingXRot) > LAZY_MOUSE_SNAP_ANGLE)
        {
            pendingYRot = 0;
            pendingXRot = 0;
            lastYRot = yRot;
            lastXRot = xRot;
            return;
        }

        double released = 1.0 - Math.exp(-frameTime / Config.Baked.lazyMouseSmoothing);
        float newYRot = lastYRot + (float) (pendingYRot * released);
        float newXRot = lastXRot + (float) (pendingXRot * released);
        pendingYRot -= newYRot - lastYRot;
        pendingXRot -= newXRot - lastXRot;

        // Moved like Entity.turn does, so the interpolation stays smooth
        player.setYRot(newYRot);
        player.setXRot(newXRot);
        player.yRotO += newYRot - yRot;
        player.xRotO = Mth.clamp(player.xRotO + newXRot - xRot, -90.0f, 90.0f);
        lastYRot = newYRot;
        lastXRot = newXRot;
    }

    /**
     * Broken legs: can't sneak.
     */
    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event)
    {
        if (!has(LimbPenalty.NO_SNEAKING))
            return;

        Player player = event.getEntity();
        Input input = event.getInput();
        // Sneaking is still needed to leave a mount
        if (input.shiftKeyDown && !player.isPassenger() && !player.isCreative() && !player.isSpectator())
            input.shiftKeyDown = false;
    }

    /**
     * Broken arms: can't fast swim. The player still moves and swims up in water, but never in the swimming pose. <br>
     * The swimming pose starts from sprinting in water, which vanilla turns on after the movement inputs are read,
     * so it's turned off at the end of the tick, only while the penalty is active.
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event)
    {
        if (!has(LimbPenalty.NO_SWIMMING))
            return;

        Player player = event.getEntity();
        if (player == Minecraft.getInstance().player && player.isSprinting() && player.isInWater() && !player.isCreative() && !player.isSpectator())
            player.setSprinting(false);
    }

    @EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBusEvents
    {
        @SubscribeEvent
        public static void onSoundEngineLoad(SoundEngineLoadEvent event)
        {
            HearingLoss.onSoundEngineLoad();
        }
    }
}
