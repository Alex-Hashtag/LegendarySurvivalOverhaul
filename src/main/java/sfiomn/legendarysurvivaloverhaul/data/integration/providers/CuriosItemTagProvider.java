package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class CuriosItemTagProvider extends ItemTagsProvider
{
    public static final TagKey<Item> BELT_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "belt"));
    public static final TagKey<Item> NECKLACE_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "necklace"));
    public static final TagKey<Item> HEAD_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "head"));
    public static final TagKey<Item> BODY_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "body"));
    public static final TagKey<Item> CHARM_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "charm"));
    public static final TagKey<Item> RING_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "ring"));

    public CuriosItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, p_275322_, "curios", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(BELT_TAG)
                .add(ItemRegistry.THERMOMETER.get());
        this.tag(NECKLACE_TAG)
                .add(ItemRegistry.NETHER_CHALICE.get());
        this.tag(HEAD_TAG)
                .add(ItemRegistry.WATER_PURIFIER.get());
        this.tag(BODY_TAG)
                .add(ItemRegistry.SPONGE.get());
        this.tag(CHARM_TAG)
                .add(ItemRegistry.FIRST_AID_SUPPLIES.get());
        this.tag(RING_TAG)
                .add(ItemRegistry.HEAT_RESISTANCE_RING.get())
                .add(ItemRegistry.COLD_RESISTANCE_RING.get())
                .add(ItemRegistry.THERMAL_RESISTANCE_RING.get());
    }
}
