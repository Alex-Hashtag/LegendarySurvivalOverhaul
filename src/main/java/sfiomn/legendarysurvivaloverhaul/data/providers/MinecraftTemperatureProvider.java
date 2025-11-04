package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.concurrent.CompletableFuture;

public class MinecraftTemperatureProvider extends TemperatureDataProvider
{

    public MinecraftTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("minecraft", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        dimension("overworld").temperature(20).hasAltitude().seaLevelHeight(64);
        dimension("the_end").temperature(-13);
        dimension("the_nether").temperature(27);

        block("campfire")
                .addTemperature(temperatureBlock(10.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("soul_campfire")
                .addTemperature(temperatureBlock(-10.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("furnace")
                .addTemperature(temperatureBlock(6.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("blast_furnace")
                .addTemperature(temperatureBlock(6.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block("smoker")
                .addTemperature(temperatureBlock(6.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        block("torch").addTemperature(temperatureBlock(1.5f));
        block("wall_torch").addTemperature(temperatureBlock(1.5f));
        block("soul_torch").addTemperature(temperatureBlock(-1.5f));
        block("soul_wall_torch").addTemperature(temperatureBlock(-1.5f));
        block("fire").addTemperature(temperatureBlock(7.0f));
        block("soul_fire").addTemperature(temperatureBlock(-7.0f));

        block("magma_block").addTemperature(temperatureBlock(12.0f));
        block("jack_o_lantern").addTemperature(temperatureBlock(3.0f));
        block("lava").addTemperature(temperatureBlock(12.5f));
        block("flowing_lava").addTemperature(temperatureBlock(12.5f));

        item("lava_bucket").temperature(6.0f);
        item("magma_block").temperature(6.0f);
        item("torch").temperature(1.0f);
        item("soul_torch").temperature(-1.0f);
        item("ice").temperature(-1.0f);
        item("packed_ice").temperature(-2.0f);
        item("blue_ice").temperature(-3.0f);

        item("leather_boots").coldResistance(0.5f);
        item("leather_leggings").coldResistance(1.0f);
        item("leather_chestplate").coldResistance(1.5f);
        item("leather_helmet").coldResistance(1.0f);

        item("golden_boots").coldResistance(0.5f);
        item("golden_leggings").coldResistance(1.0f);
        item("golden_chestplate").coldResistance(1.0f);
        item("golden_helmet").coldResistance(0.5f);

        item("iron_boots").heatResistance(0.5f);
        item("iron_leggings").heatResistance(1.0f);
        item("iron_chestplate").heatResistance(1.0f);
        item("iron_helmet").heatResistance(0.5f);

        item("diamond_boots").heatResistance(1.0f);
        item("diamond_leggings").heatResistance(1.0f);
        item("diamond_chestplate").heatResistance(1.5f);
        item("diamond_helmet").heatResistance(0.5f);

        item("netherite_boots").coldResistance(1.5f);
        item("netherite_leggings").coldResistance(1.5f);
        item("netherite_chestplate").coldResistance(2.0f);
        item("netherite_helmet").coldResistance(1.0f);

        mount("strider").heatResistance(7.0f);

        consumable("mushroom_stew")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(1).duration(1200));
        consumable("mushroom_stew")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(1).duration(1200));
        consumable("rabbit_stew")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(2).duration(1200));
        consumable("suspicious_stew")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(1).duration(1200));
        consumable("melon_slice")
                .addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.FOOD).temperatureLevel(-1).duration(1200));

        biome("crimson_forest").temperature(0.75f).isDry(false);
        biome("warped_forest").temperature(0.75f).isDry(false);
        biome("nether_wastes").temperature(1.0f).isDry(false);
        biome("soul_sand_valley").temperature(1.0f).isDry(false);
        biome("basalt_deltas").temperature(1.45f).isDry(false);
        biome("frozen_ocean").temperature(-0.5f).isDry(false);
        biome("deep_frozen_ocean").temperature(-0.5f).isDry(false);

        fuelItem("coal").thermalType(ThermalTypeEnum.HEATING).duration(600);
        fuelItem("charcoal").thermalType(ThermalTypeEnum.HEATING).duration(600);
        fuelItem("coal_block").thermalType(ThermalTypeEnum.HEATING).duration(2700);
        fuelItem("ice").thermalType(ThermalTypeEnum.COOLING).duration(400);
        fuelItem("snowball").thermalType(ThermalTypeEnum.COOLING).duration(400);
        fuelItem("snow_block").thermalType(ThermalTypeEnum.COOLING).duration(600);
        fuelItem("blue_ice").thermalType(ThermalTypeEnum.COOLING).duration(16200);
        fuelItem("packed_ice").thermalType(ThermalTypeEnum.COOLING).duration(1800);
    }
}
