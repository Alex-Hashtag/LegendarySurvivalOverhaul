package sfiomn.legendarysurvivaloverhaul.common.enchantments;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.tags.ModItemTags;

public class ModEnchantments {
    
    public static final ResourceKey<Enchantment> REFRESHING = key("refreshing");
    public static final ResourceKey<Enchantment> PURITY = key("purity");
    public static final ResourceKey<Enchantment> RESERVOIR = key("reservoir");
    
    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, name));
    }
    
    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        // Refreshing enchantment - adds extra hydration/saturation when drinking
        register(context, REFRESHING, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ModItemTags.CANTEEN_ENCHANTABLE),
                        items.getOrThrow(ModItemTags.CANTEEN_ENCHANTABLE), // primary_items - for enchanting table
                        10, // weight (common)
                        3,  // max level
                        Enchantment.dynamicCost(1, 10),  // min cost: 1, 11, 21
                        Enchantment.dynamicCost(51, 10), // max cost: 51, 61, 71
                        2,  // anvil cost
                        EquipmentSlotGroup.ANY
                )
        ));
        
        // Purity enchantment - automatically purifies water in canteen (rare but obtainable)
        register(context, PURITY, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ModItemTags.CANTEEN_ENCHANTABLE),
                        items.getOrThrow(ModItemTags.CANTEEN_ENCHANTABLE), // primary_items - for enchanting table
                        2,  // weight (rare)
                        1,  // max level
                        Enchantment.constantCost(15),  // min/max cost: 15 (accessible at mid levels)
                        Enchantment.constantCost(65),  // max cost increased to allow higher level enchanting
                        4,  // anvil cost
                        EquipmentSlotGroup.ANY
                )
        ));
        
        // Reservoir enchantment - increases canteen capacity
        register(context, RESERVOIR, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ModItemTags.CANTEEN_ENCHANTABLE),
                        items.getOrThrow(ModItemTags.CANTEEN_ENCHANTABLE), // primary_items - for enchanting table
                        10, // weight
                        5,  // max level
                        Enchantment.dynamicCost(1, 8),
                        Enchantment.dynamicCost(16, 8),
                        1,  // anvil cost
                        EquipmentSlotGroup.ANY
                )
        ));
    }
    
    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}
