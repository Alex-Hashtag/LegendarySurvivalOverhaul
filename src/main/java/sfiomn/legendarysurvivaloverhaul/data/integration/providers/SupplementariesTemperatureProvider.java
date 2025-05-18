package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureBlockData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;


public class SupplementariesTemperatureProvider extends TemperatureDataProvider {

    public SupplementariesTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("supplementaries", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        block("fire_pit")
                .addTemperature(temperatureBlock(10.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0).addProperty("lit", "false"));

        ITemperatureBlockData candleOn = temperatureBlock(1.5f).addProperty("lit", "true");
        ITemperatureBlockData candleOff = temperatureBlock(0.0f).addProperty("lit", "false");
        block("candle_holder").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_white").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_light_gray").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_gray").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_black").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_brown").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_red").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_orange").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_yellow").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_lime").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_green").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_cyan").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_light_blue").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_blue").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_purple").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_magenta").addTemperature(candleOn).addTemperature(candleOff);
        block("candle_holder_pink").addTemperature(candleOn).addTemperature(candleOff);

        block("sconce").addTemperature(candleOn).addTemperature(candleOff);
        block("sconce_lever").addTemperature(candleOn).addTemperature(candleOff);

        ITemperatureBlockData soulSconceOn = temperatureBlock(-1.5f).addProperty("lit", "true");
        ITemperatureBlockData soulSconceOff = temperatureBlock(0.0f).addProperty("lit", "false");
        block("soul_sconce").addTemperature(soulSconceOn).addTemperature(soulSconceOff);
    }
}
