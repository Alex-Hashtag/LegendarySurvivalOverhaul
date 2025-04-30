package sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries;

import net.mehvahdjukaar.supplementaries.common.items.LunchBoxItem;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import static net.mehvahdjukaar.supplementaries.common.items.forge.LunchBoxItemImpl.getLunchBoxData;


public class SupplementariesUtil {

    public static ItemStack getSelectedItemInLunchBasket(ItemStack itemStack) {
        if (LegendarySurvivalOverhaul.supplementariesLoaded && itemStack.getItem() instanceof LunchBoxItem)
            return getLunchBoxData(itemStack).getSelected();
        else
            return ItemStack.EMPTY;
    }
}
