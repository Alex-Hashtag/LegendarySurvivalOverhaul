package sfiomn.legendarysurvivaloverhaul.common.integration;

import net.neoforged.fml.ModList;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.JsonPropertyValue;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.config.json_old.JsonConfig;

import java.util.Arrays;

/**
 * This class is specifically for implementing default configuration values
 * for mods that don't require us to set up new modifiers; i.e. simply setting
 * relevant values for armor/block temperatures, consumable temperatures, etc.
 *
 * @author Icey
 */
public final class IntegrationController
{
    public static void initIntegration()
    {
        ModList mods = ModList.get();

        if (mods.isLoaded("create"))
            initCreate();
        if (mods.isLoaded("farmersdelight"))
            initFarmersDelight();
        if (mods.isLoaded("realistictorches"))
            initRealisticTorches();
        if (mods.isLoaded("byg"))
            initBYG();
        if (mods.isLoaded("artifacts"))
            initArtifacts();
        if (mods.isLoaded("endergetic"))
            initEndergeticExpansion();
        if (mods.isLoaded("infernalexp"))
            initInfernalExpansion();
        if (mods.isLoaded("biomesoplenty"))
            initBiomesOPlenty();
        if (mods.isLoaded("betterendforge"))
            initBetterEndForge();
        if (mods.isLoaded("betterend"))
            initBetterEnd();
        if (mods.isLoaded("atmospheric"))
            initAtmospheric();
        if (mods.isLoaded("legendarycreatures"))
            initLegendaryCreatures();

        if (mods.isLoaded("neapolitan"))
        {
            initNeapolitan();

            // Since Seasonals and Peculiars depend on Neapolitan,
            // we can skip checking them if Neapolitan isn't installed
            if (mods.isLoaded("seasonals"))
                initSeasonals();
            if (mods.isLoaded("peculiars"))
                initPeculiars();
        }

        if (mods.isLoaded("supplementaries"))
            initSupplementaries();
        if (mods.isLoaded("crockpot"))
            initCrockpot();
        if (mods.isLoaded("quark"))
            initQuark();
        if (mods.isLoaded("beachparty"))
            initBeachParty();
        if (mods.isLoaded("create_confectionery"))
            initCreateConfectionery();
        if (mods.isLoaded("wardrobe"))
            initWardrobe();
        if (mods.isLoaded("iceandfire"))
            initIceAndFire();
        if (mods.isLoaded("alexsmobs"))
            initAlexsMobs();
        if (mods.isLoaded("aquamirae"))
            initAquamirae();
        if (mods.isLoaded("call_of_yucutan"))
            initCallOfYucutan();
        if (mods.isLoaded("cataclysm"))
            initCataclysm();
        if (mods.isLoaded("endermanoverhaul"))
            initEndermanOverhaul();
        if (mods.isLoaded("irons_spellbooks"))
            initIronsSpellbooks();
        if (mods.isLoaded("born_in_chaos_v1"))
            initBornInChaos();
        if (mods.isLoaded("upgrade_aquatic"))
            initUpgradeAquatic();
        if (LegendarySurvivalOverhaul.terraFirmaCraftLoaded)
            initTerraFirmaCraft();
        if (mods.isLoaded("legendary_additions"))
            initLegendaryAdditions();
        if (mods.isLoaded("hardcore_torches"))
            initHardcoreTorches();
    }

    private static void initCreate()
    {
        JsonConfig.registerDefaultBlockFluidTemperature("create:blaze_burner", 2.5f, new JsonPropertyValue("blaze", "smouldering"));
        JsonConfig.registerDefaultBlockFluidTemperature("create:blaze_burner", 5.0f, new JsonPropertyValue("blaze", "kindled"));
        JsonConfig.registerDefaultBlockFluidTemperature("create:blaze_burner", 7.5f, new JsonPropertyValue("blaze", "seething"));
    }

