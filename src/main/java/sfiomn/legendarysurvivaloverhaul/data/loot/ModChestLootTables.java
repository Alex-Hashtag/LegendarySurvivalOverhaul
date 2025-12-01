package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.EnchantmentRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.Map.entry;

public class ModChestLootTables implements LootTableSubProvider {

    public static final Item PURITY_BOOK = Items.ENCHANTED_BOOK;
    
    public static Map<ResourceLocation, List<Item>> chestInjectedLootTables = Map.ofEntries(
            entry(BuiltInLootTables.BURIED_TREASURE, List.of(ItemRegistry.HEART_FRAGMENT.get())),
            entry(BuiltInLootTables.JUNGLE_TEMPLE, List.of(ItemRegistry.HEART_FRAGMENT.get())),
            entry(BuiltInLootTables.ABANDONED_MINESHAFT, List.of(ItemRegistry.HEART_FRAGMENT.get())),
            entry(BuiltInLootTables.BASTION_TREASURE, List.of(ItemRegistry.HEART_FRAGMENT.get(), ItemRegistry.COLD_RESISTANCE_RING.get(), PURITY_BOOK)),
            entry(BuiltInLootTables.BASTION_BRIDGE, List.of(PURITY_BOOK)),
            entry(BuiltInLootTables.BASTION_HOGLIN_STABLE, List.of(PURITY_BOOK)),
            entry(BuiltInLootTables.BASTION_OTHER, List.of(PURITY_BOOK)),
            entry(BuiltInLootTables.NETHER_BRIDGE, List.of(PURITY_BOOK)),
            entry(BuiltInLootTables.DESERT_PYRAMID, List.of(ItemRegistry.HEAT_RESISTANCE_RING.get())),
            entry(BuiltInLootTables.PILLAGER_OUTPOST, List.of(ItemRegistry.FIRST_AID_SUPPLIES.get()))
    );

    public ModChestLootTables() {
    }

    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {

        LootPool.Builder heartFragmentLoot = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                .add(LootItem.lootTableItem(ItemRegistry.HEART_FRAGMENT.get()).setWeight(30))
                .add(LootItem.lootTableItem(ItemRegistry.HEART_FRAGMENT.get()).setWeight(5)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0f))))
                .add(EmptyLootItem.emptyItem().setWeight(80));

        LootPool.Builder heatResistanceRing = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                .add(LootItem.lootTableItem(ItemRegistry.HEAT_RESISTANCE_RING.get()).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(95));

        LootPool.Builder coldResistanceRing = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                .add(LootItem.lootTableItem(ItemRegistry.COLD_RESISTANCE_RING.get()).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(95));

        LootPool.Builder firstAidSupplies = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                .add(LootItem.lootTableItem(ItemRegistry.FIRST_AID_SUPPLIES.get()).setWeight(1))
                .add(EmptyLootItem.emptyItem().setWeight(99));

        LootPool.Builder purityBook = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(5)
                        .apply(new SetEnchantmentsFunction.Builder(false)
                                .withEnchantment(EnchantmentRegistry.PURITY.get(), ConstantValue.exactly(1))))
                .add(EmptyLootItem.emptyItem().setWeight(95));

        for (Map.Entry<ResourceLocation, List<Item>> entry : chestInjectedLootTables.entrySet()) {
            LootTable.Builder lootTable = LootTable.lootTable();
            if (entry.getValue().contains(ItemRegistry.HEART_FRAGMENT.get())) {
                lootTable.withPool(heartFragmentLoot);
            }
            if (entry.getValue().contains(ItemRegistry.HEAT_RESISTANCE_RING.get())) {
                lootTable.withPool(heatResistanceRing);
            }
            if (entry.getValue().contains(ItemRegistry.COLD_RESISTANCE_RING.get())) {
                lootTable.withPool(coldResistanceRing);
            }
            if (entry.getValue().contains(ItemRegistry.FIRST_AID_SUPPLIES.get())) {
                lootTable.withPool(firstAidSupplies);
            }
            if (entry.getValue().contains(PURITY_BOOK)) {
                lootTable.withPool(purityBook);
            }
            biConsumer.accept(
                    new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "inject/" + entry.getKey().getPath()),
                    lootTable);
        }
    }
}
