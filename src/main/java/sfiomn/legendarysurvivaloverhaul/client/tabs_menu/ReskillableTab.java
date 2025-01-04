package sfiomn.legendarysurvivaloverhaul.client.tabs_menu;

import majik.rereskillable.client.screen.SkillScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.violetmoon.quark.addons.oddities.client.screen.BackpackInventoryScreen;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabBase;
import sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabsMenu;
import sfiomn.legendarysurvivaloverhaul.client.screens.BodyHealthScreen;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;

public class ReskillableTab extends TabBase {
    private ResourceLocation TAB_ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/tab_menu_buttons.png");
    private final int TAB_ICON_TEX_X = 27;
    private final int TAB_ICON_TEX_Y = 0;

    public ReskillableTab() {
        super();
    }

    @Override
    public void openTargetScreen(Player player) {
        if (LegendarySurvivalOverhaul.reskillableLoaded)
            Minecraft.getInstance().setScreen(new SkillScreen());
    }

    @Override
    public boolean isEnabled() {
        return Config.Baked.reskillableTabEnabled && LegendarySurvivalOverhaul.reskillableLoaded;
    }

    @Override
    public void render(GuiGraphics gui, int x, int y, boolean hover) {
        int texOffsetX = 0;
        if (hover)
            texOffsetX = 54;

        gui.blit(TAB_ICONS, x, y,TAB_ICON_TEX_X + texOffsetX, TAB_ICON_TEX_Y, TAB_WIDTH, TAB_HEIGHT);
    }

    @Override
    public boolean isCurrentlyUsed(Screen currentScreen) {
        return LegendarySurvivalOverhaul.reskillableLoaded && currentScreen instanceof SkillScreen;
    }

    @Override
    public void initTabOnScreens() {
        TabsMenu.addTabToScreen(this, InventoryScreen.class, 176, 166, 30);

        if (LegendarySurvivalOverhaul.curiosLoaded)
            TabsMenu.addTabToScreen(this, CuriosScreenV2.class, 176, 166, 30);

        if (Config.Baked.localizedBodyDamageEnabled)
            TabsMenu.addTabToScreen(this, BodyHealthScreen.class, 176, 183, 30);

        if (LegendarySurvivalOverhaul.reskillableLoaded)
            TabsMenu.addTabToScreen(this, SkillScreen.class, 176, 166, 30);

        if (LegendarySurvivalOverhaul.quarkOdditiesLoaded)
            TabsMenu.addTabToScreen(this, BackpackInventoryScreen.class, 176, 224, 30);
    }
}
