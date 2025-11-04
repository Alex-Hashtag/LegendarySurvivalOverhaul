package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.concurrent.CompletableFuture;

public class MeadowTemperatureProvider extends TemperatureDataProvider
{

    public MeadowTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("meadow", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        block("stove_tiles_wood")
                .addTemperature(temperatureBlock(6.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("stove_tiles_lid")
                .addTemperature(temperatureBlock(6.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("cooking_cauldron")
                .addTemperature(temperatureBlock(4.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("fondue")
                .addTemperature(temperatureBlock(4.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("frame").addTemperature(temperatureBlock(10.0f));

        block("oil_lantern").addTemperature(temperatureBlock(1.5f));
        item("oil_lantern").temperature(1.5f);

        item("fur_helmet").heatResistance(2.0f);
        item("fur_chestplate").heatResistance(3.5f);
        item("fur_leggings").heatResistance(3.0f);
        item("fur_boots").heatResistance(1.0f);

        consumable("sausage_with_cheese").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(2400));
        consumable("roasted_ham").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(3600));
    }
}
