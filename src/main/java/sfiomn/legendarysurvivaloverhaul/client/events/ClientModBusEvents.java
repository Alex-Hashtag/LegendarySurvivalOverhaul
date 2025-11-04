package sfiomn.legendarysurvivaloverhaul.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons.RenderSeasonCards;
import sfiomn.legendarysurvivaloverhaul.client.particles.BreathParticle;
import sfiomn.legendarysurvivaloverhaul.client.particles.FernBlossomParticle;
import sfiomn.legendarysurvivaloverhaul.client.render.*;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationClientTooltipComponent;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationTooltipComponent;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents
{

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiLayersEvent event)
    {
        // Helper layers wrapping existing IGuiOverlay instances
        LayeredDraw.Layer healthLayer = (guiGraphics, delta) -> {
            Gui gui = Minecraft.getInstance().gui;
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            RenderHealthGui.render(gui, guiGraphics, delta.getGameTimeDeltaPartialTick(false), w, h);
        };

        LayeredDraw.Layer coldHungerLayer = (guiGraphics, delta) -> {
            Gui gui = Minecraft.getInstance().gui;
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            RenderTemperatureGui.renderColdHunger(gui, guiGraphics, delta.getGameTimeDeltaPartialTick(false), w, h);
        };

        LayeredDraw.Layer thirstLayer = (guiGraphics, delta) -> {
            Gui gui = Minecraft.getInstance().gui;
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            RenderThirstGui.render(gui, guiGraphics, delta.getGameTimeDeltaPartialTick(false), w, h);
        };

        LayeredDraw.Layer temperatureLayer = (guiGraphics, delta) -> {
            Gui gui = Minecraft.getInstance().gui;
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            RenderTemperatureGui.renderTemperature(gui, guiGraphics, delta.getGameTimeDeltaPartialTick(false), w, h);
        };

        LayeredDraw.Layer wetnessLayer = (guiGraphics, delta) -> {
            Gui gui = Minecraft.getInstance().gui;
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            RenderWetnessGui.render(gui, guiGraphics, delta.getGameTimeDeltaPartialTick(false), w, h);
        };

        LayeredDraw.Layer bodyDamageLayer = (guiGraphics, delta) -> {
            Gui gui = Minecraft.getInstance().gui;
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            RenderBodyDamageGui.render(gui, guiGraphics, delta.getGameTimeDeltaPartialTick(false), w, h);
        };

        LayeredDraw.Layer temperatureOverlayLayer = (guiGraphics, delta) -> {
            Gui gui = Minecraft.getInstance().gui;
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            RenderTemperatureOverlay.render(gui, guiGraphics, delta.getGameTimeDeltaPartialTick(false), w, h);
        };

        LayeredDraw.Layer itemFrameTooltipLayer = (guiGraphics, delta) -> {
            Gui gui = Minecraft.getInstance().gui;
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            RenderTooltipFrame.render(gui, guiGraphics, delta.getGameTimeDeltaPartialTick(false), w, h);
        };

        LayeredDraw.Layer seasonCardLayer = (guiGraphics, delta) -> {
            Gui gui = Minecraft.getInstance().gui;
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            RenderSeasonCards.render(gui, guiGraphics, delta.getGameTimeDeltaPartialTick(false), w, h);
        };

        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "health_overhaul"), healthLayer);
        event.registerBelow(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cold_hunger"), coldHungerLayer);
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "thirst"), thirstLayer);
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "temperature"), temperatureLayer);
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "wetness"), wetnessLayer);
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "body_damage"), bodyDamageLayer);
        // Anchor overlays above commonly available layers to ensure rendering order
        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "temperature_overlay"), temperatureOverlayLayer);
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item_frame_tooltip"), itemFrameTooltipLayer);
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "season_card"), seasonCardLayer);
    }

    @SubscribeEvent
    public static void onTooltipRegistration(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(HydrationTooltipComponent.class, component -> new HydrationClientTooltipComponent(component.hydration(), component.saturation()));
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(ParticleTypeRegistry.SUN_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
        event.registerSpriteSet(ParticleTypeRegistry.ICE_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
        event.registerSpriteSet(ParticleTypeRegistry.COLD_BREATH.get(), BreathParticle.Factory::new);
    }
}
