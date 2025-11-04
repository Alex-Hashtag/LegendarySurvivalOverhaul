package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class BornInChaosTemperatureProvider extends TemperatureDataProvider
{

    public BornInChaosTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("born_in_chaos_v1", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        item("dark_metal_armor_helmet").heatResistance(1.0f);
        item("dark_metal_armor_chestplate").heatResistance(2.0f);
        item("dark_metal_armor_leggings").heatResistance(1.0f);
        item("dark_metal_armor_boots").heatResistance(0.5f);

        item("nightmare_mantleofthe_night_helmet").coldResistance(1.0f);
        item("nightmare_mantleofthe_night_chestplate").coldResistance(2.0f);
        item("nightmare_mantleofthe_night_leggings").coldResistance(1.0f);
        item("nightmare_mantleofthe_night_boots").coldResistance(0.5f);

        item("spiritual_guide_sombrero_helmet").coldResistance(3.5f);
    }
}