    private static void initFarmersDelight()
    {
        JsonConfig.registerDefaultBlockFluidTemperature("farmersdelight:stove", 7.5f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("farmersdelight:stove", 0.0f, new JsonPropertyValue("lit", "false"));

        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:beef_stew", 2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:chicken_soup", 2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:vegetable_soup", 2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:fish_stew", 2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:pumpkin_soup", 2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:baked_cod_stew", 2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "farmersdelight:hot_cocoa", 3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "farmersdelight:melon_juice", -1, 1200);

        JsonConfig.registerDefaultConsumableThirst("farmersdelight:chicken_soup", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("farmersdelight:vegetable_soup", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("farmersdelight:pumpkin_soup", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("farmersdelight:hot_cocoa", 4, 1.0f);
        JsonConfig.registerDefaultConsumableThirst("farmersdelight:melon_juice", 8, 4.0f);
    }

    private static void initRealisticTorches()
    {
        JsonConfig.registerDefaultBlockFluidTemperature("realistictorches:torch", 1.5f, new JsonPropertyValue("litstate", "2"));
        JsonConfig.registerDefaultBlockFluidTemperature("realistictorches:torch", 0.75f, new JsonPropertyValue("litstate", "1"));
        JsonConfig.registerDefaultBlockFluidTemperature("realistictorches:torch", 0.0f, new JsonPropertyValue("litstate", "0"));

        JsonConfig.registerDefaultBlockFluidTemperature("realistictorches:torch_wall", 1.5f, new JsonPropertyValue("litstate", "2"));
        JsonConfig.registerDefaultBlockFluidTemperature("realistictorches:torch_wall", 0.75f, new JsonPropertyValue("litstate", "1"));
        JsonConfig.registerDefaultBlockFluidTemperature("realistictorches:torch_wall", 0.0f, new JsonPropertyValue("litstate", "0"));
    }

    private static void initBYG()
    {
        JsonConfig.registerDefaultBlockFluidTemperature("byg:boric_fire", 5.0f);
        JsonConfig.registerDefaultBlockFluidTemperature("byg:boric_campfire", 10.0f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("byg:boric_campfire", 0.0f, new JsonPropertyValue("lit", "false"));

        JsonConfig.registerDefaultBlockFluidTemperature("byg:cryptic_fire", 5.0f);
        JsonConfig.registerDefaultBlockFluidTemperature("byg:cryptic_campfire", 7.5f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("byg:cryptic_campfire", 0.0f, new JsonPropertyValue("lit", "false"));
    }

    private static void initArtifacts()
    {
        JsonConfig.registerDefaultItemTemperature("artifacts:villager_hat", 0.0f, 2.5f, 0.0f, 0.0f);
        JsonConfig.registerDefaultItemTemperature("artifacts:lucky_scarf", 0.0f, 0.0f, 2.5f, 0.0f);
        JsonConfig.registerDefaultItemTemperature("artifacts:scarf_of_invisibility", 0.0f, 0.0f, 2.5f, 0.0f);
    }

    private static void initEndergeticExpansion()
    {
        JsonConfig.registerDefaultBlockFluidTemperature("endergetic:ender_fire", -7.0f);
        JsonConfig.registerDefaultBlockFluidTemperature("endergetic:ender_campfire", -10.0f);
        JsonConfig.registerDefaultBlockFluidTemperature("endergetic:ender_torch", -1.5f);
        JsonConfig.registerDefaultBlockFluidTemperature("endergetic:ender_wall_torch", -1.5f);
    }

    private static void initInfernalExpansion()
    {
        JsonConfig.registerDefaultBlockFluidTemperature("infernalexp:fire_glow", 7.0f);
        JsonConfig.registerDefaultBlockFluidTemperature("infernalexp:campfire_glow", 10.0f);
        JsonConfig.registerDefaultBlockFluidTemperature("infernalexp:torch_glow", 1.5f);
        JsonConfig.registerDefaultBlockFluidTemperature("infernalexp:torch_glow_wall", 1.5f);
    }

    private static void initBiomesOPlenty()
    {
        JsonConfig.registerDefaultBiomeOverride("biomesoplenty:crystalline_chasm", 0.8f, false);
        JsonConfig.registerDefaultBiomeOverride("biomesoplenty:undergrowth", 0.75f, false);
        JsonConfig.registerDefaultBiomeOverride("biomesoplenty:visceral_heap", 0.9f, false);
        JsonConfig.registerDefaultBiomeOverride("biomesoplenty:withered_abyss", 1.5f, false);
    }

    private static void initNeapolitan()
    {
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:ice_cubes", -1, 3600);

        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:chocolate_ice_cream", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:vanilla_ice_cream", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:strawberry_ice_cream", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:banana_ice_cream", -3, 3600);

        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:neapolitan_ice_cream", -3, 3600);

        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:chocolate_milkshake", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:vanilla_milkshake", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:strawberry_milkshake", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:banana_milkshake", -3, 3600);

        JsonConfig.registerDefaultConsumableThirst("neapolitan:chocolate_milkshake", 4, 1.0f);
        JsonConfig.registerDefaultConsumableThirst("neapolitan:vanilla_milkshake", 4, 1.0f);
        JsonConfig.registerDefaultConsumableThirst("neapolitan:strawberry_milkshake", 4, 1.0f);
        JsonConfig.registerDefaultConsumableThirst("neapolitan:banana_milkshake", 4, 1.0f);
    }

    private static void initSeasonals()
    {
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "seasonals:pumpkin_ice_cream", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "seasonals:sweet_berry_ice_cream", -3, 3600);

        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "seasonals:pumpkin_milkshake", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "seasonals:sweet_berry_milkshake", -3, 3600);
        JsonConfig.registerDefaultConsumableThirst("seasonals:pumpkin_milkshake", 4, 1.0f);
        JsonConfig.registerDefaultConsumableThirst("seasonals:sweet_berry_milkshake", 4, 1.0f);
    }

    private static void initPeculiars()
    {
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "peculiars:yucca_ice_cream", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "peculiars:aloe_ice_cream", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "peculiars:passionfruit_ice_cream", -3, 3600);

        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "peculiars:yucca_milkshake", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "peculiars:aloe_milkshake", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "peculiars:passionfruit_milkshake", -3, 3600);
        JsonConfig.registerDefaultConsumableThirst("peculiars:yucca_milkshake", 6, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("peculiars:aloe_milkshake", 6, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("peculiars:passionfruit_milkshake", 6, 2.0f);
    }

    private static void initBetterEnd()
    {
        JsonConfig.registerDefaultFuelItems("betterend:charcoal_block", ThermalTypeEnum.HEATING, 270);

        JsonConfig.registerDefaultItemTemperature("betterend:aeternium_helmet", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:aeternium_chestplate", 0, 3.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:aeternium_leggings", 0, 2.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:aeternium_boots", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:crystalite_helmet", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:crystalite_chestplate", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:crystalite_leggings", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:crystalite_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:terminite_helmet", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:terminite_chestplate", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:terminite_leggings", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:terminite_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:thallasium_helmet", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:thallasium_chestplate", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:thallasium_leggings", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betterend:thallasium_boots", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:cincinnasite_helmet", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:cincinnasite_chestplate", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:cincinnasite_leggings", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:cincinnasite_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:nether_ruby_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:nether_ruby_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:nether_ruby_leggings", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:nether_ruby_boots", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:flaming_ruby_helmet", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:flaming_ruby_chestplate", 0, 0, 2.5f, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:flaming_ruby_leggings", 0, 0, 2.5f, 0);
        JsonConfig.registerDefaultItemTemperature("betternether:flaming_ruby_boots", 0, 0, 1.5f, 0);
    }

    private static void initBetterEndForge()
    {
        JsonConfig.registerDefaultBiomeOverride("betterendforge:sulphur_springs", 1.1f);
        JsonConfig.registerDefaultBiomeOverride("betterendforge:ice_starfield", 0.1f);

        JsonConfig.registerDefaultFuelItems("betterendforge:coal_block", ThermalTypeEnum.HEATING, 270);
        JsonConfig.registerDefaultFuelItems("betterendforge:charcoal_block", ThermalTypeEnum.HEATING, 270);
    }

    private static void initAtmospheric()
    {
        JsonConfig.registerDefaultDamageSourceBodyParts("atmospheric.yuccaSapling", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        JsonConfig.registerDefaultDamageSourceBodyParts("atmospheric.yuccaFlower", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        JsonConfig.registerDefaultDamageSourceBodyParts("atmospheric.yuccaBranch", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        JsonConfig.registerDefaultDamageSourceBodyParts("atmospheric.yuccaLeaves", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        JsonConfig.registerDefaultDamageSourceBodyParts("atmospheric.barrelCactus", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        JsonConfig.registerDefaultDamageSourceBodyParts("atmospheric.aloeLeaves", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
    }

    private static void initLegendaryCreatures()
    {
        JsonConfig.registerDefaultDamageSourceBodyParts("legendarycreatures.root_attack", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));

        JsonConfig.registerDefaultItemTemperature("legendarycreatures:straw_hat", 0, 3, 0, 0);
    }

    private static void initSupplementaries()
    {
        JsonConfig.registerDefaultDamageSourceBodyParts("supplementaries.bamboo_spikes", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
    }

    private static void initCrockpot()
    {
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:asparagus_soup", 2, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:iced_tea", -2, 4800);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:ice_cream", -3, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:tea", 3, 4800);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bacon_eggs", 1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bone_stew", 3, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bunny_stew", 1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bone_soup", 2, 1800);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:california_roll", -1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:flower_salad", -3, 6000);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:fruit_medley", -1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:gazpacho", -2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:hot_chili", 2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:hot_cocoa", 3, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:kabobs", 1, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:meat_balls", 1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:moqueca", 1, 1800);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:plain_omelette", 1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:pepper_popper", 3, 6000);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:pierogi", 2, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:ratatouille", 3, 6000);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:salmon_sushi", -1, 1800);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:salsa", -2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:turkey_dinner", 2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:veg_stinger", -1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:watermelon_icle", -4, 1800);

        JsonConfig.registerDefaultConsumableThirst("crockpot:asparagus_soup", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("crockpot:bone_soup", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("crockpot:iced_tea", 8, 5.0f);
        JsonConfig.registerDefaultConsumableThirst("crockpot:tea", 5, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("crockpot:gazpacho", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("crockpot:hot_cocoa", 2, 1.0f);
        JsonConfig.registerDefaultConsumableThirst("crockpot:fruit_medley", 3, 1.0f);
        JsonConfig.registerDefaultConsumableThirst("crockpot:veg_stinger", 5, 2.0f);
    }

    private static void initQuark()
    {
        JsonConfig.registerDefaultFuelItems("quark:coal_block", ThermalTypeEnum.HEATING, 270);
        JsonConfig.registerDefaultFuelItems("quark:charcoal_block", ThermalTypeEnum.HEATING, 270);
    }

    private static void initBeachParty()
    {
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:sweetberry_icecream", -2, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:coconut_icecream", -2, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:chocolate_icecream", -3, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_coconut", -2, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_cactus", -2, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_chocolate", -2, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_sweetberries", -2, 3600);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_melon", -2, 3600);

        JsonConfig.registerDefaultConsumableThirst("beachparty:coconut_open", 3, 0.0f);

        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:coconut_cocktail", -1, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:sweetberries_cocktail", -1, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:cocoa_cocktail", -2, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:pumpkin_cocktail", -1, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:melon_cocktail", -1, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:honey_cocktail", -1, 2400);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:refreshing_drink", -1, 2400);
        JsonConfig.registerDefaultConsumableThirst("beachparty:coconut_cocktail", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("beachparty:sweetberries_cocktail", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("beachparty:cocoa_cocktail", 5, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("beachparty:pumpkin_cocktail", 5, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("beachparty:melon_cocktail", 5, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("beachparty:honey_cocktail", 7, 3.0f);
        JsonConfig.registerDefaultConsumableThirst("beachparty:refreshing_drink", 10, 5.0f);

        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:sweetberry_milkshake", -3, 4800);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:coconut_milkshake", -3, 4800);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:chocolate_milkshake", -3, 4800);
        JsonConfig.registerDefaultConsumableThirst("beachparty:sweetberry_milkshake", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("beachparty:coconut_milkshake", 5, 3.0f);
        JsonConfig.registerDefaultConsumableThirst("beachparty:chocolate_milkshake", 7, 4.0f);

        JsonConfig.registerDefaultItemTemperature("beachparty:beach_hat", 0, 2.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("beachparty:trunks", 0, 3.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("beachparty:bikine", 0, 3.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("beachparty:crocs", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("beachparty:palm_torch_item", 1, 0.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("beachparty:palm_tall_torch", 1, 0.0f, 0, 0);
    }

    private static void initCreateConfectionery()
    {
        JsonConfig.registerDefaultConsumableThirst("create_confectionery:hot_chocolate_bottle", 3, 2.0f);
    }

    private static void initWardrobe()
    {
        JsonConfig.registerDefaultItemTemperature("wardrobe:taiga_helmet", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:taiga_chestplate", 0, 0, 3.5f, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:taiga_leggings", 0, 0, 3.0f, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:taiga_boots", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:snowy_helmet", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:snowy_chestplate", 0, 0, 3.5f, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:snowy_leggings", 0, 0, 3.0f, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:snowy_boots", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:desert_helmet", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:desert_chestplate", 0, 3.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:desert_leggings", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:desert_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:jungle_chestplate", 0, 5.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:jungle_leggings", 0, 3.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:jungle_boots", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:savanna_chestplate", 0, 4.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:savanna_leggings", 0, 2.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:savanna_boots", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:wool_vest_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:chiton", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("wardrobe:farmers_hat_helmet", 0, 3.5f, 0, 0);
    }

    private static void initIceAndFire()
    {
        JsonConfig.registerDefaultEntityTemperature("iceandfire:hippocampus", 3.0f);

        JsonConfig.registerDefaultItemTemperature("iceandfire:dread_torch", -1, 0, 0, 0);

        JsonConfig.registerDefaultFuelItems("iceandfire:dragon_ice", ThermalTypeEnum.COOLING, 20);
        JsonConfig.registerDefaultFuelItems("iceandfire:dread_shard", ThermalTypeEnum.COOLING, 180);

        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_silver_metal_helmet", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_silver_metal_chestplate", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_silver_metal_leggings", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_silver_metal_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_copper_metal_helmet", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_copper_metal_chestplate", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_copper_metal_leggings", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_copper_metal_boots", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:sheep_helmet", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:sheep_chestplate", 0, 0, 3.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:sheep_leggings", 0, 0, 3.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:sheep_boots", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_yellow_helmet", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_yellow_chestplate", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_yellow_leggings", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_yellow_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_white_helmet", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_white_chestplate", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_white_leggings", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_white_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_red_helmet", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_red_chestplate", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_red_leggings", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:deathworm_red_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_ice_helmet", 0, 3.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_ice_chestplate", 0, 5.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_ice_leggings", 0, 4.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_ice_boots", 0, 2.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_fire_helmet", 0, 0, 3.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_fire_chestplate", 0, 0, 5.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_fire_leggings", 0, 0, 4.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_fire_boots", 0, 0, 2.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_lightning_helmet", 0, 0, 0, 1.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_lightning_chestplate", 0, 0, 0, 2.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_lightning_leggings", 0, 0, 0, 2.0f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:dragonsteel_lightning_boots", 0, 0, 0, 1.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_red_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_red_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_red_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_red_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_bronze_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_bronze_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_bronze_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_bronze_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_green_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_green_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_green_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_green_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_gray_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_gray_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_gray_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_gray_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_blue_helmet", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_blue_chestplate", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_blue_leggings", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_blue_boots", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_silver_helmet", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_silver_chestplate", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_silver_leggings", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_silver_boots", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_white_helmet", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_white_chestplate", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_white_leggings", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_white_boots", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_sapphire_helmet", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_sapphire_chestplate", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_sapphire_leggings", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_sapphire_boots", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_amythest_helmet", 0, 0, 0, 0.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_amythest_chestplate", 0, 0, 0, 1.0f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_amythest_leggings", 0, 0, 0, 1.0f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_amythest_boots", 0, 0, 0, 0.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_copper_helmet", 0, 0, 0, 0.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_copper_chestplate", 0, 0, 0, 1.0f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_copper_leggings", 0, 0, 0, 1.0f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_copper_boots", 0, 0, 0, 0.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_black_helmet", 0, 0, 0, 0.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_black_chestplate", 0, 0, 0, 1.0f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_black_leggings", 0, 0, 0, 1.0f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_black_boots", 0, 0, 0, 0.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_electric_helmet", 0, 0, 0, 0.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_electric_chestplate", 0, 0, 0, 1.0f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_electric_leggings", 0, 0, 0, 1.0f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:armor_electric_boots", 0, 0, 0, 0.5f);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_red_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_red_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_red_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_red_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_blue_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_blue_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_blue_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_blue_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_bronze_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_bronze_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_bronze_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_bronze_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_green_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_green_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_green_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_green_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_deepblue_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_deepblue_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_deepblue_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_deepblue_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_purple_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_purple_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_purple_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_purple_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_teal_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_teal_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_teal_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("iceandfire:tide_teal_boots", 0, 0, 1.5f, 0);
    }

    private static void initAlexsMobs()
    {
        JsonConfig.registerDefaultItemTemperature("alexsmobs:roadrunner_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("alexsmobs:crocodile_chestplate", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("alexsmobs:centipede_leggings", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("alexsmobs:frontier_cap", 0, 0, 4.0f, 0);
        JsonConfig.registerDefaultItemTemperature("alexsmobs:sombrero", 0, 4.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("alexsmobs:emu_leggings", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("alexsmobs:froststalker_helmet", 0, 3.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("alexsmobs:unsettling_kimono", 0, 2.0f, 0, 0);
    }

    private static void initAquamirae()
    {
        JsonConfig.registerDefaultItemTemperature("aquamirae:terrible_helmet", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:terrible_chestplate", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:terrible_leggings", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:terrible_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:abyssal_heaume", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:abyssal_brigantine", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:abyssal_leggings", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:abyssal_boots", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:three_bolt_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:three_bolt_suit", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:three_bolt_leggings", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("aquamirae:three_bolt_boots", 0, 0, 1.0f, 0);
    }

    private static void initCallOfYucutan()
    {
        JsonConfig.registerDefaultItemTemperature("call_of_yucutan:jades_helmet", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("call_of_yucutan:jades_chestplate", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("call_of_yucutan:jades_leggings", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("call_of_yucutan:jades_boots", 0, 0.5f, 0, 0);
    }

    private static void initCataclysm()
    {
        JsonConfig.registerDefaultItemTemperature("cataclysm:bone_reptile_helmet", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("cataclysm:bone_reptile_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("cataclysm:ignitium_helmet", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("cataclysm:ignitium_chestplate", 0, 0, 3.0f, 0);
        JsonConfig.registerDefaultItemTemperature("cataclysm:ignitium_elytra_chestplate", 0, 0, 3.0f, 0);
        JsonConfig.registerDefaultItemTemperature("cataclysm:ignitium_leggings", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("cataclysm:ignitium_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("cataclysm:monstrous_helm", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("cataclysm:bloom_stone_pauldrons", 0, 1.0f, 0, 0);
    }

    private static void initEndermanOverhaul()
    {
        JsonConfig.registerDefaultItemTemperature("endermanoverhaul:badlands_hood", 0, 0, 2.5f, 0);
        JsonConfig.registerDefaultItemTemperature("endermanoverhaul:savanna_hood", 0, 0, 2.5f, 0);
        JsonConfig.registerDefaultItemTemperature("endermanoverhaul:snowy_hood", 0, 0, 2.5f, 0);
    }

    private static void initIronsSpellbooks()
    {
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:pyromancer_helmet", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:pyromancer_chestplate", 0, 0, 2.5f, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:pyromancer_leggings", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:pyromancer_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:cryomancer_helmet", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:cryomancer_chestplate", 0, 2.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:cryomancer_leggings", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:cryomancer_boots", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:netherite_mage_helmet", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:netherite_mage_chestplate", 0, 0, 3.5f, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:netherite_mage_leggings", 0, 0, 2.5f, 0);
        JsonConfig.registerDefaultItemTemperature("irons_spellbooks:netherite_mage_boots", 0, 0, 2.0f, 0);
    }

    private static void initBornInChaos()
    {
        JsonConfig.registerDefaultItemTemperature("born_in_chaos_v1:dark_metal_armor_helmet", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("born_in_chaos_v1:dark_metal_armor_chestplate", 0, 2.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("born_in_chaos_v1:dark_metal_armor_leggings", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("born_in_chaos_v1:dark_metal_armor_boots", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("born_in_chaos_v1:spiritual_guide_sombrero_helmet", 0, 3.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("born_in_chaos_v1:nightmare_mantleofthe_night_helmet", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("born_in_chaos_v1:nightmare_mantleofthe_night_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("born_in_chaos_v1:nightmare_mantleofthe_night_leggings", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("born_in_chaos_v1:nightmare_mantleofthe_night_boots", 0, 0, 0.5f, 0);
    }

    private static void initUpgradeAquatic()
    {
        JsonConfig.registerDefaultConsumableThirst("upgrade_aquatic:mulberry", 4, 0);
    }

    private static void initTerraFirmaCraft()
    {
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:firepit", 8.0f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:firepit", 0.0f, new JsonPropertyValue("lit", "false"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:pot", 8.0f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:pot", 0.0f, new JsonPropertyValue("lit", "false"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:charcoal_forge", 15.0f, new JsonPropertyValue("heat_level", "6"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:charcoal_forge", 13.0f, new JsonPropertyValue("heat_level", "5"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:charcoal_forge", 11.0f, new JsonPropertyValue("heat_level", "4"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:charcoal_forge", 9.0f, new JsonPropertyValue("heat_level", "3"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:charcoal_forge", 6.0f, new JsonPropertyValue("heat_level", "2"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:charcoal_forge", 3.0f, new JsonPropertyValue("heat_level", "1"));
        JsonConfig.registerDefaultBlockFluidTemperature("tfc:charcoal_forge", 0.0f, new JsonPropertyValue("heat_level", "0"));
    }

    private static void initLegendaryAdditions()
    {
        JsonConfig.registerDefaultItemTemperature("legendaryadditions:tribal_torch", 1.0f, 0, 0, 0);
    }

    private static void initHardcoreTorches()
    {
        JsonConfig.registerDefaultBlockFluidTemperature("hardcore_torches:hardcore_campfire", 10f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("hardcore_torches:hardcore_campfire", 0.0f, new JsonPropertyValue("lit", "false"));

        JsonConfig.registerDefaultBlockFluidTemperature("hardcore_torches:lit_wall_torch", 1.5f);
        JsonConfig.registerDefaultBlockFluidTemperature("hardcore_torches:lit_torch", 1.5f);

        JsonConfig.registerDefaultBlockFluidTemperature("hardcore_torches:smoldering_wall_torch", 0.75f);
        JsonConfig.registerDefaultBlockFluidTemperature("hardcore_torches:smoldering_torch", 0.75f);
    }
}
