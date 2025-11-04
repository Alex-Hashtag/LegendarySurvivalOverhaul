package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class BeachPartyTemperatureProvider extends TemperatureDataProvider
{

    public BeachPartyTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("beachparty", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("sweetberry_icecream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(1200));
        consumable("coconut_icecream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(1200));
        consumable("chocolate_icecream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(1200));
        consumable("icecream_coconut").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(1200));
        consumable("icecream_cactus").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(1200));
        consumable("icecream_chocolate").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(1200));
        consumable("icecream_sweetberries").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(1200));
        consumable("icecream_melon").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(1200));

        consumable("coconut_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("sweetberries_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("cocoa_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-2).duration(2400));
        consumable("pumpkin_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("melon_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("honey_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("refreshing_drink").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("sweetberry_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(4800));
        consumable("coconut_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-2).duration(1800));
        consumable("chocolate_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-2).duration(1800));

        item("beach_hat").heatResistance(2.5f);
        item("trunks").heatResistance(3.0f);
        item("bikini").heatResistance(3.0f);
        item("crocs").heatResistance(0.5f);
        item("palm_torch_item").temperature(1.5f);
        item("tall_palm_torch").temperature(2.0f);
        block("palm_torch_item").addTemperature(temperatureBlock(1.5f));
        block("tall_palm_torch").addTemperature(temperatureBlock(2.0f));
    }
}
