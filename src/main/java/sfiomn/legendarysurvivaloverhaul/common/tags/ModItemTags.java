package sfiomn.legendarysurvivaloverhaul.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public final class ModItemTags {
    public static final TagKey<Item> CANTEEN_ENCHANTABLE = ItemTags.create(
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "enchantable/canteen")
    );

    private ModItemTags() {}
}
