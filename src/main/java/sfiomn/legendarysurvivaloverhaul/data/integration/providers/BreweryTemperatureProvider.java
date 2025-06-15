package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class BreweryTemperatureProvider extends TemperatureDataProvider {

    public BreweryTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("brewery", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {

        consumable("fried_chicken").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumable("sausage").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumable("pork_knuckle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumable("half_chicken").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));

        consumable("potato_salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));

        ITemperatureConsumableData whiskeyTemperature = temperatureConsumable(DRINK).temperatureLevel(1).duration(2400);
        consumable("whiskey_jojannik").addTemperature(whiskeyTemperature);
        consumable("whiskey_lilitusinglemalt").addTemperature(whiskeyTemperature);
        consumable("whiskey_cristelwalker").addTemperature(whiskeyTemperature);
        consumable("whiskey_maggoallan").addTemperature(whiskeyTemperature);
        consumable("whiskey_carrasconlabel").addTemperature(whiskeyTemperature);
        consumable("whiskey_ak").addTemperature(whiskeyTemperature);
        consumable("whiskey_highland_hearth").addTemperature(whiskeyTemperature);
        consumable("whiskey_smokey_reverie").addTemperature(whiskeyTemperature);
        consumable("whiskey_jamesons_malt").addTemperature(whiskeyTemperature);

        ITemperatureConsumableData beerTemperature = temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400);
        consumable("beer_wheat").addTemperature(beerTemperature);
        consumable("beer_barley").addTemperature(beerTemperature);
        consumable("beer_hops").addTemperature(beerTemperature);
        consumable("beer_nettle").addTemperature(beerTemperature);
        consumable("beer_oat").addTemperature(beerTemperature);
        consumable("beer_haley").addTemperature(beerTemperature);

        item("brewfest_hat").heatResistance(1.0f);
        item("brewfest_regalia").heatResistance(1.5f);
        item("brewfest_trousers").heatResistance(1.0f);
        item("brewfest_boots").heatResistance(0.5f);

        item("brewfest_hat_red").heatResistance(1.0f);
        item("brewfest_blouse").heatResistance(1.5f);
        item("brewfest_dress").heatResistance(1.0f);
        item("brewfest_shoes").heatResistance(0.5f);
    }
}