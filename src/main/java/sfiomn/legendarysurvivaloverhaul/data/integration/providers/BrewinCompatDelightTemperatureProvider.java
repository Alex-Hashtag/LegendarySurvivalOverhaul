package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;

public class BrewinCompatDelightTemperatureProvider extends TemperatureDataProvider
{

    public BrewinCompatDelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("brewincompatdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        ITemperatureConsumableData beer = temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400);
        consumable("beer_knees").addTemperature(beer);
        consumable("brass_monkey").addTemperature(beer);

        ITemperatureConsumableData cocktail = temperatureConsumable(DRINK).temperatureLevel(-2).duration(2400);
        consumable("aqua_velva").addTemperature(cocktail);
        consumable("blue_curacao").addTemperature(cocktail);
        consumable("china_blue").addTemperature(cocktail);
        consumable("gin_and_juice").addTemperature(cocktail);
        consumable("gin_and_tonic").addTemperature(cocktail);
        consumable("half_and_half").addTemperature(cocktail);
        consumable("margarita").addTemperature(cocktail);
        consumable("mojito").addTemperature(cocktail);
        consumable("pina_colada").addTemperature(cocktail);
        consumable("salted_margarita").addTemperature(cocktail);
        consumable("singapore_sling").addTemperature(cocktail);
        consumable("tequila_sunrise").addTemperature(cocktail);

        ITemperatureConsumableData wine = temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400);
        consumable("mulled_wine").addTemperature(wine);
        consumable("peach_wine").addTemperature(wine);
        consumable("red_wine").addTemperature(wine);
        consumable("sweet_red_wine").addTemperature(wine);
        consumable("white_wine").addTemperature(wine);

        ITemperatureConsumableData alcohol = temperatureConsumable(DRINK).temperatureLevel(2).duration(2400);
        consumable("gin").addTemperature(alcohol);
        consumable("kraken_rum").addTemperature(alcohol);
        consumable("screwdriver").addTemperature(alcohol);
        consumable("tequila").addTemperature(alcohol);

        ITemperatureConsumableData soft = temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400);
        consumable("hard_lemonade").addTemperature(soft);
        consumable("hard_cider").addTemperature(soft);
        consumable("lemon_lime").addTemperature(soft);
        consumable("mermaid_lemonade").addTemperature(soft);
        consumable("tonic_water").addTemperature(soft);
    }
}
