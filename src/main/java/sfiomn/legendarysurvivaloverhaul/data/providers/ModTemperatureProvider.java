package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.IceFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.SunFernBlock;

import java.util.concurrent.CompletableFuture;

public class ModTemperatureProvider extends TemperatureDataProvider {

    public ModTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(LegendarySurvivalOverhaul.MOD_ID, output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        block("cooler")
                .addTemperature(temperatureBlock(-15.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("heater")
                .addTemperature(temperatureBlock(15.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        block("ice_fern_crop").addTemperature(temperatureBlock(-1.5f).addProperty(IceFernBlock.AGE.getName(), String.valueOf(IceFernBlock.MAX_AGE)));
        block("sun_fern_crop").addTemperature(temperatureBlock(1.5f).addProperty(SunFernBlock.AGE.getName(), String.valueOf(SunFernBlock.MAX_AGE)));

        item("snow_boots").coldResistance(0.5f);
        item("snow_leggings").coldResistance(2.5f);
        item("snow_chestplate").coldResistance(3.0f);
        item("snow_helmet").coldResistance(1.5f);

        item("desert_boots").heatResistance(0.5f);
        item("desert_leggings").heatResistance(2.5f);
        item("desert_chestplate").heatResistance(3.0f);
        item("desert_helmet").heatResistance(1.5f);

        item("nether_chalice").temperature(2.0f);

        consumable("melon_juice").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-1).duration(1200));
        consumable("melon_juice").addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-2).duration(1200));
    }
}
