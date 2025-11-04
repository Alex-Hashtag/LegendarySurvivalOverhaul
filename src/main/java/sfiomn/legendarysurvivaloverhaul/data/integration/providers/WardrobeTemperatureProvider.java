package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class WardrobeTemperatureProvider extends TemperatureDataProvider
{

    public WardrobeTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("wardrobe", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        item("taiga_helmet").coldResistance(2.0f);
        item("taiga_chestplate").coldResistance(3.5f);
        item("taiga_leggings").coldResistance(3.0f);
        item("taiga_boots").coldResistance(1.0f);

        item("snowy_helmet").coldResistance(2.0f);
        item("snowy_chestplate").coldResistance(3.5f);
        item("snowy_leggings").coldResistance(3.0f);
        item("snowy_boots").coldResistance(1.0f);

        item("desert_helmet").heatResistance(2.0f);
        item("desert_chestplate").heatResistance(3.0f);
        item("desert_leggings").heatResistance(2.0f);
        item("desert_boots").heatResistance(1.0f);

        item("jungle_chestplate").heatResistance(5.0f);
        item("jungle_leggings").heatResistance(3.0f);
        item("jungle_boots").heatResistance(2.0f);

        item("savanna_chestplate").heatResistance(4.0f);
        item("savanna_leggings").heatResistance(2.5f);
        item("savanna_boots").heatResistance(1.5f);

        item("wool_vest_chestplate").coldResistance(2.0f);
        item("chiton").heatResistance(2.0f);
        item("farmers_hat_helmet").heatResistance(3.5f);
    }
}
