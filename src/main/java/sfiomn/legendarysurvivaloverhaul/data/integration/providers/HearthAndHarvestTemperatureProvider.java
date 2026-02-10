package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;

public class HearthAndHarvestTemperatureProvider extends TemperatureDataProvider
{

    public HearthAndHarvestTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("hearthandharvest", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("blueberry_juice").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1800));
        consumable("blueberry_wine").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("cherry_juice").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1800));
        consumable("cherry_wine").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("green_grape_juice").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1800));
        consumable("green_grape_wine").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("mead").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("raspberry_juice").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1800));
        consumable("raspberry_wine").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("red_grape_juice").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1800));
        consumable("red_grape_wine").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("sweet_berry_wine").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
    }
}
