package sfiomn.legendarysurvivaloverhaul.client.tabs_menu;

import majik.rereskillable.client.screen.SkillScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabBase;
import sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabsMenu;
import sfiomn.legendarysurvivaloverhaul.client.screens.BodyHealthScreen;
import sfiomn.legendarysurvivaloverhaul.client.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;

public class BodyDamageTab extends TabBase {
    private ResourceLocation TAB_ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/tab_menu_buttons.png");
    private final int TAB_ICON_TEX_X = 0;
    private final int TAB_ICON_TEX_Y = 23;

    public BodyDamageTab() {
        super();
    }

    @Override
    public void openTargetScreen(Player player) {
        if (Config.Baked.localizedBodyDamageEnabled)
            ClientHooks.openBodyHealthScreen(player);
    }

    @Override
    public boolean isEnabled() {
        return Config.Baked.bodyDamageTabEnabled && Config.Baked.localizedBodyDamageEnabled;
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
        return Config.Baked.localizedBodyDamageEnabled && currentScreen instanceof BodyHealthScreen;
    }

    @Override
    public void initTabOnScreens() {
        TabsMenu.addTabToScreen(this, InventoryScreen.class, 176, 166, 50);

        if (LegendarySurvivalOverhaul.curiosLoaded)
            TabsMenu.addTabToScreen(this, CuriosScreenV2.class, 176, 166, 50);

        if (Config.Baked.localizedBodyDamageEnabled)
            TabsMenu.addTabToScreen(this, BodyHealthScreen.class, 176, 183, 50);

        if (LegendarySurvivalOverhaul.reskillableLoaded)
            TabsMenu.addTabToScreen(this, SkillScreen.class, 176, 166, 50);
    }
}
