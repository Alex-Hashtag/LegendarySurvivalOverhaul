package sfiomn.legendarysurvivaloverhaul.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.client.effects.TemperatureBreathEffect;
import sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons.RenderSeasonCards;
import sfiomn.legendarysurvivaloverhaul.client.render.*;
import sfiomn.legendarysurvivaloverhaul.client.sounds.TemperatureBreathSound;
import sfiomn.legendarysurvivaloverhaul.common.attachments.thirst.ThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.payloads.DrinkBlockFluidMessage;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

import java.util.ListIterator;

import static sfiomn.legendarysurvivaloverhaul.common.events.CommonNeoForgeEvents.playerDrinkEffect;
import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.plantCanGrow;
import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.seasonTooltip;
import static sfiomn.legendarysurvivaloverhaul.util.ItemUtil.compassLocation;
import static sfiomn.legendarysurvivaloverhaul.util.WorldUtil.timeInGame;

@EventBusSubscriber(
        modid = LegendarySurvivalOverhaul.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME,
        value = Dist.CLIENT
)
public class ClientForgeEvents
{

    public static boolean hasOpened = false;
    public static int warningPageDelay = 40;

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event)
    {
        Player player = Minecraft.getInstance().player;
        if (player == null)
        {
            return;
        }
        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded &&
                BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem())
                        .equals(ResourceLocation.fromNamespaceAndPath("sereneseasons", "calendar")))
        {
            player.displayClientMessage(seasonTooltip(player.blockPosition(), player.level()), true);
        } else if (event.getItemStack().getItem() == Items.CLOCK)
        {
            player.displayClientMessage(Component.literal(timeInGame(Minecraft.getInstance())), true);
        } else if (event.getItemStack().getItem() == Items.COMPASS)
        {
            String compassLocation = compassLocation(player);
            if (!compassLocation.isEmpty())
                player.displayClientMessage(Component.literal(compassLocation), true);
        } else if (Config.Baked.showCoordinateOnMap && event.getItemStack().getItem() == Items.FILLED_MAP)
        {
            MapItemSavedData mapData = MapItem.getSavedData(event.getItemStack(), event.getLevel());
            if (mapData != null)
                player.displayClientMessage(
                        Component.translatable("message.legendarysurvivaloverhaul.filled_map.destination",
                                mapData.centerX, mapData.centerZ), true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onApplyBonemeal(BonemealEvent event)
    {
        Block plant = event.getLevel().getBlockState(event.getPos()).getBlock();
        Player player = Minecraft.getInstance().player;
        if (player == null || BuiltInRegistries.BLOCK.getKey(plant) == null)
        {
            return;
        }

        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && !plantCanGrow(event.getLevel(), event.getPos(), event.getLevel().getBlockState(event.getPos())))
        {
            player.displayClientMessage(Component.translatable("message." + LegendarySurvivalOverhaul.MOD_ID + ".bonemeal.not_correct_season"), true);
        }
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event)
    {
        if (shouldApplyThirst(event.getEntity()))
        {
            // Only run on main hand (otherwise it runs twice)
            if (event.getHand() == InteractionHand.MAIN_HAND && event.getEntity().getMainHandItem().isEmpty())
            {
                Player player = event.getEntity();

                ThirstAttachment thirstAttachment = AttachmentUtil.getThirstAttachment(player);
                if (!thirstAttachment.isHydrationLevelAtMax())
                {
                    JsonThirstBlock jsonFluidThirst = ThirstUtil.getFluidThirstLookedAt(player, (float) Math.max(3.0, player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) / 2));

                    if (jsonFluidThirst != null && (jsonFluidThirst.hydration != 0 || jsonFluidThirst.saturation != 0))
                    {
                        playerDrinkEffect(event.getEntity());
                        DrinkBlockFluidMessage.sendToServer();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void preRenderGameOverlay(RenderGuiLayerEvent.Pre event)
    {
        if (event.getName().equals(VanillaGuiLayers.FOOD_LEVEL))
        {

            // Cancel the vanilla food rendering when cold hunger effect active (the mod redraws a custom food bar)
            if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(MobEffectRegistry.COLD_HUNGER))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlayEventHideDebugInfo(CustomizeGuiOverlayEvent.DebugText event)
    {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator() || !Config.Baked.hideInfoFromDebug)
            return;

        for (ListIterator<String> it = event.getRight().listIterator(); it.hasNext(); )
        {
            String line = it.next();
            if (line.contains("Targeted"))
            {
                line = line.split(":")[0] + ":";
                it.remove();
                it.add(line);
            }
        }

        for (ListIterator<String> it = event.getLeft().listIterator(); it.hasNext(); )
        {
            String line = it.next();
            if (line.contains("XYZ:"))
            {
                line = Component.translatable("message." + LegendarySurvivalOverhaul.MOD_ID + ".warning_use_compass").getString();
                it.remove();
                it.add(line);
            }
            if (line.startsWith("Chunk:") || line.startsWith("Block:") || line.startsWith("Facing:"))
                it.remove();
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        Player player = Minecraft.getInstance().player;
        if (!Minecraft.getInstance().isPaused() && player != null)
        {
            if (Config.Baked.temperatureEnabled)
            {
                RenderTemperatureGui.updateTimer();
                RenderTemperatureOverlay.updateTemperatureEffect(player);
                if (Config.Baked.coldBreathEffectThreshold != -1000)
                    TemperatureBreathEffect.tickPlay(player);
                if (Config.Baked.breathingSoundEnabled)
                    TemperatureBreathSound.tickPlay(player);
            }

            if (Config.Baked.wetnessEnabled)
                RenderWetnessGui.updateTimer();

            if (shouldApplyThirst(player) && Config.Baked.lowHydrationEffect)
                RenderBlurOverlay.updateBlurIntensity(player);

            if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && Config.Baked.ssSeasonCardsEnabled)
                RenderSeasonCards.updateSeasonCardFading(player);

            if (Config.Baked.localizedBodyDamageEnabled)
            {
                RenderBodyDamageGui.updateFlashingTimer();

                if (KeyMappingRegistry.showBodyHealth.consumeClick())
                    ClientHooks.openBodyHealthScreen(player);
            }

            if (Config.Baked.thirstEnabled && Config.Baked.showDrinkPreview)
                RenderThirstGui.updateTimer();

            if (LegendarySurvivalOverhaul.curiosLoaded && player.tickCount % 10 == 0)
                CuriosUtil.isThermometerEquipped = CuriosUtil.isCurioItemEquipped(player, ItemRegistry.THERMOMETER.get());
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event)
    {
        Player player = Minecraft.getInstance().player;
        if (player != null && event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER
                && Config.Baked.lowHydrationEffect && shouldApplyThirst(player))
        {
            RenderBlurOverlay.render(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event)
    {
        if (event.getLevel().isClientSide && event.getEntity() instanceof Player)
        {
            if (LegendarySurvivalOverhaul.sereneSeasonsLoaded)
                RenderSeasonCards.init();
        }
    }

    private static boolean shouldApplyThirst(Player player)
    {
        return Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player);
    }
}
