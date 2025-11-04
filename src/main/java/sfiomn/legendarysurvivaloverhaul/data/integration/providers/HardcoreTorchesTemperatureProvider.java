package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class HardcoreTorchesTemperatureProvider extends TemperatureDataProvider
{

    public HardcoreTorchesTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("hardcore_torches", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        block("hardcore_campfire")
                .addTemperature(temperatureBlock(10.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        block("lit_wall_torch").addTemperature(temperatureBlock(1.5f));
        block("lit_torch").addTemperature(temperatureBlock(1.5f));
        item("lit_torch").temperature(1.5f);

        block("smoldering_wall_torch").addTemperature(temperatureBlock(0.75f));
        block("smoldering_torch").addTemperature(temperatureBlock(0.75f));
        item("smoldering_torch").temperature(0.75f);
    }
}
