package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.concurrent.CompletableFuture;

public class VineryTemperatureProvider extends TemperatureDataProvider
{

    public VineryTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("vinery", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        item("straw_hat").heatResistance(3);

        consumable("eiswein").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-1).duration(1200));
        consumable("cristel_wine").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(2).duration(2400));
    }
}
