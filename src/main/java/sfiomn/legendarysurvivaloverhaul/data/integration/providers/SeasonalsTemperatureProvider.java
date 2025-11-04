package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class SeasonalsTemperatureProvider extends TemperatureDataProvider
{

    public SeasonalsTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("seasonals", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("pumpkin_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));
        consumable("sweet_berry_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));

        consumable("pumpkin_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(3600));
        consumable("sweet_berry_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(3600));
    }
}
