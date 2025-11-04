package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class BygTemperatureProvider extends TemperatureDataProvider
{

    public BygTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("byg", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        block("boric_fire")
                .addTemperature(temperatureBlock(5.0f));

        block("boric_campfire")
                .addTemperature(temperatureBlock(10.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        block("cryptic_fire")
                .addTemperature(temperatureBlock(5.0f));

        block("cryptic_campfire")
                .addTemperature(temperatureBlock(7.5f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
    }
}
