package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public class ModItemModelProvider extends ItemModelProvider
{
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, LegendarySurvivalOverhaul.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        simpleJuiceItem(ItemRegistry.APPLE_JUICE);
        simpleJuiceItem(ItemRegistry.BEETROOT_JUICE);
        simpleJuiceItem(ItemRegistry.CACTUS_JUICE);
        simpleJuiceItem(ItemRegistry.CARROT_JUICE);
        simpleJuiceItem(ItemRegistry.CHORUS_FRUIT_JUICE);
        simpleJuiceItem(ItemRegistry.GLISTERING_MELON_JUICE);
        simpleJuiceItem(ItemRegistry.GOLDEN_APPLE_JUICE);
        simpleJuiceItem(ItemRegistry.GOLDEN_CARROT_JUICE);
        simpleJuiceItem(ItemRegistry.MELON_JUICE);
        simpleJuiceItem(ItemRegistry.PUMPKIN_JUICE);
        simpleJuiceItem(ItemRegistry.SWEET_BERRIES_JUICE);
        simpleJuiceItem(ItemRegistry.GLOW_BERRIES_JUICE);
        simpleJuiceItem(ItemRegistry.GOLDEN_APPLE_JUICE);

        simpleHealingItem(ItemRegistry.BANDAGE);
        simpleHealingItem(ItemRegistry.HEALING_HERBS);
        simpleHealingItem(ItemRegistry.MEDKIT);
        simpleHealingItem(ItemRegistry.MORPHINE);
        simpleHealingItem(ItemRegistry.PLASTER);
        simpleHealingItem(ItemRegistry.TONIC);

        canteenItem(ItemRegistry.CANTEEN);
        canteenItem(ItemRegistry.LARGE_CANTEEN);

        simpleItem(ItemRegistry.PURIFIED_WATER_BOTTLE);
        simpleItem(ItemRegistry.ICE_FERN);
        simpleItem(ItemRegistry.ICE_FERN_SEEDS);
        simpleItem(ItemRegistry.ICE_FERN_GOLD);
        simpleItem(ItemRegistry.SUN_FERN);
        simpleItem(ItemRegistry.SUN_FERN_SEEDS);
        simpleItem(ItemRegistry.SUN_FERN_GOLD);
        simpleItem(ItemRegistry.WATER_PLANT_BAG);
        simpleItem(ItemRegistry.WATER_PLANT_SEEDS);
        simpleItem(ItemRegistry.COLD_STRING);
        simpleItem(ItemRegistry.WARM_STRING);
        simpleItem(ItemRegistry.NETHER_CHALICE);
        simpleItem(ItemRegistry.SPONGE);
        simpleItem(ItemRegistry.WATER_PURIFIER);
        simpleItem(ItemRegistry.HEAT_RESISTANCE_RING);
        simpleItem(ItemRegistry.COLD_RESISTANCE_RING);
        simpleItem(ItemRegistry.THERMAL_RESISTANCE_RING);
        simpleItem(ItemRegistry.FIRST_AID_SUPPLIES);
        simpleItem(ItemRegistry.HEART_CONTAINER);
        simpleItem(ItemRegistry.HEART_FRAGMENT);
        simpleItem(ItemRegistry.SHIELD_CONTAINER);

        simpleCoatItem(ItemRegistry.COOLING_COAT_1);
        simpleCoatItem(ItemRegistry.COOLING_COAT_2);
        simpleCoatItem(ItemRegistry.COOLING_COAT_3);
        simpleCoatItem(ItemRegistry.HEATING_COAT_1);
        simpleCoatItem(ItemRegistry.HEATING_COAT_2);
        simpleCoatItem(ItemRegistry.HEATING_COAT_3);
        simpleCoatItem(ItemRegistry.THERMAL_COAT_1);
        simpleCoatItem(ItemRegistry.THERMAL_COAT_2);
        simpleCoatItem(ItemRegistry.THERMAL_COAT_3);

        simpleArmorItem(ItemRegistry.DESERT_BOOTS);
        simpleArmorItem(ItemRegistry.DESERT_CHEST);
        simpleArmorItem(ItemRegistry.DESERT_HELMET);
        simpleArmorItem(ItemRegistry.DESERT_LEGGINGS);
        simpleArmorItem(ItemRegistry.SNOW_BOOTS);
        simpleArmorItem(ItemRegistry.SNOW_CHEST);
        simpleArmorItem(ItemRegistry.SNOW_HELMET);
        simpleArmorItem(ItemRegistry.SNOW_LEGGINGS);

        for (int i = 0; i < 30; i++)
        {
            String jsonName = String.format("%02d", i);
            singleTexture("item/thermometer/thermometer_" + jsonName, ResourceLocation.parse("item/generated"),
                    "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/thermometer/thermometer_" + jsonName));
        }

        String[] serene_seasons = {"autumn", "dry", "spring", "summer", "wet", "winter"};
        for (int i = 1; i < 4; i++)
        {
            for (String season : serene_seasons)
            {
                String jsonName = season + i;
                singleTexture("item/seasonal_calendar/serene_seasons/seasonal_calendar_" + jsonName, ResourceLocation.parse("item/generated"),
                        "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/seasonal_calendar/serene_seasons/" + jsonName));
            }
        }

        String[] ecliptic_seasons = {"autumn", "spring", "summer", "winter"};
        for (int i = 1; i < 7; i++)
        {
            for (String season : ecliptic_seasons)
            {
                String jsonName = season + i;
                singleTexture("item/seasonal_calendar/ecliptic_seasons/seasonal_calendar_" + jsonName, ResourceLocation.parse("item/generated"),
                        "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/seasonal_calendar/ecliptic_seasons/" + jsonName));
            }
        }

        singleTexture("item/seasonal_calendar/seasonal_calendar_null", ResourceLocation.parse("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/seasonal_calendar/calendar_null"));
    }

    private void simpleItem(DeferredHolder<Item, ? extends Item> item)
    {
        singleTexture(item.getId().getPath(), ResourceLocation.parse("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/" + item.getId().getPath()));
    }

    private void simpleItem(String jsonName)
    {
        singleTexture(jsonName, ResourceLocation.parse("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/" + jsonName));
    }

    private void simpleArmorItem(DeferredHolder<Item, ? extends Item> item)
    {
        singleTexture(item.getId().getPath(), ResourceLocation.parse("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/armor/" + item.getId().getPath()));
    }

    private void simpleCoatItem(DeferredHolder<Item, ? extends Item> item)
    {
        singleTexture(item.getId().getPath(), ResourceLocation.parse("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/coat/" + item.getId().getPath()));
    }

    private void simpleHealingItem(DeferredHolder<Item, ? extends Item> item)
    {
        singleTexture(item.getId().getPath(), ResourceLocation.parse("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/healing/" + item.getId().getPath()));
    }

    private void simpleJuiceItem(DeferredHolder<Item, ? extends Item> item)
    {
        singleTexture(item.getId().getPath(), ResourceLocation.parse("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/juice/" + item.getId().getPath()));
    }

    private void simplePaddingItem(DeferredHolder<Item, ? extends Item> item)
    {
        singleTexture(item.getId().getPath(), ResourceLocation.parse("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/padding/" + item.getId().getPath()));
    }

    private void canteenItem(DeferredHolder<Item, ? extends Item> canteenItem)
    {
        withExistingParent(canteenItem.getId().getPath(), ResourceLocation.parse("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/" + canteenItem.getId().getPath()))
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "thirstenum"), 0.3f)
                .model(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item/" + canteenItem.getId().getPath() + "_purified")))
                .end();

        simpleItem(canteenItem.getId().getPath() + "_purified");
    }
}
