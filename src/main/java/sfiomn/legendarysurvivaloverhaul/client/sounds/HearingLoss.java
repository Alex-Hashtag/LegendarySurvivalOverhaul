package sfiomn.legendarysurvivaloverhaul.client.sounds;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.neoforged.fml.ModList;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTEfx;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbConditions;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbPenalty;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Hearing loss of the local player from a damaged head: sounds are muffled with an OpenAL low pass filter on each sound source,
 * cutting the high frequencies and lowering the volume. <br>
 * When the head gets worse, the player is nearly deaf for a moment (concussion), then the hearing recovers to its muffled level.
 * <p>
 * The filter is only touched when the hearing changes: a sound starting gets the current filter, and the playing sounds
 * are only updated while the hearing changes. The limb penalty sounds and the interface sounds are never muffled.
 * <p>
 * Without the OpenAL EFX extension, or with a mod already filtering the sound sources (Sound Physics Remastered),
 * only the master volume is lowered.
 */
public class HearingLoss
{
    private static final float CONCUSSION_VOLUME = 0.1f;
    private static final float CONCUSSION_CLARITY = 0.01f;
    private static final String[] FILTERING_MODS = {"sound_physics_remastered"};

    // Hearing the player is going to, and the hearing at the start of the concussion recovery
    private static float targetVolume = 1.0f;
    private static float targetClarity = 1.0f;
    private static float startVolume = 1.0f;
    private static float startClarity = 1.0f;
    private static int recoveryTicks;
    private static int recoveryDuration;

    // Current hearing, read by the sound thread
    private static volatile float volume = 1.0f;
    private static volatile float clarity = 1.0f;

    // Sound thread only
    private static int filter;
    private static volatile boolean filterUsed;
    private static volatile boolean efxUnavailable;

    private static boolean masterVolumeFallback;
    private static Field soundEngineField;
    private static Field instanceToChannelField;
    private static Field channelSourceField;

    /**
     * Called when the limb conditions of the local player change.
     *
     * @param headWorsened whether the head just got worse, triggering a concussion
     */
    public static void update(int conditions, boolean headWorsened)
    {
        float newVolume = 1.0f;
        float newClarity = 1.0f;
        if (LimbConditions.has(conditions, LimbPenalty.DEAFENED_HEARING))
        {
            newVolume = (float) Config.Baked.deafenedHearingVolume;
            newClarity = (float) Config.Baked.deafenedHearingClarity;
        } else if (LimbConditions.has(conditions, LimbPenalty.MUFFLED_HEARING))
        {
            newVolume = (float) Config.Baked.muffledHearingVolume;
            newClarity = (float) Config.Baked.muffledHearingClarity;
        }

        boolean concussion = headWorsened && newVolume < 1.0f && Config.Baked.concussionDeafnessDuration > 0;
        if (!concussion && newVolume == targetVolume && newClarity == targetClarity)
            return;

        targetVolume = newVolume;
        targetClarity = newClarity;

        if (concussion)
        {
            startVolume = Math.min(CONCUSSION_VOLUME, newVolume);
            startClarity = Math.min(CONCUSSION_CLARITY, newClarity);
            recoveryDuration = Config.Baked.concussionDeafnessDuration;
            recoveryTicks = 0;
            setHearing(startVolume, startClarity);
        } else
        {
            recoveryDuration = 0;
            setHearing(newVolume, newClarity);
        }
    }

    /**
     * Called every client tick, only does something while the hearing recovers from a concussion.
     */
    public static void tick()
    {
        // The sound thread found out EFX can't be used: lower the master volume instead
        if (efxUnavailable && !masterVolumeFallback && isActive())
            setHearing(volume, clarity);

        if (recoveryDuration <= 0 || Minecraft.getInstance().isPaused())
            return;

        recoveryTicks++;
        if (recoveryTicks >= recoveryDuration)
        {
            recoveryDuration = 0;
            setHearing(targetVolume, targetClarity);
            return;
        }

        // Slow at first then faster, like the ears unblocking
        float progress = (float) recoveryTicks / recoveryDuration;
        progress = progress * progress * (3 - 2 * progress);
        setHearing(Mth.lerp(progress, startVolume, targetVolume), Mth.lerp(progress, startClarity, targetClarity));
    }

    public static void reset()
    {
        targetVolume = 1.0f;
        targetClarity = 1.0f;
        recoveryDuration = 0;
        setHearing(1.0f, 1.0f);
    }

    public static boolean isActive()
    {
        return volume < 1.0f || clarity < 1.0f;
    }

