package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class OriginsTemperatureProvider extends TemperatureDataProvider
{

    public OriginsTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("origins", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        origin("blazeborn").temperature(-5).heatResistance(15).coldResistance(-2);
        origin("merling").coldResistance(3);
        origin("arachnid").heatResistance(-5);
        origin("feline").thermalResistance(2);
        origin("enderian").coldResistance(10);
    }
}
