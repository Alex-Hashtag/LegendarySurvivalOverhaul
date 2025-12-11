package sfiomn.legendarysurvivaloverhaul.common.items.drink;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.ModEnchantments;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class LargeCanteenItem extends CanteenItem
{

    public LargeCanteenItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public int getMaxCapacity()
    {
        return Config.Baked.largeCanteenCapacity;
    }

    @Override
    public int getMaxCapacity(ItemStack stack, Level level)
    {
        int baseCapacity = getMaxCapacity();
        if (level == null) return baseCapacity;
        int reservoirLevel = stack.getEnchantmentLevel(level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(ModEnchantments.RESERVOIR));
        return baseCapacity + reservoirLevel;
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack stack)
    {
        if (ThirstUtil.getCapacityTag(stack) == 0)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + "." + "large_canteen_empty";

        if (ThirstUtil.getHydrationEnumTag(stack) == HydrationEnum.PURIFIED)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + "." + "large_canteen_purified";
        else
            return "item." + LegendarySurvivalOverhaul.MOD_ID + "." + "large_canteen";
    }
}
