package sfiomn.legendarysurvivaloverhaul.client.screens;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbConditions;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbPenalty;

import java.util.ArrayList;
import java.util.List;

/**
 * Column of the active limb penalties, shown on the left of the body health screen. <br>
 * Built from limb_effects.png: the scroll bar part on the left, then the effect column stacked from a top, one middle per
 * visible effect and a bottom. Scrolls one effect at a time with the mouse wheel, the scroll button or the scroll track.
 */
public class LimbEffectsPanel extends AbstractWidget
{
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/limb_effects.png");
    private static final int TEXTURE_SIZE = 256;

    // Scroll bar part, on the left. Open on its right side, the effect column closes the frame
    private static final int SCROLL_PART_WIDTH = 15;
    private static final int SCROLL_PART_TEX_HEIGHT = 178;
    private static final int SCROLL_TRACK_X = 6;
    private static final int SCROLL_TRACK_Y = 7;
    private static final int SCROLL_TRACK_TEX_HEIGHT = 163;
    // The scroll bar part is cut in the track to fit the height of the effect column: its last rows are drawn at the bottom
    private static final int SCROLL_PART_BOTTOM_HEIGHT = SCROLL_PART_TEX_HEIGHT - SCROLL_TRACK_Y - SCROLL_TRACK_TEX_HEIGHT;
    private static final int TEX_SCROLL_BUTTON_X = 49;
    private static final int TEX_SCROLL_BUTTON_Y = 0;
    private static final int SCROLL_BUTTON_WIDTH = 7;
    private static final int SCROLL_BUTTON_HEIGHT = 15;

    // Effect column, right after the scroll bar part: a top, one middle per visible effect, a bottom
    private static final int COLUMN_TEX_X = 18;
    private static final int COLUMN_WIDTH = 31;
    private static final int TOP_TEX_Y = 0;
    private static final int TOP_HEIGHT = 5;
    private static final int MIDDLE_TEX_Y = 5;
    private static final int MIDDLE_HEIGHT = 23;
    private static final int BOTTOM_TEX_Y = 28;
    private static final int BOTTOM_HEIGHT = 6;
    // Effect icon, relative to its middle part (24, 9 on the texture)
    private static final int ICON_X = 24 - COLUMN_TEX_X;
    private static final int ICON_Y = 9 - MIDDLE_TEX_Y;
    private static final int ICON_SIZE = 16;
    // Effect icons: a grid of 16x16 icons, 2 per row, read left to right then top to bottom
    private static final int TEX_ICONS_X = 15;
    private static final int TEX_ICONS_Y = 63;
    private static final int ICONS_PER_ROW = 2;

    public static final int WIDTH = SCROLL_PART_WIDTH + COLUMN_WIDTH;
    public static final int MAX_VISIBLE_EFFECTS = 7;

    private final List<LimbPenalty> effects = new ArrayList<>();
    private final int centerY;
    private int conditions = -1;
    private int scroll;
    private boolean draggingScrollButton;
    private int dragOffset;
    private LimbPenalty tooltipEffect;

    /**
     * @param centerY the panel is centered vertically on it, whatever its height
     */
    public LimbEffectsPanel(int x, int centerY)
    {
        super(x, centerY, WIDTH, 0, Component.empty());
        this.centerY = centerY;
    }

    /**
     * Update the shown effects, only rebuilt when the limb conditions changed.
     */
    public void setConditions(int newConditions)
    {
        if (newConditions == conditions)
            return;
        conditions = newConditions;

        effects.clear();
        for (LimbPenalty penalty : LimbPenalty.cachedValues())
        {
            if (LimbConditions.has(conditions, penalty) && !isReplaced(penalty))
                effects.add(penalty);
        }
        scroll = Mth.clamp(scroll, 0, getMaxScroll());
        this.visible = !effects.isEmpty();
        this.height = TOP_HEIGHT + getVisibleEffects() * MIDDLE_HEIGHT + BOTTOM_HEIGHT;
        setY(centerY - this.height / 2);
    }

