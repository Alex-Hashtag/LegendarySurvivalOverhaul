package sfiomn.legendarysurvivaloverhaul.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.json_old.JsonConfigRegistration;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Config
{
    // Every config file is stored in config/legendarysurvivaloverhaul/
    private static final String CONFIG_FOLDER = LegendarySurvivalOverhaul.MOD_ID + "/";

    public static final ModConfigSpec COMMON_SPEC;
    public static final CommonConfig COMMON;

    public static final ModConfigSpec TEMPERATURE_SPEC;
    public static final TemperatureConfig TEMPERATURE;

    public static final ModConfigSpec SEASONS_SPEC;
    public static final SeasonsConfig SEASONS;

    public static final ModConfigSpec THIRST_SPEC;
    public static final ThirstConfig THIRST;

    public static final ModConfigSpec HEALTH_SPEC;
    public static final HealthConfig HEALTH;

    public static final ModConfigSpec BODY_DAMAGE_SPEC;
    public static final BodyDamageConfig BODY_DAMAGE;

    public static final ModConfigSpec CLIENT_SPEC;
    public static final ClientConfig CLIENT;

    static
    {
        final Pair<CommonConfig, ModConfigSpec> common = new ModConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = common.getRight();
        COMMON = common.getLeft();

        final Pair<TemperatureConfig, ModConfigSpec> temperature = new ModConfigSpec.Builder().configure(TemperatureConfig::new);
        TEMPERATURE_SPEC = temperature.getRight();
        TEMPERATURE = temperature.getLeft();

        final Pair<SeasonsConfig, ModConfigSpec> seasons = new ModConfigSpec.Builder().configure(SeasonsConfig::new);
        SEASONS_SPEC = seasons.getRight();
        SEASONS = seasons.getLeft();

        final Pair<ThirstConfig, ModConfigSpec> thirst = new ModConfigSpec.Builder().configure(ThirstConfig::new);
        THIRST_SPEC = thirst.getRight();
        THIRST = thirst.getLeft();

        final Pair<HealthConfig, ModConfigSpec> health = new ModConfigSpec.Builder().configure(HealthConfig::new);
        HEALTH_SPEC = health.getRight();
        HEALTH = health.getLeft();

        final Pair<BodyDamageConfig, ModConfigSpec> bodyDamage = new ModConfigSpec.Builder().configure(BodyDamageConfig::new);
        BODY_DAMAGE_SPEC = bodyDamage.getRight();
        BODY_DAMAGE = bodyDamage.getLeft();

        final Pair<ClientConfig, ModConfigSpec> client = new ModConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = client.getRight();
        CLIENT = client.getLeft();
    }

    public static void register()
    {
        for (Path configPath : new Path[]{LegendarySurvivalOverhaul.modConfigPath})
        {
            try
            {
                Files.createDirectory(configPath);
            } catch (FileAlreadyExistsException ignored)
            {
            } catch (IOException e)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to create Legendary Survival Overhaul config directory " + configPath);
                LegendarySurvivalOverhaul.LOGGER.error(e.getStackTrace());
            }
        }

        ModContainer container = ModLoadingContext.get().getActiveContainer();
        container.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, CONFIG_FOLDER + "client.toml");
        container.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, CONFIG_FOLDER + "common.toml");
        container.registerConfig(ModConfig.Type.COMMON, TEMPERATURE_SPEC, CONFIG_FOLDER + "temperature.toml");
        container.registerConfig(ModConfig.Type.COMMON, SEASONS_SPEC, CONFIG_FOLDER + "seasons.toml");
        container.registerConfig(ModConfig.Type.COMMON, THIRST_SPEC, CONFIG_FOLDER + "thirst.toml");
        container.registerConfig(ModConfig.Type.COMMON, HEALTH_SPEC, CONFIG_FOLDER + "health.toml");
        container.registerConfig(ModConfig.Type.COMMON, BODY_DAMAGE_SPEC, CONFIG_FOLDER + "body_damage.toml");

        JsonConfigRegistration.init(LegendarySurvivalOverhaul.modConfigJsons.toFile());
    }

    /**
     * Bakes the values of the given spec into {@link Baked}. Does nothing if the spec doesn't belong to this mod.
     */
    public static void bake(IConfigSpec spec)
    {
        if (spec == CLIENT_SPEC)
            Baked.bakeClient();
        else if (spec == COMMON_SPEC)
            Baked.bakeCommon();
        else if (spec == TEMPERATURE_SPEC)
            Baked.bakeTemperature();
        else if (spec == SEASONS_SPEC)
            Baked.bakeSeasons();
        else if (spec == THIRST_SPEC)
            Baked.bakeThirst();
        else if (spec == HEALTH_SPEC)
            Baked.bakeHealth();
        else if (spec == BODY_DAMAGE_SPEC)
            Baked.bakeBodyDamage();
    }

    static boolean validateDouble(final Object obj)
    {
        return obj instanceof Double;
    }

    static boolean validatePositiveInt(final Object obj)
    {
        return obj instanceof final Integer intValue && intValue >= 0;
    }

    static boolean validatePercentDouble(final Object obj)
    {
        return obj instanceof final Double doubleValue && doubleValue >= 0 && doubleValue <= 1;
    }

    static boolean validateEffectName(final Object obj)
    {
        if (!(obj instanceof final String effectName))
            return false;

        try
        {
            // Only validate that it's a valid ResourceLocation format
            // Don't check if it exists in registry as modded effects may not be loaded yet
            ResourceLocation.parse(effectName);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    static boolean validateEntityType(final Object obj)
    {
        if (!(obj instanceof final String entityName))
            return false;

        try
        {
            // Only validate that it's a valid ResourceLocation format
            // Don't check if it exists in registry as modded entities may not be loaded yet
            ResourceLocation.parse(entityName);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static class Baked
    {
        // Core
        public static int routinePacketSync;
        public static boolean hideInfoFromDebug;
        public static boolean naturalRegenerationEnabled;
        public static boolean vanillaFreezeEnabled;
        public static EnumUtil.CompassInfo compassInfoMode;
        public static boolean showCoordinateOnMap;
        public static double initialHealth;
        public static EnumUtil.DifficultyMode difficultyMode;

        // Food
        public static double baseFoodExhaustion;
        public static double sprintingFoodExhaustion;
        public static double onAttackFoodExhaustion;

        // Temperature
        public static boolean temperatureEnabled;
        public static int tempTickTime;
        public static double minTemperatureModification;
        public static double maxTemperatureModification;

        public static boolean dangerousHeatTemperature;
        public static boolean dangerousColdTemperature;
        public static double goldFernChance;
        public static double goldFernWorldgenChance;

        public static boolean temperatureImmunityOnDeathEnabled;
        public static int temperatureImmunityOnDeathTime;
        public static boolean temperatureImmunityOnFirstSpawnEnabled;
        public static int temperatureImmunityOnFirstSpawnTime;

        public static boolean heatTemperatureSecondaryEffects;
        public static boolean coldTemperatureSecondaryEffects;
        public static double heatThirstEffectModifier;
        public static double coldHungerEffectModifier;

        public static boolean biomeEffectsEnabled;
        public static double biomeDrynessMultiplier;
        public static double biomeTemperatureMultiplier;

        public static double undergroundBiomeTemperatureMultiplier;
        public static int undergroundEffectStartDistanceToWS;
        public static int undergroundEffectEndDistanceToWS;

        public static double rainTemperatureModifier;
        public static double snowTemperatureModifier;

        public static double maxFreezeTemperatureModifier;
        public static int maxFreezeEffectTick;

        public static double altitudeModifier;

        public static double timeModifier;
        public static double biomeTimeMultiplier;
        public static double shadeTimeModifier;
        public static double shadeTimeModifierThreshold;
        public static int tempInfluenceMaximumDist;
        public static double tempInfluenceUpDistMultiplier;
        public static double tempInfluenceOutsideDistMultiplier;
        public static double tempInfluenceInWaterDistMultiplier;
        public static double sprintModifier;
        public static double onFireModifier;

        public static double playerHuddlingModifier;
        public static int playerHuddlingRadius;

        public static boolean wetnessEnabled;
        public static List<? extends String> wetnessImmunityMounts;
        public static double wetMultiplier;
        public static int wetnessTickTimer;
        public static int wetnessDecrease;
        public static int wetnessRainIncrease;
        public static int wetnessFluidIncrease;

        public static double heatingCoat1Modifier;
        public static double heatingCoat2Modifier;
        public static double heatingCoat3Modifier;

        public static double coolingCoat1Modifier;
        public static double coolingCoat2Modifier;
        public static double coolingCoat3Modifier;

        public static double thermalCoat1Modifier;
        public static double thermalCoat2Modifier;
        public static double thermalCoat3Modifier;

        public static double tfcItemHeatMultiplier;
        public static double tfcTemperatureMultiplier;

        public static boolean sereneSeasonsEnabled;
        public static boolean ssTropicalSeasonsEnabled;
        public static boolean ssSeasonCardsEnabled;
        public static boolean ssDefaultSeasonEnabled;

        public static double ssEarlySpringModifier;
        public static double ssMidSpringModifier;
        public static double ssLateSpringModifier;

        public static double ssEarlySummerModifier;
        public static double ssMidSummerModifier;
        public static double ssLateSummerModifier;

        public static double ssEarlyAutumnModifier;
        public static double ssMidAutumnModifier;
        public static double ssLateAutumnModifier;

        public static double ssEarlyWinterModifier;
        public static double ssMidWinterModifier;
        public static double ssLateWinterModifier;

        public static double ssEarlyWetSeasonModifier;
        public static double ssMidWetSeasonModifier;
        public static double ssLateWetSeasonModifier;

        public static double ssEarlyDrySeasonModifier;
        public static double ssMidDrySeasonModifier;
        public static double ssLateDrySeasonModifier;

        public static boolean eclipticSeasonsEnabled;
        public static List<? extends Double> esSpringModifier;
        public static List<? extends Double> esSummerModifier;
        public static List<? extends Double> esAutumnModifier;
        public static List<? extends Double> esWinterModifier;

        // Thirst
        public static boolean thirstEnabled;
        public static boolean dangerousDehydration;
        public static boolean cumulativeThirstEffectDuration;
        public static double dehydrationDamageScaling;
        public static double thirstEffectModifier;
        public static double baseHydrationExhaustion;
        public static double sprintingHydrationExhaustion;
        public static double onJumpHydrationExhaustion;
        public static double onBlockBreakHydrationExhaustion;
        public static double onAttackHydrationExhaustion;
        public static boolean selfWateringCanteenEnabled;
        public static int selfWateringCanteenWetnessIncrease;
        public static int canteenCapacity;
        public static int largeCanteenCapacity;
        public static boolean allowOverridePurifiedWater;
        public static int hydrationLava;
        public static double saturationLava;
        public static boolean glassBottleLootAfterDrink;
        public static int hydrationLavaBlazeborn;
        public static double saturationLavaBlazeborn;
        public static double extraThirstExhaustionShulk;
        public static double extraThirstExhaustionPhantom;
        public static boolean thirstEnabledIfVampire;

        // Health Overhaul
        public static boolean healthOverhaulEnabled;
        public static double maxAdditionalHealth;
        public static double maxShieldHealth;
        public static boolean absorptionEffectOverride;
        public static int heartsLostOnDeath;
        public static int permanentHearts;
        public static int resilientHeartsWithBrokenHearts;
        public static double brokenHeartsPerInjuredLimb;
        public static EnumUtil.brokenHeartsPerInjuredLimbMode brokenHeartsPerInjuredLimbMode;

        // Body members damage
        public static boolean localizedBodyDamageEnabled;
        public static double headCriticalShotMultiplier;
        public static double bodyDamageMultiplier;
        public static double bodyHealthRatioRecoveredFromSleep;
        public static double healthRatioRecoveredFromSleep;
        public static double bodyHealingFoodExhaustion;
        public static int minFoodOnBodyHealing;
        public static int painkillerAddictionDuration;

        public static boolean passiveLimbRegenerationOnFullHealth;
        public static List<? extends String> passiveLimbRegenerationEffects;
        public static boolean passiveLimbRegenerationAmplificationEnabled;
        public static double passiveLimbHealthRegenerated;
        public static int passiveLimbRegenerationTickTimer;

        public static double proportionalLimbRegenMinThreshold;
        public static double proportionalLimbRegenMinHealValue;
        public static double proportionalLimbRegenMaxHealValue;
        public static int proportionalLimbRegenTickRate;
        public static double proportionalLimbRegenIncreaseRate;

        public static boolean customHealthRegenEnabled;
        public static double customHealthRegenRate;
        public static int customHealthRegenTickRate;
        public static double customHealthRegenFoodExhaustion;

        public static double firstAidSuppliesLimbHealthRegenerated;
        public static EnumUtil.limbRegenerationMode limbRegenerationMode;
        public static boolean firstAidSuppliesHealingOverflow;
        public static int firstAidSuppliesTickTimer;
        public static boolean firstAidSuppliesExhaustsFood;
        public static List<? extends String> firstAidSuppliesBoostedOnEffects;
        public static double firstAidSuppliesBoostedTickTimerMultiplier;

        public static EnumUtil.bodyPartHealthMode bodyPartHealthMode;
        public static double headPartHealth;
        public static double armsPartHealth;
        public static double chestPartHealth;
        public static double legsPartHealth;
        public static double feetPartHealth;

        public static double recoveryEffectHealingValue;
        public static int healingHerbsUseTime;
        public static int plasterUseTime;
        public static int bandageUseTime;
        public static int tonicUseTime;
        public static int medkitUseTime;
        public static int morphineUseTime;
        public static int morphinePainkillerTickDuration;

        public static List<? extends String> headPartEffects;
        public static List<? extends Integer> headPartEffectAmplifiers;
        public static List<? extends Double> headPartEffectThresholds;

        public static List<? extends String> armsPartEffects;
        public static List<? extends Integer> armsPartEffectAmplifiers;
        public static List<? extends Double> armsPartEffectThresholds;
        public static List<? extends String> bothArmsPartEffects;
        public static List<? extends Integer> bothArmsPartEffectAmplifiers;
        public static List<? extends Double> bothArmsPartEffectThresholds;

        public static List<? extends String> chestPartEffects;
        public static List<? extends Integer> chestPartEffectAmplifiers;
        public static List<? extends Double> chestPartEffectThresholds;

        public static List<? extends String> legsPartEffects;
        public static List<? extends Integer> legsPartEffectAmplifiers;
        public static List<? extends Double> legsPartEffectThresholds;
        public static List<? extends String> bothLegsPartEffects;
        public static List<? extends Integer> bothLegsPartEffectAmplifiers;
        public static List<? extends Double> bothLegsPartEffectThresholds;

        public static List<? extends String> feetPartEffects;
        public static List<? extends Integer> feetPartEffectAmplifiers;
        public static List<? extends Double> feetPartEffectThresholds;
        public static List<? extends String> bothFeetPartEffects;
        public static List<? extends Integer> bothFeetPartEffectAmplifiers;
        public static List<? extends Double> bothFeetPartEffectThresholds;

        public static boolean morphineSyringeApplyPainkillerAddiction;

        // Client Config
        public static EnumUtil.temperatureDisplayMode temperatureDisplayMode;
        public static int temperatureDisplayOffsetX;
        public static int temperatureDisplayOffsetY;
        public static int bodyTemperatureDisplayOffsetX;
        public static int bodyTemperatureDisplayOffsetY;
        public static boolean heatTemperatureOverlay;
        public static boolean coldTemperatureOverlay;
        public static boolean breathingSoundEnabled;
        public static double coldBreathEffectThreshold;
        public static boolean renderTemperatureInFahrenheit;

        public static boolean foodSaturationDisplayed;
        public static boolean showVanillaBarAnimationOverlay;

        public static int seasonCardsDisplayOffsetX;
        public static int seasonCardsDisplayOffsetY;
        public static int seasonCardsSpawnDimensionDelayInTicks;
        public static int seasonCardsDisplayTimeInTicks;
        public static int seasonCardsFadeInInTicks;
        public static int seasonCardsFadeOutInTicks;

        public static int wetnessIndicatorOffsetX;
        public static int wetnessIndicatorOffsetY;

        public static int bodyDamageIndicatorOffsetX;
        public static int bodyDamageIndicatorOffsetY;
        public static double bodyDamageIndicatorRenderHealthLimit;

        public static boolean showHydrationTooltip;
        public static boolean mergeHydrationAndSaturationTooltip;
        public static boolean hydrationSaturationDisplayed;
        public static boolean hydrationExhaustionDisplayed;
        public static boolean lowHydrationEffect;
        public static boolean showHydrationBar;
        public static boolean showDrinkPreview;
        public static int hydrationBarOffsetX;
        public static int hydrationBarOffsetY;

        public static boolean appendBrokenShieldHeartsToHealthBar;

        public static void bakeCommon()
        {
            LegendarySurvivalOverhaul.LOGGER.debug("Load Common configuration from file");
            try
            {
                hideInfoFromDebug = COMMON.hideInfoFromDebug.get();
                routinePacketSync = COMMON.routinePacketSync.get();
                compassInfoMode = COMMON.compassInfoMode.get();
                showCoordinateOnMap = COMMON.showCoordinateOnMap.get();
                difficultyMode = COMMON.difficultyMode.get();
                baseFoodExhaustion = COMMON.baseFoodExhaustion.get();
                sprintingFoodExhaustion = COMMON.sprintingFoodExhaustion.get();
                onAttackFoodExhaustion = COMMON.onAttackFoodExhaustion.get();
            } catch (Exception e)
            {
                LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the Common config for Legendary Survival Overhaul");
                LegendarySurvivalOverhaul.LOGGER.warn(e.getStackTrace());
            }
        }

        public static void bakeTemperature()
        {
            LegendarySurvivalOverhaul.LOGGER.debug("Load Temperature configuration from file");
            try
            {
                vanillaFreezeEnabled = TEMPERATURE.vanillaFreezeEnabled.get();
                temperatureEnabled = TEMPERATURE.temperatureEnabled.get();
                tempTickTime = TEMPERATURE.tempTickTime.get();
                minTemperatureModification = TEMPERATURE.minTemperatureModification.get();
                maxTemperatureModification = TEMPERATURE.maxTemperatureModification.get();
                dangerousHeatTemperature = TEMPERATURE.dangerousHeatTemperature.get();
                dangerousColdTemperature = TEMPERATURE.dangerousColdTemperature.get();
                goldFernChance = TEMPERATURE.goldFernChance.get();
                goldFernWorldgenChance = TEMPERATURE.goldFernWorldgenChance.get();
                temperatureImmunityOnDeathEnabled = TEMPERATURE.temperatureImmunityOnDeathEnabled.get();
                temperatureImmunityOnDeathTime = TEMPERATURE.temperatureImmunityOnDeathTime.get();
                temperatureImmunityOnFirstSpawnEnabled = TEMPERATURE.temperatureImmunityOnFirstSpawnEnabled.get();
                temperatureImmunityOnFirstSpawnTime = TEMPERATURE.temperatureImmunityOnFirstSpawnTime.get();
                heatTemperatureSecondaryEffects = TEMPERATURE.heatTemperatureSecondaryEffects.get();
                coldTemperatureSecondaryEffects = TEMPERATURE.coldTemperatureSecondaryEffects.get();
                heatThirstEffectModifier = TEMPERATURE.heatThirstEffectModifier.get();
                coldHungerEffectModifier = TEMPERATURE.coldHungerEffectModifier.get();
                rainTemperatureModifier = TEMPERATURE.rainTemperatureModifier.get();
                snowTemperatureModifier = TEMPERATURE.snowTemperatureModifier.get();
                maxFreezeTemperatureModifier = TEMPERATURE.maxFreezeTemperatureModifier.get();
                maxFreezeEffectTick = TEMPERATURE.maxFreezeEffectTick.get();
                undergroundBiomeTemperatureMultiplier = TEMPERATURE.undergroundBiomeTemperatureMultiplier.get();
                undergroundEffectStartDistanceToWS = TEMPERATURE.undergroundEffectStartDistanceToWS.get();
                undergroundEffectEndDistanceToWS = TEMPERATURE.undergroundEffectEndDistanceToWS.get();
                altitudeModifier = TEMPERATURE.altitudeModifier.get();
                biomeEffectsEnabled = TEMPERATURE.biomeEffectsEnabled.get();
                biomeDrynessMultiplier = TEMPERATURE.biomeDrynessMultiplier.get();
                biomeTemperatureMultiplier = TEMPERATURE.biomeTemperatureMultiplier.get();
                timeModifier = TEMPERATURE.timeModifier.get();
                biomeTimeMultiplier = TEMPERATURE.biomeTimeMultiplier.get();
                shadeTimeModifier = TEMPERATURE.shadeTimeModifier.get();
                shadeTimeModifierThreshold = TEMPERATURE.shadeTimeModifierThreshold.get();
                tempInfluenceMaximumDist = TEMPERATURE.tempInfluenceMaximumDist.get();
                tempInfluenceUpDistMultiplier = TEMPERATURE.tempInfluenceUpDistMultiplier.get();
                tempInfluenceInWaterDistMultiplier = TEMPERATURE.tempInfluenceInWaterDistMultiplier.get();
                tempInfluenceOutsideDistMultiplier = TEMPERATURE.tempInfluenceOutsideDistMultiplier.get();
                onFireModifier = TEMPERATURE.onFireModifier.get();
                sprintModifier = TEMPERATURE.sprintModifier.get();
                wetnessEnabled = TEMPERATURE.wetnessEnabled.get();
                wetnessImmunityMounts = TEMPERATURE.wetnessImmunityMounts.get();
                wetMultiplier = TEMPERATURE.wetMultiplier.get();
                wetnessTickTimer = TEMPERATURE.wetnessTickTimer.get();
                wetnessDecrease = TEMPERATURE.wetnessDecrease.get();
                wetnessRainIncrease = TEMPERATURE.wetnessRainIncrease.get();
                wetnessFluidIncrease = TEMPERATURE.wetnessFluidIncrease.get();
                playerHuddlingModifier = TEMPERATURE.playerHuddlingModifier.get();
                playerHuddlingRadius = TEMPERATURE.playerHuddlingRadius.get();
                heatingCoat1Modifier = TEMPERATURE.heatingCoat1Modifier.get();
                heatingCoat2Modifier = TEMPERATURE.heatingCoat2Modifier.get();
                heatingCoat3Modifier = TEMPERATURE.heatingCoat3Modifier.get();
                coolingCoat1Modifier = TEMPERATURE.coolingCoat1Modifier.get();
                coolingCoat2Modifier = TEMPERATURE.coolingCoat2Modifier.get();
                coolingCoat3Modifier = TEMPERATURE.coolingCoat3Modifier.get();
                thermalCoat1Modifier = TEMPERATURE.thermalCoat1Modifier.get();
                thermalCoat2Modifier = TEMPERATURE.thermalCoat2Modifier.get();
                thermalCoat3Modifier = TEMPERATURE.thermalCoat3Modifier.get();
                tfcItemHeatMultiplier = TEMPERATURE.tfcItemHeatMultiplier.get();
                tfcTemperatureMultiplier = TEMPERATURE.tfcTemperatureMultiplier.get();
            } catch (Exception e)
            {
                LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the Temperature config for Legendary Survival Overhaul");
                LegendarySurvivalOverhaul.LOGGER.warn(e.getStackTrace());
            }
        }

        public static void bakeSeasons()
        {
            LegendarySurvivalOverhaul.LOGGER.debug("Load Seasons configuration from file");
            try
            {
                sereneSeasonsEnabled = SEASONS.sereneSeasonsEnabled.get();
                ssTropicalSeasonsEnabled = SEASONS.ssTropicalSeasonsEnabled.get();
                ssSeasonCardsEnabled = SEASONS.ssSeasonCardsEnabled.get();
                ssDefaultSeasonEnabled = SEASONS.ssDefaultSeasonEnabled.get();
                ssEarlySpringModifier = SEASONS.ssEarlySpringModifier.get();
                ssMidSpringModifier = SEASONS.ssMidSpringModifier.get();
                ssLateSpringModifier = SEASONS.ssLateSpringModifier.get();
                ssEarlySummerModifier = SEASONS.ssEarlySummerModifier.get();
                ssMidSummerModifier = SEASONS.ssMidSummerModifier.get();
                ssLateSummerModifier = SEASONS.ssLateSummerModifier.get();
                ssEarlyAutumnModifier = SEASONS.ssEarlyAutumnModifier.get();
                ssMidAutumnModifier = SEASONS.ssMidAutumnModifier.get();
                ssLateAutumnModifier = SEASONS.ssLateAutumnModifier.get();
                ssEarlyWinterModifier = SEASONS.ssEarlyWinterModifier.get();
                ssMidWinterModifier = SEASONS.ssMidWinterModifier.get();
                ssLateWinterModifier = SEASONS.ssLateWinterModifier.get();
                ssEarlyWetSeasonModifier = SEASONS.ssEarlyWetSeasonModifier.get();
                ssMidWetSeasonModifier = SEASONS.ssMidWetSeasonModifier.get();
                ssLateWetSeasonModifier = SEASONS.ssLateWetSeasonModifier.get();
                ssEarlyDrySeasonModifier = SEASONS.ssEarlyDrySeasonModifier.get();
                ssMidDrySeasonModifier = SEASONS.ssMidDrySeasonModifier.get();
                ssLateDrySeasonModifier = SEASONS.ssLateDrySeasonModifier.get();
                eclipticSeasonsEnabled = SEASONS.eclipticSeasonsEnabled.get();
                esSpringModifier = SEASONS.esSpringModifier.get();
                esSummerModifier = SEASONS.esSummerModifier.get();
                esAutumnModifier = SEASONS.esAutumnModifier.get();
                esWinterModifier = SEASONS.esWinterModifier.get();
            } catch (Exception e)
            {
                LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the Seasons config for Legendary Survival Overhaul");
                LegendarySurvivalOverhaul.LOGGER.warn(e.getStackTrace());
            }
        }

        public static void bakeThirst()
        {
            LegendarySurvivalOverhaul.LOGGER.debug("Load Thirst configuration from file");
            try
            {
                thirstEnabled = THIRST.thirstEnabled.get();
                dangerousDehydration = THIRST.dangerousDehydration.get();
                cumulativeThirstEffectDuration = THIRST.cumulativeThirstEffectDuration.get();
                dehydrationDamageScaling = THIRST.dehydrationDamageScaling.get();
                thirstEffectModifier = THIRST.thirstEffectModifier.get();
                baseHydrationExhaustion = THIRST.baseHydrationExhaustion.get();
                sprintingHydrationExhaustion = THIRST.sprintingHydrationExhaustion.get();
                onJumpHydrationExhaustion = THIRST.onJumpHydrationExhaustion.get();
                onBlockBreakHydrationExhaustion = THIRST.onBlockBreakHydrationExhaustion.get();
                onAttackHydrationExhaustion = THIRST.onAttackHydrationExhaustion.get();
                selfWateringCanteenEnabled = THIRST.selfWateringCanteenEnabled.get();
                selfWateringCanteenWetnessIncrease = THIRST.selfWateringCanteenWetnessIncrease.get();
                canteenCapacity = THIRST.canteenCapacity.get();
                largeCanteenCapacity = THIRST.largeCanteenCapacity.get();
                allowOverridePurifiedWater = THIRST.allowOverridePurifiedWater.get();
                hydrationLava = THIRST.hydrationLava.get();
                saturationLava = THIRST.saturationLava.get();
                glassBottleLootAfterDrink = THIRST.glassBottleLootAfterDrink.get();
                hydrationLavaBlazeborn = THIRST.hydrationLavaBlazeborn.get();
                saturationLavaBlazeborn = THIRST.saturationLavaBlazeborn.get();
                extraThirstExhaustionShulk = THIRST.extraThirstExhaustionShulk.get();
                extraThirstExhaustionPhantom = THIRST.extraThirstExhaustionPhantom.get();
                thirstEnabledIfVampire = THIRST.thirstEnabledIfVampire.get();
            } catch (Exception e)
            {
                LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the Thirst config for Legendary Survival Overhaul");
                LegendarySurvivalOverhaul.LOGGER.warn(e.getStackTrace());
            }
        }

        public static void bakeHealth()
        {
            LegendarySurvivalOverhaul.LOGGER.debug("Load Health configuration from file");
            try
            {
                naturalRegenerationEnabled = HEALTH.naturalRegenerationEnabled.get();
                initialHealth = HEALTH.initialHealth.get();
                healthOverhaulEnabled = HEALTH.healthOverhaulEnabled.get();
                maxAdditionalHealth = HEALTH.maxAdditionalHealth.get();
                maxShieldHealth = HEALTH.maxShieldHealth.get();
                absorptionEffectOverride = HEALTH.absorptionEffectOverride.get();
                heartsLostOnDeath = HEALTH.heartsLostOnDeath.get();
                permanentHearts = HEALTH.permanentHearts.get();
                resilientHeartsWithBrokenHearts = HEALTH.resilientHeartsWithBrokenHearts.get();
                brokenHeartsPerInjuredLimb = HEALTH.brokenHeartsPerInjuredLimb.get();
                brokenHeartsPerInjuredLimbMode = HEALTH.brokenHeartsPerInjuredLimbMode.get();
                healthRatioRecoveredFromSleep = HEALTH.healthRatioRecoveredFromSleep.get();
                customHealthRegenEnabled = HEALTH.customHealthRegenEnabled.get();
                customHealthRegenRate = HEALTH.customHealthRegenRate.get();
                customHealthRegenTickRate = HEALTH.customHealthRegenTickRate.get();
                customHealthRegenFoodExhaustion = HEALTH.customHealthRegenFoodExhaustion.get();
            } catch (Exception e)
            {
                LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the Health config for Legendary Survival Overhaul");
                LegendarySurvivalOverhaul.LOGGER.warn(e.getStackTrace());
            }
        }

        public static void bakeBodyDamage()
        {
            LegendarySurvivalOverhaul.LOGGER.debug("Load Body Damage configuration from file");
            try
            {
                localizedBodyDamageEnabled = BODY_DAMAGE.localizedBodyDamageEnabled.get();
                headCriticalShotMultiplier = BODY_DAMAGE.headCriticalShotMultiplier.get();
                bodyDamageMultiplier = BODY_DAMAGE.bodyDamageMultiplier.get();
                bodyHealthRatioRecoveredFromSleep = BODY_DAMAGE.bodyHealthRatioRecoveredFromSleep.get();
                bodyHealingFoodExhaustion = BODY_DAMAGE.bodyHealingFoodExhaustion.get();
                minFoodOnBodyHealing = BODY_DAMAGE.minFoodOnBodyHealing.get();
                painkillerAddictionDuration = BODY_DAMAGE.painkillerAddictionDuration.get();
                passiveLimbRegenerationOnFullHealth = BODY_DAMAGE.passiveLimbRegenerationOnFullHealth.get();
                passiveLimbRegenerationEffects = BODY_DAMAGE.passiveLimbRegenerationEffects.get();
                passiveLimbRegenerationAmplificationEnabled = BODY_DAMAGE.passiveLimbRegenerationAmplificationEnabled.get();
                passiveLimbHealthRegenerated = BODY_DAMAGE.passiveLimbHealthRegenerated.get();
                passiveLimbRegenerationTickTimer = BODY_DAMAGE.passiveLimbRegenerationTickTimer.get();
                proportionalLimbRegenMinThreshold = BODY_DAMAGE.proportionalLimbRegenMinThreshold.get();
                proportionalLimbRegenMinHealValue = BODY_DAMAGE.proportionalLimbRegenMinHealValue.get();
                proportionalLimbRegenMaxHealValue = BODY_DAMAGE.proportionalLimbRegenMaxHealValue.get();
                proportionalLimbRegenTickRate = BODY_DAMAGE.proportionalLimbRegenTickRate.get();
                proportionalLimbRegenIncreaseRate = BODY_DAMAGE.proportionalLimbRegenIncreaseRate.get();
                firstAidSuppliesLimbHealthRegenerated = BODY_DAMAGE.firstAidSuppliesLimbHealthRegenerated.get();
                limbRegenerationMode = BODY_DAMAGE.firstAidSuppliesLimbRegenerationMode.get();
                firstAidSuppliesHealingOverflow = BODY_DAMAGE.firstAidSuppliesHealingOverflow.get();
                firstAidSuppliesTickTimer = BODY_DAMAGE.firstAidSuppliesTickTimer.get();
                firstAidSuppliesExhaustsFood = BODY_DAMAGE.firstAidSuppliesExhaustsFood.get();
                firstAidSuppliesBoostedOnEffects = BODY_DAMAGE.firstAidSuppliesBoostedOnEffects.get();
                firstAidSuppliesBoostedTickTimerMultiplier = BODY_DAMAGE.firstAidSuppliesBoostedTickTimerMultiplier.get();
                recoveryEffectHealingValue = BODY_DAMAGE.recoveryEffectHealingValue.get();
                healingHerbsUseTime = BODY_DAMAGE.healingHerbsUseTime.get();
                plasterUseTime = BODY_DAMAGE.plasterUseTime.get();
                bandageUseTime = BODY_DAMAGE.bandageUseTime.get();
                tonicUseTime = BODY_DAMAGE.tonicUseTime.get();
                medkitUseTime = BODY_DAMAGE.medkitUseTime.get();
                morphineUseTime = BODY_DAMAGE.morphineUseTime.get();
                morphinePainkillerTickDuration = BODY_DAMAGE.morphinePainkillerTickDuration.get();
                bodyPartHealthMode = BODY_DAMAGE.bodyPartHealthMode.get();
                headPartHealth = BODY_DAMAGE.headPartHealth.get();
                chestPartHealth = BODY_DAMAGE.chestPartHealth.get();
                armsPartHealth = BODY_DAMAGE.armsPartHealth.get();
                legsPartHealth = BODY_DAMAGE.legsPartHealth.get();
                feetPartHealth = BODY_DAMAGE.feetPartHealth.get();
                headPartEffects = BODY_DAMAGE.headPartEffects.get();
                headPartEffectAmplifiers = BODY_DAMAGE.headPartEffectAmplifiers.get();
                headPartEffectThresholds = BODY_DAMAGE.headPartEffectThresholds.get();
                armsPartEffects = BODY_DAMAGE.armsPartEffects.get();
                armsPartEffectAmplifiers = BODY_DAMAGE.armsPartEffectAmplifiers.get();
                armsPartEffectThresholds = BODY_DAMAGE.armsPartEffectThresholds.get();
                bothArmsPartEffects = BODY_DAMAGE.bothArmsPartEffects.get();
                bothArmsPartEffectAmplifiers = BODY_DAMAGE.bothArmsPartEffectAmplifiers.get();
                bothArmsPartEffectThresholds = BODY_DAMAGE.bothArmsPartEffectThresholds.get();
                chestPartEffects = BODY_DAMAGE.chestPartEffects.get();
                chestPartEffectAmplifiers = BODY_DAMAGE.chestPartEffectAmplifiers.get();
                chestPartEffectThresholds = BODY_DAMAGE.chestPartEffectThresholds.get();
                legsPartEffects = BODY_DAMAGE.legsPartEffects.get();
                legsPartEffectAmplifiers = BODY_DAMAGE.legsPartEffectAmplifiers.get();
                legsPartEffectThresholds = BODY_DAMAGE.legsPartEffectThresholds.get();
                bothLegsPartEffects = BODY_DAMAGE.bothLegsPartEffects.get();
                bothLegsPartEffectAmplifiers = BODY_DAMAGE.bothLegsPartEffectAmplifiers.get();
                bothLegsPartEffectThresholds = BODY_DAMAGE.bothLegsPartEffectThresholds.get();
                feetPartEffects = BODY_DAMAGE.feetPartEffects.get();
                feetPartEffectAmplifiers = BODY_DAMAGE.feetPartEffectAmplifiers.get();
                feetPartEffectThresholds = BODY_DAMAGE.feetPartEffectThresholds.get();
                bothFeetPartEffects = BODY_DAMAGE.bothFeetPartEffects.get();
                bothFeetPartEffectAmplifiers = BODY_DAMAGE.bothFeetPartEffectAmplifiers.get();
                bothFeetPartEffectThresholds = BODY_DAMAGE.bothFeetPartEffectThresholds.get();
                morphineSyringeApplyPainkillerAddiction = BODY_DAMAGE.morphineSyringeApplyPainkillerAddiction.get();
            } catch (Exception e)
            {
                LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the Body Damage config for Legendary Survival Overhaul");
                LegendarySurvivalOverhaul.LOGGER.warn(e.getStackTrace());
            }
        }

        public static void bakeClient()
        {
            LegendarySurvivalOverhaul.LOGGER.debug("Load Client configuration from file");
            try
            {
                temperatureDisplayMode = CLIENT.temperatureDisplayMode.get();
                temperatureDisplayOffsetX = CLIENT.temperatureDisplayOffsetX.get();
                temperatureDisplayOffsetY = CLIENT.temperatureDisplayOffsetY.get();
                bodyTemperatureDisplayOffsetX = CLIENT.bodyTemperatureDisplayOffsetX.get();
                bodyTemperatureDisplayOffsetY = CLIENT.bodyTemperatureDisplayOffsetY.get();
                heatTemperatureOverlay = CLIENT.heatTemperatureOverlay.get();
                coldTemperatureOverlay = CLIENT.coldTemperatureOverlay.get();
                breathingSoundEnabled = CLIENT.breathingSoundEnabled.get();
                coldBreathEffectThreshold = CLIENT.coldBreathEffectThreshold.get();
                renderTemperatureInFahrenheit = CLIENT.renderTemperatureInFahrenheit.get();

                foodSaturationDisplayed = CLIENT.foodSaturationDisplayed.get();
                showVanillaBarAnimationOverlay = CLIENT.showVanillaBarAnimationOverlay.get();

                seasonCardsDisplayOffsetX = CLIENT.seasonCardsDisplayOffsetX.get();
                seasonCardsDisplayOffsetY = CLIENT.seasonCardsDisplayOffsetY.get();
                seasonCardsSpawnDimensionDelayInTicks = CLIENT.seasonCardsSpawnDimensionDelayInTicks.get();
                seasonCardsDisplayTimeInTicks = CLIENT.seasonCardsDisplayTimeInTicks.get();
                seasonCardsFadeInInTicks = CLIENT.seasonCardsFadeInInTicks.get();
                seasonCardsFadeOutInTicks = CLIENT.seasonCardsFadeOutInTicks.get();

                wetnessIndicatorOffsetX = CLIENT.wetnessIndicatorOffsetX.get();
                wetnessIndicatorOffsetY = CLIENT.wetnessIndicatorOffsetY.get();

                bodyDamageIndicatorOffsetX = CLIENT.bodyDamageIndicatorOffsetX.get();
                bodyDamageIndicatorOffsetY = CLIENT.bodyDamageIndicatorOffsetY.get();
                bodyDamageIndicatorRenderHealthLimit = CLIENT.bodyDamageIndicatorRenderHealthLimit.get();

                hydrationSaturationDisplayed = CLIENT.hydrationSaturationDisplayed.get();
                hydrationExhaustionDisplayed = CLIENT.hydrationExhaustionDisplayed.get();
                showHydrationTooltip = CLIENT.showHydrationTooltip.get();
                mergeHydrationAndSaturationTooltip = CLIENT.mergeHydrationAndSaturationTooltip.get();
                lowHydrationEffect = CLIENT.lowHydrationEffect.get();
                showHydrationBar = CLIENT.showHydrationBar.get();
                showDrinkPreview = CLIENT.showDrinkPreview.get();
                hydrationBarOffsetX = CLIENT.hydrationBarOffsetX.get();
                hydrationBarOffsetY = CLIENT.hydrationBarOffsetY.get();

                appendBrokenShieldHeartsToHealthBar = CLIENT.appendBrokenShieldHeartsToHealthBar.get();
            } catch (Exception e)
            {
                LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the client config for Legendary Survival Overhaul.");
                LegendarySurvivalOverhaul.LOGGER.warn(e.getStackTrace());
            }
        }
    }
}