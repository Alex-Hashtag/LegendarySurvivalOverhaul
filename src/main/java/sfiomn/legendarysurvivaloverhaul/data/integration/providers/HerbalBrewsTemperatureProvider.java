package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.concurrent.CompletableFuture;

public class HerbalBrewsTemperatureProvider extends TemperatureDataProvider
{

    public HerbalBrewsTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("herbalbrews", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        block("stove")
                .addTemperature(temperatureBlock(6.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("cauldron").addTemperature(temperatureBlock(6.0f));
        block("tea_kettle")
                .addTemperature(temperatureBlock(4.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("copper_tea_kettle")
                .addTemperature(temperatureBlock(4.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        consumable("green_tea")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(1).duration(2400));
        consumable("black_tea")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(1).duration(3600));
        consumable("hibiscus_tea")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-1).duration(3600));
        consumable("lavender_tea")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-1).duration(3600));
        consumable("coffee")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(1).duration(3600));
        consumable("milk_coffee")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(1).duration(3600));
        consumable("rooibos_tea")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(1).duration(1200));
        consumable("oolong_tea")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(1).duration(2400));
        consumable("yerba_mate_tea")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(2).duration(3600));
    }
}
