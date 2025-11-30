package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.loot_modifiers.AdditionalLootTable;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.data.loot.ModBarteringLootTables.barteringInjectedLootTables;
import static sfiomn.legendarysurvivaloverhaul.data.loot.ModChestLootTables.chestInjectedLootTables;
import static sfiomn.legendarysurvivaloverhaul.data.loot.ModEntityLootTables.entityInjectedLootTables;
import static sfiomn.legendarysurvivaloverhaul.data.loot.ModFishingLootTables.fishingInjectedLootTables;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider
{

    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider, LegendarySurvivalOverhaul.MOD_ID);
    }

    @Override
    protected void start()
    {
        for (ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootTableKey : chestInjectedLootTables.keySet())
        {
            ResourceLocation lootTable = lootTableKey.location();
            this.add(lootTable.getPath(), new AdditionalLootTable(
                    new LootItemCondition[]{LootTableIdCondition.builder(lootTable).build()},
                    ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "inject/" + lootTable.getPath()),
                    false
            ));
        }


        for (ResourceLocation lootTable : entityInjectedLootTables)
        {
            this.add(lootTable.getPath(), new AdditionalLootTable(
                    new LootItemCondition[]{LootTableIdCondition.builder(lootTable).build()},
                    ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "inject/" + lootTable.getPath()),
                    false));
        }

        for (ResourceLocation lootTable : fishingInjectedLootTables)
        {
            this.add(lootTable.getPath(), new AdditionalLootTable(
                    new LootItemCondition[]{LootTableIdCondition.builder(lootTable).build()},
                    ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "inject/" + lootTable.getPath()),
                    false));
        }

        for (ResourceLocation lootTable : barteringInjectedLootTables)
        {
            this.add(lootTable.getPath(), new AdditionalLootTable(
                    new LootItemCondition[]{LootTableIdCondition.builder(lootTable).build()},
                    ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "inject/" + lootTable.getPath()),
                    false));
        }
    }
}
