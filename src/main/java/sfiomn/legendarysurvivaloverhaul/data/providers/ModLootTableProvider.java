package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import sfiomn.legendarysurvivaloverhaul.data.loot.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider
{
    public static LootTableProvider createLootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(ModChestLootTables::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(ModEntityLootTables::new, LootContextParamSets.ENTITY),
                new LootTableProvider.SubProviderEntry(ModFishingLootTables::new, LootContextParamSets.FISHING),
                new LootTableProvider.SubProviderEntry(ModBarteringLootTables::new, LootContextParamSets.PIGLIN_BARTER)
        ), lookupProvider);
    }
}
