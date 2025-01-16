package sfiomn.legendarysurvivaloverhaul.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.client.shaders.FocusShader;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderThirstOverlay {

    private static FocusShader focusShader;
    private static final float DEFAULT_SHADER_INTENSITY = 0;
    private static final float MAX_SHADER_INTENSITY = 4;
    private static final float SHADER_INTENSITY_STEP = 0.05f;
    private static final int HYDRATION_LEVEL_MIN_EFFECT = 6;
    private static final int HYDRATION_LEVEL_MAX_EFFECT = 2;
    private static float shaderIntensity = 0;
    private static int updateTimer = 0;

    public static void render(Player player) {
        if (focusShader != null && (player.isSpectator() || player.isCreative() || shaderIntensity == 0)) {
            focusShader.stopRender();
            focusShader = null;
        } else if (shaderIntensity > 0 && !(Minecraft.getInstance().screen instanceof DeathScreen)) {
            if (focusShader == null)
                focusShader = new FocusShader();
            focusShader.render(shaderIntensity);
        }
    }

    public static void updateThirstEffect(@Nullable Player player) {
        float targetShaderIntensity = DEFAULT_SHADER_INTENSITY;
        if (player != null && player.isAlive() && !player.isCreative() && !player.isSpectator()) {

            ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);
            // hydration is 0 - 20
            int hydration = thirstCap.getHydrationLevel();

            if (hydration <= HYDRATION_LEVEL_MIN_EFFECT) {
                targetShaderIntensity = (1 - ((float) (hydration - HYDRATION_LEVEL_MAX_EFFECT) / (float) (HYDRATION_LEVEL_MIN_EFFECT - HYDRATION_LEVEL_MAX_EFFECT))) * MAX_SHADER_INTENSITY;
            }

            if (updateTimer++ % 2 == 0) {
                if (targetShaderIntensity > shaderIntensity) {
                    shaderIntensity = Math.min(shaderIntensity + SHADER_INTENSITY_STEP, targetShaderIntensity);
                } else if (targetShaderIntensity < shaderIntensity) {
                    shaderIntensity = Math.max(shaderIntensity - SHADER_INTENSITY_STEP, targetShaderIntensity);
                }
            }
        } else {
            shaderIntensity = 0;
        }
    }
}
