package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class DecorativeBlocksTemperatureProvider extends TemperatureDataProvider
{

    public DecorativeBlocksTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("decorative_blocks", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        block("brazier")
                .addTemperature(temperatureBlock(12.5f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0).addProperty("lit", "false"));

        block("soul_brazier")
                .addTemperature(temperatureBlock(-12.5f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0).addProperty("lit", "false"));

    }
}
