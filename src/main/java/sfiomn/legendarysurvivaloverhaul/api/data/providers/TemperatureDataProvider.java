package sfiomn.legendarysurvivaloverhaul.api.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
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

public abstract class TemperatureDataProvider implements DataProvider
{
    private final String modId;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final PackOutput.PathProvider consumablesPathProvider;
    private final PackOutput.PathProvider consumableBlocksPathProvider;
    private final PackOutput.PathProvider blocksPathProvider;
    private final PackOutput.PathProvider itemsPathProvider;
    private final PackOutput.PathProvider biomesPathProvider;
    private final PackOutput.PathProvider fuelItemPathProvider;
    private final PackOutput.PathProvider dimensionPathProvider;
    private final PackOutput.PathProvider mountPathProvider;
    private final PackOutput.PathProvider originPathProvider;
    private final Map<String, ITemperatureConsumableDataHolder> consumableBuilders = new HashMap<>();
    private final Map<String, ITemperatureConsumableBlockDataHolder> consumableBlockBuilders = new HashMap<>();
    private final Map<String, ITemperatureBlockDataHolder> blockBuilders = new HashMap<>();
    private final Map<String, ITemperatureResistanceData> itemBuilders = new HashMap<>();
    private final Map<String, ITemperatureBiomeOverrideData> biomeBuilders = new HashMap<>();
    private final Map<String, ITemperatureFuelItemData> fuelItemBuilders = new HashMap<>();
    private final Map<String, ITemperatureDimensionData> dimensionBuilders = new HashMap<>();
    private final Map<String, ITemperatureResistanceData> mountBuilders = new HashMap<>();
    private final Map<String, ITemperatureResistanceData> originBuilders = new HashMap<>();
    private final ExistingFileHelper fileHelper;

    public TemperatureDataProvider(String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        this.modId = modId;
        this.fileHelper = fileHelper;
        this.consumablesPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/consumables");
        this.consumableBlocksPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/consumable_blocks");
        this.blocksPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/blocks");
        this.itemsPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/items");
        this.biomesPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/biomes");
        this.fuelItemPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/fuel_items");
        this.dimensionPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/dimensions");
        this.mountPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/mounts");
        this.originPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/temperature/origins");
        this.lookupProvider = lookupProvider;
    }

