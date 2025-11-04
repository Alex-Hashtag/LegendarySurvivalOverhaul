package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class BrewinAndChewinTemperatureProvider extends TemperatureDataProvider
{

    public BrewinAndChewinTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("brewinandchewin", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        ITemperatureConsumableData drinkTemperature = temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400);
        consumable("beer").addTemperature(drinkTemperature);
        consumable("bloody_mary").addTemperature(drinkTemperature);
        consumable("mead").addTemperature(drinkTemperature);
        consumable("pale_jane").addTemperature(drinkTemperature);
        consumable("steel_toe_stout").addTemperature(drinkTemperature);
        consumable("strongroot_ale").addTemperature(drinkTemperature);

        ITemperatureConsumableData drinkTemperature2 = temperatureConsumable(DRINK).temperatureLevel(-2).duration(2400);
        consumable("glittering_grenadine").addTemperature(drinkTemperature2);
        consumable("kombucha").addTemperature(drinkTemperature2);

        consumable("egg_grog").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(2400));
        consumable("vodka").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(2400));
        consumable("dread_nog").addTemperature(temperatureConsumable(DRINK).temperatureLevel(2).duration(2400));
        consumable("red_rum").addTemperature(temperatureConsumable(DRINK).temperatureLevel(3).duration(2400));
        consumable("saccharine_rum").addTemperature(temperatureConsumable(DRINK).temperatureLevel(3).duration(2400));

        consumable("fiery_fondue").addTemperature(temperatureConsumable(FOOD).temperatureLevel(3).duration(3600));

        block("fiery_fondue_pot")
                .addTemperature(temperatureBlock(5).addProperty("level", "3"))
                .addTemperature(temperatureBlock(4).addProperty("level", "2"))
                .addTemperature(temperatureBlock(2).addProperty("level", "1"))
                .addTemperature(temperatureBlock(0).addProperty("level", "0"));
    }
}
