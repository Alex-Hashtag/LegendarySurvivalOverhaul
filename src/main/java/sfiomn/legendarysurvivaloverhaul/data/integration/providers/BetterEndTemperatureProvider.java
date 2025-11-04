package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class BetterEndTemperatureProvider extends TemperatureDataProvider
{

    public BetterEndTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("betterend", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        fuelItem("charcoal_block").thermalType(ThermalTypeEnum.HEATING).duration(5400);

        fuelItem("emerald_ice").thermalType(ThermalTypeEnum.COOLING).duration(1800);
        fuelItem("dense_emerald_ice").thermalType(ThermalTypeEnum.COOLING).duration(3600);
        fuelItem("ancient_emerald_ice").thermalType(ThermalTypeEnum.COOLING).duration(16200);

        item("emerald_ice").temperature(-2.0f);
        item("dense_emerald_ice").temperature(-3.0f);
        item("ancient_emerald_ice").temperature(-4.0f);

        item("aeternium_helmet").heatResistance(2.0f);
        item("aeternium_chestplate").heatResistance(3.0f);
        item("aeternium_leggings").heatResistance(2.5f);
        item("aeternium_boots").heatResistance(2.0f);

        item("crystalite_helmet").heatResistance(1.5f);
        item("crystalite_chestplate").heatResistance(2.0f);
        item("crystalite_leggings").heatResistance(2.0f);
        item("crystalite_boots").heatResistance(1.0f);

        item("terminite_helmet").heatResistance(1.0f);
        item("terminite_chestplate").heatResistance(1.5f);
        item("crystalite_leggings").heatResistance(1.0f);
        item("crystalite_boots").heatResistance(1.0f);

        item("thallasium_helmet").heatResistance(0.5f);
        item("thallasium_chestplate").heatResistance(1.0f);
        item("thallasium_leggings").heatResistance(0.5f);
        item("thallasium_boots").heatResistance(0.5f);

        item("cincinnasite_helmet").heatResistance(1.0f);
        item("cincinnasite_chestplate").heatResistance(2.0f);
        item("cincinnasite_leggings").heatResistance(2.0f);
        item("cincinnasite_boots").heatResistance(1.0f);

        item("nether_ruby_helmet").coldResistance(1.0f);
        item("nether_ruby_chestplate").coldResistance(2.0f);
        item("nether_ruby_leggings").coldResistance(2.0f);
        item("nether_ruby_boots").coldResistance(1.0f);

        item("flaming_ruby_helmet").coldResistance(1.5f);
        item("flaming_ruby_chestplate").coldResistance(2.5f);
        item("flaming_ruby_leggings").coldResistance(2.5f);
        item("flaming_ruby_boots").coldResistance(1.5f);
    }
}
