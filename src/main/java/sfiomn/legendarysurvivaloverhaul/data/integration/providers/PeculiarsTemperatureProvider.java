package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class PeculiarsTemperatureProvider extends TemperatureDataProvider
{

    public PeculiarsTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("peculiars", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("yucca_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));
        consumable("aloe_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));
        consumable("passionfruit_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(3600));

        consumable("yucca_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(3600));
        consumable("aloe_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(3600));
        consumable("passionfruit_milkshake").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-3).duration(3600));
    }
}
