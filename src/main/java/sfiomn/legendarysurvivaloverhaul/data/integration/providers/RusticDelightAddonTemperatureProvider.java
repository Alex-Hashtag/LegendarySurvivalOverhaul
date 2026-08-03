package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

import java.util.concurrent.CompletableFuture;

public class RusticDelightAddonTemperatureProvider extends TemperatureDataProvider
{

    public RusticDelightAddonTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("rusticdelight_addon", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("chocolate_coffee").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1200));
        consumable("coffee").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1200));
        consumable("honey_coffee").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1200));
        consumable("milk_coffee").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1200));
        consumable("syrup_coffee").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1200));
    }
}