package sfiomn.legendarysurvivaloverhaul.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;

public class ReservoirEnchantment extends Enchantment {
    
    public ReservoirEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{});
    }
    
    @Override
    public int getMinCost(int level) {
        return 1 + level * 8;
    }
    
    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public boolean canEnchant(@NotNull ItemStack stack) {
        return stack.getItem() instanceof CanteenItem;
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof CanteenItem;
    }
    
    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }
}
