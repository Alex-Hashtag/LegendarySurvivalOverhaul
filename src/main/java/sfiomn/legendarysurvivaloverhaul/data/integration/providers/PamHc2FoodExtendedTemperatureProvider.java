package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

import java.util.concurrent.CompletableFuture;

public class PamHc2FoodExtendedTemperatureProvider extends TemperatureDataProvider
{

    public PamHc2FoodExtendedTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("pamhc2foodextended", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("applesnowitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
        consumable("apricotsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("bananasmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("blackberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("blueberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("breadfruitsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("cactusfruitsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("candleberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("cantaloupesmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("chaiteaitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("cherrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("chickencurryitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1800));
        consumable("chickengumboitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1800));
        consumable("chilidogitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(800));
        consumable("chipsandsalsaitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(800));
        consumable("coffeeconlecheitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("cranberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("creamedbroccolisoupitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("creamedcornitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("creamofavocadosoup").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("creamofchickenitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("creamofmushroomitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("dandelionteaitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1200));
        consumable("datesmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("dragonfruitsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("duriansmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("earlgrayteaitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1200));
        consumable("elderberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("espressoitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("figsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("gooseberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("grapefruitsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("grapesmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("greengrapesmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("guavasmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("huckleberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("jackfruitsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("juniperberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("kiwismoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("lemonaideitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1800));
        consumable("lemonsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("limesmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("lycheesmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("mangosmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("mulberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("neapolitanicecreamitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
        consumable("orangesmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("papayasmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("passonfruitsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("pawpawsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("peachsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("pearsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("persimmonsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("pineapplesmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("plumsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("pomegranatesmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("pumpkinspicelatteitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(1).duration(1200));
        consumable("rambutansmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("raspberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("salsaitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(800));
        consumable("soursopsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("starfruitsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("strawberrysmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("tamarindsmoothieitem").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("vanillaicecreamitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
    }
}
