package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class RusticDelightTemperatureProvider extends TemperatureDataProvider
{

    public RusticDelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("rusticdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        ITemperatureConsumableData coffee = temperatureConsumable(DRINK).temperatureLevel(2).duration(2400);
        consumable("dark_coffee").addTemperature(coffee);
        consumable("coffee").addTemperature(coffee);
        consumable("milk_coffee").addTemperature(coffee);
        consumable("syrup_coffee").addTemperature(coffee);
        consumable("chocolate_coffee").addTemperature(coffee);
        consumable("honey_coffee").addTemperature(coffee);

        consumable("bell_pepper_green").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));
        consumable("bell_pepper_yellow").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));
        consumable("bell_pepper_red").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));

        consumable("potato_salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));
        consumable("sweet_salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));
    }
}
