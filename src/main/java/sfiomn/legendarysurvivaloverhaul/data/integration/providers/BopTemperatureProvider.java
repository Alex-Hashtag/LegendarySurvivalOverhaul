package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class BopTemperatureProvider extends TemperatureDataProvider
{

    public BopTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("biomesoplenty", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        biome("crystalline_chasm").temperature(0.8f).isDry(false);
        biome("undergrowth").temperature(0.75f).isDry(false);
        biome("visceral_heap").temperature(0.9f).isDry(false);
        biome("withered_abyss").temperature(1.5f).isDry(false);
    }
}
