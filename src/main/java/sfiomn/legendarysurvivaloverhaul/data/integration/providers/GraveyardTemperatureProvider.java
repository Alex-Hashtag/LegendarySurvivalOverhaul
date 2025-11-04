package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;


public class GraveyardTemperatureProvider extends TemperatureDataProvider
{

    public GraveyardTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("graveyard", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        block("fire_brazier")
                .addTemperature(temperatureBlock(12.5f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0).addProperty("lit", "false"));

        block("soul_fire_brazier")
                .addTemperature(temperatureBlock(-12.5f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0).addProperty("lit", "false"));
    }
}
