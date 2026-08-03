package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

import java.util.concurrent.CompletableFuture;

public class PamHc2CropsTemperatureProvider extends TemperatureDataProvider
{

    public PamHc2CropsTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("pamhc2crops", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("chilipepperitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("hotcoffeeitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1800));
        consumable("hotnettleteaitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1800));
        consumable("hotteaitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1800));
    }
}
