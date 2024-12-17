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

public class ModFishingLootTables implements LootTableSubProvider {

    public static List<ResourceLocation> fishingInjectedLootTables = Arrays.asList(
            new ResourceLocation("gameplay/fishing/treasure")
    );

    public ModFishingLootTables() {
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {

        for (ResourceLocation lootTable : fishingInjectedLootTables) {
            biConsumer.accept(
                    new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "inject/" + lootTable.getPath()),
                    LootTable.lootTable().withPool(
                            LootPool.lootPool()
                                    .add(LootItem.lootTableItem(ItemRegistry.SPONGE.get()).setWeight(20))
                                    .add(EmptyLootItem.emptyItem().setWeight(80))
                    ));
        }
    }
}
