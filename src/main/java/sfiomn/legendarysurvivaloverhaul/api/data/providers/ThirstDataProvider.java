package sfiomn.legendarysurvivaloverhaul.api.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstDataHolder;
import sfiomn.legendarysurvivaloverhaul.data.builders.ThirstData;
import sfiomn.legendarysurvivaloverhaul.data.builders.ThirstDataHolder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ThirstDataProvider implements DataProvider {
    private final String modId;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final PackOutput.PathProvider consumablesPathProvider;
    private final PackOutput.PathProvider blocksPathProvider;
    private final Map<String, IThirstDataHolder> consumablesBuilders = new HashMap<>();
    private final Map<String, IThirstDataHolder> blocksBuilders = new HashMap<>();
    private final ExistingFileHelper fileHelper;

    public ThirstDataProvider(String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        this.modId = modId;
        this.fileHelper = fileHelper;
        this.consumablesPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/thirst/consumables");
        this.blocksPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/thirst/blocks");
        this.lookupProvider = lookupProvider;
    }

    public abstract void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper);

    @Nonnull
    public CompletableFuture<?> run(@Nonnull CachedOutput pOutput) {
        return this.lookupProvider.thenCompose((p_255484_) -> {
            List<CompletableFuture<?>> list = new ArrayList<>();
            this.generate(p_255484_, this.fileHelper);
            this.consumablesBuilders.forEach((consumable, builder) -> {
                ResourceLocation jsonKey = consumable.split(":").length == 1 ?
                        new ResourceLocation(this.modId, consumable.toLowerCase()) : new ResourceLocation(consumable);
                list.add(DataProvider.saveStable(pOutput, builder.build(), this.consumablesPathProvider.json(jsonKey)));
            });
            this.blocksBuilders.forEach((block, builder) -> {
                ResourceLocation jsonKey = block.split(":").length == 1 ?
                        new ResourceLocation(this.modId, block.toLowerCase()) : new ResourceLocation(block);
                list.add(DataProvider.saveStable(pOutput, builder.build(), this.blocksPathProvider.json(jsonKey)));
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    public final IThirstDataHolder consumable(String id) {
        return this.consumablesBuilders.computeIfAbsent(id, (k) -> new ThirstDataHolder());
    }

    public final IThirstDataHolder consumable(Item item) {
        ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(item);
        assert itemRegistryName != null;
        return this.consumablesBuilders.computeIfAbsent(itemRegistryName.toString(), (k) -> new ThirstDataHolder());
    }

    public final IThirstDataHolder block(String id) {
        return this.blocksBuilders.computeIfAbsent(id, (k) -> new ThirstDataHolder());
    }

    public final IThirstDataHolder block(Block block) {
        ResourceLocation blockRegistryName = ForgeRegistries.BLOCKS.getKey(block);
        assert blockRegistryName != null;
        return this.blocksBuilders.computeIfAbsent(blockRegistryName.toString(), (k) -> new ThirstDataHolder());
    }

    public final IThirstDataHolder consumableAndBlock(String id, IThirstData data) {
        this.consumablesBuilders.computeIfAbsent(id, (k) -> new ThirstDataHolder().addThirst(data));
        return this.blocksBuilders.computeIfAbsent(id, (k) -> new ThirstDataHolder().addThirst(data));
    }

    public final IThirstData thirstData(int hydration, float saturation) {
        return new ThirstData().hydration(hydration).saturation(saturation);
    }

    @Nonnull
    public final String getName() {
        return "Thirst for " + this.modId;
    }
}
