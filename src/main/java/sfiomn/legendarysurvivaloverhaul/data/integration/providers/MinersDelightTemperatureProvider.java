package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.concurrent.CompletableFuture;

public class MinersDelightTemperatureProvider extends TemperatureDataProvider
{

    public MinersDelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("miners_delight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        ITemperatureConsumableData soupTemperature = temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(1).duration(2400);
        consumable("beetroot_soup_cup").addTemperature(soupTemperature);
        consumable("cave_soup_cup").addTemperature(soupTemperature);
        consumable("bat_soup_cup").addTemperature(soupTemperature);
        consumable("vegetable_soup_cup").addTemperature(soupTemperature);
        consumable("noodle_soup_cup").addTemperature(soupTemperature);
        consumable("chicken_soup_cup").addTemperature(soupTemperature);
        consumable("pumpkin_soup_cup").addTemperature(soupTemperature);

        ITemperatureConsumableData soupTemperature2 = temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(2).duration(2400);
        consumable("cave_soup").addTemperature(soupTemperature2);
        consumable("bat_soup").addTemperature(soupTemperature2);

        ITemperatureConsumableData stewTemperature = temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(1).duration(2400);
        consumable("mushroom_stew_cup").addTemperature(stewTemperature);
        consumable("insect_stew_cup").addTemperature(stewTemperature);
        consumable("fish_stew_cup").addTemperature(stewTemperature);
        consumable("rabbit_stew_cup").addTemperature(stewTemperature);
        consumable("beef_stew_cup").addTemperature(stewTemperature);
        consumable("baked_cod_stew_cup").addTemperature(stewTemperature);

        ITemperatureConsumableData stewTemperature2 = temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(2400);
        consumable("insect_stew").addTemperature(stewTemperature2);

    }
}
