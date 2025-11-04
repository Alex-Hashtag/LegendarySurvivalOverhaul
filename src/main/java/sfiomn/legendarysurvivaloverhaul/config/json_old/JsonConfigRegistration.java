package sfiomn.legendarysurvivaloverhaul.config.json_old;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import org.apache.commons.io.FileUtils;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.JsonPropertyValue;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.bodydamage.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.bodydamage.JsonConsumableHeal;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.temperature.*;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.thirst.JsonEffectParameter;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.IceFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.SunFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.integration.IntegrationController;
import sfiomn.legendarysurvivaloverhaul.config.JsonFileName;
import sfiomn.legendarysurvivaloverhaul.config.JsonTypeToken;
import sfiomn.legendarysurvivaloverhaul.data.builders.*;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static sfiomn.legendarysurvivaloverhaul.util.internal.ThirstUtilInternal.HYDRATION_ENUM_TAG;

public class JsonConfigRegistration
{
    public static Path customDatapackFolder = Paths.get(LegendarySurvivalOverhaul.modConfigPath.toString(), "customDataPack");

    public static void init(File configDir)
    {

        if (configDir.exists())
        {
            registerDefaults(configDir);

            processAllJson(configDir);

            //writeAllToJson(configDir);
            //deleteAllJson(configDir);
        }
    }

