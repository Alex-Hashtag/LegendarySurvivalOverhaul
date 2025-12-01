package sfiomn.legendarysurvivaloverhaul.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;

public class PurityEnchantment extends Enchantment {
    
    public PurityEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{});
    }
    
    @Override
    public int getMinCost(int level) {
        return 20;
    }
    
    @Override
    public int getMaxCost(int level) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public boolean canEnchant(@NotNull ItemStack stack) {
        return stack.getItem() instanceof CanteenItem;
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        // Purity cannot be obtained from enchanting table
        return false;
    }
    
    @Override
    public boolean isTreasureOnly() {
        return true;
    }
    
    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }
}
