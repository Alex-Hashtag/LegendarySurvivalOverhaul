package sfiomn.legendarysurvivaloverhaul.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage.BodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.network.payloads.BodyPartHealingTimeMessage;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BodyHealthScreen extends Screen
{
    public static final ResourceLocation BODY_HEALTH_SCREEN = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/body_health_screen.png");
    public static final ResourceLocation TUTORIAL_PAGES = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/tutorial_pages.png");
    public static final int HEALTH_SCREEN_WIDTH = 176;
    public static final int HEALTH_SCREEN_HEIGHT = 183;
    public static final int HEALTH_BAR_WIDTH = 30;
    public static final int HEALTH_BAR_HEIGHT = 5;
    public static final int TEX_HEALTH_BAR_X = 176;
    public static final int TEX_HEALTH_BAR_Y = 0;

    // Tutorial toggle button constants (relative to body_health_screen.png)
    public static final int TUTORIAL_TOGGLE_BUTTON_X = 155;
    public static final int TUTORIAL_TOGGLE_BUTTON_Y = 8;
    public static final int TUTORIAL_TOGGLE_BUTTON_WIDTH = 15;
    public static final int TUTORIAL_TOGGLE_BUTTON_HEIGHT = 16;

    // Tutorial page constants (relative to body_health_screen.png)
    public static final int TUTORIAL_PAGE_X = 176;
    public static final int TUTORIAL_PAGE_Y = 3;
    public static final int TUTORIAL_PAGE_WIDTH = 118;
    public static final int TUTORIAL_PAGE_HEIGHT = 177;

    // Tutorial navigation button constants (relative to tutorial page)
    public static final int TUTORIAL_NAV_LEFT_BUTTON_X = 5;
    public static final int TUTORIAL_NAV_RIGHT_BUTTON_X = 96;
    public static final int TUTORIAL_NAV_BUTTON_Y = 160;
    public static final int TUTORIAL_NAV_BUTTON_WIDTH = 15;
    public static final int TUTORIAL_NAV_BUTTON_HEIGHT = 11;

    // Tutorial page texture coordinates on tutorial_pages.png
    // Each tutorial page is 118x177, stored at these coordinates
    public static final int[][] TUTORIAL_PAGE_TEX_COORDS = {
            {0, 0},      // Page 1
            {118, 0},    // Page 2
            {236, 0},    // Page 3
            {354, 0},    // Page 4
            {0, 177}     // Page 5
    };
    public static final int TUTORIAL_PAGE_COUNT = TUTORIAL_PAGE_TEX_COORDS.length;

    // Arrow texture coordinates on tutorial_pages.png (15x11)
    public static final int ARROW_TEX_WIDTH = 15;
    public static final int ARROW_TEX_HEIGHT = 11;
    public static final int[] ARROW_RIGHT_TEX_COORD = {0, 354};   // Right arrow
    public static final int[] ARROW_LEFT_TEX_COORD = {15, 354};    // Left arrow

    // Toggle button texture coordinates on tutorial_pages.png (15x16)
    public static final int TOGGLE_BUTTON_TEX_WIDTH = 15;
    public static final int TOGGLE_BUTTON_TEX_HEIGHT = 16;
    public static final int[] TOGGLE_BUTTON_OFF_TEX_COORD = {0, 365};  // Button OFF
    public static final int[] TOGGLE_BUTTON_ON_TEX_COORD = {15, 365};  // Button ON

    // Tutorial texture atlas dimensions
    public static final int TUTORIAL_TEXTURE_WIDTH = 512;
    public static final int TUTORIAL_TEXTURE_HEIGHT = 512;

    private final Map<BodyPartEnum, BodyPartButton> bodyPartButtons = new HashMap<>();

    private final Map<BodyPartEnum, Integer> flashCounters = new HashMap<>();
    private final Map<BodyPartEnum, Float> bodyPartHealth = new HashMap<>();
    private final Player player;
    private final InteractionHand hand;
    private final float healingValue;
    private final int healingTime;
    private int leftPos;
    private int topPos;
    private int healingCharges;
    private boolean consumeItem;
    private boolean applyEffect;

    private boolean tutorialOpen = false;
    private int currentTutorialPage = 0;
    private TutorialToggleButton tutorialToggleButton;
    private TutorialNavButton tutorialLeftButton;
    private TutorialNavButton tutorialRightButton;
    private int leftArrowPressCounter = 0;
    private int rightArrowPressCounter = 0;

    public BodyHealthScreen(Player player, InteractionHand hand, boolean alreadyConsumed, int healingCharges, float healingValue, int healingTime)
    {
        super(Component.translatable("screen." + LegendarySurvivalOverhaul.MOD_ID + ".body_health_screen"));

        this.player = player;
        this.hand = hand;
        this.healingCharges = healingCharges;
        this.healingValue = healingValue;
        this.healingTime = healingTime;
        this.consumeItem = !alreadyConsumed;
        this.applyEffect = true;
    }

    @Override
    protected void init()
    {
        super.init();
        assert this.minecraft != null;

        this.leftPos = (this.width - HEALTH_SCREEN_WIDTH) / 2;
        this.topPos = (this.height - HEALTH_SCREEN_HEIGHT) / 2;

        addWidget(new BodyPartButton(BodyPartEnum.HEAD, this.leftPos + 68, this.topPos + 46, 38, 34, button -> sendBodyPartHeal(BodyPartEnum.HEAD)));
        addWidget(new BodyPartButton(BodyPartEnum.RIGHT_ARM, this.leftPos + 101, this.topPos + 79, 50, 38, button -> sendBodyPartHeal(BodyPartEnum.RIGHT_ARM)));
        addWidget(new BodyPartButton(BodyPartEnum.LEFT_ARM, this.leftPos + 23, this.topPos + 79, 50, 38, button -> sendBodyPartHeal(BodyPartEnum.LEFT_ARM)));
        addWidget(new BodyPartButton(BodyPartEnum.CHEST, this.leftPos + 73, this.topPos + 79, 28, 38, button -> sendBodyPartHeal(BodyPartEnum.CHEST)));
        addWidget(new BodyPartButton(BodyPartEnum.RIGHT_LEG, this.leftPos + 87, this.topPos + 117, 49, 36, button -> sendBodyPartHeal(BodyPartEnum.RIGHT_LEG)));
        addWidget(new BodyPartButton(BodyPartEnum.RIGHT_FOOT, this.leftPos + 87, this.topPos + 153, 49, 10, button -> sendBodyPartHeal(BodyPartEnum.RIGHT_FOOT)));
        addWidget(new BodyPartButton(BodyPartEnum.LEFT_LEG, this.leftPos + 38, this.topPos + 117, 49, 36, button -> sendBodyPartHeal(BodyPartEnum.LEFT_LEG)));
        addWidget(new BodyPartButton(BodyPartEnum.LEFT_FOOT, this.leftPos + 38, this.topPos + 153, 49, 10, button -> sendBodyPartHeal(BodyPartEnum.LEFT_FOOT)));

        tutorialToggleButton = new TutorialToggleButton(
            this.leftPos + TUTORIAL_TOGGLE_BUTTON_X,
            this.topPos + TUTORIAL_TOGGLE_BUTTON_Y,
            TUTORIAL_TOGGLE_BUTTON_WIDTH,
            TUTORIAL_TOGGLE_BUTTON_HEIGHT,
            button -> toggleTutorial()
        );
        addWidget(tutorialToggleButton);

        int navButtonBaseY = this.topPos + TUTORIAL_PAGE_Y + TUTORIAL_NAV_BUTTON_Y;

        tutorialLeftButton = new TutorialNavButton(
            this.leftPos + TUTORIAL_PAGE_X + TUTORIAL_NAV_LEFT_BUTTON_X,
            navButtonBaseY,
            TUTORIAL_NAV_BUTTON_WIDTH,
            TUTORIAL_NAV_BUTTON_HEIGHT,
            true,
            button -> previousTutorialPage()
        );
        addWidget(tutorialLeftButton);

        tutorialRightButton = new TutorialNavButton(
            this.leftPos + TUTORIAL_PAGE_X + TUTORIAL_NAV_RIGHT_BUTTON_X,
            navButtonBaseY,
            TUTORIAL_NAV_BUTTON_WIDTH,
            TUTORIAL_NAV_BUTTON_HEIGHT,
            false,
            button -> nextTutorialPage()
        );
        addWidget(tutorialRightButton);

        bodyPartButtons.clear();
        for (GuiEventListener button : this.children())
        {
            if (button instanceof BodyPartButton bodyPartButton)
            {
                if (this.hand == null)
                {
                    bodyPartButton.active = false;
                }
                bodyPartButtons.put(bodyPartButton.bodyPart, bodyPartButton);
            }
        }

        flashCounters.clear();

        BodyDamageAttachment cap = AttachmentUtil.getBodyDamageAttachment(player);
        bodyPartHealth.clear();
        for (BodyPartEnum bodyPart : BodyPartEnum.values())
        {
            bodyPartHealth.put(bodyPart, cap.getBodyPartDamage(bodyPart));
        }
    }

    public void sendBodyPartHeal(BodyPartEnum bodyPart)
    {
        if (healingCharges > 0)
        {
            BodyPartHealingTimeMessage.sendToServer(bodyPart, this.hand, this.consumeItem, this.applyEffect);
            BodyDamageUtil.applyHealingTimeBodyPart(player, bodyPart, this.healingValue, this.healingTime);
            if (this.consumeItem)
                this.consumeItem = false;
            if (this.applyEffect)
                this.applyEffect = false;
            this.healingCharges--;
        }
    }

    @Override
    public void tick()
    {
        Iterator<Map.Entry<BodyPartEnum, Integer>> iter = flashCounters.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry<BodyPartEnum, Integer> flashBodyPart = iter.next();
            if (flashBodyPart.getValue() > 0)
                flashBodyPart.setValue(flashBodyPart.getValue() - 1);
            else
                iter.remove();
        }

        BodyDamageAttachment cap = AttachmentUtil.getBodyDamageAttachment(player);
        for (BodyPartEnum bodyPart : BodyPartEnum.values())
        {
            if (bodyPartHealth.get(bodyPart) != cap.getBodyPartDamage(bodyPart))
            {
                flashCounters.put(bodyPart, 4);
                bodyPartHealth.put(bodyPart, cap.getBodyPartDamage(bodyPart));
            }
        }

        if (leftArrowPressCounter > 0)
            leftArrowPressCounter--;
        if (rightArrowPressCounter > 0)
            rightArrowPressCounter--;

        super.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (this.minecraft != null && this.minecraft.player != null)
        {
            if (this.minecraft.options.keyInventory.matches(keyCode, scanCode))
            {
                this.minecraft.setScreen(new InventoryScreen(this.minecraft.player));
                return true;
            }

            if (KeyMappingRegistry.showBodyHealth.matches(keyCode, scanCode))
            {
                this.onClose();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc()
    {
        return true;
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks)
    {
        checkAutoCloseWhenHealing();

        // Draw vanilla background blur/dim behind our screen contents
        super.renderBackground(gui, mouseX, mouseY, partialTicks);

        // Render the screen texture
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        gui.blit(BODY_HEALTH_SCREEN, leftPos, topPos, 0, 0, HEALTH_SCREEN_WIDTH, HEALTH_SCREEN_HEIGHT);

        renderTutorialToggleButton(gui);

        // Render health bars on top
        for (BodyPartEnum bodyPart : BodyPartEnum.values())
            renderBodyPartHealth(gui, bodyPart, mouseX, mouseY, partialTicks);

        if (tutorialOpen) {
            renderTutorial(gui, mouseX, mouseY, partialTicks);
        }

        // Render buttons and widgets on top
        super.render(gui, mouseX, mouseY, partialTicks);
    }

    public void checkAutoCloseWhenHealing()
    {
        if (hand != null && healingCharges == 0)
        {
            assert this.minecraft != null;
            this.minecraft.setScreen(null);
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick)
    {
        // Don't render background blur - we handle rendering in render() method
    }

    private void renderTutorialToggleButton(GuiGraphics gui) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        int[] texCoord = tutorialOpen ? TOGGLE_BUTTON_ON_TEX_COORD : TOGGLE_BUTTON_OFF_TEX_COORD;
        gui.blit(TUTORIAL_PAGES,
            leftPos + TUTORIAL_TOGGLE_BUTTON_X,
            topPos + TUTORIAL_TOGGLE_BUTTON_Y,
            texCoord[0], texCoord[1],
            TOGGLE_BUTTON_TEX_WIDTH, TOGGLE_BUTTON_TEX_HEIGHT,
            TUTORIAL_TEXTURE_WIDTH, TUTORIAL_TEXTURE_HEIGHT);
    }

    private void renderTutorial(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        int[] texCoord = TUTORIAL_PAGE_TEX_COORDS[currentTutorialPage];
        gui.blit(TUTORIAL_PAGES,
            leftPos + TUTORIAL_PAGE_X,
            topPos + TUTORIAL_PAGE_Y,
            texCoord[0], texCoord[1],
            TUTORIAL_PAGE_WIDTH, TUTORIAL_PAGE_HEIGHT,
            TUTORIAL_TEXTURE_WIDTH, TUTORIAL_TEXTURE_HEIGHT);

        tutorialLeftButton.visible = currentTutorialPage > 0;
        tutorialRightButton.visible = currentTutorialPage < TUTORIAL_PAGE_COUNT - 1;

        if (tutorialLeftButton.visible && (tutorialLeftButton.isHovered() || leftArrowPressCounter > 0)) {
            gui.blit(TUTORIAL_PAGES,
                tutorialLeftButton.getX(),
                tutorialLeftButton.getY(),
                ARROW_LEFT_TEX_COORD[0], ARROW_LEFT_TEX_COORD[1],
                ARROW_TEX_WIDTH, ARROW_TEX_HEIGHT,
                TUTORIAL_TEXTURE_WIDTH, TUTORIAL_TEXTURE_HEIGHT);
        }

        if (tutorialRightButton.visible && (tutorialRightButton.isHovered() || rightArrowPressCounter > 0)) {
            gui.blit(TUTORIAL_PAGES,
                tutorialRightButton.getX(),
                tutorialRightButton.getY(),
                ARROW_RIGHT_TEX_COORD[0], ARROW_RIGHT_TEX_COORD[1],
                ARROW_TEX_WIDTH, ARROW_TEX_HEIGHT,
                TUTORIAL_TEXTURE_WIDTH, TUTORIAL_TEXTURE_HEIGHT);
        }
    }

    private void toggleTutorial() {
        tutorialOpen = !tutorialOpen;
        if (!tutorialOpen) {
            currentTutorialPage = 0;
        }
    }

    private void previousTutorialPage() {
        if (currentTutorialPage > 0) {
            leftArrowPressCounter = 4;
            currentTutorialPage--;
        }
    }

    private void nextTutorialPage() {
        if (currentTutorialPage < TUTORIAL_PAGE_COUNT - 1) {
            rightArrowPressCounter = 4;
            currentTutorialPage++;
        }
    }

    private void renderBodyPartHealth(GuiGraphics gui, BodyPartEnum bodyPart, int mouseX, int mouseY, float partialTicks)
    {
        if (minecraft == null)
        {
            return;
        }

        float healthRatio = BodyDamageUtil.getHealthRatio(this.player, bodyPart);
        float totalRemainingHealing = BodyDamageUtil.getTotalRemainingHealing(this.player, bodyPart);
        float maxHealth = BodyDamageUtil.getMaxHealth(this.player, bodyPart);

        BodyPartButton bodyPartButton = this.bodyPartButtons.get(bodyPart);

        // Update button health ratio
        bodyPartButton.setHealthRatio(healthRatio);

        // Define button inactive if full health, or pressed, or no healing items used
        if (healthRatio >= 1 || bodyPartButton.isPressed || this.hand == null)
        {
            if (bodyPartButton.active)
            {
                bodyPartButton.active = false;
            }
            if (totalRemainingHealing > 0.0f)
            {
                bodyPartButton.isPressed = false;
            }
        } else if (!bodyPartButton.active)
        {
            bodyPartButton.active = true;
        }

        bodyPartButton.render(gui, mouseX, mouseY, partialTicks);

        if (bodyPartButton.isMouseOver(mouseX, mouseY) && this.hand != null)
        {
            totalRemainingHealing += this.healingValue;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        drawHealthBar(gui, bodyPart, healthRatio, MathUtil.round(totalRemainingHealing / maxHealth, 2));
    }

    private void drawHealthBar(GuiGraphics gui, BodyPartEnum bodyPart, float healthRatio, float totalRemainingHealingRatio)
    {
        HealthBarIcon healthBarIcon = HealthBarIcon.get(bodyPart);
        HealthBarCondition healthBarCondition = HealthBarCondition.get(healthRatio);
        if (healthBarIcon == null)
            return;

        boolean shouldFlash = flashCounters.containsKey(bodyPart);

        // Draw empty health bar
        gui.blit(BODY_HEALTH_SCREEN, this.leftPos + healthBarIcon.x, this.topPos + healthBarIcon.y,
                TEX_HEALTH_BAR_X + HEALTH_BAR_WIDTH * HealthBarCondition.DEAD.iconIndexX,
                TEX_HEALTH_BAR_Y + HEALTH_BAR_HEIGHT * HealthBarCondition.DEAD.iconIndexY,
                HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

        int healthBarWidth = (int) Math.ceil(HEALTH_BAR_WIDTH * healthRatio);
        int healthBarPreviewWidth = (int) Math.min(Math.ceil(HEALTH_BAR_WIDTH * totalRemainingHealingRatio), HEALTH_BAR_WIDTH - healthBarWidth);

        int texX = TEX_HEALTH_BAR_X + HEALTH_BAR_WIDTH * healthBarCondition.iconIndexX;
        int texY = TEX_HEALTH_BAR_Y + HEALTH_BAR_HEIGHT * healthBarCondition.iconIndexY;

        sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul.LOGGER.info(
                "[drawHealthBar] {}: ratio={}, barWidth={}/{}, condition={}, texCoords=({},{})",
                bodyPart.name(), healthRatio, healthBarWidth, HEALTH_BAR_WIDTH, healthBarCondition.name(), texX, texY
        );

        // Draw current health bar
        if (healthBarWidth > 0)
        {
            sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul.LOGGER.info(
                    "[drawHealthBar] Drawing filled bar at screen({},{}) from tex({},{}) size({},{})",
                    this.leftPos + healthBarIcon.x, this.topPos + healthBarIcon.y,
                    texX, texY, healthBarWidth, HEALTH_BAR_HEIGHT
            );
            gui.blit(BODY_HEALTH_SCREEN, this.leftPos + healthBarIcon.x, this.topPos + healthBarIcon.y,
                    texX, texY, healthBarWidth, HEALTH_BAR_HEIGHT);
        }

        // Draw healing bar
        if (healthBarPreviewWidth > 0)
            gui.blit(BODY_HEALTH_SCREEN, this.leftPos + healthBarIcon.x + healthBarWidth, this.topPos + healthBarIcon.y,
                    TEX_HEALTH_BAR_X + healthBarWidth + HEALTH_BAR_WIDTH * healthBarCondition.getPreviewVariant().iconIndexX,
                    TEX_HEALTH_BAR_Y + HEALTH_BAR_HEIGHT * healthBarCondition.getPreviewVariant().iconIndexY,
                    healthBarPreviewWidth, HEALTH_BAR_HEIGHT);

        if (shouldFlash)
            gui.blit(BODY_HEALTH_SCREEN, this.leftPos + healthBarIcon.x, this.topPos + healthBarIcon.y,
                    TEX_HEALTH_BAR_X + HEALTH_BAR_WIDTH * HealthBarCondition.FLASH.iconIndexX,
                    TEX_HEALTH_BAR_Y + HEALTH_BAR_HEIGHT * HealthBarCondition.FLASH.iconIndexY,
                    HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
    }

    private enum HealthBarIcon
    {
        HEAD(72, 46),
        RIGHT_ARM(118, 80),
        LEFT_ARM(27, 80),
        CHEST(72, 92),
        RIGHT_LEG(106, 134),
        LEFT_LEG(39, 134),
        RIGHT_FOOT(106, 156),
        LEFT_FOOT(39, 156);

        public final int x;
        public final int y;

        HealthBarIcon(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public static HealthBarIcon get(BodyPartEnum bodyPart)
        {
            return switch (bodyPart)
            {
                case HEAD -> HEAD;
                case RIGHT_ARM -> RIGHT_ARM;
                case LEFT_ARM -> LEFT_ARM;
                case CHEST -> CHEST;
                case RIGHT_LEG -> RIGHT_LEG;
                case RIGHT_FOOT -> RIGHT_FOOT;
                case LEFT_LEG -> LEFT_LEG;
                case LEFT_FOOT -> LEFT_FOOT;
                default -> null;
            };
        }
    }

    private enum HealthBarCondition
    {
        HEALTHY(0, 0),
        WOUNDED(0, 1),
        HEAVILY_WOUNDED(0, 2),
        DEAD(0, 3),
        HEALTHY_PREVIEW(0, 4),
        WOUNDED_PREVIEW(0, 5),
        HEAVILY_WOUNDED_PREVIEW(0, 6),
        DEAD_PREVIEW(0, 7),
        FLASH(1, 8);

        public final int iconIndexX;
        public final int iconIndexY;

        HealthBarCondition(int iconIndexX, int iconIndexY)
        {
            this.iconIndexX = iconIndexX;
            this.iconIndexY = iconIndexY;
        }

        public static HealthBarCondition get(float healthRatio)
        {
            if (healthRatio >= 0.66f)
            {
                return HEALTHY;
            } else if (healthRatio >= 0.33f)
            {
                return WOUNDED;
            } else if (healthRatio > 0)
            {
                return HEAVILY_WOUNDED;
            } else
            {
                return DEAD;
            }
        }

        public HealthBarCondition getPreviewVariant()
        {
            return switch (this)
            {
                case HEALTHY -> HEALTHY_PREVIEW;
                case WOUNDED -> WOUNDED_PREVIEW;
                case HEAVILY_WOUNDED -> HEAVILY_WOUNDED_PREVIEW;
                case DEAD -> DEAD_PREVIEW;
                default -> this;
            };
        }
    }

    private static class TutorialToggleButton extends net.minecraft.client.gui.components.Button {
        public TutorialToggleButton(int x, int y, int width, int height, OnPress onPress) {
            super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        }
    }

    private static class TutorialNavButton extends net.minecraft.client.gui.components.Button {
        private final boolean isLeft;

        public TutorialNavButton(int x, int y, int width, int height, boolean isLeft, OnPress onPress) {
            super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
            this.isLeft = isLeft;
            this.visible = false;
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        }
    }
}
