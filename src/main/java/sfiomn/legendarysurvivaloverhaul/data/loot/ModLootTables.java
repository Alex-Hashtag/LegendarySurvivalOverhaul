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
import java.util.function.BiConsumer;

public class ModLootTables implements LootTableSubProvider {
    public ModLootTables() {
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {

        for (ResourceLocation lootTable : Arrays.asList(
                BuiltInLootTables.BURIED_TREASURE,
                BuiltInLootTables.JUNGLE_TEMPLE,
                BuiltInLootTables.ABANDONED_MINESHAFT,
                BuiltInLootTables.BASTION_TREASURE,
                BuiltInLootTables.STRONGHOLD_CORRIDOR
        )) {
            biConsumer.accept(
                    new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "inject/" + lootTable.getPath()),
                    LootTable.lootTable().withPool(
                            LootPool.lootPool()
                                    .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                    .add(LootItem.lootTableItem(ItemRegistry.HEART_FRAGMENT.get()).setWeight(15))
                                    .add(LootItem.lootTableItem(ItemRegistry.HEART_FRAGMENT.get()).setWeight(5)
                                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0f))))
                                    .add(EmptyLootItem.emptyItem().setWeight(80))
                    ));
        }
    }
}
