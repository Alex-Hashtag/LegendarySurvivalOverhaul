package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class CrockpotTemperatureProvider extends TemperatureDataProvider
{

    public CrockpotTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("crockpot", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        block("crock_pot")
                .addTemperature(temperatureBlock(7.5f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        block("portable_crock_pot")
                .addTemperature(temperatureBlock(7.5f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        consumable("iced_tea").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-2).duration(4800));
        consumable("tea").addTemperature(temperatureConsumable(DRINK).temperatureLevel(3).duration(4800));
        consumable("fruit_medley").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-1).duration(1200));
        consumable("hot_cocoa").addTemperature(temperatureConsumable(DRINK).temperatureLevel(2).duration(2400));

        consumable("ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(1200));
        consumable("bone_stew").addTemperature(temperatureConsumable(FOOD).temperatureLevel(3).duration(2400));
        consumable("bunny_stew").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("flower_salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-3).duration(6000));
        consumable("hot_chili").addTemperature(temperatureConsumable(FOOD).temperatureLevel(2).duration(2400));
        consumable("pepper_popper").addTemperature(temperatureConsumable(FOOD).temperatureLevel(3).duration(6000));
        consumable("salsa").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-2).duration(2400));
        consumable("watermelon_icle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-4).duration(1800));
    }
}
