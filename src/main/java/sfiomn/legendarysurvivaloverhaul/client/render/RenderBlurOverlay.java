package sfiomn.legendarysurvivaloverhaul.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.client.shaders.FocusShader;
import sfiomn.legendarysurvivaloverhaul.common.attachments.thirst.ThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderBlurOverlay
{

    private static final float DEFAULT_SHADER_INTENSITY = 0;
    private static final float MAX_SHADER_INTENSITY = 4;
    private static final float SHADER_INTENSITY_STEP = 0.05f;
    private static final int HYDRATION_LEVEL_MIN_EFFECT = 6;
    private static final int HYDRATION_LEVEL_MAX_EFFECT = 2;
    private static FocusShader focusShader;
    private static float shaderIntensity = 0;
    private static int updateTimer = 0;
    private static boolean hasShownBlurWarning = false;

    public static void render(Player player)
    {
        if (focusShader != null && (player.isSpectator() || player.isCreative() || shaderIntensity == 0))
        {
            focusShader.stopRender();
            focusShader = null;
        } else if (shaderIntensity > 0 && !(Minecraft.getInstance().screen instanceof DeathScreen))
        {
            if (focusShader == null)
                focusShader = new FocusShader();
            focusShader.render(shaderIntensity);
        }
    }

    public static void updateBlurIntensity(@Nullable Player player)
    {
        float targetShaderIntensity = DEFAULT_SHADER_INTENSITY;
        if (player != null && player.isAlive() && !player.isCreative() && !player.isSpectator())
        {

            ThirstAttachment thirstCap = AttachmentUtil.getThirstAttachment(player);
            // hydration is 0 - 20
            int hydration = thirstCap.getHydrationLevel();

            if (hydration <= HYDRATION_LEVEL_MIN_EFFECT)
            {
                targetShaderIntensity = (1 - ((float) (hydration - HYDRATION_LEVEL_MAX_EFFECT) / (float) (HYDRATION_LEVEL_MIN_EFFECT - HYDRATION_LEVEL_MAX_EFFECT))) * MAX_SHADER_INTENSITY;
                
                // Show first-time blur warning
                if (!hasShownBlurWarning && targetShaderIntensity > 0)
                {
                    player.displayClientMessage(Component.literal("Vision is getting blurry. Drink some water"), true);
                    hasShownBlurWarning = true;
                }
            }

            if (updateTimer++ % 2 == 0)
            {
                if (targetShaderIntensity > shaderIntensity)
                {
                    shaderIntensity = Math.min(shaderIntensity + SHADER_INTENSITY_STEP, targetShaderIntensity);
                } else if (targetShaderIntensity < shaderIntensity)
                {
                    shaderIntensity = Math.max(shaderIntensity - SHADER_INTENSITY_STEP, targetShaderIntensity);
                }
            }
        } else
        {
            shaderIntensity = 0;
        }
    }
}
