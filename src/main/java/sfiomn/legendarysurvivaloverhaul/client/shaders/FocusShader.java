package sfiomn.legendarysurvivaloverhaul.client.shaders;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FocusShader {
    public static final ResourceLocation BLOBS2_SHADER = ResourceLocation.withDefaultNamespace("shaders/post/blobs2.json");
    public static final ResourceLocation BLUR_SHADER = ResourceLocation.withDefaultNamespace("shaders/post/blur.json");

    private static boolean shaderLoadAttempted = false;
    private static boolean shaderAvailable = false;
    private static String loadedShaderName = null;

    public FocusShader() {}

    public void render(float intensity) {
        if (intensity <= 0) return;

        var mc = Minecraft.getInstance();
        PostChain effect = mc.gameRenderer.currentEffect();

        // Load once: try blobs2.json first, then blur.json; remember result to prevent repeated attempts/log spam
        if (!shaderLoadAttempted) {
            shaderLoadAttempted = true;
            try {
                mc.gameRenderer.loadEffect(BLOBS2_SHADER);
                effect = mc.gameRenderer.currentEffect();
                shaderAvailable = (effect != null);
                loadedShaderName = shaderAvailable ? effect.getName() : null;
            } catch (Throwable t) {
                // Fallback to blur.json on 1.21.x where blobs2.json was removed
                try {
                    mc.gameRenderer.loadEffect(BLUR_SHADER);
                    effect = mc.gameRenderer.currentEffect();
                    shaderAvailable = (effect != null);
                    loadedShaderName = shaderAvailable ? effect.getName() : null;
                } catch (Throwable ignored) {
                    shaderAvailable = false;
                }
            }
        }

        if (!shaderAvailable) return;

        // Ensure the effect is still active; if some other mod shut it down, don't spam reloads
        if (effect == null || (loadedShaderName != null && !effect.getName().equals(loadedShaderName))) {
            return;
        }

        updateIntensity(intensity);
    }

    public void stopRender() {
        var mc = Minecraft.getInstance();
        PostChain effect = mc.gameRenderer.currentEffect();
        if (effect != null) {
            mc.gameRenderer.shutdownEffect();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void updateIntensity(float intensity) {
        var effect = Minecraft.getInstance().gameRenderer.currentEffect();
        if (effect != null) {
            // Use the public API instead of reflection into passes
            effect.setUniform("Radius", intensity);
        }
    }
}
