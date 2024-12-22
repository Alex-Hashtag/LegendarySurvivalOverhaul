package sfiomn.legendarysurvivaloverhaul.api.tabs_menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;


public abstract class TabBase {
    public static final int TAB_HEIGHT = 22;
    public static final int TAB_WIDTH = 26;

    public TabBase() {
    }

    public abstract void openTargetScreen(Player player);

    public abstract boolean isEnabled();

    public abstract void initTabOnScreens();

    public abstract void render(GuiGraphics gui, int x, int y, boolean hover);

    public abstract boolean isCurrentlyUsed(Screen currentScreen);
}
