package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Random;

public class RenderHealthGui
{
	private static HealthCapability HEALTH_CAP = null;
	private static final Random rand = new Random();

	public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
	protected static final ResourceLocation MINECRAFT_GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

	// Row position on the overlay sheet
	private static final int HEART_TEXTURE_POS_Y = 146;
	private static final int HEART_TEXTURE_POS_X = 0;

	// Dimensions of the icon
	private static final int HEART_TEXTURE_WIDTH = 9;
	private static final int HEART_TEXTURE_HEIGHT = 9;

	public static final IGuiOverlay HEALTH_GUI = (forgeGui, guiGraphics, partialTicks, width, height) -> {
		if (Config.Baked.healthOverhaulEnabled
				&& !Minecraft.getInstance().options.hideGui
				&& forgeGui.shouldDrawSurvivalElements()) {
			Player player = forgeGui.getMinecraft().player;

			if (player != null) {

				rand.setSeed(player.tickCount * 445L);
				forgeGui.setupOverlayRenderState(true, false);

				Minecraft.getInstance().getProfiler().push("health");

				drawHealthBar(forgeGui, guiGraphics, player, width, height);
				Minecraft.getInstance().getProfiler().pop();

				RenderSystem.depthMask(true);
				RenderSystem.enableDepthTest();
			}
		}
	};
	
	public static void drawHealthBar(ForgeGui forgeGui, GuiGraphics gui, Player player, int width, int height) {
		if (HEALTH_CAP == null || player.tickCount % 20 == 0)
			HEALTH_CAP = CapabilityUtil.getHealthCapability(player);

		int brokenHearts = Mth.clamp(Mth.ceil(Config.Baked.initialHealth + HEALTH_CAP.getAdditionalHealth() - player.getMaxHealth() / 2.0), 0, (int) player.getAttributeValue(AttributeRegistry.BROKEN_HEART.get()));
		float shieldHealth = HEALTH_CAP.getShieldHealth();

		if (brokenHearts + shieldHealth == 0)
			return;

		int left = width / 2 - 91; // Same x offset as the health bar
		int top = height - forgeGui.leftHeight;

		int healthRows = Mth.ceil((Mth.ceil(shieldHealth / 2.0F) + brokenHearts)  / 10.0F);

		forgeGui.leftHeight += healthRows * 10;

		renderHearts(gui, left, top, 10, brokenHearts, Mth.ceil(player.getHealth()), shieldHealth);
	}

	public static void renderHearts(GuiGraphics gui, int left, int top, int rowHeight, int brokenHearts, int health, float shieldHealth) {
		int shieldHearts = Mth.ceil((double)shieldHealth / 2.0);

		for(int i1 = shieldHearts + brokenHearts - 1; i1 >= 0; --i1) {
			int j1 = i1 / 10;
			int k1 = i1 % 10;
			int x = left + k1 * 8;
			int y = top - j1 * rowHeight;
			if (health + shieldHealth <= 4) {
				y += rand.nextInt(2);
			}

			boolean flag = i1 >= shieldHearts;
			if (flag) {
				renderHeart(gui, HeartType.BROKEN, x, y, 0, false);
			} else {
				renderHeart(gui, HeartType.CONTAINER, x, y, 0, false);
				renderHeart(gui, HeartType.SHIELD, x, y, 0, shieldHealth < shieldHearts * 2 && i1 == shieldHearts - 1);
			}
		}

	}

	public static void renderHeart(GuiGraphics gui, HeartType heartType, int x, int y, int yTexture, boolean halfIcon) {
		gui.blit(heartType.location, x, y, heartType.getX(halfIcon), yTexture, 9, 9);
	}

	@OnlyIn(Dist.CLIENT)
	public static enum HeartType {
		CONTAINER(MINECRAFT_GUI_ICONS_LOCATION, 0),
		SHIELD(MINECRAFT_GUI_ICONS_LOCATION, 8),
		BROKEN(ICONS, 0);

		private final ResourceLocation location;
		private final int index;

		private HeartType(ResourceLocation location, int index) {
			this.location = location;
			this.index = index;
		}

		public int getX(boolean halfIcon) {
			if (this == BROKEN) {
				return (16 + this.index) * HEART_TEXTURE_WIDTH;
			} else {
				int i = 0;
				if (this == SHIELD)
					i = halfIcon ? 1 : 0;
				return 16 + (this.index * 2 + i) * 9;
			}
		}
	}
}
