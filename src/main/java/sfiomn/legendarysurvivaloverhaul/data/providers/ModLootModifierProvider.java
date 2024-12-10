package sfiomn.legendarysurvivaloverhaul.data.providers;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.data.loot.ModLootTables;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootModifierProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final Set<ResourceLocation> requiredTables;
    private final List<LootTableProvider.SubProviderEntry> subProviders;

    public ModLootModifierProvider(PackOutput packOutput, Set<ResourceLocation> p_254481_, List<LootTableProvider.SubProviderEntry> p_253798_) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "loot_modifiers");
        this.subProviders = p_253798_;
        this.requiredTables = p_254481_;
    }

    public static ModLootModifierProvider createLootTables(PackOutput output) {
        return new ModLootModifierProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModLootTables::new, LootContextParamSets.CHEST)
        ));
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        final Map<ResourceLocation, LootTable> map = Maps.newHashMap();
        Map<RandomSupport.Seed128bit, ResourceLocation> map1 = new Object2ObjectOpenHashMap<>();
        this.getTables().forEach((subProviderEntry) -> {
            subProviderEntry.provider().get().generate((p_288259_, p_288260_) -> {
                ResourceLocation resourcelocation = map1.put(RandomSequence.seedForKey(p_288259_), p_288259_);
                if (resourcelocation != null) {
                    Util.logAndPauseIfInIde("Loot table random sequence seed collision on " + resourcelocation + " and " + p_288259_);
                }

                p_288260_.setRandomSequence(p_288259_);
                if (map.put(p_288259_, p_288260_.setParamSet(subProviderEntry.paramSet()).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + p_288259_);
                }
            });
        });
        ValidationContext validationcontext = new ValidationContext(LootContextParamSets.ALL_PARAMS, new LootDataResolver() {
            @Nullable
            public <T> T getElement(@NotNull LootDataId<T> lootDataId) {
                return lootDataId.type() == LootDataType.TABLE && map.get(lootDataId.location()) != null ? (T) map.get(lootDataId.location()) : null;
            }
        });
        this.validate(map, validationcontext);
        Multimap<String, String> multimap = validationcontext.getProblems();
        if (!multimap.isEmpty()) {
            multimap.forEach((p_124446_, p_124447_) -> {
                LOGGER.warn("Found validation problem in {}: {}", p_124446_, p_124447_);
            });
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            return CompletableFuture.allOf(map.entrySet().stream().map((p_278900_) -> {
                ResourceLocation resourcelocation = p_278900_.getKey();
                LootTable loottable = p_278900_.getValue();
                Path path = this.pathProvider.json(resourcelocation);
                return DataProvider.saveStable(cachedOutput, LootDataType.TABLE.parser().toJsonTree(loottable), path);
            }).toArray(CompletableFuture[]::new));
        }
    }

    public List<LootTableProvider.SubProviderEntry> getTables() {
        return this.subProviders;
    }

    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext) {

        for (ResourceLocation resourcelocation : Sets.difference(this.requiredTables, map.keySet())) {
            validationcontext.reportProblem("Missing built-in table: " + resourcelocation);
        }

        map.forEach((p_278897_, p_278898_) -> {
            p_278898_.validate(validationcontext.setParams(p_278898_.getParamSet()).enterElement("{" + p_278897_ + "}", new LootDataId(LootDataType.TABLE, p_278897_)));
        });
    }

    @Override
    public @NotNull String getName() {
        return "Loot Modifiers : " + LegendarySurvivalOverhaul.MOD_ID;
    }
}
