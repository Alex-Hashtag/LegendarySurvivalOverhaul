package sfiomn.legendarysurvivaloverhaul.common.tabs_menu;

import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
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
import sfiomn.legendarysurvivaloverhaul.config.Config;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;

public class FtbQuestsTab extends TabBase {
    private ResourceLocation TAB_ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/tab_menu_buttons.png");
    private final int TAB_ICON_TEX_X = 27;
    private final int TAB_ICON_TEX_Y = 23;

    public FtbQuestsTab() {
        super();
    }

    @Override
    public void openTargetScreen(Player player) {
        if (LegendarySurvivalOverhaul.ftbQuestsLoaded && player.level().isClientSide)
            FTBQuestsClient.openGui();
    }

    @Override
    public boolean isEnabled() {
        return Config.Baked.ftbQuestsTabEnabled && LegendarySurvivalOverhaul.ftbQuestsLoaded;
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
        return false;
    }

    @Override
    public void initTabOnScreens() {
        TabsMenu.addTabToScreen(this, InventoryScreen.class, 176, 166, 70);

        if (LegendarySurvivalOverhaul.curiosLoaded)
            TabsMenu.addTabToScreen(this, CuriosScreenV2.class, 176, 166, 70);

        if (Config.Baked.localizedBodyDamageEnabled)
            TabsMenu.addTabToScreen(this, BodyHealthScreen.class, 176, 183, 70);

        if (LegendarySurvivalOverhaul.reskillableLoaded)
            TabsMenu.addTabToScreen(this, SkillScreen.class, 176, 166, 70);
    }
}
