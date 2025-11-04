package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.concurrent.CompletableFuture;

public class CrabbersDelightTemperatureProvider extends TemperatureDataProvider
{

    public CrabbersDelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("crabbersdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("kelp_shake").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-1).duration(2400));
        consumable("sea_pickle_juice").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-1).duration(2400));
    }
}
