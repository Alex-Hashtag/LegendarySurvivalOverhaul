package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class EndergeticTemperatureProvider extends TemperatureDataProvider
{

    public EndergeticTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("endergetic", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        block("ender_fire").addTemperature(temperatureBlock(-7.0f));
        block("ender_campfire").addTemperature(temperatureBlock(-10.0f));
        block("ender_torch").addTemperature(temperatureBlock(-1.5f));
        block("ender_wall_torch").addTemperature(temperatureBlock(-1.5f));
    }
}
