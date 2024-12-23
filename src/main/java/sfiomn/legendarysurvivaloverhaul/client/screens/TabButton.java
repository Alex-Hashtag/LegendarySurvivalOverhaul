package sfiomn.legendarysurvivaloverhaul.client.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import static sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabBase.TAB_HEIGHT;
import static sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabBase.TAB_WIDTH;

public class TabButton extends Button {
    public int tabPositionIndex;
    public TabBase tabBase;
    public boolean isDisabled;

    public TabButton(TabBase tabBase, boolean disabled, int tabPositionIndex, int leftScreenPos, int topScreenPos, OnPress press) {
        super(leftScreenPos + tabPositionIndex * (TAB_WIDTH + 1) + Config.Baked.tabsMenuOffsetX, topScreenPos - TAB_HEIGHT + Config.Baked.tabsMenuOffsetY, TAB_WIDTH, TAB_HEIGHT, Component.literal(""), press, DEFAULT_NARRATION);
        this.tabBase = tabBase;
        this.tabPositionIndex = tabPositionIndex;
        this.isDisabled = disabled;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partial) {
        this.tabBase.render(gui, this.getX(), this.getY(), this.isDisabled || this.isMouseOver(mouseX, mouseY));
    }

    public void updatePosition(int leftScreenPos, int topScreenPos) {
        setX(leftScreenPos + tabPositionIndex * (TAB_WIDTH + 1) + Config.Baked.tabsMenuOffsetX);
        setY(topScreenPos - TAB_HEIGHT + Config.Baked.tabsMenuOffsetY);
    }
}
