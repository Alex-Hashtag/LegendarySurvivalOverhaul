package sfiomn.legendarysurvivaloverhaul.common.items.armor;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.EnumMap;
import java.util.List;

public class ArmorMaterialBase
{

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, LegendarySurvivalOverhaul.MOD_ID);

    // Snow armor material (defense: 1/2/1/1, ench: 17, leather equip sound, repair: wool, toughness 0, kb-res 0)
    public static final Holder<ArmorMaterial> SNOW = ARMOR_MATERIALS.register("snow", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 2);
                map.put(ArmorItem.Type.CHESTPLATE, 1);
                map.put(ArmorItem.Type.HELMET, 1);
                map.put(ArmorItem.Type.BODY, 0); // non-player
            }),
            17,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(ItemTags.WOOL),
            List.of(
                    new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "snow"))
            ),
            0,
            0
    ));

    // Desert armor material (same defense, ench: 19, leather equip sound, repair: leather)
    public static final Holder<ArmorMaterial> DESERT = ARMOR_MATERIALS.register("desert", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 2);
                map.put(ArmorItem.Type.CHESTPLATE, 1);
                map.put(ArmorItem.Type.HELMET, 1);
                map.put(ArmorItem.Type.BODY, 0);
            }),
            19,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(Items.LEATHER),
            List.of(
                    new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "desert"))
            ),
            0,
            0
    ));

    public static void register(IEventBus eventBus)
    {
        ARMOR_MATERIALS.register(eventBus);
    }
}
