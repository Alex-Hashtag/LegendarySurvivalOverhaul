package sfiomn.legendarysurvivaloverhaul.util;

import sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabBase;

import static sfiomn.legendarysurvivaloverhaul.registry.TabsMenuRegistry.TABS_MENU_REGISTRY;

public class TabsMenuUtil {
    public static void initializeTabsMenu() {
        for (TabBase tabBase : TABS_MENU_REGISTRY.get().getValues()) {
            if (tabBase.isEnabled()) {
                tabBase.initTabOnScreens();
            }
        }
    }
}
