package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

import java.util.concurrent.CompletableFuture;

public class ExtraDelightTemperatureProvider extends TemperatureDataProvider
{

    public ExtraDelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("extradelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("apple_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("apple_popsicle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("chocolate_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("cookie_dough_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("fudge_popsicle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("glow_berry_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("glow_berry_popsicle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("honey_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("honey_popsicle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("hot_sauce_item").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(1800));
        consumable("ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("lemonage").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("mint_chip_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("pumpkin_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("sweet_berry_ice_cream").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
        consumable("sweet_berry_popsicle").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(1800));
    }
}
