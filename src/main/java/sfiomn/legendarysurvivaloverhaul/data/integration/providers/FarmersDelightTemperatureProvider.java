package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class FarmersDelightTemperatureProvider extends TemperatureDataProvider
{

    public FarmersDelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("farmersdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        block("stove")
                .addTemperature(temperatureBlock(7.5f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        consumable("beef_stew").addTemperature(temperatureConsumable(FOOD).temperatureLevel(2).duration(2400));
        consumable("fish_stew").addTemperature(temperatureConsumable(FOOD).temperatureLevel(2).duration(2400));
        consumable("ratatouille").addTemperature(temperatureConsumable(FOOD).temperatureLevel(2).duration(3600));
        consumable("baked_cod_stew").addTemperature(temperatureConsumable(FOOD).temperatureLevel(2).duration(2400));
        consumable("melon_popsicle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(3600));
        consumable("fruit_salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));

        consumable("chicken_soup").addTemperature(temperatureConsumable(DRINK).temperatureLevel(2).duration(2400));
        consumable("vegetable_soup").addTemperature(temperatureConsumable(DRINK).temperatureLevel(2).duration(2400));
        consumable("pumpkin_soup").addTemperature(temperatureConsumable(DRINK).temperatureLevel(2).duration(2400));
        consumable("hot_cocoa").addTemperature(temperatureConsumable(DRINK).temperatureLevel(3).duration(3600));
        consumable("melon_juice").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
    }
}
