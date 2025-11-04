package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.overflowingbars.OverflowingBarsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Random;

public class RenderHealthGui
{
    private static HealthCapability HEALTH_CAP = null;
    private static final Random rand = new Random();

    public static final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
    protected static final ResourceLocation MINECRAFT_GUI_ICONS_LOCATION = ResourceLocation.parse("textures/gui/icons.png");

    private static final int HEART_TEXTURE_WIDTH = 9;
    private static final int HEART_TEXTURE_HEIGHT = 9;

    public static void render(Gui gui, GuiGraphics guiGraphics, float partialTicks, int width, int height) {
        if (Config.Baked.healthOverhaulEnabled && !Minecraft.getInstance().options.hideGui) {
            Player player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative() && !player.isSpectator()) {
                rand.setSeed(player.tickCount * 445L);

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);

                Minecraft.getInstance().getProfiler().push("health_gui");
                int leftHeight = gui.leftHeight;
                int used = drawHealthBar(guiGraphics, player, width, height, leftHeight);
                if (used > 0) gui.leftHeight += used;
                Minecraft.getInstance().getProfiler().pop();

                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
            }
        }
    }

    /**
     * Draws the health overlay and returns how many pixels of left HUD height were consumed.
     */
    public static int drawHealthBar(GuiGraphics gui, Player player, int width, int height, int leftHeight)
    {
        if (HEALTH_CAP == null || player.tickCount % 20 == 0)
            HEALTH_CAP = CapabilityUtil.getHealthCapability(player);

        int brokenHearts = HealthUtil.getEffectiveBrokenHearts(player);
        float shieldHealth = HEALTH_CAP.getShieldHealth();

        if (brokenHearts + shieldHealth == 0)
            return 0;

        int left = width / 2 - 91;           // vanilla health bar X
        int top = height - leftHeight;       // stack-aware baseline like thirst does on the right

        int playerHearts = 0;
        int totalHearts = Mth.ceil(shieldHealth / 2.0F) + brokenHearts;

        boolean appendedRow = false;
        if (Config.Baked.appendBrokenShieldHeartsToHealthBar) {
            playerHearts = Mth.ceil(player.getMaxHealth() / 2.0f);

            if (OverflowingBarsUtil.isHealthBarOverflowing())
                playerHearts = Math.min(10, playerHearts);

            playerHearts = playerHearts % 10;

            if (playerHearts > 0) {
                totalHearts += playerHearts;
                top += 10;          // matches old Forge behavior
                appendedRow = true; // we "borrowed" 10px before adding rows
            }
        }

        int healthRows = Mth.ceil(totalHearts / 10.0F);

        // Render
        renderHearts(gui, left, top, 10, playerHearts, brokenHearts, Mth.ceil(player.getHealth()), shieldHealth);

        // Report how much vertical space we actually used on the left HUD stack.
        // Matches old Forge logic: start -10 when we appended the extra player row, then add rows * 10.
        int used = healthRows * 10 - (appendedRow ? 10 : 0);
        return Math.max(used, 0);
    }

    public static void renderHearts(GuiGraphics gui, int left, int top, int rowHeight, int playerHearts, int brokenHearts, int health, float shieldHealth) {
        int shieldHearts = Mth.ceil((double)shieldHealth / 2.0);

        for (int i1 = playerHearts + shieldHearts + brokenHearts - 1; i1 >= playerHearts; --i1) {
            int j1 = i1 / 10;
            int k1 = i1 % 10;
            int x = left + k1 * 8;
            int y = top - j1 * rowHeight;
            if (health + shieldHealth <= 4) {
                y += rand.nextInt(2);
            }

            boolean flag = i1 >= brokenHearts + playerHearts;
            if (flag) {
                renderHeart(gui, HeartType.CONTAINER, x, y, 0, false);
                renderHeart(gui, HeartType.SHIELD, x, y, 0, shieldHealth < shieldHearts * 2 && i1 == shieldHearts - 1);
            } else {
                renderHeart(gui, HeartType.BROKEN, x, y, 0, false);
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
