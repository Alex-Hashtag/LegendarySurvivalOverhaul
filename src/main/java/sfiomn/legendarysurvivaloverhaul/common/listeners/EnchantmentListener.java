package sfiomn.legendarysurvivaloverhaul.common.listeners;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.ModEnchantments;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;

@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EnchantmentListener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        
        if (left.isEmpty() || right.isEmpty()) return;
        if (!(left.getItem() instanceof CanteenItem)) return;
        
        boolean shouldPurify = false;
        
        // Check if right item is an enchanted book with Purity
        if (right.getItem() == Items.ENCHANTED_BOOK) {
            ItemEnchantments bookEnchantments = right.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
            
            for (Holder<Enchantment> enchHolder : bookEnchantments.keySet()) {
                if (enchHolder.is(ModEnchantments.PURITY)) {
                    shouldPurify = true;
                    break;
                }
            }
        }
        
        // Handle combining two canteens where one has Purity
        if (right.getItem() instanceof CanteenItem) {
            ItemEnchantments rightEnchantments = right.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            
            for (Holder<Enchantment> enchHolder : rightEnchantments.keySet()) {
                if (enchHolder.is(ModEnchantments.PURITY)) {
                    shouldPurify = true;
                    break;
                }
            }
        }
        
        // If we should purify, modify the output after vanilla/other mods have set it
        if (shouldPurify) {
            ItemStack output = event.getOutput();
            if (!output.isEmpty() && output.getItem() instanceof CanteenItem) {
                // Check if the output actually has Purity enchantment applied
                ItemEnchantments outputEnchantments = output.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                boolean hasPurity = false;
                
                for (Holder<Enchantment> enchHolder : outputEnchantments.keySet()) {
                    if (enchHolder.is(ModEnchantments.PURITY)) {
                        hasPurity = true;
                        break;
                    }
                }
                
                // Only purify if Purity enchantment is actually on the output
                if (hasPurity) {
                    // Create a copy and purify it
                    ItemStack newOutput = output.copy();
                    CanteenItem.onPurityApplied(newOutput);
                    event.setOutput(newOutput);
                }
            }
        }
    }
    
    /**
     * Handle enchanting from enchanting table
     */
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();
        Player player = event.getEntity();
        
        // Check if the result is a canteen with Purity enchantment
        if (!result.isEmpty() && result.getItem() instanceof CanteenItem) {
            if (player.level().isClientSide) return;
            
            // Check if it has Purity enchantment
            ItemEnchantments enchantments = result.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            
            for (Holder<Enchantment> enchHolder : enchantments.keySet()) {
                if (enchHolder.is(ModEnchantments.PURITY)) {
                    CanteenItem.onPurityApplied(result);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onGrindstone(GrindstoneEvent.OnPlaceItem event) {
        ItemStack topItem = event.getTopItem();
        ItemStack bottomItem = event.getBottomItem();
        
        // Check if either item is a canteen with Purity
        if (topItem.getItem() instanceof CanteenItem) {
            // Grindstone removes enchantments, but water will remain purified
            // No special handling needed
        }
        
        if (bottomItem.getItem() instanceof CanteenItem) {
            // Same as above
        }
    }
}
