package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

import java.util.concurrent.CompletableFuture;

public class ExpandedDelightTemperatureProvider extends TemperatureDataProvider
{

    public ExpandedDelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("expandeddelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("asparagus_soup").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("asparagus_soup_creamy").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("chili_pepper").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("chili_pepper_salmon").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("peanut_honey_soup").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
    }
}
