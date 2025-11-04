package sfiomn.legendarysurvivaloverhaul.api.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IBodyPartResistanceData;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IBodyPartsDamageSourceData;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IHealingConsumableData;
import sfiomn.legendarysurvivaloverhaul.data.builders.BodyPartResistanceData;
import sfiomn.legendarysurvivaloverhaul.data.builders.BodyPartsDamageSourceData;
import sfiomn.legendarysurvivaloverhaul.data.builders.HealingConsumableData;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class BodyDamageDataProvider implements DataProvider
{
    private final String modId;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final PackOutput.PathProvider consumablesPathProvider;
    private final PackOutput.PathProvider bodyPartsDamageSourcePathProvider;
    private final PackOutput.PathProvider bodyPartResistancePathProvider;
    private final Map<String, IHealingConsumableData> consumablesBuilders = new HashMap<>();
    private final Map<String, IBodyPartsDamageSourceData> bodyPartsDamageSourceBuilders = new HashMap<>();
    private final Map<String, IBodyPartResistanceData> bodyPartResistanceBuilders = new HashMap<>();
    private final ExistingFileHelper fileHelper;

    public BodyDamageDataProvider(String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        this.modId = modId;
        this.fileHelper = fileHelper;
        this.consumablesPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/body_damage/consumables");
        this.bodyPartsDamageSourcePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/body_damage/damage_sources");
        this.bodyPartResistancePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, LegendarySurvivalOverhaul.MOD_ID + "/body_damage/items");
        this.lookupProvider = lookupProvider;
    }

    public abstract void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper);

    @Nonnull
    public CompletableFuture<?> run(@Nonnull CachedOutput pOutput)
    {
        return this.lookupProvider.thenCompose((p_255484_) -> {
            List<CompletableFuture<?>> list = new ArrayList<>();
            this.generate(p_255484_, this.fileHelper);
            this.consumablesBuilders.forEach((consumable, builder) -> {
                ResourceLocation jsonKey = consumable.split(":").length == 1 ?
                        ResourceLocation.fromNamespaceAndPath(this.modId, consumable.toLowerCase()) : ResourceLocation.parse(consumable);
                list.add(DataProvider.saveStable(pOutput, builder.build(), this.consumablesPathProvider.json(jsonKey)));
            });
            this.bodyPartsDamageSourceBuilders.forEach((damageSource, builder) -> {
                Path path = this.bodyPartsDamageSourcePathProvider.json(ResourceLocation.fromNamespaceAndPath(this.modId, damageSource.toLowerCase()));
                list.add(DataProvider.saveStable(pOutput, builder.build(), path));
            });
            this.bodyPartResistanceBuilders.forEach((bodyResistance, builder) -> {
                ResourceLocation jsonKey = bodyResistance.split(":").length == 1 ?
                        ResourceLocation.fromNamespaceAndPath(this.modId, bodyResistance.toLowerCase()) : ResourceLocation.parse(bodyResistance);
                list.add(DataProvider.saveStable(pOutput, builder.build(), this.bodyPartResistancePathProvider.json(jsonKey)));
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    public final IHealingConsumableData consumable(String id)
    {
        return this.consumablesBuilders.computeIfAbsent(id, (k) -> new HealingConsumableData());
    }

    public final IHealingConsumableData consumable(Item item)
    {
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(item);
        assert itemRegistryName != null;
        return this.consumablesBuilders.computeIfAbsent(itemRegistryName.toString(), (k) -> new HealingConsumableData());
    }

    public final IBodyPartsDamageSourceData damageSource(String id)
    {
        return this.bodyPartsDamageSourceBuilders.computeIfAbsent(id, (k) -> new BodyPartsDamageSourceData());
    }

    public final IBodyPartResistanceData item(String id)
    {
        return this.bodyPartResistanceBuilders.computeIfAbsent(id, (k) -> new BodyPartResistanceData());
    }

    public final IBodyPartResistanceData item(Item item)
    {
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(item);
        assert itemRegistryName != null;
        return this.bodyPartResistanceBuilders.computeIfAbsent(itemRegistryName.toString(), (k) -> new BodyPartResistanceData());
    }

    @Nonnull
    public final String getName()
    {
        return "Body Damage for " + this.modId;
    }
}