    public abstract void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper);

    @Nonnull
    public CompletableFuture<?> run(@Nonnull CachedOutput pOutput)
    {
        return this.lookupProvider.thenCompose((p_255484_) -> {
            List<CompletableFuture<?>> list = new ArrayList<>();
            this.generate(p_255484_, this.fileHelper);
            this.consumableBuilders.forEach((consumable, builder) -> {
                ResourceLocation jsonKey = consumable.split(":").length == 1 ?
                        ResourceLocation.fromNamespaceAndPath(this.modId, consumable.toLowerCase()) : ResourceLocation.parse(consumable);
                list.add(DataProvider.saveStable(pOutput, builder.build(), this.consumablesPathProvider.json(jsonKey)));
            });
            this.consumableBlockBuilders.forEach((consumableBlock, builder) -> {
                ResourceLocation jsonKey = consumableBlock.split(":").length == 1 ?
                        ResourceLocation.fromNamespaceAndPath(this.modId, consumableBlock.toLowerCase()) : ResourceLocation.parse(consumableBlock);
                list.add(DataProvider.saveStable(pOutput, builder.build(), this.consumableBlocksPathProvider.json(jsonKey)));
            });
            this.blockBuilders.forEach((block, builder) -> {
                ResourceLocation jsonKey = block.split(":").length == 1 ?
                        ResourceLocation.fromNamespaceAndPath(this.modId, block.toLowerCase()) : ResourceLocation.parse(block);
                list.add(DataProvider.saveStable(pOutput, builder.build(), this.blocksPathProvider.json(jsonKey)));
            });
            this.itemBuilders.forEach((item, builder) -> {
                ResourceLocation jsonKey = item.split(":").length == 1 ?
                        ResourceLocation.fromNamespaceAndPath(this.modId, item.toLowerCase()) : ResourceLocation.parse(item);
                list.add(DataProvider.saveStable(pOutput, builder.build(), this.itemsPathProvider.json(jsonKey)));
            });
            this.biomeBuilders.forEach((biome, builder) -> {
                Path path = this.biomesPathProvider.json(ResourceLocation.fromNamespaceAndPath(this.modId, biome.toLowerCase()));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.fuelItemBuilders.forEach((fuelItem, builder) -> {
                ResourceLocation jsonKey = fuelItem.split(":").length == 1 ?
                        ResourceLocation.fromNamespaceAndPath(this.modId, fuelItem.toLowerCase()) : ResourceLocation.parse(fuelItem);
                list.add(DataProvider.saveStable(pOutput, builder.build(), this.fuelItemPathProvider.json(jsonKey)));
            });
            this.dimensionBuilders.forEach((dimension, builder) -> {
                Path path = this.dimensionPathProvider.json(ResourceLocation.fromNamespaceAndPath(this.modId, dimension.toLowerCase()));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.mountBuilders.forEach((mount, builder) -> {
                Path path = this.mountPathProvider.json(ResourceLocation.fromNamespaceAndPath(this.modId, mount.toLowerCase()));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.originBuilders.forEach((origin, builder) -> {
                Path path = this.originPathProvider.json(ResourceLocation.fromNamespaceAndPath(this.modId, origin.toLowerCase()));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    public final ITemperatureConsumableDataHolder consumable(String id)
    {
        return this.consumableBuilders.computeIfAbsent(id, (k) -> new TemperatureConsumableDataHolder());
    }

    public final ITemperatureConsumableDataHolder consumable(Item item)
    {
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(item);
        return this.consumableBuilders.computeIfAbsent(itemRegistryName.toString(), (k) -> new TemperatureConsumableDataHolder());
    }

    public final ITemperatureConsumableData temperatureConsumable(TemporaryModifierGroupEnum group)
    {
        return new TemperatureConsumableData().group(group);
    }

    public final ITemperatureConsumableBlockDataHolder consumableBlock(String id)
    {
        return this.consumableBlockBuilders.computeIfAbsent(id, (k) -> new TemperatureConsumableBlockDataHolder());
    }

    public final ITemperatureConsumableBlockDataHolder consumableAndConsumableBlock(String id, ITemperatureConsumableData data)
    {
        this.consumableBuilders.computeIfAbsent(id, (k) -> new TemperatureConsumableDataHolder().addTemperature(data));
        return this.consumableBlockBuilders.computeIfAbsent(id, (k) -> new TemperatureConsumableBlockDataHolder().addTemperature(data.asBlock()));
    }

    public final ITemperatureConsumableBlockData temperatureConsumableBlock(TemporaryModifierGroupEnum group)
    {
        return new TemperatureConsumableBlockData().group(group);
    }

    public final ITemperatureBlockDataHolder block(String id)
    {
        return this.blockBuilders.computeIfAbsent(id, (k) -> new TemperatureBlockDataHolder());
    }

    public final ITemperatureBlockDataHolder block(Block block)
    {
        ResourceLocation blockRegistryName = BuiltInRegistries.BLOCK.getKey(block);
        assert blockRegistryName != null;
        return this.blockBuilders.computeIfAbsent(blockRegistryName.toString(), (k) -> new TemperatureBlockDataHolder());
    }

    public final ITemperatureBlockData temperatureBlock(float temperatureValue)
    {
        return new TemperatureBlockData().temperature(temperatureValue);
    }

    public final ITemperatureResistanceData item(String id)
    {
        return this.itemBuilders.computeIfAbsent(id, (k) -> new TemperatureResistanceData());
    }

    public final ITemperatureResistanceData item(Item item)
    {
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(item);
        assert itemRegistryName != null;
        return this.itemBuilders.computeIfAbsent(itemRegistryName.toString(), (k) -> new TemperatureResistanceData());
    }

    public final ITemperatureBiomeOverrideData biome(String id)
    {
        return this.biomeBuilders.computeIfAbsent(id, (k) -> new TemperatureBiomeOverrideData());
    }

    public final ITemperatureFuelItemData fuelItem(String id)
    {
        return this.fuelItemBuilders.computeIfAbsent(id, (k) -> new TemperatureFuelItemData());
    }

    public final ITemperatureDimensionData dimension(String id)
    {
        return this.dimensionBuilders.computeIfAbsent(id, (k) -> new TemperatureDimensionData());
    }

    public final ITemperatureResistanceData mount(String id)
    {
        return this.mountBuilders.computeIfAbsent(id, (k) -> new TemperatureResistanceData());
    }

    public final ITemperatureResistanceData origin(String id)
    {
        return this.originBuilders.computeIfAbsent(id, (k) -> new TemperatureResistanceData());
    }

    @Nonnull
    public final String getName()
    {
        return "Temperature for " + this.modId;
    }
}
