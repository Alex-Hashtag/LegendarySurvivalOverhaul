package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class InfernalExpansionTemperatureProvider extends TemperatureDataProvider
{

    public InfernalExpansionTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("infernalexp", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        block("fire_glow").addTemperature(temperatureBlock(7.0f));
        block("campfire_glow").addTemperature(temperatureBlock(10.0f));
        block("torch_glow").addTemperature(temperatureBlock(1.5f));
        block("torch_glow_wall").addTemperature(temperatureBlock(1.5f));
    }
}
