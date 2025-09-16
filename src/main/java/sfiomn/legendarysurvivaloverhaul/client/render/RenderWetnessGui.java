package sfiomn.legendarysurvivaloverhaul.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Random;

public class RenderWetnessGui
{
	private static WetnessCapability WETNESS_CAP = null;
	private static final Random rand = new Random();

	private static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
	
	private static final int WETNESS_TEXTURE_POS_Y = 96;
	
	private static final int WETNESS_TEXTURE_WIDTH = 10;
	private static final int WETNESS_TEXTURE_HEIGHT = 10;

	private static int frameCounter = -1;
	private static boolean startAnimation = false;
	private static WetnessIcon lastWetnessIcon;
	private static int flashCounter = -1;
	
	public static IGuiOverlay WETNESS_GUI = (forgeGui, guiGraphics, partialTicks, width, height) -> {
		if (Config.Baked.wetnessEnabled
				&& !Minecraft.getInstance().options.hideGui
				&& forgeGui.shouldDrawSurvivalElements()) {
			Player player = forgeGui.getMinecraft().player;

			if (player != null) {
				rand.setSeed(player.tickCount * 445L);

				forgeGui.setupOverlayRenderState(true, false);

				Minecraft.getInstance().getProfiler().push("wetness_gui");
				drawWetness(guiGraphics, player, width, height);
				Minecraft.getInstance().getProfiler().pop();
			}
		}
	};
	
	public static void drawWetness(GuiGraphics gui, Player player, int width, int height)
	{
		if (WETNESS_CAP == null || player.tickCount % 20 == 0)
			WETNESS_CAP = CapabilityUtil.getWetnessCapability(player);

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

	private enum WetnessIcon {
		WETNESS_0(0),
		WETNESS_1(1),
		WETNESS_2(2),
		WETNESS_3(3);

		public final int iconIndexX;

		WetnessIcon(int iconIndexX) {
			this.iconIndexX = iconIndexX;
		}

		public int getXTextureOffset() {
			return iconIndexX * WETNESS_TEXTURE_WIDTH;
		}

		public int getYTextureOffset(boolean isFlash) {
			return WETNESS_TEXTURE_POS_Y + (isFlash ? WETNESS_TEXTURE_HEIGHT : 0);
		}

		public static WetnessIcon get(int wetness) {
			float wetnessRation =  wetness / (float) WetnessCapability.WETNESS_LIMIT;
            if (wetnessRation <= 0.25f)
				return WETNESS_0;
			else if (wetnessRation <= 0.5f)
				return WETNESS_1;
			else if (wetnessRation <= 0.75f)
				return WETNESS_2;
			else
				return WETNESS_3;
        }
	}
}
