package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class NeapolitanTemperatureProvider extends TemperatureDataProvider
{

    public NeapolitanTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("neapolitan", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("ice_cubes").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(3600));
        consumable("chocolate_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));
        consumable("vanilla_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));
        consumable("strawberry_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));
        consumable("banana_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));
        consumable("neapolitan_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));

        consumable("chocolate_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(3600));
        consumable("vanilla_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(3600));
        consumable("strawberry_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(3600));
        consumable("banana_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(3600));
    }
}
