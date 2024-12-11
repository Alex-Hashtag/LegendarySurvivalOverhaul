package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class ModEntitiesLootTables implements LootTableSubProvider {

    public static List<ResourceLocation> spongeInjectedLootTables = Arrays.asList(
            new ResourceLocation("entities/drowned")
    );

    public ModEntitiesLootTables() {
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {

        for (ResourceLocation lootTable : spongeInjectedLootTables) {
            biConsumer.accept(
                    new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "inject/" + lootTable.getPath()),
                    LootTable.lootTable().withPool(
                            LootPool.lootPool()
                                    .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                    .add(LootItem.lootTableItem(ItemRegistry.SPONGE.get()).setWeight(5))
                                    .add(EmptyLootItem.emptyItem().setWeight(100))
                    ));
        }
    }
}
