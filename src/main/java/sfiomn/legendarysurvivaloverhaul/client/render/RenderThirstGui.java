package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Holder;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.effects.ThirstEffect;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Random;

public class RenderThirstGui
{
	private static ThirstCapability THIRST_CAP = null;
	private static final Random rand = new Random();

    public static final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");

	// Dimensions of the icon
	private static final int THIRST_TEXTURE_WIDTH = 9;
	private static final int THIRST_TEXTURE_HEIGHT = 9;

	@Nullable
	private static Item heldItemOnPreview;
	private static int heldItemHydration;
	private static float heldItemSaturation;
	private static boolean heldItemThirst;
	private static float alphaPreview;
	private static float unclampedAlphaPreview;
	private static int alphaDirection = 1;

	public static void render(Gui gui, GuiGraphics guiGraphics, float partialTicks, int width, int height) {
		if (Config.Baked.thirstEnabled && Config.Baked.showHydrationBar && !Minecraft.getInstance().options.hideGui) {
			Player player = Minecraft.getInstance().player;
			if (player != null && !player.isCreative() && !player.isSpectator()) {
				if (!ThirstUtil.isThirstActive(player)) return;
				rand.setSeed(player.tickCount * 445L);
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);

				Minecraft.getInstance().getProfiler().push("thirst_gui");
				int rightHeight = gui.rightHeight;
				drawHydrationBar(guiGraphics, player, width, height, rightHeight);
				gui.rightHeight += 10;
				Minecraft.getInstance().getProfiler().pop();

				RenderSystem.depthMask(true);
				RenderSystem.enableDepthTest();
				RenderSystem.disableBlend();
			}
		}
	}

	public static void drawHydrationBar(GuiGraphics gui, Player player, int width, int height, int rightHeight)
    {
        // Update player's thirst capability every 20 ticks
        if (THIRST_CAP == null || player.tickCount % 20 == 0)
            THIRST_CAP = CapabilityUtil.getThirstCapability(player);

        // Calculation of hydration preview
        ItemStack currentHeldItemStack = player.getMainHandItem();
        if (Config.Baked.showDrinkPreview)
        {

            if (player.tickCount % 10 == 0)
            {
                JsonThirstConsumable jsonThirstConsumable = ThirstDataManager.getConsumable(currentHeldItemStack);
                heldItemHydration = (jsonThirstConsumable != null) ? jsonThirstConsumable.hydration : 0;
                heldItemSaturation = (jsonThirstConsumable != null) ? jsonThirstConsumable.saturation : 0;
                heldItemThirst = jsonThirstConsumable != null && jsonThirstConsumable.effects.stream().anyMatch(jsonEffectParameter -> jsonEffectParameter.name.equals(LegendarySurvivalOverhaul.MOD_ID + ":thirst"));
            }

            // Force a reset flash when item becomes edible && avoid this reset if moving from edible to edible
            // Improve the sync with appleskin flashing
            boolean currentIsEdible = currentHeldItemStack.has(net.minecraft.core.component.DataComponents.FOOD);
            boolean previousIsEdible = heldItemOnPreview != null && heldItemOnPreview.components().has(net.minecraft.core.component.DataComponents.FOOD);
            if (heldItemOnPreview == null || currentIsEdible != previousIsEdible)
            {
                heldItemOnPreview = currentHeldItemStack.getItem();
                resetFlash();
            }

            if (heldItemHydration == 0 && heldItemSaturation == 0 && !heldItemThirst)
                resetFlash();

            // hydration is 0 - 20
            int hydration = THIRST_CAP.getHydrationLevel();
            float saturation = THIRST_CAP.getSaturationLevel();
            // Use rightHeight to position above other right-side HUD elements
            int left = width / 2 + 91 + Config.Baked.hydrationBarOffsetX;
            int top = height - rightHeight + Config.Baked.hydrationBarOffsetY;
            boolean hasThirstEffect = player.hasEffect(MobEffectRegistry.THIRST);
            boolean hasHeatThirstEffect = player.hasEffect(MobEffectRegistry.HEAT_THIRST);
            ThirstEffect thirstEffect = ThirstEffect.getEffect(hasThirstEffect, hasHeatThirstEffect);
            ThirstEffect targetThirstEffect = ThirstEffect.getEffect(hasThirstEffect || heldItemThirst, hasHeatThirstEffect);

            // Draw hydration & saturation icons
            for (int i = 0; i < 10; i++)
            {
                int halfIcon = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;
                int yOffset = 0;

                // Shake based on hydration level and saturation level
                if (Config.Baked.showVanillaBarAnimationOverlay && saturation <= 0.0f && player.tickCount % (hydration * 3 + 1) == 0)
                {
                    yOffset = rand.nextInt(3) - 1;
                }

                if (hydration + Math.min(heldItemHydration, 0) <= halfIcon && halfIcon <= hydration + Math.max(heldItemHydration, 0))
                {
                    renderFading(gui, x, y + yOffset,
                            new ThirstIcon(thirstEffect.getXTextureOffset(halfIcon == hydration, heldItemHydration > 0), thirstEffect.getYTextureOffset()),
                            new ThirstIcon(targetThirstEffect.getXTextureOffset(halfIcon == hydration + heldItemHydration, heldItemHydration < 0), thirstEffect.getYTextureOffset()));
                } else
                {
                    // Used to render the full hydration bar thirsty in the preview
				/*if (thirstEffect != targetThirstEffect) {
					renderFading(gui, x, y + yOffset,
							new ThirstIcon(thirstEffect.getXTextureOffset(false, halfIcon > hydration + Math.max(heldItemHydration, 0)), thirstEffect.getYTextureOffset()),
							new ThirstIcon(targetThirstEffect.getXTextureOffset(false, halfIcon > hydration + Math.max(heldItemHydration, 0)), thirstEffect.getYTextureOffset()));
				} else {
					gui.blit(ICONS, x, y + yOffset, thirstEffect.getXTextureOffset(false, halfIcon > hydration + Math.max(heldItemHydration, 0)), thirstEffect.getYTextureOffset(), THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
				}*/
                    gui.blit(ICONS, x, y + yOffset, thirstEffect.getXTextureOffset(false, halfIcon > hydration + Math.max(heldItemHydration, 0)), thirstEffect.getYTextureOffset(), THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
                }

                // Draw saturation icons if enabled
                if (Config.Baked.hydrationSaturationDisplayed)
                {
                    if (Mth.ceil(saturation + Math.min(heldItemSaturation, 0)) <= halfIcon && halfIcon <= Mth.ceil(saturation + Math.max(heldItemSaturation, 0)))
                    {
                        if (heldItemSaturation < 0 || halfIcon == Mth.ceil(saturation))
                        {
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1 - alphaPreview);
                            gui.blit(ICONS, x, y, thirstEffect.getXTextureOffsetSaturation(halfIcon == Mth.ceil(saturation)), 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
                        }
                        if (heldItemSaturation > 0 || Mth.ceil(saturation + heldItemSaturation) == halfIcon)
                        {
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alphaPreview);
                            gui.blit(ICONS, x, y, targetThirstEffect.getXTextureOffsetSaturation(halfIcon == Mth.ceil(saturation + heldItemSaturation)), 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
                        }
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    } else if (halfIcon < Mth.ceil(saturation + Math.min(heldItemSaturation, 0)))
                    {
                        // Used to render the full saturation bar thirsty in the preview
					/*if (thirstEffect != targetThirstEffect) {
						renderFading(gui, x, y + yOffset,
								new ThirstIcon(thirstEffect.getXTextureOffsetSaturation(false), thirstEffect.getYTextureOffset()),
								new ThirstIcon(targetThirstEffect.getXTextureOffsetSaturation(false), thirstEffect.getYTextureOffset()));
					} else {
						gui.blit(ICONS, x, y + yOffset, thirstEffect.getXTextureOffsetSaturation(false), 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
					}*/
                        gui.blit(ICONS, x, y + yOffset, thirstEffect.getXTextureOffsetSaturation(false), 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
                    }
                }
            }
        }
    }

	public static void renderFading(GuiGraphics gui, int x, int y, ThirstIcon thirstIconFrom, ThirstIcon thirstIconTo) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1 - alphaPreview);
		gui.blit(ICONS, x, y, thirstIconFrom.xTextureOffset, thirstIconFrom.yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alphaPreview);
		gui.blit(ICONS, x, y, thirstIconTo.xTextureOffset, thirstIconTo.yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void updateTimer() {
		// Same as Appleskin flash speed
		unclampedAlphaPreview += alphaDirection * 0.125f;
		if (unclampedAlphaPreview >= 1.5f) {
			alphaDirection = -1;
		} else if (unclampedAlphaPreview <= -0.5f) {
			alphaDirection = 1;
		}
		alphaPreview = Mth.clamp(unclampedAlphaPreview, 0.0f, 1.0f);
	}

	public static void resetFlash()
	{
		unclampedAlphaPreview = alphaPreview = 0f;
		alphaDirection = 1;
	}

	public record ThirstIcon(int xTextureOffset, int yTextureOffset) {

	}

	public enum ThirstEffect {
		NONE(0, 0, THIRST_TEXTURE_WIDTH * 6),
		THIRST(THIRST_TEXTURE_WIDTH * 3, 0, THIRST_TEXTURE_WIDTH * 8),
		HEAT_THIRST(0, THIRST_TEXTURE_HEIGHT, THIRST_TEXTURE_WIDTH * 8),
		BOTH(THIRST_TEXTURE_WIDTH * 3, THIRST_TEXTURE_HEIGHT, THIRST_TEXTURE_WIDTH * 14);

		private final int xTextureOffset;
		private final int yTextureOffset;
		private final int xTextureOffsetSaturation;

		ThirstEffect(int xTextureOffset, int yTextureOffset, int xTextureOffsetSaturation) {
			this.xTextureOffset = xTextureOffset;
			this.yTextureOffset = yTextureOffset;
			this.xTextureOffsetSaturation = xTextureOffsetSaturation;
		}

		public int getXTextureOffset(boolean isHalfIcon, boolean isContainer) {
			return isHalfIcon ? xTextureOffset + (THIRST_TEXTURE_WIDTH * 2) : isContainer ? xTextureOffset : xTextureOffset + THIRST_TEXTURE_WIDTH;
		}

		public int getYTextureOffset() {
			return yTextureOffset;
		}

		public int getXTextureOffsetSaturation(boolean isHalfIcon) {
			return isHalfIcon ? xTextureOffsetSaturation + THIRST_TEXTURE_WIDTH : xTextureOffsetSaturation;
		}

		// Method to get the appropriate effect based on player's status
		public static ThirstEffect getEffect(boolean hasThirstEffect, boolean hasHeatThirstEffect) {
			if (hasThirstEffect && hasHeatThirstEffect) {
				return BOTH;
			} else if (hasThirstEffect) {
				return THIRST;
			} else if (hasHeatThirstEffect) {
				return HEAT_THIRST;
			} else {
				return NONE;
			}
		}
	}
}
