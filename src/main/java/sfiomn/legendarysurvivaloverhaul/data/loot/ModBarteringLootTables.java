package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class ModBarteringLootTables implements LootTableSubProvider
{

    public static List<ResourceLocation> barteringInjectedLootTables = List.of(
            ResourceLocation.parse("gameplay/piglin_bartering")
    );

    public ModBarteringLootTables(HolderLookup.Provider provider)
    {
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer)
    {

        for (ResourceLocation lootTable : barteringInjectedLootTables)
        {
            biConsumer.accept(
                    ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "inject/" + lootTable.getPath())),
                    LootTable.lootTable().withPool(
                            LootPool.lootPool()
                                    .add(LootItem.lootTableItem(ItemRegistry.NETHER_CHALICE.get()))
                                    .add(EmptyLootItem.emptyItem().setWeight(99))
                    ));
        }
    }
}
