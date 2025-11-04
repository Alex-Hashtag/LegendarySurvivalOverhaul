package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.attachments.wetness.WetnessAttachment;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

import java.util.Random;

public class RenderWetnessGui
{
    private static final Random rand = new Random();
    private static final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
    private static final int WETNESS_TEXTURE_POS_Y = 96;
    private static final int WETNESS_TEXTURE_WIDTH = 10;
    private static final int WETNESS_TEXTURE_HEIGHT = 10;
    private static WetnessAttachment WETNESS_CAP = null;
    private static int frameCounter = -1;
    private static boolean startAnimation = false;
    private static WetnessIcon lastWetnessIcon;
    private static int flashCounter = -1;

    public static void render(Gui gui, GuiGraphics guiGraphics, float partialTicks, int width, int height)
    {
        if (Config.Baked.wetnessEnabled && !Minecraft.getInstance().options.hideGui)
        {
            Player player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative() && !player.isSpectator())
            {
                rand.setSeed(player.tickCount * 445L);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                Minecraft.getInstance().getProfiler().push("wetness_gui");
                drawWetness(guiGraphics, player, width, height);
                Minecraft.getInstance().getProfiler().pop();

                RenderSystem.disableBlend();
            }
        }
    }

    public static void drawWetness(GuiGraphics gui, Player player, int width, int height)
    {
        if (WETNESS_CAP == null || player.tickCount % 20 == 0)
            WETNESS_CAP = AttachmentUtil.getWetnessAttachment(player);

        int wetness = WETNESS_CAP.getWetness();
        if (wetness == 0)
            return;

        int x = width / 2 - (WETNESS_TEXTURE_WIDTH / 2) + Config.Baked.wetnessIndicatorOffsetX;
        int y = height - 61 + Config.Baked.wetnessIndicatorOffsetY;

        if (CuriosUtil.isThermometerEquipped && Config.Baked.wetnessIndicatorOffsetY == 0)
            y += 10;

        WetnessIcon wetnessIcon = WetnessIcon.get(wetness);

        if (lastWetnessIcon != wetnessIcon)
        {
            flashCounter = 3;
            lastWetnessIcon = wetnessIcon;
        }

        gui.blit(ICONS, x, y, wetnessIcon.getXTextureOffset(), wetnessIcon.getYTextureOffset(flashCounter >= 0), WETNESS_TEXTURE_WIDTH, WETNESS_TEXTURE_HEIGHT);
    }

    public static void updateTimer()
    {
        if (frameCounter >= 0)
            frameCounter--;
        if (flashCounter >= 0)
            flashCounter--;

        if (startAnimation)
        {
            frameCounter = 24;
            startAnimation = false;
        }
    }

    private enum WetnessIcon
    {
        WETNESS_0(0),
        WETNESS_1(1),
        WETNESS_2(2),
        WETNESS_3(3);

        public final int iconIndexX;

        WetnessIcon(int iconIndexX)
        {
            this.iconIndexX = iconIndexX;
        }

        public static WetnessIcon get(int wetness)
        {
            float wetnessRation = wetness / (float) WetnessAttachment.WETNESS_LIMIT;
            if (wetnessRation <= 0.25f)
                return WETNESS_0;
            else if (wetnessRation <= 0.5f)
                return WETNESS_1;
            else if (wetnessRation <= 0.75f)
                return WETNESS_2;
            else
                return WETNESS_3;
        }

        public int getXTextureOffset()
        {
            return iconIndexX * WETNESS_TEXTURE_WIDTH;
        }

        public int getYTextureOffset(boolean isFlash)
        {
            return WETNESS_TEXTURE_POS_Y + (isFlash ? WETNESS_TEXTURE_HEIGHT : 0);
        }
    }
}
