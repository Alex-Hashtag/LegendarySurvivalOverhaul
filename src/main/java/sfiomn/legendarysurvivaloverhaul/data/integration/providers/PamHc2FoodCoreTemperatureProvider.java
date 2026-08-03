package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

import java.util.concurrent.CompletableFuture;

public class PamHc2FoodCoreTemperatureProvider extends TemperatureDataProvider
{

    public PamHc2FoodCoreTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("pamhc2foodcore", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("applepopsicleitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
        consumable("beefnoodlesoup").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("caramelicecreamitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("chickennoodlesoupitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("chocolateicecreamitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("choruspopsicleitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
        consumable("fudgesicleitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
        consumable("glowberrypopsicleitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
        consumable("hotchocolate").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1800));
        consumable("icecreamitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("melonpopsicleitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
        consumable("porknoodlesoupitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("potroastitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("rabbitnoodlesoupitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1200));
        consumable("sweetberrypopsicleitem").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1200));
    }
}
