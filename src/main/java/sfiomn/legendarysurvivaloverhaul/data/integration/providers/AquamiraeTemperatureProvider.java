package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class AquamiraeTemperatureProvider extends TemperatureDataProvider
{

    public AquamiraeTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("aquamirae", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        item("terrible_helmet").heatResistance(1.0f);
        item("terrible_chestplate").heatResistance(1.5f);
        item("terrible_leggings").heatResistance(1.5f);
        item("terrible_boots").heatResistance(1.0f);
        item("abyssal_heaume").coldResistance(1.0f);
        item("abyssal_brigantine").coldResistance(2.0f);
        item("abyssal_leggings").coldResistance(1.0f);
        item("abyssal_boots").coldResistance(1.0f);
        item("three_bolt_helmet").coldResistance(1.0f);
        item("three_bolt_suit").coldResistance(1.0f);
        item("three_bolt_leggings").coldResistance(1.0f);
        item("three_bolt_boots").coldResistance(1.0f);
    }
}
