package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class TerraFirmaCraftTemperatureProvider extends TemperatureDataProvider
{

    public TerraFirmaCraftTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("tfc", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        block("firepit")
                .addTemperature(temperatureBlock(8.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        block("pot")
                .addTemperature(temperatureBlock(8.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        block("charcoal_forge")
                .addTemperature(temperatureBlock(15.0f).addProperty("heat_level", "6"))
                .addTemperature(temperatureBlock(13.0f).addProperty("heat_level", "5"))
                .addTemperature(temperatureBlock(11.0f).addProperty("heat_level", "4"))
                .addTemperature(temperatureBlock(9.0f).addProperty("heat_level", "3"))
                .addTemperature(temperatureBlock(6.0f).addProperty("heat_level", "2"))
                .addTemperature(temperatureBlock(3.0f).addProperty("heat_level", "1"))
                .addTemperature(temperatureBlock(0.0f).addProperty("heat_level", "0"));
    }
}
