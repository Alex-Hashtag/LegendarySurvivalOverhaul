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
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage.BodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.network.payloads.HealBodyPartPayload;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BodyHealthScreen extends Screen
{
    public static final ResourceLocation BODY_HEALTH_SCREEN = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/body_health_screen.png");
    public static final ResourceLocation TUTORIAL_PAGES = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/tutorial_pages.png");
    public static final int HEALTH_SCREEN_WIDTH = 176;
    public static final int HEALTH_SCREEN_HEIGHT = 183;
    // Limb hearts drawn in the limb boxes of the screen texture
    public static final int LIMB_HEART_SIZE = 7;
    public static final int LIMB_HEART_SPACING = 8;
    public static final int TEX_LIMB_HEART_X = 176;
    public static final int TEX_LIMB_HEART_Y = 47;
    public static final int TEX_LIMB_SHIELD_X = 176;
    public static final int TEX_LIMB_SHIELD_Y = 54;

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
    // Limb damage and shield of each limb, to flash the limbs that changed
    private final Map<BodyPartEnum, Integer> bodyPartState = new HashMap<>();
    private final Player player;
    private final InteractionHand hand;
    private final int limbHealing;
    private final boolean healsBrokenLimbs;
    private int leftPos;
    private int topPos;
    private int healingCharges;

    private boolean tutorialOpen = false;
    private int currentTutorialPage = 0;
    private TutorialToggleButton tutorialToggleButton;
    private TutorialNavButton tutorialLeftButton;
    private TutorialNavButton tutorialRightButton;
    private LimbEffectsPanel limbEffectsPanel;
    private static final int LIMB_EFFECTS_GAP = 2;
    private int leftArrowPressCounter = 0;
    private int rightArrowPressCounter = 0;

    /**
     * @param hand              hand holding the healing item, null to only look at the limbs
     * @param healingConsumable healing behavior of the item used, null to only look at the limbs
     */
    public BodyHealthScreen(Player player, @Nullable InteractionHand hand, @Nullable JsonHealingConsumable healingConsumable)
    {
        super(Component.translatable("screen." + LegendarySurvivalOverhaul.MOD_ID + ".body_health_screen"));

        this.player = player;
        this.hand = healingConsumable != null ? hand : null;
        this.healingCharges = healingConsumable != null ? healingConsumable.healingCharges : 0;
        this.limbHealing = healingConsumable != null ? healingConsumable.limbHealing : 0;
        this.healsBrokenLimbs = healingConsumable != null && healingConsumable.healsBrokenLimbs;
    }

    private static int getBodyPartState(IBodyDamageAttachment cap, BodyPartEnum bodyPart)
    {
        return cap.getLimbDamage(bodyPart) | (cap.hasLimbShield(bodyPart) ? 4 : 0);
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

        // Active limb effects, 2 pixels left of the screen and centered on its height
        limbEffectsPanel = new LimbEffectsPanel(this.leftPos - LIMB_EFFECTS_GAP - LimbEffectsPanel.WIDTH, this.topPos + HEALTH_SCREEN_HEIGHT / 2);
        limbEffectsPanel.setConditions(AttachmentUtil.getBodyDamageAttachment(player).getLimbConditions());
        addRenderableWidget(limbEffectsPanel);

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
        bodyPartState.clear();
        for (BodyPartEnum bodyPart : BodyPartEnum.values())
        {
            bodyPartState.put(bodyPart, getBodyPartState(cap, bodyPart));
        }
    }

    public void sendBodyPartHeal(BodyPartEnum bodyPart)
    {
        BodyDamageAttachment cap = AttachmentUtil.getBodyDamageAttachment(player);
        if (healingCharges > 0 && cap.canHealLimb(bodyPart, this.healsBrokenLimbs))
        {
            HealBodyPartPayload.sendToServer(bodyPart);
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
        limbEffectsPanel.setConditions(cap.getLimbConditions());
        for (BodyPartEnum bodyPart : BodyPartEnum.values())
        {
            int state = getBodyPartState(cap, bodyPart);
            if (bodyPartState.get(bodyPart) != state)
            {
                flashCounters.put(bodyPart, 4);
                bodyPartState.put(bodyPart, state);
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

        BodyDamageAttachment cap = AttachmentUtil.getBodyDamageAttachment(player);
        float healthRatio = cap.getLimbHealthRatio(bodyPart);

        BodyPartButton bodyPartButton = this.bodyPartButtons.get(bodyPart);

        // Update button health ratio
        bodyPartButton.setHealthRatio(healthRatio);

        // A limb can be chosen once per healing item, if the item can heal it
        bodyPartButton.active = this.hand != null && !bodyPartButton.isPressed && cap.canHealLimb(bodyPart, this.healsBrokenLimbs);

        bodyPartButton.render(gui, mouseX, mouseY, partialTicks);

        // Limb hearts the hovered limb would get back from the healing item
        int previewHearts = 0;
        if (bodyPartButton.active && bodyPartButton.isMouseOver(mouseX, mouseY))
            previewHearts = Math.min(this.limbHealing, cap.getLimbDamage(bodyPart));

        drawLimbHearts(gui, bodyPart, cap.getLimbDamage(bodyPart), previewHearts, cap.hasLimbShield(bodyPart));
    }

    /**
     * Draw the limb hearts of a limb in its box: remaining hearts from the left, then the hearts the hovered healing item
     * would restore, faded. A shield covers the most left heart, even if the limb is broken.
     */
    private void drawLimbHearts(GuiGraphics gui, BodyPartEnum bodyPart, int damage, int previewHearts, boolean shielded)
    {
        LimbHeartsBox box = LimbHeartsBox.get(bodyPart);
        int x = this.leftPos + box.x;
        int y = this.topPos + box.y;
        int remainingHearts = IBodyDamageAttachment.MAX_LIMB_DAMAGE - damage;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();

        // The limb blinks for a few ticks when it changes
        Integer flashCounter = flashCounters.get(bodyPart);
        if (flashCounter == null || flashCounter % 2 == 0)
        {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            for (int i = 0; i < remainingHearts; i++)
                gui.blit(BODY_HEALTH_SCREEN, x + i * LIMB_HEART_SPACING, y, TEX_LIMB_HEART_X, TEX_LIMB_HEART_Y, LIMB_HEART_SIZE, LIMB_HEART_SIZE);

            RenderSystem.setShaderColor(1, 1, 1, 0.5f);
            for (int i = remainingHearts; i < remainingHearts + previewHearts; i++)
                gui.blit(BODY_HEALTH_SCREEN, x + i * LIMB_HEART_SPACING, y, TEX_LIMB_HEART_X, TEX_LIMB_HEART_Y, LIMB_HEART_SIZE, LIMB_HEART_SIZE);
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (shielded)
            gui.blit(BODY_HEALTH_SCREEN, x, y, TEX_LIMB_SHIELD_X, TEX_LIMB_SHIELD_Y, LIMB_HEART_SIZE, LIMB_HEART_SIZE);

        RenderSystem.disableBlend();
    }

    /**
     * Top left corner of the 23 x 8 box holding the limb hearts of each limb, in the body health screen texture.
     */
    private enum LimbHeartsBox
    {
        HEAD(75, 46),
        LEFT_ARM(30, 80),
        RIGHT_ARM(121, 80),
        CHEST(75, 92),
        LEFT_LEG(42, 134),
        RIGHT_LEG(109, 134),
        LEFT_FOOT(42, 156),
        RIGHT_FOOT(109, 156);

        public final int x;
        public final int y;

        LimbHeartsBox(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public static LimbHeartsBox get(BodyPartEnum bodyPart)
        {
            return switch (bodyPart)
            {
                case HEAD -> HEAD;
                case LEFT_ARM -> LEFT_ARM;
                case RIGHT_ARM -> RIGHT_ARM;
                case CHEST -> CHEST;
                case LEFT_LEG -> LEFT_LEG;
                case RIGHT_LEG -> RIGHT_LEG;
                case LEFT_FOOT -> LEFT_FOOT;
                case RIGHT_FOOT -> RIGHT_FOOT;
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
