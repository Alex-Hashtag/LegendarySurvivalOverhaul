package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class RealisticTorchesTemperatureProvider extends TemperatureDataProvider
{

    public RealisticTorchesTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("realistictorches", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        block("torch")
                .addTemperature(temperatureBlock(1.5f).addProperty("litstate", "2"))
                .addTemperature(temperatureBlock(0.75f).addProperty("litstate", "1"))
                .addTemperature(temperatureBlock(0.0f).addProperty("litstate", "0"));
        block("torch_wall")
                .addTemperature(temperatureBlock(1.5f).addProperty("litstate", "2"))
                .addTemperature(temperatureBlock(0.75f).addProperty("litstate", "1"))
                .addTemperature(temperatureBlock(0.0f).addProperty("litstate", "0"));
    }
}