    /**
     * @return the volume to play a limb penalty sound at, so it stays audible when only the master volume can be lowered
     */
    public static float compensate(float soundVolume)
    {
        if (!masterVolumeFallback || volume >= 1.0f || volume <= 0)
            return soundVolume;
        return Math.min(1.0f, soundVolume / volume);
    }

    private static void setHearing(float newVolume, float newClarity)
    {
        volume = newVolume;
        clarity = newClarity;

        if (useMasterVolume())
            applyMasterVolume();
        else
            applyToPlayingSounds();
    }

    private static boolean useMasterVolume()
    {
        if (!masterVolumeFallback && efxUnavailable)
        {
            masterVolumeFallback = true;
            LegendarySurvivalOverhaul.LOGGER.info("OpenAL EFX unavailable, the hearing loss only lowers the volume");
        }
        if (!masterVolumeFallback)
        {
            for (String modId : FILTERING_MODS)
            {
                if (ModList.get().isLoaded(modId))
                {
                    masterVolumeFallback = true;
                    LegendarySurvivalOverhaul.LOGGER.info("{} filters the sounds, the hearing loss only lowers the volume", modId);
                }
            }
        }
        return masterVolumeFallback;
    }

    /**
     * Called by the options and the sound engine reload, which set the master volume back.
     */
    public static void applyMasterVolume()
    {
        if (!masterVolumeFallback)
            return;
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getSoundManager().updateSourceVolume(SoundSource.MASTER, minecraft.options.getSoundSourceVolume(SoundSource.MASTER) * volume);
    }

    /**
     * Called when the sound engine starts again, its OpenAL objects are gone.
     */
    public static void onSoundEngineLoad()
    {
        filter = 0;
        filterUsed = false;
        applyMasterVolume();
    }

    @SuppressWarnings("unchecked")
    private static void applyToPlayingSounds()
    {
        try
        {
            if (soundEngineField == null)
            {
                soundEngineField = ObfuscationReflectionHelper.findField(SoundManager.class, "soundEngine");
                instanceToChannelField = ObfuscationReflectionHelper.findField(SoundEngine.class, "instanceToChannel");
            }
            SoundEngine soundEngine = (SoundEngine) soundEngineField.get(Minecraft.getInstance().getSoundManager());
            Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel = (Map<SoundInstance, ChannelAccess.ChannelHandle>) instanceToChannelField.get(soundEngine);

            for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry : instanceToChannel.entrySet())
            {
                SoundInstance sound = entry.getKey();
                entry.getValue().execute(channel -> applyFilter(sound, channel));
            }
        } catch (Exception e)
        {
            LegendarySurvivalOverhaul.LOGGER.error("Failed to muffle the playing sounds", e);
            efxUnavailable = true;
        }
    }

    /**
     * Called on the sound thread when a sound starts playing.
     */
    public static void onSourcePlay(SoundInstance sound, Channel channel)
    {
        // Sources are reused: once a filter was used, a source may still have it from its previous sound
        if ((isActive() || filterUsed) && !masterVolumeFallback)
            applyFilter(sound, channel);
    }

    /**
     * Set the low pass filter of a sound source. Sound thread only: the filter is copied in the source when attached.
     */
    private static void applyFilter(SoundInstance sound, Channel channel)
    {
        if (efxUnavailable)
            return;

        try
        {
            if (channelSourceField == null)
                channelSourceField = ObfuscationReflectionHelper.findField(Channel.class, "source");
            int source = channelSourceField.getInt(channel);

            if (!isActive() || isExempt(sound))
            {
                AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, EXTEfx.AL_FILTER_NULL);
                return;
            }

            if (filter == 0)
            {
                filter = EXTEfx.alGenFilters();
                EXTEfx.alFilteri(filter, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
            }
            EXTEfx.alFilterf(filter, EXTEfx.AL_LOWPASS_GAIN, volume);
            EXTEfx.alFilterf(filter, EXTEfx.AL_LOWPASS_GAINHF, clarity);
            AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, filter);
            filterUsed = true;

            if (AL10.alGetError() != AL10.AL_NO_ERROR)
                throw new IllegalStateException("OpenAL error while setting the low pass filter");
        } catch (Throwable e)
        {
            LegendarySurvivalOverhaul.LOGGER.warn("Can't muffle the sounds with OpenAL EFX: {}", e.toString());
            efxUnavailable = true;
        }
    }

    /**
     * Sounds heard inside the head (limb penalties) and interface sounds aren't muffled.
     */
    private static boolean isExempt(SoundInstance sound)
    {
        if (sound.getSource() == SoundSource.MASTER)
            return true;

        ResourceLocation location = sound.getLocation();
        return location.getNamespace().equals(LegendarySurvivalOverhaul.MOD_ID) && location.getPath().startsWith("limb_");
    }
}
