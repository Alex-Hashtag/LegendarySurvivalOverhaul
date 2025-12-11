package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.ModEnchantments;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.Map.entry;

public class ModChestLootTables implements LootTableSubProvider
{
    private static final Item PURITY_BOOK = Items.ENCHANTED_BOOK;

    public static Map<ResourceKey<LootTable>, List<Item>> chestInjectedLootTables = Map.ofEntries(
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

    private final HolderLookup.Provider registries;

    public ModChestLootTables(HolderLookup.Provider provider)
    {
        this.registries = provider;
    }

    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer)
    {

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

        HolderLookup.RegistryLookup<Enchantment> enchantmentLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootPool.Builder purityBook = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(5)
                        .apply(new EnchantRandomlyFunction.Builder().withEnchantment(enchantmentLookup.getOrThrow(ModEnchantments.PURITY))))
                .add(EmptyLootItem.emptyItem().setWeight(95));

        for (Map.Entry<ResourceKey<LootTable>, List<Item>> entry : chestInjectedLootTables.entrySet())
        {
            LootTable.Builder lootTable = LootTable.lootTable();
            if (entry.getValue().contains(ItemRegistry.HEART_FRAGMENT.get()))
            {
                lootTable.withPool(heartFragmentLoot);
            }
            if (entry.getValue().contains(ItemRegistry.HEAT_RESISTANCE_RING.get()))
            {
                lootTable.withPool(heatResistanceRing);
            }
            if (entry.getValue().contains(ItemRegistry.COLD_RESISTANCE_RING.get()))
            {
                lootTable.withPool(coldResistanceRing);
            }
            if (entry.getValue().contains(ItemRegistry.FIRST_AID_SUPPLIES.get()))
            {
                lootTable.withPool(firstAidSupplies);
            }
            if (entry.getValue().contains(PURITY_BOOK))
            {
                lootTable.withPool(purityBook);
            }
            biConsumer.accept(
                    ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "inject/" + entry.getKey().location().getPath())),
                    lootTable);
        }
    }
}
