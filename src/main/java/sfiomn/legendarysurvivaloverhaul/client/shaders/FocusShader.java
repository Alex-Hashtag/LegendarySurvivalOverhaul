package sfiomn.legendarysurvivaloverhaul.client.shaders;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FocusShader {
    public static final ResourceLocation BLUR_SHADER = ResourceLocation.parse("shaders/post/blobs2.json");

    public FocusShader() {}

    public void render(float intensity) {
        if (intensity <= 0) return;

        var mc = Minecraft.getInstance();
        PostChain effect = mc.gameRenderer.currentEffect();

        if (effect == null || !effect.getName().equals("minecraft:shaders/post/blobs2.json")) {
            try {
                mc.gameRenderer.loadEffect(BLUR_SHADER);
                effect = mc.gameRenderer.currentEffect();
            } catch (NullPointerException e) {
                return;
            }
        }

        updateIntensity(intensity);
    }

    public void stopRender() {
        var mc = Minecraft.getInstance();
        PostChain effect = mc.gameRenderer.currentEffect();
        if (effect != null && effect.getName().equals("minecraft:shaders/post/blobs2.json")) {
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
