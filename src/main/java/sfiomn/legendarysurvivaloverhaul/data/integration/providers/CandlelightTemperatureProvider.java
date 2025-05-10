package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureBlockData;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;
import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.FOOD;

public class CandlelightTemperatureProvider extends TemperatureDataProvider {

    public CandlelightTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("candlelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {

        consumable("beetroot_salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));
        consumable("salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));
        consumable("fresh_garden_salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));
        consumable("tomato_mozzarella_salad").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));
        consumable("harvest_plate").addTemperature(temperatureConsumable(FOOD).temperatureLevel(-1).duration(2400));

        consumable("chicken_alfredo").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumable("chicken_alfredo").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumable("roastbeef_with_glazed_carrots").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumable("bolognese").addTemperature(temperatureConsumable(FOOD).temperatureLevel(1).duration(2400));
        consumable("beef_wellington").addTemperature(temperatureConsumable(FOOD).temperatureLevel(2).duration(2400));
        consumable("lasagne").addTemperature(temperatureConsumable(FOOD).temperatureLevel(2).duration(2400));
        consumable("pasta_with_bolognese").addTemperature(temperatureConsumable(FOOD).temperatureLevel(2).duration(2400));
        consumable("beef_with_mushroom_in_wine_and_potatoes").addTemperature(temperatureConsumable(FOOD).temperatureLevel(3).duration(4800));

        ITemperatureBlockData stoveTemp = temperatureBlock(6.0f).addProperty("lit", "true");
        ITemperatureBlockData stoveTempOff = temperatureBlock(0.0f).addProperty("lit", "false");
        block("cobblestone_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("sandstone_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("stone_bricks_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("deepslate_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("granit_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("end_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("mud_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("quartz_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("red_nether_bricks_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("basalt_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);
        block("bamboo_stove").addTemperature(stoveTemp).addTemperature(stoveTempOff);

        item("flower_crown").heatResistance(1.0f);
        item("dress").heatResistance(1.5f);
    }
}