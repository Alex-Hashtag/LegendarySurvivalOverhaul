package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.concurrent.CompletableFuture;

public class NetherVineryTemperatureProvider extends TemperatureDataProvider
{

    public NetherVineryTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("nethervinery", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("blazewine_pinot").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(1).duration(2400));
        consumable("lava_fizz").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(2).duration(3600));
        consumable("improved_lava_fizz").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(2).duration(4800));
        consumable("nether_fizz").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-2).duration(3600));
        consumable("improved_nether_fizz").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-2).duration(4800));
    }
}
