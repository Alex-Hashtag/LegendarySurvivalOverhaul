package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class FarmersRespiteTemperatureProvider extends TemperatureDataProvider
{

    public FarmersRespiteTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("farmersrespite", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("coffee").addTemperature(temperatureConsumable(DRINK).temperatureLevel(2).duration(2400));
        consumable("long_coffee").addTemperature(temperatureConsumable(DRINK).temperatureLevel(2).duration(4800));
        consumable("strong_coffee").addTemperature(temperatureConsumable(DRINK).temperatureLevel(3).duration(2400));

        consumable("strong_hot_cocoa").addTemperature(temperatureConsumable(DRINK).temperatureLevel(4).duration(3600));
        consumable("strong_melon_juice").addTemperature(temperatureConsumable(DRINK).temperatureLevel(-2).duration(2400));

        ITemperatureConsumableData teaTemperature = temperatureConsumable(DRINK).temperatureLevel(1).duration(2400);
        consumable("black_tea").addTemperature(teaTemperature);
        consumable("dandelion_tea").addTemperature(teaTemperature);
        consumable("gamblers_tea").addTemperature(teaTemperature);
        consumable("green_tea").addTemperature(teaTemperature);
        consumable("yellow_tea").addTemperature(teaTemperature);
        consumable("rose_hip_tea").addTemperature(teaTemperature);
        consumable("purulent_tea").addTemperature(teaTemperature);

        ITemperatureConsumableData longTeaTemperature = temperatureConsumable(DRINK).temperatureLevel(1).duration(4800);
        consumable("long_black_tea").addTemperature(longTeaTemperature);
        consumable("long_dandelion_tea").addTemperature(longTeaTemperature);
        consumable("long_gamblers_tea").addTemperature(longTeaTemperature);
        consumable("long_green_tea").addTemperature(longTeaTemperature);
        consumable("long_yellow_tea").addTemperature(longTeaTemperature);
        consumable("long_rose_hip_tea").addTemperature(longTeaTemperature);
        consumable("long_purulent_tea").addTemperature(longTeaTemperature);

        ITemperatureConsumableData strongTeaTemperature = temperatureConsumable(DRINK).temperatureLevel(2).duration(2400);
        consumable("strong_black_tea").addTemperature(strongTeaTemperature);
        consumable("long_dandelion_tea").addTemperature(strongTeaTemperature);
        consumable("strong_gamblers_tea").addTemperature(strongTeaTemperature);
        consumable("strong_green_tea").addTemperature(strongTeaTemperature);
        consumable("strong_yellow_tea").addTemperature(strongTeaTemperature);
        consumable("strong_rose_hip_tea").addTemperature(strongTeaTemperature);
        consumable("strong_purulent_tea").addTemperature(strongTeaTemperature);

        consumable("blazing_chili").addTemperature(temperatureConsumable(FOOD).temperatureLevel(3).duration(4800));

        block("kettle")
                .addTemperature(temperatureBlock(0.0f).addProperty("lid", "false"))
                .addTemperature(temperatureBlock(4.0f).addProperty("lid", "true"));

    }
}
