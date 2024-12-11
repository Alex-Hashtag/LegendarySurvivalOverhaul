package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.loot_modifiers.AdditionalLootTable;

import static sfiomn.legendarysurvivaloverhaul.data.loot.ModChestLootTables.heartFragmentInjectedLootTables;
import static sfiomn.legendarysurvivaloverhaul.data.loot.ModEntitiesLootTables.spongeInjectedLootTables;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output) {
        super(output, LegendarySurvivalOverhaul.MOD_ID);
    }

    @Override
    protected void start() {
        for (ResourceLocation lootTable: heartFragmentInjectedLootTables) {
            this.add(lootTable.getPath(), new AdditionalLootTable(
                    new LootItemCondition[]{LootTableIdCondition.builder(lootTable).build()},
                    new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID,
                            "inject/" + lootTable.getPath()),
                    false));
        }

        for (ResourceLocation lootTable: spongeInjectedLootTables) {
            this.add(lootTable.getPath(), new AdditionalLootTable(
                    new LootItemCondition[]{LootTableIdCondition.builder(lootTable).build()},
                    new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID,
                            "inject/" + lootTable.getPath()),
                    false));
        }
    }
}
