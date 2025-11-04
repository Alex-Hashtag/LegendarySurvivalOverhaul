package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class BetterEndForgeTemperatureProvider extends TemperatureDataProvider
{

    public BetterEndForgeTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("betterendforge", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        fuelItem("coal_block").thermalType(ThermalTypeEnum.HEATING).duration(5400);
        fuelItem("charcoal_block").thermalType(ThermalTypeEnum.HEATING).duration(5400);

        biome("sulphur_springs").temperature(1.1f);
        biome("ice_starfield").temperature(0.1f);
    }
}
