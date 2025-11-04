package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum.COOLING;

public class IceAndFireTemperatureProvider extends TemperatureDataProvider
{

    public IceAndFireTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("iceandfire", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        mount("hippocampus").coldResistance(4.0f);
        mount("ice_dragon").temperature(-7.0f);
        mount("fire_dragon").temperature(10.0f);
        mount("lightning_dragon").temperature(10.0f);

        block("chared_cobblestone").addTemperature(temperatureBlock(6.0f));
        block("chared_dirt").addTemperature(temperatureBlock(6.0f));
        block("chared_dirt_path").addTemperature(temperatureBlock(6.0f));
        block("chared_grass").addTemperature(temperatureBlock(6.0f));
        block("chared_gravel").addTemperature(temperatureBlock(6.0f));
        block("chared_stone").addTemperature(temperatureBlock(6.0f));

        block("dragonforge_fire_brick").addTemperature(temperatureBlock(12.0f));
        block("dragonforge_fire_core").addTemperature(temperatureBlock(16.0f));
        block("dragonforge_fire_core_disabled").addTemperature(temperatureBlock(12.0f));
        block("dragonforge_fire_input").addTemperature(temperatureBlock(12.0f));

        block("dragonforge_ice_brick").addTemperature(temperatureBlock(-12.0f));
        block("dragonforge_ice_core").addTemperature(temperatureBlock(-16.0f));
        block("dragonforge_ice_core_disabled").addTemperature(temperatureBlock(-12.0f));
        block("dragonforge_ice_input").addTemperature(temperatureBlock(-12.0f));

        block("dragonscale_blue").addTemperature(temperatureBlock(-8.0f));
        block("dragonscale_bronze").addTemperature(temperatureBlock(8.0f));
        block("dragonscale_gray").addTemperature(temperatureBlock(8.0f));
        block("dragonscale_green").addTemperature(temperatureBlock(8.0f));
        block("dragonscale_red").addTemperature(temperatureBlock(8.0f));
        block("dragonscale_sapphire").addTemperature(temperatureBlock(-8.0f));
        block("dragonscale_silver").addTemperature(temperatureBlock(-8.0f));
        block("dragonscale_white").addTemperature(temperatureBlock(-8.0f));

        block("frozen_cobblestone").addTemperature(temperatureBlock(-6.0f));
        block("frozen_dirt").addTemperature(temperatureBlock(-6.0f));
        block("frozen_dirt_path").addTemperature(temperatureBlock(-6.0f));
        block("frozen_grass").addTemperature(temperatureBlock(-6.0f));
        block("frozen_gravel").addTemperature(temperatureBlock(-6.0f));
        block("frozen_splinters").addTemperature(temperatureBlock(-6.0f));
        block("frozen_stone").addTemperature(temperatureBlock(-6.0f));

        block("dragon_ice").addTemperature(temperatureBlock(-6.0f));
        item("dragon_ice").temperature(-4.0f);

        block("dread_torch").addTemperature(temperatureBlock(-1.0f));
        item("dread_torch").temperature(-1.0f);

        fuelItem("dragon_ice").thermalType(COOLING).duration(400);
        fuelItem("dread_shard").thermalType(COOLING).duration(3600);

        item("armor_silver_metal_helmet").heatResistance(0.5f);
        item("armor_silver_metal_chestplate").heatResistance(1.5f);
        item("armor_silver_metal_leggings").heatResistance(1.0f);
        item("armor_silver_metal_boots").heatResistance(1.0f);
        item("armor_copper_metal_helmet").heatResistance(0.5f);
        item("armor_copper_metal_chestplate").heatResistance(0.5f);
        item("armor_copper_metal_leggings").heatResistance(0.5f);
        item("armor_copper_metal_boots").heatResistance(0.5f);
        item("sheep_helmet").coldResistance(2.0f);
        item("sheep_chestplate").coldResistance(3.5f);
        item("sheep_leggings").coldResistance(3.0f);
        item("sheep_boots").coldResistance(1.0f);
        item("deathworm_yellow_helmet").heatResistance(0.5f);
        item("deathworm_yellow_chestplate").heatResistance(1.5f);
        item("deathworm_yellow_leggings").heatResistance(1.0f);
        item("deathworm_yellow_boots").heatResistance(1.0f);
        item("deathworm_white_helmet").heatResistance(0.5f);
        item("deathworm_white_chestplate").heatResistance(1.5f);
        item("deathworm_white_leggings").heatResistance(1.0f);
        item("deathworm_white_boots").heatResistance(1.0f);
        item("deathworm_red_helmet").heatResistance(0.5f);
        item("deathworm_red_chestplate").heatResistance(1.5f);
        item("deathworm_red_leggings").heatResistance(1.0f);
        item("deathworm_red_boots").heatResistance(1.0f);
        item("dragonsteel_ice_helmet").heatResistance(3.0f);
        item("dragonsteel_ice_chestplate").heatResistance(5.0f);
        item("dragonsteel_ice_leggings").heatResistance(4.5f);
        item("dragonsteel_ice_boots").heatResistance(2.5f);
        item("dragonsteel_fire_helmet").coldResistance(3.0f);
        item("dragonsteel_fire_chestplate").coldResistance(5.0f);
        item("dragonsteel_fire_leggings").coldResistance(4.5f);
        item("dragonsteel_fire_boots").coldResistance(2.5f);
        item("dragonsteel_lightning_helmet").thermalResistance(1.5f);
        item("dragonsteel_lightning_chestplate").thermalResistance(2.5f);
        item("dragonsteel_lightning_leggings").thermalResistance(2.0f);
        item("dragonsteel_lightning_boots").thermalResistance(1.5f);
        item("armor_red_helmet").coldResistance(1.0f);
        item("armor_red_chestplate").coldResistance(2.0f);
        item("armor_red_leggings").coldResistance(1.5f);
        item("armor_red_boots").coldResistance(1.5f);
        item("armor_bronze_helmet").coldResistance(1.0f);
        item("armor_bronze_chestplate").coldResistance(2.0f);
        item("armor_bronze_leggings").coldResistance(1.5f);
        item("armor_bronze_boots").coldResistance(1.5f);
        item("armor_green_helmet").coldResistance(1.0f);
        item("armor_green_chestplate").coldResistance(2.0f);
        item("armor_green_leggings").coldResistance(1.5f);
        item("armor_green_boots").coldResistance(1.5f);
        item("armor_gray_helmet").coldResistance(1.0f);
        item("armor_gray_chestplate").coldResistance(2.0f);
        item("armor_gray_leggings").coldResistance(1.5f);
        item("armor_gray_boots").coldResistance(1.5f);
        item("armor_blue_helmet").heatResistance(1.0f);
        item("armor_blue_chestplate").heatResistance(2.0f);
        item("armor_blue_leggings").heatResistance(1.5f);
        item("armor_blue_boots").heatResistance(1.5f);
        item("armor_silver_helmet").heatResistance(1.0f);
        item("armor_silver_chestplate").heatResistance(2.0f);
        item("armor_silver_leggings").heatResistance(1.5f);
        item("armor_silver_boots").heatResistance(1.5f);
        item("armor_white_helmet").heatResistance(1.0f);
        item("armor_white_chestplate").heatResistance(2.0f);
        item("armor_white_leggings").heatResistance(1.5f);
        item("armor_white_boots").heatResistance(1.5f);
        item("armor_sapphire_helmet").heatResistance(1.0f);
        item("armor_sapphire_chestplate").heatResistance(2.0f);
        item("armor_sapphire_leggings").heatResistance(1.5f);
        item("armor_sapphire_boots").heatResistance(1.5f);
        item("armor_amythest_helmet").thermalResistance(0.5f);
        item("armor_amythest_chestplate").thermalResistance(1.0f);
        item("armor_amythest_leggings").thermalResistance(1.0f);
        item("armor_amythest_boots").thermalResistance(0.5f);
        item("armor_copper_helmet").thermalResistance(0.5f);
        item("armor_copper_chestplate").thermalResistance(1.0f);
        item("armor_copper_leggings").thermalResistance(1.0f);
        item("armor_copper_boots").thermalResistance(0.5f);
        item("armor_black_helmet").thermalResistance(0.5f);
        item("armor_black_chestplate").thermalResistance(1.0f);
        item("armor_black_leggings").thermalResistance(1.0f);
        item("armor_black_boots").thermalResistance(0.5f);
        item("armor_electric_helmet").thermalResistance(0.5f);
        item("armor_electric_chestplate").thermalResistance(1.0f);
        item("armor_electric_leggings").thermalResistance(1.0f);
        item("armor_electric_boots").thermalResistance(0.5f);
        item("tide_red_helmet").coldResistance(1.0f);
        item("tide_red_chestplate").coldResistance(2.0f);
        item("tide_red_leggings").coldResistance(1.5f);
        item("tide_red_boots").coldResistance(1.5f);
        item("tide_blue_helmet").coldResistance(1.0f);
        item("tide_blue_chestplate").coldResistance(2.0f);
        item("tide_blue_leggings").coldResistance(1.5f);
        item("tide_blue_boots").coldResistance(1.5f);
        item("tide_bronze_helmet").coldResistance(1.0f);
        item("tide_bronze_chestplate").coldResistance(2.0f);
        item("tide_bronze_leggings").coldResistance(1.5f);
        item("tide_bronze_boots").coldResistance(1.5f);
        item("tide_green_helmet").coldResistance(1.0f);
        item("tide_green_chestplate").coldResistance(2.0f);
        item("tide_green_leggings").coldResistance(1.5f);
        item("tide_green_boots").coldResistance(1.5f);
        item("tide_deepblue_helmet").coldResistance(1.0f);
        item("tide_deepblue_chestplate").coldResistance(2.0f);
        item("tide_deepblue_leggings").coldResistance(1.5f);
        item("tide_deepblue_boots").coldResistance(1.5f);
        item("tide_purple_helmet").coldResistance(1.0f);
        item("tide_purple_chestplate").coldResistance(2.0f);
        item("tide_purple_leggings").coldResistance(1.5f);
        item("tide_purple_boots").coldResistance(1.5f);
        item("tide_teal_helmet").coldResistance(1.0f);
        item("tide_teal_chestplate").coldResistance(2.0f);
        item("tide_teal_leggings").coldResistance(1.5f);
        item("tide_teal_boots").coldResistance(1.5f);
    }
}
