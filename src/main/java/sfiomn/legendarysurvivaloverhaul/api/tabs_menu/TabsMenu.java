package sfiomn.legendarysurvivaloverhaul.api.tabs_menu;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import sfiomn.legendarysurvivaloverhaul.client.screens.TabButton;

import java.util.*;

import static sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabBase.TAB_HEIGHT;

public class TabsMenu {
    private static final Map<Class<? extends Screen>, ScreenInfo> tabsScreens = new HashMap<>();
    private static int leftScreenPos;
    private static int topScreenPos;

    private TabsMenu() {
    }

    public static void updateButtonsPosition(Screen screen, int leftScreenPos, int topScreenPos) {
        if (TabsMenu.leftScreenPos != leftScreenPos || TabsMenu.topScreenPos != topScreenPos) {
            TabsMenu.leftScreenPos = leftScreenPos;
            TabsMenu.topScreenPos = topScreenPos;
            for (GuiEventListener button: screen.children()) {
                if (button instanceof TabButton tabButton) {
                    tabButton.updatePosition(TabsMenu.leftScreenPos, TabsMenu.topScreenPos);
                }
            }
        }
    }

    public static void addTabToScreen(TabBase newTab, Class<? extends Screen> screen, int screenWidth, int screenHeight, int priority) {
        if (tabsScreens.containsKey(screen)) {
            tabsScreens.get(screen).tabs.put(priority, newTab);
        } else {
            ScreenInfo screenInfo = new ScreenInfo(screenWidth, screenHeight, newTab, priority);
            tabsScreens.put(screen, screenInfo);
        }
    }

    public static void initScreenButtons(ScreenEvent.Init.Post event) {
        if (tabsScreens.containsKey(event.getScreen().getClass())) {
            ScreenInfo screenInfo = tabsScreens.get(event.getScreen().getClass());
            TabsMenu.leftScreenPos = (event.getScreen().width - screenInfo.width) / 2;
            TabsMenu.topScreenPos = (event.getScreen().height - screenInfo.height) / 2;

            if (TabsMenu.topScreenPos - TAB_HEIGHT >= 0) {
                int tabPositionIndex = 0;
                for (TabBase tabBase: screenInfo.tabs.values()) {
                    if (tabBase.isCurrentlyUsed(event.getScreen()))
                        event.addListener(new TabButton(tabBase, true, tabPositionIndex, TabsMenu.leftScreenPos, TabsMenu.topScreenPos, button -> {}));
                    else
                        event.addListener(new TabButton(tabBase, false, tabPositionIndex, TabsMenu.leftScreenPos, TabsMenu.topScreenPos, button -> tabBase.openTargetScreen(event.getScreen().getMinecraft().player)));
                    tabPositionIndex++;
                }
            }
        }
    }

    public static class ScreenInfo {
        public int width;
        public int height;
        public Map<Integer, TabBase> tabs;
        public ScreenInfo(int width, int height, TabBase newTab, int priority) {
            this.width = width;
            this.height = height;
            this.tabs = new TreeMap<>();
            this.tabs.put(priority, newTab);
        }
    }
}
