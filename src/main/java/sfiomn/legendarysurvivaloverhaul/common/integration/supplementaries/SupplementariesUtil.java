package sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries;

import net.mehvahdjukaar.supplementaries.common.items.LunchBoxItem;
import net.mehvahdjukaar.supplementaries.common.items.components.LunchBaskedContent;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class SupplementariesUtil
{

    public static ItemStack getSelectedItemInLunchBasket(ItemStack stack)
    {
        if (!LegendarySurvivalOverhaul.supplementariesLoaded) return ItemStack.EMPTY;
        if (!(stack.getItem() instanceof LunchBoxItem lb)) return ItemStack.EMPTY;

        // Read the lunch box’s data component
        LunchBaskedContent data = stack.get(lb.getComponentType());
        if (data == null || !data.canEatFrom()) return ItemStack.EMPTY;

        return data.getSelected();
    }
}
