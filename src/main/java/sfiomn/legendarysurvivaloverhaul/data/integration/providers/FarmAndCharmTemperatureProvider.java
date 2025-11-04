package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class FarmAndCharmTemperatureProvider extends TemperatureDataProvider
{

    public FarmAndCharmTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("farm_and_charm", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("farmer_salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));

        consumable("goulash").addTemperature(temperatureConsumable(FOOD).temperatureLevel(3).duration(4800));
        consumable("chicken_wrapped_in_bacon").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(4800));
        consumableAndConsumableBlock("stuffed_rabbit", temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumableAndConsumableBlock("stuffed_chicken", temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumableAndConsumableBlock("farmers_breakfast", temperatureConsumable(FOOD).temperatureLevel(1).duration(7200));
        consumable("sausage_with_oat_patty").addTemperature(temperatureConsumable(FOOD).temperatureLevel(2).duration(4800));

        ITemperatureConsumableData soupTemperatureData = temperatureConsumable(DRINK).temperatureLevel(2).duration(3600);
        consumable("simple_tomato_soup").addTemperature(soupTemperatureData);
        consumable("barley_soup").addTemperature(soupTemperatureData);
        consumable("onion_soup").addTemperature(soupTemperatureData);
        consumable("potato_soup").addTemperature(soupTemperatureData);

        consumable("strawberry_tea").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-2).duration(3600));
        consumable("nettle_tea").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-2).duration(3600));
        consumable("ribwort_tea").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-2).duration(3600));
        consumable("strawberry_tea_cup").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("nettle_tea_cup").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("ribwort_tea_cup").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));

        block("roaster")
                .addTemperature(temperatureBlock(0).addProperty("lit", "false"))
                .addTemperature(temperatureBlock(4).addProperty("lit", "true"));

        block("cooking_pot")
                .addTemperature(temperatureBlock(0).addProperty("lit", "false"))
                .addTemperature(temperatureBlock(3).addProperty("lit", "true"));

        block("stove")
                .addTemperature(temperatureBlock(0).addProperty("lit", "false"))
                .addTemperature(temperatureBlock(6).addProperty("lit", "true"));
    }
}
