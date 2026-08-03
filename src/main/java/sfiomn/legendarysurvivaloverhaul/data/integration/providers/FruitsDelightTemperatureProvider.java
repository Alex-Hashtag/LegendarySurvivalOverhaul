package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

import java.util.concurrent.CompletableFuture;

public class FruitsDelightTemperatureProvider extends TemperatureDataProvider
{

    public FruitsDelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("fruitsdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("hamimelon_popsicle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
        consumable("kiwi_popsicle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
        consumable("pineapple_slice").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1000));
    }
}
