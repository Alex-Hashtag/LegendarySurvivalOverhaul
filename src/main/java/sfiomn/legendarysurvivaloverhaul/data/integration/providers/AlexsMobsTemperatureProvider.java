package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class AlexsMobsTemperatureProvider extends TemperatureDataProvider
{

    public AlexsMobsTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("alexsmobs", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        item("roadrunner_boots").heatResistance(1.0f);
        item("crocodile_chestplate").coldResistance(1.0f);
        item("centipede_leggings").heatResistance(1.0f);
        item("frontier_cap").coldResistance(4.0f);
        item("sombrero").heatResistance(4.0f);
        item("emu_leggings").heatResistance(1.5f);
        item("froststalker_helmet").heatResistance(3.0f);
        item("unsettling_kimono").heatResistance(2.0f);
    }
}
