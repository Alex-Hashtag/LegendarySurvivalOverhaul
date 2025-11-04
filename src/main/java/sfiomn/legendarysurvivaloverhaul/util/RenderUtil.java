package sfiomn.legendarysurvivaloverhaul.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public final class RenderUtil
{
    private RenderUtil()
    {
    }

    /**
     * Basically a more sensibly-named version of Minecraft's included blit function
     * Useful to specify different texWidth and width as the corresponded blit function is protected
     *
     * @param matrix    The matrix this should be drawn from
     * @param x         Horizontal position on the screen where this texture should be drawn
     * @param y         Vertical position on the screen where this texture should be drawn
     * @param texX      Horizontal position of the texture on the currently bound texture sheet.
     * @param texY      Vertical position of the texture on the currently bound texture sheet.
     * @param texWidth  Width of the given texture, in pixels
     * @param texHeight Height of the given texture, in pixels
     */
    public static void drawTexturedModelRect(Matrix4f matrix, float x, float y, int width, int height, int texX, int texY, int texWidth, int texHeight)
    {
        // f & f1 means 1/256 as we assume the image size to be 256x256
        float f = 0.00390625f;
        float f1 = 0.00390625f;
        float z = 0.0f;

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        bufferBuilder.addVertex(matrix, x, y + height, z)
                .setUv((texX * f), (texY + texHeight) * f1).setColor(255, 255, 255, 122);
        bufferBuilder.addVertex(matrix, (x + width), y + height, z)
                .setUv((texX + texWidth) * f, (texY + texHeight) * f1).setColor(255, 255, 255, 122);
        bufferBuilder.addVertex(matrix, (x + width), y, z)
                .setUv((texX + texWidth) * f, (texY * f1)).setColor(255, 255, 255, 122);
        bufferBuilder.addVertex(matrix, x, y, z)
                .setUv((texX * f), (texY * f1)).setColor(255, 255, 255, 255);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    public static void drawTexturedModelRectWithAlpha(Matrix4f matrix, float x, float y, int width, int height, int texX, int texY, int texWidth, int texHeight, float alpha)
    {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

        RenderUtil.drawTexturedModelRect(matrix, x, y, width, height, texX, texY, texWidth, texHeight);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderTextureOverlay(GuiGraphics gui, ResourceLocation resourceLocation, int screenWidth, int screenHeight, float alpha)
    {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        gui.setColor(1.0F, 1.0F, 1.0F, alpha);

        gui.blit(resourceLocation, 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        gui.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
