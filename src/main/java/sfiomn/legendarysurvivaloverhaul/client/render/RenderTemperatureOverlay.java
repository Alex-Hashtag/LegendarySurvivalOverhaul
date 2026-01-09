package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.attachments.temperature.TemperatureAttachment;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import static sfiomn.legendarysurvivaloverhaul.util.RenderUtil.renderTextureOverlay;

@OnlyIn(Dist.CLIENT)
public class RenderTemperatureOverlay
{
    private static final ResourceLocation FROSTBITE_EFFECT = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/freeze_effect.png");
    private static final ResourceLocation HEAT_STROKE_EFFECT = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/heat_effect.png");
    private static TemperatureAttachment TEMPERATURE_CAP = null;
    private static ResourceLocation temperatureEffect = null;
    private static float fadeLevel = 0;
    private static boolean triggerTemperatureSoundEffect;

    public static void render(Gui gui, GuiGraphics guiGraphics, float partialTicks, int width, int height)
    {
        if (Config.Baked.temperatureEnabled && temperatureEffect != null)
        {
            Player player = net.minecraft.client.Minecraft.getInstance().player;
            if (player != null && temperatureEffect != null && !player.isCreative() && !player.isSpectator())
            {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                renderTextureOverlay(guiGraphics, temperatureEffect, width, height, fadeLevel);
                RenderSystem.disableBlend();
            }
        }
    }

    public static void updateTemperatureEffect(Player player)
    {
        if (player != null && player.isAlive())
        {

            if (TEMPERATURE_CAP == null || player.tickCount % 20 == 0)
                TEMPERATURE_CAP = AttachmentUtil.getTempAttachment(player);

            float temperature = TEMPERATURE_CAP.getTemperatureLevel();
            TemperatureEnum tempEnum = TEMPERATURE_CAP.getTemperatureEnum();

            boolean frostbiteLimit = temperature <= TemperatureEnum.FROSTBITE.getValue() + 1;
            boolean heatstrokeLimit = temperature >= TemperatureEnum.HEAT_STROKE.getValue() - 1;

            float targetFadeLevel;
            if (Config.Baked.coldTemperatureOverlay && tempEnum == TemperatureEnum.FROSTBITE && !FrostbiteEffect.playerIsImmuneToFrost(player))
            {
                temperatureEffect = FROSTBITE_EFFECT;
                if (frostbiteLimit)
                {
                    targetFadeLevel = 0.75f;
                    if (triggerTemperatureSoundEffect)
                    {
                        triggerTemperatureSoundEffect = false;
                        player.playSound(SoundRegistry.FROSTBITE.get(), 1.0f, 1.0f);
                    }
                } else
                {
                    targetFadeLevel = 0.35f;
                    triggerTemperatureSoundEffect = true;
                    if (fadeLevel == 0)
                    {
                        player.playSound(SoundRegistry.FROSTBITE_EARLY.get(), 1.0f, 1.0f);
                    }
                }
            } else if (Config.Baked.heatTemperatureOverlay && tempEnum == TemperatureEnum.HEAT_STROKE && !HeatStrokeEffect.playerIsImmuneToHeat(player))
            {
                temperatureEffect = HEAT_STROKE_EFFECT;

                if (heatstrokeLimit)
                {
                    targetFadeLevel = 0.6f;
                    if (triggerTemperatureSoundEffect)
                    {
                        triggerTemperatureSoundEffect = false;
                        player.playSound(SoundRegistry.HEAT_STROKE.get(), 1.0f, 1.0f);
                    }
                } else
                {
                    targetFadeLevel = 0.25f;
                    triggerTemperatureSoundEffect = true;
                    if (fadeLevel == 0)
                    {
                        player.playSound(SoundRegistry.HEAT_STROKE_EARLY.get(), 1.0f, 1.0f);
                    }
                }
            } else
            {
                triggerTemperatureSoundEffect = true;
                targetFadeLevel = 0;
            }

            if (targetFadeLevel > fadeLevel)
            {
                fadeLevel = Math.min(targetFadeLevel, fadeLevel + MathUtil.round(1.0f / 20.0f, 2));
            }
            if (targetFadeLevel < fadeLevel)
            {
                fadeLevel = Math.max(targetFadeLevel, fadeLevel - MathUtil.round(1.0f / 20.0f, 2));
            }

            if (fadeLevel == 0)
            {
                temperatureEffect = null;
            }
        }
    }
}