    public static void registerDefaults(File configDir)
    {
        JsonConfig.registerDefaultDimensionTemperature("minecraft:overworld", 20);
        JsonConfig.registerDefaultDimensionTemperature("minecraft:the_end", -15);
        JsonConfig.registerDefaultDimensionTemperature("minecraft:the_nether", 28);

        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:campfire", 10f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:campfire", 0.0f, new JsonPropertyValue("lit", "false"));
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:soul_campfire", -10f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:soul_campfire", 0.0f, new JsonPropertyValue("lit", "false"));

        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:torch", 1.5f);
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:soul_torch", -1.5f);

        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:wall_torch", 1.5f);
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:soul_wall_torch", -1.5f);

        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:fire", 7.0f);
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:soul_fire", -7.0f);

        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:furnace", 6.0f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:blast_furnace", 6.0f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:smoker", 6.0f, new JsonPropertyValue("lit", "true"));

        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:furnace", 0.0f, new JsonPropertyValue("lit", "false"));
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:blast_furnace", 0.0f, new JsonPropertyValue("lit", "false"));
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:smoker", 0.0f, new JsonPropertyValue("false", "true"));

        JsonConfig.registerDefaultBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":cooler", -15f, new JsonPropertyValue("lit", "true"));
        JsonConfig.registerDefaultBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":heater", 15f, new JsonPropertyValue("lit", "true"));

        JsonConfig.registerDefaultBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":cooler", 0.0f, new JsonPropertyValue("lit", "false"));
        JsonConfig.registerDefaultBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":heater", 0.0f, new JsonPropertyValue("lit", "false"));

        JsonConfig.registerDefaultBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":ice_fern_crop", -1.5f, new JsonPropertyValue(IceFernBlock.AGE.getName(), String.valueOf(IceFernBlock.MAX_AGE)));
        JsonConfig.registerDefaultBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":sun_fern_crop", 1.5f, new JsonPropertyValue(SunFernBlock.AGE.getName(), String.valueOf(SunFernBlock.MAX_AGE)));

        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:magma_block", 12.0f);

        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:jack_o_lantern", 3.0f);

        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:lava", 12.5f);
        JsonConfig.registerDefaultBlockFluidTemperature("minecraft:flowing_lava", 12.5f);

        JsonConfig.registerDefaultItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_boots", 0, 0, 0.5f, 0);
        JsonConfig.registerDefaultItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_leggings", 0, 0, 2.5f, 0);
        JsonConfig.registerDefaultItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_chestplate", 0, 0, 3.0f, 0);
        JsonConfig.registerDefaultItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_helmet", 0, 0, 1.5f, 0);

        JsonConfig.registerDefaultItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_boots", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_leggings", 0, 2.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_chestplate", 0, 3.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_helmet", 0, 1.5f, 0, 0);

        JsonConfig.registerDefaultItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":nether_chalice", 2f);
        JsonConfig.registerDefaultItemTemperature("minecraft:lava_bucket", 6f);
        JsonConfig.registerDefaultItemTemperature("minecraft:magma_block", 6f);

        JsonConfig.registerDefaultItemTemperature("minecraft:leather_boots", 0, 0, 0.5f, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:leather_leggings", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:leather_chestplate", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:leather_helmet", 0, 0, 1.0f, 0);

        JsonConfig.registerDefaultItemTemperature("minecraft:golden_boots", 0, 0, 0.5f, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:golden_leggings", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:golden_chestplate", 0, 0, 1.0f, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:golden_helmet", 0, 0, 0.5f, 0);

        JsonConfig.registerDefaultItemTemperature("minecraft:iron_boots", 0, 0.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:iron_leggings", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:iron_chestplate", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:iron_helmet", 0, 0.5f, 0, 0);

        JsonConfig.registerDefaultItemTemperature("minecraft:diamond_boots", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:diamond_leggings", 0, 1.0f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:diamond_chestplate", 0, 1.5f, 0, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:diamond_helmet", 0, 0.5f, 0, 0);

        JsonConfig.registerDefaultItemTemperature("minecraft:netherite_boots", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:netherite_leggings", 0, 0, 1.5f, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:netherite_chestplate", 0, 0, 2.0f, 0);
        JsonConfig.registerDefaultItemTemperature("minecraft:netherite_helmet", 0, 0, 1.0f, 0);

        JsonConfig.registerDefaultItemTemperature("minecraft:torch", 1.0f);
        JsonConfig.registerDefaultItemTemperature("minecraft:soul_torch", -1.0f);
        JsonConfig.registerDefaultItemTemperature("minecraft:ice", -1.0f);
        JsonConfig.registerDefaultItemTemperature("minecraft:packed_ice", -2.0f);
        JsonConfig.registerDefaultItemTemperature("minecraft:blue_ice", -3.0f);

        JsonConfig.registerDefaultEntityTemperature("minecraft:strider", -3.0f);

        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:mushroom_stew", 1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "minecraft:mushroom_stew", 1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:rabbit_stew", 2, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:suspicious_stew", 1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:melon_slice", -1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, LegendarySurvivalOverhaul.MOD_ID + ":melon_juice", -1, 1200);
        JsonConfig.registerDefaultConsumableTemperature(TemporaryModifierGroupEnum.DRINK, LegendarySurvivalOverhaul.MOD_ID + ":glistering_melon_juice", -2, 3600);

        JsonConfig.registerDefaultBiomeOverride("minecraft:crimson_forest", 0.75f, false);
        JsonConfig.registerDefaultBiomeOverride("minecraft:warped_forest", 0.75f, false);
        JsonConfig.registerDefaultBiomeOverride("minecraft:nether_wastes", 1.0f, false);
        JsonConfig.registerDefaultBiomeOverride("minecraft:soul_sand_valley", 1.0f, false);
        JsonConfig.registerDefaultBiomeOverride("minecraft:basalt_deltas", 1.45f, false);
        JsonConfig.registerDefaultBiomeOverride("minecraft:frozen_ocean", -0.5f, false);
        JsonConfig.registerDefaultBiomeOverride("minecraft:deep_frozen_ocean", -0.5f, false);

        JsonConfig.registerDefaultFuelItems("minecraft:coal", ThermalTypeEnum.HEATING, 30);
        JsonConfig.registerDefaultFuelItems("minecraft:charcoal", ThermalTypeEnum.HEATING, 30);
        JsonConfig.registerDefaultFuelItems("minecraft:coal_block", ThermalTypeEnum.HEATING, 270);
        JsonConfig.registerDefaultFuelItems("minecraft:ice", ThermalTypeEnum.COOLING, 20);
        JsonConfig.registerDefaultFuelItems("minecraft:snowball", ThermalTypeEnum.COOLING, 20);
        JsonConfig.registerDefaultFuelItems("minecraft:snow_block", ThermalTypeEnum.COOLING, 30);
        JsonConfig.registerDefaultFuelItems("minecraft:blue_ice", ThermalTypeEnum.COOLING, 1620);
        JsonConfig.registerDefaultFuelItems("minecraft:packed_ice", ThermalTypeEnum.COOLING, 180);

        JsonConfig.registerDefaultBlockFluidThirst("minecraft:rain", 1, 0);
        JsonConfig.registerDefaultBlockFluidThirst("minecraft:flowing_water", 3, 0, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)});
        JsonConfig.registerDefaultBlockFluidThirst("minecraft:water", 3, 0, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)});
        JsonConfig.registerDefaultBlockFluidThirst("minecraft:water_cauldron", 3, 0, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)});

        JsonConfig.registerDefaultConsumableThirst("minecraft:melon_slice", 2, 1.0f);
        JsonConfig.registerDefaultConsumableThirst("minecraft:apple", 2, 0.5f);
        JsonConfig.registerDefaultConsumableThirst("minecraft:beetroot_soup", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("minecraft:rabbit_stew", 6, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("minecraft:mushroom_stew", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("minecraft:suspicious_stew", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst("minecraft:rotten_flesh", -2, -1.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 1.0f, 600, 0)});
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":apple_juice", 6, 3.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":beetroot_juice", 9, 4.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":cactus_juice", 9, 3.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":carrot_juice", 4, 2.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":chorus_fruit_juice", 12, 8.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":golden_apple_juice", 20, 20.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":golden_carrot_juice", 12, 12.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":glistering_melon_juice", 16, 16.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":melon_juice", 8, 4.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":pumpkin_juice", 7, 4.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":purified_water_bottle", 6, 1.5f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":water_plant_bag", 3, 0.0f);
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":canteen", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue(HYDRATION_ENUM_TAG, HydrationEnum.NORMAL.getName()));
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":canteen", 6, 1.5f, new JsonPropertyValue(HYDRATION_ENUM_TAG, HydrationEnum.PURIFIED.getName()));
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":large_canteen", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue(HYDRATION_ENUM_TAG, HydrationEnum.NORMAL.getName()));
        JsonConfig.registerDefaultConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":large_canteen", 6, 1.5f, new JsonPropertyValue(HYDRATION_ENUM_TAG, HydrationEnum.PURIFIED.getName()));

        JsonConfig.registerDefaultConsumableThirst("minecraft:potion", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue("Potion", "minecraft:water"));
        JsonConfig.registerDefaultConsumableThirst("minecraft:potion", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue("Potion", "minecraft:mundane"));
        JsonConfig.registerDefaultConsumableThirst("minecraft:potion", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue("Potion", "minecraft:thick"));
        JsonConfig.registerDefaultConsumableThirst("minecraft:potion", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue("Potion", "minecraft:awkward"));
        JsonConfig.registerDefaultConsumableThirst("minecraft:potion", 0, 0.0f, new JsonEffectParameter[]{}, new JsonPropertyValue("Potion", "minecraft:empty"));
        JsonConfig.registerDefaultConsumableThirst("minecraft:potion", 6, 1.5f, new JsonEffectParameter[]{});

        // Body Damage
        JsonConfig.registerDefaultConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":healing_herbs", 1, 2, 600);
        JsonConfig.registerDefaultConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":plaster", 1, 3, 400);
        JsonConfig.registerDefaultConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":bandage", 3, 3, 300);
        JsonConfig.registerDefaultConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":tonic", 0, 5, 600);
        JsonConfig.registerDefaultConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":medikit", 0, 8, 400);

        JsonConfig.registerDefaultDamageSourceBodyParts("fall", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
        JsonConfig.registerDefaultDamageSourceBodyParts("hotFloor", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
        JsonConfig.registerDefaultDamageSourceBodyParts("fallingBlock", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.HEAD));
        JsonConfig.registerDefaultDamageSourceBodyParts("flyIntoWall", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.HEAD));
        JsonConfig.registerDefaultDamageSourceBodyParts("anvil", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.HEAD));
        JsonConfig.registerDefaultDamageSourceBodyParts("lightningBolt", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
        JsonConfig.registerDefaultDamageSourceBodyParts("onFire", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
        JsonConfig.registerDefaultDamageSourceBodyParts("explosion", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
        JsonConfig.registerDefaultDamageSourceBodyParts("bad_respawn_point", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
        JsonConfig.registerDefaultDamageSourceBodyParts("dragonBreath", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
        JsonConfig.registerDefaultDamageSourceBodyParts("inFire", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        JsonConfig.registerDefaultDamageSourceBodyParts("cactus", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        JsonConfig.registerDefaultDamageSourceBodyParts("sweetBerryBush", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        JsonConfig.registerDefaultDamageSourceBodyParts("in_wall", DamageDistributionEnum.NONE, Collections.emptyList());
        JsonConfig.registerDefaultDamageSourceBodyParts("drown", DamageDistributionEnum.NONE, Collections.emptyList());
        JsonConfig.registerDefaultDamageSourceBodyParts("starve", DamageDistributionEnum.NONE, Collections.emptyList());
        JsonConfig.registerDefaultDamageSourceBodyParts("magic", DamageDistributionEnum.NONE, Collections.emptyList());
        JsonConfig.registerDefaultDamageSourceBodyParts("wither", DamageDistributionEnum.NONE, Collections.emptyList());
        JsonConfig.registerDefaultDamageSourceBodyParts(LegendarySurvivalOverhaul.MOD_ID + ".hypothermia", DamageDistributionEnum.NONE, Collections.emptyList());
        JsonConfig.registerDefaultDamageSourceBodyParts(LegendarySurvivalOverhaul.MOD_ID + ".hyperthermia", DamageDistributionEnum.NONE, Collections.emptyList());
        JsonConfig.registerDefaultDamageSourceBodyParts(LegendarySurvivalOverhaul.MOD_ID + ".dehydration", DamageDistributionEnum.NONE, Collections.emptyList());

        IntegrationController.initIntegration();
    }

    public static void writeAllToJson(File jsonDir)
    {
        manuallyWriteToJson(JsonFileName.DIMENSION_TEMP, JsonConfig.defaultDimensionTemperatures, jsonDir);
        manuallyWriteToJson(JsonFileName.BIOME_TEMP, JsonConfig.defaultBiomeOverrides, jsonDir);
        manuallyWriteToJson(JsonFileName.ITEM_TEMP, JsonConfig.defaultItemTemperatures, jsonDir);
        manuallyWriteToJson(JsonFileName.BLOCK_TEMP, JsonConfig.defaultBlockFluidTemperatures, jsonDir);
        manuallyWriteToJson(JsonFileName.FUEL, JsonConfig.defaultFuelItems, jsonDir);
        manuallyWriteToJson(JsonFileName.CONSUMABLE_TEMP, JsonConfig.defaultConsumableTemperature, jsonDir);
        manuallyWriteToJson(JsonFileName.BLOCK_THIRST, JsonConfig.defaultBlockFluidThirst, jsonDir);
        manuallyWriteToJson(JsonFileName.CONSUMABLE_THIRST, JsonConfig.defaultConsumableThirst, jsonDir);
        manuallyWriteToJson(JsonFileName.CONSUMABLE_HEAL, JsonConfig.defaultConsumableHeal, jsonDir);
        manuallyWriteToJson(JsonFileName.DAMAGE_SOURCE_BODY_PARTS, JsonConfig.defaultDamageSourceBodyParts, jsonDir);
    }

    public static void deleteAllJson(File jsonDir)
    {
        deleteJson(JsonFileName.DIMENSION_TEMP, jsonDir);
        deleteJson(JsonFileName.BIOME_TEMP, jsonDir);
        deleteJson(JsonFileName.ITEM_TEMP, jsonDir);
        deleteJson(JsonFileName.BLOCK_TEMP, jsonDir);
        deleteJson(JsonFileName.FUEL, jsonDir);
        deleteJson(JsonFileName.CONSUMABLE_TEMP, jsonDir);
        deleteJson(JsonFileName.BLOCK_THIRST, jsonDir);
        deleteJson(JsonFileName.CONSUMABLE_THIRST, jsonDir);
        deleteJson(JsonFileName.CONSUMABLE_HEAL, jsonDir);
        deleteJson(JsonFileName.DAMAGE_SOURCE_BODY_PARTS, jsonDir);
    }

    public static void deleteJsonDirs()
    {
        if (LegendarySurvivalOverhaul.modIntegrationConfigJsons.toFile().exists())
            LegendarySurvivalOverhaul.modIntegrationConfigJsons.toFile().delete();
        if (LegendarySurvivalOverhaul.modConfigJsons.toFile().exists())
            LegendarySurvivalOverhaul.modConfigJsons.toFile().delete();
    }

    private static void createDirectories(Path directory)
    {
        try
        {
            Files.createDirectories(directory);
        } catch (FileAlreadyExistsException ignored)
        {
        } catch (IOException e)
        {
            LegendarySurvivalOverhaul.LOGGER.error("Failed to create custom datapack directory {}", directory);
            LegendarySurvivalOverhaul.LOGGER.error(e.getStackTrace());
        }
    }

    private static void writeInFile(File file, JsonObject json)
    {

        try
        {
            FileUtils.write(file, json.toString(), (String) null);
        } catch (IOException err)
        {

            LegendarySurvivalOverhaul.LOGGER.error("Failed to create json file {}", file);
            LegendarySurvivalOverhaul.LOGGER.error(err.getStackTrace());
        }
    }

    private static void writeInFile(File file, JsonArray json)
    {

        try
        {
            FileUtils.write(file, json.toString(), (String) null);
        } catch (IOException err)
        {

            LegendarySurvivalOverhaul.LOGGER.error("Failed to create json file {}", file);
            LegendarySurvivalOverhaul.LOGGER.error(err.getStackTrace());
        }
    }

    public static void processAllJson(File jsonDir)
    {
        Gson gson = buildNewGson();

        // Temperature
        Map<String, JsonTemperature> jsonDimensionTemperatures = processJson(JsonFileName.DIMENSION_TEMP, jsonDir);

        if (jsonDimensionTemperatures != null)
        {
            // remove default biome config
            //JsonConfig.dimensionTemperatures.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonDimensionTemperatures.size() + " dimension temperature overrides from JSON");
            for (Map.Entry<String, JsonTemperature> entry : jsonDimensionTemperatures.entrySet())
            {
                JsonConfig.registerDimensionTemperature(entry.getKey(), entry.getValue().temperature);
            }

            MapDifference<String, JsonTemperature> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultDimensionTemperatures, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.DIMENSION_TEMP))), Map.class), gson.fromJson(gson.toJson(JsonConfig.dimensionTemperatures, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.DIMENSION_TEMP))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "temperature", "dimensions");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    TemperatureDimensionData tdd = new TemperatureDimensionData();
                    tdd.temperature(JsonConfig.dimensionTemperatures.get(e).temperature);
                    if (!rl.equals(ResourceLocation.parse("the_nether")) && !rl.equals(ResourceLocation.parse("the_end")))
                    {
                        tdd.hasAltitude().seaLevelHeight(64);
                    }

                    writeInFile(jsonFile, tdd.build());
                }
            }
        }

        Map<String, JsonBiomeIdentity> jsonBiomeIdentities = processJson(JsonFileName.BIOME_TEMP, jsonDir);

        if (jsonBiomeIdentities != null)
        {
            // remove default biome config
            //JsonConfig.biomeOverrides.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonBiomeIdentities.size() + " biome temperature overrides from JSON");
            for (Map.Entry<String, JsonBiomeIdentity> entry : jsonBiomeIdentities.entrySet())
            {
                JsonConfig.registerBiomeOverride(entry.getKey(), entry.getValue().temperature, entry.getValue().isDry);
            }

            MapDifference<String, JsonBiomeIdentity> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultBiomeOverrides, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.BIOME_TEMP))), Map.class), gson.fromJson(gson.toJson(JsonConfig.biomeOverrides, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.BIOME_TEMP))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "temperature", "biomes");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    TemperatureBiomeOverrideData td = new TemperatureBiomeOverrideData();
                    td.temperature(JsonConfig.biomeOverrides.get(e).temperature);
                    td.isDry(JsonConfig.biomeOverrides.get(e).isDry);

                    writeInFile(jsonFile, td.build());
                }
            }
        }

        Map<String, JsonTemperatureResistance> jsonItemTemperatures = processJson(JsonFileName.ITEM_TEMP, jsonDir);

        if (jsonItemTemperatures != null)
        {
            // remove default item config
            //JsonConfig.itemTemperatures.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonItemTemperatures.size() + " item temperature values from JSON");
            for (Map.Entry<String, JsonTemperatureResistance> entry : jsonItemTemperatures.entrySet())
            {
                JsonConfig.registerItemTemperature(entry.getKey(), entry.getValue().temperature, entry.getValue().heatResistance, entry.getValue().coldResistance, entry.getValue().thermalResistance);
            }

            MapDifference<String, JsonBiomeIdentity> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultItemTemperatures, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.ITEM_TEMP))), Map.class), gson.fromJson(gson.toJson(JsonConfig.itemTemperatures, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.ITEM_TEMP))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "temperature", "items");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    TemperatureResistanceData td = new TemperatureResistanceData();
                    td.temperature(JsonConfig.itemTemperatures.get(e).temperature);
                    td.heatResistance(JsonConfig.itemTemperatures.get(e).heatResistance);
                    td.coldResistance(JsonConfig.itemTemperatures.get(e).coldResistance);
                    td.thermalResistance(JsonConfig.itemTemperatures.get(e).thermalResistance);

                    writeInFile(jsonFile, td.build());
                }
            }
        }

        Map<String, JsonTemperature> jsonEntityTemperatures = processJson(JsonFileName.ENTITY_TEMP, jsonDir);

        if (jsonEntityTemperatures != null)
        {
            // remove default armor config
            //JsonConfig.entityTemperatures.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonEntityTemperatures.size() + " entity temperature values from JSON");
            for (Map.Entry<String, JsonTemperature> entry : jsonEntityTemperatures.entrySet())
            {
                JsonConfig.registerEntityTemperature(entry.getKey(), entry.getValue().temperature);
            }

            MapDifference<String, JsonBiomeIdentity> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultEntityTemperatures, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.ENTITY_TEMP))), Map.class), gson.fromJson(gson.toJson(JsonConfig.entityTemperatures, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.ENTITY_TEMP))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "temperature", "entities");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    TemperatureData td = new TemperatureData();
                    td.temperature(JsonConfig.entityTemperatures.get(e).temperature);

                    writeInFile(jsonFile, td.build());
                }
            }
        }

        Map<String, List<JsonBlockFluidTemperature>> jsonBlockFluidTemperatures = processJson(JsonFileName.BLOCK_TEMP, jsonDir);

        if (jsonBlockFluidTemperatures != null)
        {
            // remove default block config
            //JsonConfig.blockFluidTemperatures.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonBlockFluidTemperatures.size() + " block/fluid temperature values from JSON");
            for (Map.Entry<String, List<JsonBlockFluidTemperature>> entry : jsonBlockFluidTemperatures.entrySet())
            {
                for (JsonBlockFluidTemperature propTemp : entry.getValue())
                {
                    JsonConfig.registerBlockFluidTemperature(entry.getKey(), propTemp.temperature, propTemp.getPropertyArray());
                }
            }

            MapDifference<String, List<JsonBlockFluidTemperature>> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultBlockFluidTemperatures, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.BLOCK_TEMP))), Map.class), gson.fromJson(gson.toJson(JsonConfig.blockFluidTemperatures, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.BLOCK_TEMP))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "temperature", "blocks");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    JsonArray ja = new JsonArray();
                    for (JsonBlockFluidTemperature jbft : JsonConfig.blockFluidTemperatures.get(e))
                    {
                        TemperatureBlockData tbd = new TemperatureBlockData();
                        tbd.temperature(jbft.temperature);
                        for (Map.Entry<String, String> prop : jbft.properties.entrySet())
                        {
                            tbd.addProperty(prop.getKey(), prop.getValue());
                        }
                        ja.add(tbd.build());
                    }

                    writeInFile(jsonFile, ja);
                }
            }
        }

        Map<String, JsonFuelItem> jsonFuelItemIdentities = processJson(JsonFileName.FUEL, jsonDir);

        if (jsonFuelItemIdentities != null)
        {
            // remove default fuel config
            //JsonConfig.fuelItems.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonFuelItemIdentities.size() + " fuel item values from JSON");
            for (Map.Entry<String, JsonFuelItem> entry : jsonFuelItemIdentities.entrySet())
            {
                JsonConfig.registerFuelItems(entry.getKey(), entry.getValue().thermalType, entry.getValue().fuelValue);
            }

            MapDifference<String, JsonFuelItem> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultFuelItems, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.FUEL))), Map.class), gson.fromJson(gson.toJson(JsonConfig.fuelItems, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.FUEL))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "temperature", "fuel_items");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    TemperatureFuelItemData td = new TemperatureFuelItemData();
                    td.thermalType(JsonConfig.fuelItems.get(e).thermalType);
                    td.duration(JsonConfig.fuelItems.get(e).fuelValue * 20);

                    writeInFile(jsonFile, td.build());
                }
            }
        }

        Map<String, List<JsonConsumableTemperature>> jsonConsumableTemperatures = processJson(JsonFileName.CONSUMABLE_TEMP, jsonDir);

        if (jsonConsumableTemperatures != null)
        {
            // remove default consumables config
            //JsonConfig.consumableTemperature.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonConsumableTemperatures.size() + " consumable temperature values from JSON");
            for (Map.Entry<String, List<JsonConsumableTemperature>> entry : jsonConsumableTemperatures.entrySet())
            {
                for (JsonConsumableTemperature jct : entry.getValue())
                {
                    JsonConfig.registerConsumableTemperature(jct.group, entry.getKey(), jct.temperatureLevel, jct.duration);
                }
            }

            MapDifference<String, JsonConsumableTemperature> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultConsumableTemperature, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.CONSUMABLE_TEMP))), Map.class), gson.fromJson(gson.toJson(JsonConfig.consumableTemperature, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.CONSUMABLE_TEMP))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "temperature", "consumables");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    JsonArray ja = new JsonArray();
                    for (JsonConsumableTemperature jct : JsonConfig.consumableTemperature.get(e))
                    {
                        TemperatureConsumableData tcd = new TemperatureConsumableData();
                        tcd.group(jct.group);
                        tcd.duration(jct.duration);
                        tcd.temperatureLevel(jct.temperatureLevel);
                        ja.add(tcd.build());
                    }

                    writeInFile(jsonFile, ja);
                }
            }
        }

        // Thirst

        Map<String, List<JsonBlockFluidThirst>> jsonBlockFluidThirsts = processJson(JsonFileName.BLOCK_THIRST, jsonDir);

        if (jsonBlockFluidThirsts != null)
        {
            // remove default block config
            //JsonConfig.blockFluidThirst.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonBlockFluidThirsts.size() + " block/fluid thirst values from JSON");
            for (Map.Entry<String, List<JsonBlockFluidThirst>> entry : jsonBlockFluidThirsts.entrySet())
            {
                for (JsonBlockFluidThirst propThirst : entry.getValue())
                {
                    JsonConfig.registerBlockFluidThirst(entry.getKey(), propThirst.hydration, propThirst.saturation, propThirst.effects.toArray(new JsonEffectParameter[0]), propThirst.getPropertyArray());
                }
            }

            MapDifference<String, List<JsonBlockFluidThirst>> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultBlockFluidThirst, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.BLOCK_THIRST))), Map.class), gson.fromJson(gson.toJson(JsonConfig.blockFluidThirst, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.BLOCK_THIRST))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "thirst", "blocks");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    JsonArray ja = new JsonArray();
                    for (JsonBlockFluidThirst jbft : JsonConfig.blockFluidThirst.get(e))
                    {
                        ThirstData td = new ThirstData();
                        td.hydration(jbft.hydration);
                        td.saturation(jbft.saturation);
                        for (JsonEffectParameter jep : jbft.effects)
                        {
                            MobEffect me = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(jep.name));
                            if (me != null)
                                td.addEffect(me, jep.duration, jep.chance, jep.amplifier);
                        }
                        for (Map.Entry<String, String> prop : jbft.properties.entrySet())
                        {
                            td.addProperty(prop.getKey(), prop.getValue());
                        }
                        ja.add(td.build());
                    }

                    writeInFile(jsonFile, ja);
                }
            }
        }

        Map<String, List<JsonConsumableThirst>> jsonConsumableThirsts = processJson(JsonFileName.CONSUMABLE_THIRST, jsonDir);

        if (jsonConsumableThirsts != null)
        {
            // remove default consumables config
            //JsonConfig.consumableThirst.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonConsumableThirsts.size() + " consumable thirst values from JSON");
            for (Map.Entry<String, List<JsonConsumableThirst>> entry : jsonConsumableThirsts.entrySet())
            {
                for (JsonConsumableThirst jct : entry.getValue())
                    JsonConfig.registerConsumableThirst(entry.getKey(), jct.hydration, jct.saturation, jct.effects.toArray(new JsonEffectParameter[0]), jct.getNbtArray());
            }

            MapDifference<String, List<JsonConsumableThirst>> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultConsumableThirst, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.CONSUMABLE_THIRST))), Map.class), gson.fromJson(gson.toJson(JsonConfig.consumableThirst, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.CONSUMABLE_THIRST))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "thirst", "consumables");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    JsonArray ja = new JsonArray();
                    for (JsonConsumableThirst jct : JsonConfig.consumableThirst.get(e))
                    {
                        ThirstData td = new ThirstData();
                        td.hydration(jct.hydration);
                        td.saturation(jct.saturation);
                        for (JsonEffectParameter jep : jct.effects)
                        {
                            MobEffect me = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(jep.name));
                            if (me != null)
                                td.addEffect(me, jep.duration, jep.chance, jep.amplifier);
                        }
                        for (Map.Entry<String, String> prop : jct.nbt.entrySet())
                        {
                            td.addProperty(prop.getKey(), prop.getValue());
                        }
                        ja.add(td.build());
                    }

                    writeInFile(jsonFile, ja);
                }
            }
        }

        // Healing items
        Map<String, JsonConsumableHeal> jsonConsumableHeal = processJson(JsonFileName.CONSUMABLE_HEAL, jsonDir);

        if (jsonConsumableHeal != null)
        {
            // remove default consumables config
            //JsonConfig.consumableHeal.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonConsumableHeal.size() + " consumable heal values from JSON");
            for (Map.Entry<String, JsonConsumableHeal> entry : jsonConsumableHeal.entrySet())
            {
                JsonConfig.registerConsumableHeal(entry.getKey(), entry.getValue().healingCharges, entry.getValue().healingValue, entry.getValue().healingTime);
            }

            MapDifference<String, JsonConsumableHeal> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultConsumableHeal, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.CONSUMABLE_HEAL))), Map.class), gson.fromJson(gson.toJson(JsonConfig.consumableHeal, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.CONSUMABLE_HEAL))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "body_damage", "consumables");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    HealingConsumableData td = new HealingConsumableData();
                    td.duration(JsonConfig.consumableHeal.get(e).healingTime);
                    td.healingCharges(JsonConfig.consumableHeal.get(e).healingCharges);
                    td.healingValue(JsonConfig.consumableHeal.get(e).healingValue);

                    writeInFile(jsonFile, td.build());
                }
            }
        }

        // Damage Sources Body Parts
        Map<String, JsonBodyPartsDamageSource> jsonDamageSourceBodyParts = processJson(JsonFileName.DAMAGE_SOURCE_BODY_PARTS, jsonDir);

        if (jsonDamageSourceBodyParts != null)
        {
            // remove default consumables config
            //JsonConfig.damageSourceBodyParts.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonDamageSourceBodyParts.size() + " damage source body part values from JSON");
            for (Map.Entry<String, JsonBodyPartsDamageSource> entry : jsonDamageSourceBodyParts.entrySet())
            {
                JsonConfig.registerDamageSourceBodyParts(entry.getKey(), entry.getValue().damageDistribution, entry.getValue().bodyParts);
            }

            MapDifference<String, JsonBodyPartsDamageSource> diff = Maps.difference(gson.fromJson(gson.toJson(JsonConfig.defaultDamageSourceBodyParts, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.DAMAGE_SOURCE_BODY_PARTS))), Map.class), gson.fromJson(gson.toJson(JsonConfig.damageSourceBodyParts, Objects.requireNonNull(JsonTypeToken.get(JsonFileName.DAMAGE_SOURCE_BODY_PARTS))), Map.class));

            List<String> entriesToKeep = new ArrayList<>();
            if (!diff.entriesOnlyOnRight().isEmpty() || !diff.entriesDiffering().isEmpty())
            {
                entriesToKeep.addAll(diff.entriesOnlyOnRight().keySet());
                entriesToKeep.addAll(diff.entriesDiffering().keySet());

                for (String e : entriesToKeep)
                {
                    ResourceLocation rl = ResourceLocation.parse(e);
                    Path endPath = Paths.get(customDatapackFolder.toString(), "data", rl.getNamespace(), LegendarySurvivalOverhaul.MOD_ID, "body_damage", "damage_sources");
                    if (!endPath.toFile().exists())
                    {
                        createDirectories(endPath);
                    }

                    File jsonFile = new File(endPath.toFile(), rl.getPath() + ".json");
                    BodyPartsDamageSourceData td = new BodyPartsDamageSourceData();
                    td.addBodyParts(JsonConfig.damageSourceBodyParts.get(e).bodyParts);
                    td.damageDistribution(JsonConfig.damageSourceBodyParts.get(e).damageDistribution);

                    writeInFile(jsonFile, td.build());
                }
            }
        }

        if (customDatapackFolder.toFile().exists())
        {
            JsonObject packMcMetaContent = new JsonObject();
            JsonObject packMcMetaContent2 = new JsonObject();
            packMcMetaContent2.addProperty("pack_format", 10);
            packMcMetaContent2.addProperty("description", "Legendary Survival Overhaul datapack (modifies temperature/thirst/body damage values)");
            packMcMetaContent.add("pack", packMcMetaContent2);
            File packMcMeta = new File(customDatapackFolder.toFile(), "pack.mcmeta");
            writeInFile(packMcMeta, packMcMetaContent);
        }
    }

    @Nullable
    public static <T> T processJson(JsonFileName jfn, File jsonDir)
    {
        try
        {
            return processUncaughtJson(jfn, jsonDir);
        } catch (Exception e)
        {
            LegendarySurvivalOverhaul.LOGGER.error("Error managing JSON file: " + jfn.get(), e);

            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T processUncaughtJson(JsonFileName jfn, File jsonDir) throws Exception
    {
        String jsonFileName = jfn.get();
        Type type = JsonTypeToken.get(jfn);

        File jsonFile = new File(jsonDir, jsonFileName);

        if (jsonFile.exists())
        {
            Gson gson = buildNewGson();

            return gson.fromJson(new FileReader(jsonFile), type);
        }
        return null;
    }

    public static void deleteJson(JsonFileName jfn, File jsonDir)
    {

        String jsonFileName = jfn.get();
        File jsonFile = new File(jsonDir, jsonFileName);

        if (jsonFile.exists())
        {
            jsonFile.delete();
        }
    }

    public static <T> void manuallyWriteToJson(JsonFileName jfn, final T container, File jsonDir)
    {
        try
        {
            manuallyWriteToJson(jfn, container, jsonDir, false);
        } catch (Exception e)
        {
            LegendarySurvivalOverhaul.LOGGER.error("Error writing " + jfn + " JSON file", e);
        }
    }

    private static <T> void manuallyWriteToJson(JsonFileName jfn, final T container, File jsonDir, boolean forceWrite) throws Exception
    {
        String jsonFileName = jfn.get();
        Type type = JsonTypeToken.get(jfn);

        Gson gson = buildNewGson();
        File jsonFile = new File(jsonDir, jsonFileName);
        if (jsonFile.exists())
        {
            LegendarySurvivalOverhaul.LOGGER.debug(jsonFile.getName() + " already exists!");

            if (forceWrite)
                LegendarySurvivalOverhaul.LOGGER.debug("Overriding...");
            else
                return;
        }
        FileUtils.write(jsonFile, gson.toJson(container, type), (String) null);
    }

    private static Gson buildNewGson()
    {
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
    }
}
