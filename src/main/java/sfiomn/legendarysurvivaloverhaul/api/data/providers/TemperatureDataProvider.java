package sfiomn.legendarysurvivaloverhaul.api.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.*;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.data.builders.*;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class TemperatureDataProvider implements DataProvider {
    private final String modId;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final PackOutput.PathProvider consumablesPathProvider;
    private final PackOutput.PathProvider blocksPathProvider;
    private final PackOutput.PathProvider itemsPathProvider;
    private final PackOutput.PathProvider biomesPathProvider;
    private final PackOutput.PathProvider fuelItemPathProvider;
    private final PackOutput.PathProvider dimensionPathProvider;
    private final PackOutput.PathProvider mountPathProvider;
    private final Map<String, ITemperatureConsumableDataHolder> consumableBuilders = new HashMap<>();
    private final Map<String, ITemperatureBlockDataHolder> blockBuilders = new HashMap<>();
    private final Map<String, ITemperatureResistanceData> itemBuilders = new HashMap<>();
    private final Map<String, ITemperatureBiomeOverrideData> biomeBuilders = new HashMap<>();
    private final Map<String, ITemperatureFuelItemData> fuelItemBuilders = new HashMap<>();
    private final Map<String, ITemperatureData> dimensionBuilders = new HashMap<>();
    private final Map<String, ITemperatureData> mountBuilders = new HashMap<>();
    private final ExistingFileHelper fileHelper;

    public TemperatureDataProvider(String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        this.modId = modId;
        this.fileHelper = fileHelper;
        this.consumablesPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/consumables");
        this.blocksPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/blocks");
        this.itemsPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/items");
        this.biomesPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/biomes");
        this.fuelItemPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/fuel_items");
        this.dimensionPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/dimensions");
        this.mountPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/mounts");
        this.lookupProvider = lookupProvider;
    }

    public abstract void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper);

    @Nonnull
    public CompletableFuture<?> run(@Nonnull CachedOutput pOutput) {
        return this.lookupProvider.thenCompose((p_255484_) -> {
            List<CompletableFuture<?>> list = new ArrayList<>();
            this.generate(p_255484_, this.fileHelper);
            this.consumableBuilders.forEach((consumable, builder) -> {
                Path path = this.consumablesPathProvider.json(new ResourceLocation(this.modId, consumable));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.blockBuilders.forEach((block, builder) -> {
                Path path = this.blocksPathProvider.json(new ResourceLocation(this.modId, block));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.itemBuilders.forEach((block, builder) -> {
                Path path = this.itemsPathProvider.json(new ResourceLocation(this.modId, block));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.biomeBuilders.forEach((block, builder) -> {
                Path path = this.biomesPathProvider.json(new ResourceLocation(this.modId, block));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.fuelItemBuilders.forEach((block, builder) -> {
                Path path = this.fuelItemPathProvider.json(new ResourceLocation(this.modId, block));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.dimensionBuilders.forEach((block, builder) -> {
                Path path = this.dimensionPathProvider.json(new ResourceLocation(this.modId, block));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.mountBuilders.forEach((block, builder) -> {
                Path path = this.mountPathProvider.json(new ResourceLocation(this.modId, block));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    public final ITemperatureConsumableDataHolder consumable(String id) {
        return this.consumableBuilders.computeIfAbsent(id, (k) -> new TemperatureConsumableDataHolder());
    }

    public final ITemperatureConsumableData temperatureConsumable(TemporaryModifierGroupEnum group) {
        return new TemperatureConsumableData().group(group);
    }

    public final ITemperatureBlockDataHolder block(String id) {
        return this.blockBuilders.computeIfAbsent(id, (k) -> new TemperatureBlockDataHolder());
    }

    public final ITemperatureBlockData temperatureBlock(float temperatureValue) {
        return new TemperatureBlockData().temperature(temperatureValue);
    }

    public final ITemperatureResistanceData item(String id) {
        return this.itemBuilders.computeIfAbsent(id, (k) -> new TemperatureItemData());
    }

    public final ITemperatureBiomeOverrideData biome(String id) {
        return this.biomeBuilders.computeIfAbsent(id, (k) -> new TemperatureBiomeOverrideData());
    }

    public final ITemperatureFuelItemData fuelItem(String id) {
        return this.fuelItemBuilders.computeIfAbsent(id, (k) -> new TemperatureFuelItemData());
    }

    public final ITemperatureData dimension(String id) {
        return this.dimensionBuilders.computeIfAbsent(id, (k) -> new TemperatureDimensionData());
    }

    public final ITemperatureData mount(String id) {
        return this.mountBuilders.computeIfAbsent(id, (k) -> new TemperatureMountData());
    }

    @Nonnull
    public final String getName() {
        return "Temperature for " + this.modId;
    }
}
