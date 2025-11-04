package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.concurrent.CompletableFuture;

public class CreateTemperatureProvider extends TemperatureDataProvider
{

    public CreateTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("create", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        block("blaze_burner")
                .addTemperature(temperatureBlock(2.5f).addProperty("blaze", "smouldering"))
                .addTemperature(temperatureBlock(5.0f).addProperty("blaze", "kindled"))
                .addTemperature(temperatureBlock(7.5f).addProperty("blaze", "seething"));

        consumable("builders_tea").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(2).duration(3600));
    }
}
