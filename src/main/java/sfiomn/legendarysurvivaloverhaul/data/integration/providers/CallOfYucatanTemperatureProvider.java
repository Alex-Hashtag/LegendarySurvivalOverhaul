package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class CallOfYucatanTemperatureProvider extends TemperatureDataProvider
{

    public CallOfYucatanTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("call_of_yucutan", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        item("jades_helmet").heatResistance(0.5f);
        item("jades_chestplate").coldResistance(1.5f);
        item("jades_leggings").coldResistance(1.5f);
        item("jades_boots").coldResistance(0.5f);

        block("bone_torch_ground").addTemperature(temperatureBlock(1.5f));
        item("bone_torch_ground").temperature(1.5f);

        block("golden_torch_ground").addTemperature(temperatureBlock(1.5f));
        item("golden_torch_ground").temperature(1.5f);
    }
}
