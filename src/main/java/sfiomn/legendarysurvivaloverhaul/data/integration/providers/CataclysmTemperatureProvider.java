package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class CataclysmTemperatureProvider extends TemperatureDataProvider
{

    public CataclysmTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("cataclysm", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        item("bone_reptile_helmet").coldResistance(1.5f);
        item("bone_reptile_chestplate").coldResistance(2.0f);

        item("ignitium_helmet").coldResistance(1.5f);
        item("ignitium_chestplate").coldResistance(3.0f);
        item("ignitium_elytra_chestplate").coldResistance(3.0f);
        item("ignitium_leggings").coldResistance(2.0f);
        item("ignitium_boots").coldResistance(1.5f);

        item("monstrous_helm").coldResistance(1.0f);
        item("bloom_stone_pauldrons").heatResistance(1.0f);
    }
}
