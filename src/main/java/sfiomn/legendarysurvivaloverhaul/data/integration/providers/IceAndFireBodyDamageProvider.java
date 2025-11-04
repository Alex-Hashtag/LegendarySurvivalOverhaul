package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;

import java.util.concurrent.CompletableFuture;

public class IceAndFireBodyDamageProvider extends BodyDamageDataProvider
{

    public IceAndFireBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("iceandfire", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        item("dragonsteel_ice_helmet").headResistance(0.2f);
        item("dragonsteel_ice_chestplate").chestResistance(0.2f);
        item("dragonsteel_ice_leggings").legsResistance(0.2f);
        item("dragonsteel_ice_boots").feetResistance(0.2f);
        item("dragonsteel_fire_helmet").headResistance(0.2f);
        item("dragonsteel_fire_chestplate").chestResistance(0.2f);
        item("dragonsteel_fire_leggings").legsResistance(0.2f);
        item("dragonsteel_fire_boots").feetResistance(0.2f);
        item("dragonsteel_lightning_helmet").headResistance(0.2f);
        item("dragonsteel_lightning_chestplate").chestResistance(0.2f);
        item("dragonsteel_lightning_leggings").legsResistance(0.2f);
        item("dragonsteel_lightning_boots").feetResistance(0.2f);

        item("armor_red_helmet").headResistance(0.15f);
        item("armor_red_chestplate").chestResistance(0.15f);
        item("armor_red_leggings").legsResistance(0.15f);
        item("armor_red_boots").feetResistance(0.15f);
        item("armor_bronze_helmet").headResistance(0.15f);
        item("armor_bronze_chestplate").chestResistance(0.15f);
        item("armor_bronze_leggings").legsResistance(0.15f);
        item("armor_bronze_boots").feetResistance(0.15f);
        item("armor_green_helmet").headResistance(0.15f);
        item("armor_green_chestplate").chestResistance(0.15f);
        item("armor_green_leggings").legsResistance(0.15f);
        item("armor_green_boots").feetResistance(0.15f);
        item("armor_gray_helmet").headResistance(0.15f);
        item("armor_gray_chestplate").chestResistance(0.15f);
        item("armor_gray_leggings").legsResistance(0.15f);
        item("armor_gray_boots").feetResistance(0.15f);
        item("armor_blue_helmet").headResistance(0.15f);
        item("armor_blue_chestplate").chestResistance(0.15f);
        item("armor_blue_leggings").legsResistance(0.15f);
        item("armor_blue_boots").feetResistance(0.15f);
        item("armor_silver_helmet").headResistance(0.15f);
        item("armor_silver_chestplate").chestResistance(0.15f);
        item("armor_silver_leggings").legsResistance(0.15f);
        item("armor_silver_boots").feetResistance(0.15f);
        item("armor_white_helmet").headResistance(0.15f);
        item("armor_white_chestplate").chestResistance(0.15f);
        item("armor_white_leggings").legsResistance(0.15f);
        item("armor_white_boots").feetResistance(0.15f);
        item("armor_sapphire_helmet").headResistance(0.15f);
        item("armor_sapphire_chestplate").chestResistance(0.15f);
        item("armor_sapphire_leggings").legsResistance(0.15f);
        item("armor_sapphire_boots").feetResistance(0.15f);
        item("armor_amythest_helmet").headResistance(0.15f);
        item("armor_amythest_chestplate").chestResistance(0.15f);
        item("armor_amythest_leggings").legsResistance(0.15f);
        item("armor_amythest_boots").feetResistance(0.15f);
        item("armor_copper_helmet").headResistance(0.15f);
        item("armor_copper_chestplate").chestResistance(0.15f);
        item("armor_copper_leggings").legsResistance(0.15f);
        item("armor_copper_boots").feetResistance(0.15f);
        item("armor_black_helmet").headResistance(0.15f);
        item("armor_black_chestplate").chestResistance(0.15f);
        item("armor_black_leggings").legsResistance(0.15f);
        item("armor_black_boots").feetResistance(0.15f);
        item("armor_electric_helmet").headResistance(0.15f);
        item("armor_electric_chestplate").chestResistance(0.15f);
        item("armor_electric_leggings").legsResistance(0.15f);
        item("armor_electric_boots").feetResistance(0.15f);
        item("tide_red_helmet").headResistance(0.15f);
        item("tide_red_chestplate").chestResistance(0.15f);
        item("tide_red_leggings").legsResistance(0.15f);
        item("tide_red_boots").feetResistance(0.15f);
        item("tide_blue_helmet").headResistance(0.15f);
        item("tide_blue_chestplate").chestResistance(0.15f);
        item("tide_blue_leggings").legsResistance(0.15f);
        item("tide_blue_boots").feetResistance(0.15f);
        item("tide_bronze_helmet").headResistance(0.15f);
        item("tide_bronze_chestplate").chestResistance(0.15f);
        item("tide_bronze_leggings").legsResistance(0.15f);
        item("tide_bronze_boots").feetResistance(0.15f);
        item("tide_green_helmet").headResistance(0.15f);
        item("tide_green_chestplate").chestResistance(0.15f);
        item("tide_green_leggings").legsResistance(0.15f);
        item("tide_green_boots").feetResistance(0.15f);
        item("tide_deepblue_helmet").headResistance(0.15f);
        item("tide_deepblue_chestplate").chestResistance(0.15f);
        item("tide_deepblue_leggings").legsResistance(0.15f);
        item("tide_deepblue_boots").feetResistance(0.15f);
        item("tide_purple_helmet").headResistance(0.15f);
        item("tide_purple_chestplate").chestResistance(0.15f);
        item("tide_purple_leggings").legsResistance(0.15f);
        item("tide_purple_boots").feetResistance(0.15f);
        item("tide_teal_helmet").headResistance(0.15f);
        item("tide_teal_chestplate").chestResistance(0.15f);
        item("tide_teal_leggings").legsResistance(0.15f);
        item("tide_teal_boots").feetResistance(0.15f);
    }
}
