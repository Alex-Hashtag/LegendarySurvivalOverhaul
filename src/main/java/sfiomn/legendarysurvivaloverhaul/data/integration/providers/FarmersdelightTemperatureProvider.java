package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.concurrent.CompletableFuture;

public class FarmersdelightTemperatureProvider extends TemperatureDataProvider {

    public FarmersdelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("farmersdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {

        block("stove")
                .addTemperature(temperatureBlock(7.5f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        consumable("beef_stew").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(2400));
        consumable("chicken_soup").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(2400));
        consumable("vegetable_soup").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(2400));
        consumable("fish_stew").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(2400));
        consumable("pumpkin_soup").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(2400));
        consumable("baked_cod_stew").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(2400));
        consumable("hot_cocoa").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(3).duration(3600));
        consumable("melon_juice").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-1).duration(1200));
    }
}