    /**
     * The weaker penalty of a pair isn't shown once the stronger one is active.
     */
    private boolean isReplaced(LimbPenalty penalty)
    {
        LimbPenalty stronger = switch (penalty)
        {
            case MUFFLED_HEARING -> LimbPenalty.DEAFENED_HEARING;
            case HEARTBEAT_ON_BREAK -> LimbPenalty.CONSTANT_HEARTBEAT;
            case FRAGILE_TORSO -> LimbPenalty.VERY_FRAGILE_TORSO;
            case HARD_FALLING -> LimbPenalty.VERY_HARD_FALLING;
            default -> null;
        };
        return stronger != null && LimbConditions.has(conditions, stronger);
    }

    private int getMaxScroll()
    {
        return Math.max(0, effects.size() - MAX_VISIBLE_EFFECTS);
    }

    private int getVisibleEffects()
    {
        return Math.min(effects.size(), MAX_VISIBLE_EFFECTS);
    }

    private int getTrackHeight()
    {
        return this.height - SCROLL_TRACK_Y - SCROLL_PART_BOTTOM_HEIGHT;
    }

    private int getScrollButtonY()
    {
        int maxScroll = getMaxScroll();
        int travel = getTrackHeight() - SCROLL_BUTTON_HEIGHT;
        return getY() + SCROLL_TRACK_Y + (maxScroll == 0 ? 0 : travel * scroll / maxScroll);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick)
    {
        int x = getX();
        int y = getY();

        // Scroll bar part, cut in the track to be as high as the effect column
        int topHeight = this.height - SCROLL_PART_BOTTOM_HEIGHT;
        gui.blit(TEXTURE, x, y, 0, 0, SCROLL_PART_WIDTH, topHeight, TEXTURE_SIZE, TEXTURE_SIZE);
        gui.blit(TEXTURE, x, y + topHeight, 0, SCROLL_PART_TEX_HEIGHT - SCROLL_PART_BOTTOM_HEIGHT, SCROLL_PART_WIDTH, SCROLL_PART_BOTTOM_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
        gui.blit(TEXTURE, x + SCROLL_TRACK_X, getScrollButtonY(), TEX_SCROLL_BUTTON_X, TEX_SCROLL_BUTTON_Y,
                SCROLL_BUTTON_WIDTH, SCROLL_BUTTON_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);

        // Effect column
        int columnX = x + SCROLL_PART_WIDTH;
        gui.blit(TEXTURE, columnX, y, COLUMN_TEX_X, TOP_TEX_Y, COLUMN_WIDTH, TOP_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);

        tooltipEffect = null;
        int rowY = y + TOP_HEIGHT;
        for (int i = 0; i < getVisibleEffects(); i++)
        {
            LimbPenalty effect = effects.get(scroll + i);
            gui.blit(TEXTURE, columnX, rowY, COLUMN_TEX_X, MIDDLE_TEX_Y, COLUMN_WIDTH, MIDDLE_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);

            int iconX = columnX + ICON_X;
            int iconY = rowY + ICON_Y;
            int iconIndex = getIconIndex(effect);
            gui.blit(TEXTURE, iconX, iconY, getIconTexX(iconIndex), getIconTexY(iconIndex), ICON_SIZE, ICON_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);

            if (mouseX >= iconX && mouseX < iconX + ICON_SIZE && mouseY >= iconY && mouseY < iconY + ICON_SIZE)
                tooltipEffect = effect;
            rowY += MIDDLE_HEIGHT;
        }

        gui.blit(TEXTURE, columnX, rowY, COLUMN_TEX_X, BOTTOM_TEX_Y, COLUMN_WIDTH, BOTTOM_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);

        if (tooltipEffect != null)
        {
            Screen screen = Minecraft.getInstance().screen;
            if (screen != null)
                screen.setTooltipForNextRenderPass(Tooltip.create(getEffectDescription(tooltipEffect)), DefaultTooltipPositioner.INSTANCE, true);
        }
    }

    private static Component getEffectDescription(LimbPenalty effect)
    {
        String key = "limb_penalty." + LegendarySurvivalOverhaul.MOD_ID + "." + effect.name().toLowerCase();
        int iconIndex = getIconIndex(effect);
        return Component.translatable(key).withStyle(ChatFormatting.RED)
                .append(Component.literal("\n"))
                .append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY))
                // TEMPORARY: icon position on limb_effects.png, to remove once the icons are drawn
                .append(Component.literal("\nIcon: " + getIconTexX(iconIndex) + ", " + getIconTexY(iconIndex)).withStyle(ChatFormatting.DARK_GRAY));
    }

    /**
     * Index of the effect icon in the icon grid. The weaker and stronger penalties of a pair share their icon.
     */
    private static int getIconIndex(LimbPenalty effect)
    {
        return switch (effect)
        {
            case MUFFLED_HEARING, DEAFENED_HEARING -> 0;
            case EAR_RINGING -> 1;
            case MOTION_BLUR -> 2;
            case LAZY_MOUSE -> 3;
            case HEARTBEAT_ON_BREAK, CONSTANT_HEARTBEAT -> 4;
            case FRAGILE_TORSO, VERY_FRAGILE_TORSO -> 5;
            case SHORTNESS_OF_BREATH -> 6;
            case SLOW_BOW_DRAW -> 7;
            case SLOW_EATING -> 8;
            case NO_SWIMMING -> 9;
            case SLOW_BANDAGING -> 10;
            case WEAK_PROJECTILES -> 11;
            case SLOW_SPRINT -> 12;
            case NO_SNEAKING -> 13;
            case HARD_FALLING, VERY_HARD_FALLING -> 14;
        };
    }

    private static int getIconTexX(int iconIndex)
    {
        return TEX_ICONS_X + (iconIndex % ICONS_PER_ROW) * ICON_SIZE;
    }

    private static int getIconTexY(int iconIndex)
    {
        return TEX_ICONS_Y + (iconIndex / ICONS_PER_ROW) * ICON_SIZE;
    }

    private boolean isOverScrollTrack(double mouseX, double mouseY)
    {
        return mouseX >= getX() + SCROLL_TRACK_X && mouseX < getX() + SCROLL_TRACK_X + SCROLL_BUTTON_WIDTH
                && mouseY >= getY() + SCROLL_TRACK_Y && mouseY < getY() + SCROLL_TRACK_Y + getTrackHeight();
    }

    private void scrollTo(double scrollButtonTop)
    {
        int maxScroll = getMaxScroll();
        double progress = (scrollButtonTop - getY() - SCROLL_TRACK_Y) / (getTrackHeight() - SCROLL_BUTTON_HEIGHT);
        scroll = Mth.clamp((int) Math.round(progress * maxScroll), 0, maxScroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        draggingScrollButton = false;
        if (!this.visible || button != 0 || getMaxScroll() == 0 || !isOverScrollTrack(mouseX, mouseY))
            return false;

        int buttonY = getScrollButtonY();
        if (mouseY >= buttonY && mouseY < buttonY + SCROLL_BUTTON_HEIGHT)
            dragOffset = (int) mouseY - buttonY;
        else
        {
            // Clicking the track jumps the scroll button under the mouse
            dragOffset = SCROLL_BUTTON_HEIGHT / 2;
            scrollTo(mouseY - dragOffset);
        }
        draggingScrollButton = true;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        if (!draggingScrollButton)
            return false;
        scrollTo(mouseY - dragOffset);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if (!draggingScrollButton)
            return false;
        draggingScrollButton = false;
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        if (!this.visible || !isMouseOver(mouseX, mouseY) || getMaxScroll() == 0)
            return false;
        scroll = Mth.clamp(scroll - (int) Math.signum(scrollY), 0, getMaxScroll());
        return true;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output)
    {
    }
}
