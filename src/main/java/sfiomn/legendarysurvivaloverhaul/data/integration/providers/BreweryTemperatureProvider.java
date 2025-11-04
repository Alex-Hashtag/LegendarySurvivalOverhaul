package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class BreweryTemperatureProvider extends TemperatureDataProvider
{

    public BreweryTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("brewery", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumableAndConsumableBlock("fried_chicken", temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumableAndConsumableBlock("pork_knuckle", temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumableAndConsumableBlock("half_chicken", temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));

        consumableAndConsumableBlock("potato_salad", temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));

        ITemperatureConsumableData whiskeyTemperature = temperatureConsumable(DRINK).temperatureLevel(1).duration(2400);
        consumableAndConsumableBlock("whiskey_jojannik", whiskeyTemperature);
        consumableAndConsumableBlock("whiskey_lilitusinglemalt", whiskeyTemperature);
        consumableAndConsumableBlock("whiskey_cristelwalker", whiskeyTemperature);
        consumableAndConsumableBlock("whiskey_maggoallan", whiskeyTemperature);
        consumableAndConsumableBlock("whiskey_carrasconlabel", whiskeyTemperature);
        consumableAndConsumableBlock("whiskey_ak", whiskeyTemperature);
        consumableAndConsumableBlock("whiskey_highland_hearth", whiskeyTemperature);
        consumableAndConsumableBlock("whiskey_smokey_reverie", whiskeyTemperature);
        consumableAndConsumableBlock("whiskey_jamesons_malt", whiskeyTemperature);

        ITemperatureConsumableData beerTemperature = temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400);
        consumableAndConsumableBlock("beer_wheat", beerTemperature);
        consumableAndConsumableBlock("beer_barley", beerTemperature);
        consumableAndConsumableBlock("beer_hops", beerTemperature);
        consumableAndConsumableBlock("beer_nettle", beerTemperature);
        consumableAndConsumableBlock("beer_oat", beerTemperature);
        consumableAndConsumableBlock("beer_haley", beerTemperature);

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
