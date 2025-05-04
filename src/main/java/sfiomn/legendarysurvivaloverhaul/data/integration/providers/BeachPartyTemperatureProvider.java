package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class BeachPartyTemperatureProvider extends TemperatureDataProvider {

    public BeachPartyTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("beachparty", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {

        consumable("sweetberry_icecream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(3600));
        consumable("coconut_icecream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(3600));
        consumable("chocolate_icecream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));
        consumable("icecream_coconut").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(3600));
        consumable("icecream_cactus").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(3600));
        consumable("icecream_chocolate").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(3600));
        consumable("icecream_sweetberries").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(3600));
        consumable("icecream_melon").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(3600));

        consumable("coconut_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("sweetberries_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("cocoa_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-2).duration(2400));
        consumable("pumpkin_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("melon_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("honey_cocktail").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("refreshing_drink").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(2400));
        consumable("sweetberry_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(4800));
        consumable("coconut_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(4800));
        consumable("chocolate_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(4800));

        item("beach_hat").heatResistance(2.5f);
        item("trunks").heatResistance(3.0f);
        item("bikine").heatResistance(3.0f);
        item("crocs").heatResistance(0.5f);
        item("palm_torch_item").temperature(1.0f);
        item("palm_tall_torch").temperature(1.0f);
    }
}