package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.data.loot.LootTableSubProvider;
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

public class ModBarteringLootTables implements LootTableSubProvider {

    public static List<ResourceLocation> barteringInjectedLootTables = Arrays.asList(
            new ResourceLocation("gameplay/piglin_bartering")
    );

    public ModBarteringLootTables() {
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {

        for (ResourceLocation lootTable : barteringInjectedLootTables) {
            biConsumer.accept(
                    new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "inject/" + lootTable.getPath()),
                    LootTable.lootTable().withPool(
                            LootPool.lootPool()
                                    .add(LootItem.lootTableItem(ItemRegistry.NETHER_CHALICE.get()))
                                    .add(EmptyLootItem.emptyItem().setWeight(99))
                    ));
        }
    }
}
