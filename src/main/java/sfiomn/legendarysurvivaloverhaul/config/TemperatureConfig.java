package sfiomn.legendarysurvivaloverhaul.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class TemperatureConfig
{
    public final ModConfigSpec.BooleanValue temperatureEnabled;
    public final ModConfigSpec.IntValue tempTickTime;
    public final ModConfigSpec.DoubleValue minTemperatureModification;
    public final ModConfigSpec.DoubleValue maxTemperatureModification;
    public final ModConfigSpec.BooleanValue dangerousHeatTemperature;
    public final ModConfigSpec.BooleanValue dangerousColdTemperature;
    public final ModConfigSpec.DoubleValue goldFernChance;
    public final ModConfigSpec.DoubleValue goldFernWorldgenChance;
    public final ModConfigSpec.BooleanValue temperatureImmunityOnDeathEnabled;
    public final ModConfigSpec.IntValue temperatureImmunityOnDeathTime;
    public final ModConfigSpec.BooleanValue temperatureImmunityOnFirstSpawnEnabled;
    public final ModConfigSpec.IntValue temperatureImmunityOnFirstSpawnTime;

    public final ModConfigSpec.BooleanValue heatTemperatureSecondaryEffects;
    public final ModConfigSpec.BooleanValue coldTemperatureSecondaryEffects;
    public final ModConfigSpec.DoubleValue heatThirstEffectModifier;
    public final ModConfigSpec.DoubleValue coldHungerEffectModifier;

    public final ModConfigSpec.BooleanValue biomeEffectsEnabled;
    public final ModConfigSpec.DoubleValue biomeDrynessMultiplier;
    public final ModConfigSpec.DoubleValue biomeTemperatureMultiplier;

    public final ModConfigSpec.DoubleValue timeModifier;
    public final ModConfigSpec.DoubleValue biomeTimeMultiplier;
    public final ModConfigSpec.DoubleValue shadeTimeModifier;
    public final ModConfigSpec.DoubleValue shadeTimeModifierThreshold;

    public final ModConfigSpec.DoubleValue altitudeModifier;
    public final ModConfigSpec.DoubleValue sprintModifier;
    public final ModConfigSpec.DoubleValue onFireModifier;

    public final ModConfigSpec.BooleanValue wetnessEnabled;
    public final ModConfigSpec.ConfigValue<List<? extends String>> wetnessImmunityMounts;
    public final ModConfigSpec.DoubleValue wetMultiplier;
    public final ModConfigSpec.IntValue wetnessTickTimer;
    public final ModConfigSpec.IntValue wetnessDecrease;
    public final ModConfigSpec.IntValue wetnessRainIncrease;
    public final ModConfigSpec.IntValue wetnessFluidIncrease;

    public final ModConfigSpec.IntValue tempInfluenceMaximumDist;
    public final ModConfigSpec.DoubleValue tempInfluenceUpDistMultiplier;
    public final ModConfigSpec.DoubleValue tempInfluenceInWaterDistMultiplier;
    public final ModConfigSpec.DoubleValue tempInfluenceOutsideDistMultiplier;

    public final ModConfigSpec.DoubleValue undergroundBiomeTemperatureMultiplier;
    public final ModConfigSpec.IntValue undergroundEffectStartDistanceToWS;
    public final ModConfigSpec.IntValue undergroundEffectEndDistanceToWS;

    public final ModConfigSpec.DoubleValue rainTemperatureModifier;
    public final ModConfigSpec.DoubleValue snowTemperatureModifier;

    public final ModConfigSpec.BooleanValue vanillaFreezeEnabled;
    public final ModConfigSpec.DoubleValue maxFreezeTemperatureModifier;
    public final ModConfigSpec.IntValue maxFreezeEffectTick;

    public final ModConfigSpec.DoubleValue playerHuddlingModifier;
    public final ModConfigSpec.IntValue playerHuddlingRadius;

    public final ModConfigSpec.DoubleValue heatingCoat1Modifier;
    public final ModConfigSpec.DoubleValue heatingCoat2Modifier;
    public final ModConfigSpec.DoubleValue heatingCoat3Modifier;

    public final ModConfigSpec.DoubleValue coolingCoat1Modifier;
    public final ModConfigSpec.DoubleValue coolingCoat2Modifier;
    public final ModConfigSpec.DoubleValue coolingCoat3Modifier;

    public final ModConfigSpec.DoubleValue thermalCoat1Modifier;
    public final ModConfigSpec.DoubleValue thermalCoat2Modifier;
    public final ModConfigSpec.DoubleValue thermalCoat3Modifier;

    public final ModConfigSpec.DoubleValue tfcItemHeatMultiplier;
    public final ModConfigSpec.DoubleValue tfcTemperatureMultiplier;

    TemperatureConfig(ModConfigSpec.Builder builder)
    {
        temperatureEnabled = builder
                .comment(" Whether the temperature system is enabled.",
                        " Season related temperature options are in seasons.toml.")
                .define("Temperature Enabled", true);
        dangerousHeatTemperature = builder
                .comment(" If enabled, players will take damage from the effects of high temperature.")
                .define("Dangerous Heat Temperature Effects", true);
        dangerousColdTemperature = builder
                .comment(" If enabled, players will take damage from the effects of low temperature.")
                .define("Dangerous Cold Temperature Effects", true);
        goldFernChance = builder
                .comment(" Chance of the ferns to become a gold fern when grow mature.")
                .defineInRange("Gold Fern Chance", 0.01, 0, 2);
        goldFernWorldgenChance = builder
                .comment(" Chance of the ferns to become a gold fern when generated in the world.")
                .defineInRange("Gold Fern Worldgen Chance", 0.0002, 0, 2);
        onFireModifier = builder
                .comment(" How much of an effect being on fire has on a player's temperature.")
                .defineInRange("Player On Fire Modifier", 12.5, -1000, 1000);
        sprintModifier = builder
                .comment(" How much of an effect sprinting has on a player's temperature.")
                .defineInRange("Player Sprint Modifier", 1.5, -1000, 1000);
        altitudeModifier = builder
                .comment(" How much of an effect altitude has on player's temperature.",
                        " Each 64 blocks further from sea level will impact player's temperature by this value.",
                        " The sea level can be defined via datapack under the dimension's temperature.",
                        " As an example, a value of -6 will reduce the player's temperature by 6 for each 64 blocks (the calculus is done linearly).")
                .defineInRange("Altitude Modifier", -6.0, -1000, 1000);

        builder.push("temperature-immunity");
        temperatureImmunityOnDeathEnabled = builder
                .comment(" If enabled, players will be immune to temperature effects after death.")
                .define("Temperature Immunity On Death Enabled", true);
        temperatureImmunityOnDeathTime = builder
                .comment(" Temperature immunity period in ticks while the player is immune to temperature effects after death.")
                .defineInRange("Temperature Immunity On Death Time", 1800, 0, 100000);
        temperatureImmunityOnFirstSpawnEnabled = builder
                .comment(" If enabled, players will be immune to temperature effects on first spawn in a world.")
                .define("Temperature Immunity On First Spawn Enabled", true);
        temperatureImmunityOnFirstSpawnTime = builder
                .comment(" Temperature immunity period in ticks while the player is immune to temperature effects on first spawn.")
                .defineInRange("Temperature Immunity On First Spawn Time", 1800, 0, 100000);
        builder.pop();

        builder.push("secondary_effects");
        heatTemperatureSecondaryEffects = builder
                .comment(" If enabled, players will also receive other effects from their current temperature state.",
                        " If the player is too hot, hydration will deplete faster.")
                .define("Heat Temperature Secondary Effects", true);
        coldTemperatureSecondaryEffects = builder
                .comment(" If enabled, players will also receive other effects from their current temperature state.",
                        " If the player is too cold, hunger will deplete faster.")
                .define("Cold Temperature Secondary Effects", true);
        heatThirstEffectModifier = builder
                .comment(" How much thirst exhaustion will be added every 50 ticks with no amplification effect, when the player suffers from heat.")
                .defineInRange("Heat Thirst Effect Modifier", 0.2d, 0, 1000.0d);
        coldHungerEffectModifier = builder
                .comment(" How much food exhaustion will be added every 50 ticks with no amplification effect, when the player suffers from frostbite.",
                        " As reference, the hunger effect add 0.025 food exhaustion every 50 ticks.")
                .defineInRange("Cold Hunger Modifier", 0.1d, 0, 1000.0d);
        builder.pop();

        builder.push("wetness");
        wetnessEnabled = builder
                .comment(" Enable the wetness mechanic.")
                .define("Wetness Enabled", true);
        wetnessImmunityMounts = builder
                .comment(" List of mounts that provide a wetness immunity.")
                .defineListAllowEmpty("Wetness Immunity Mounts", List.of("alexscaves:submarine", "immersive_machinery:bamboo_bee", "immersive_machinery:tunnel_digger", "immersive_machinery:redstone_sheep", "immersive_machinery:copperfin"), Config::validateEntityType);
        wetMultiplier = builder
                .comment(" How much being wet influences the player's temperature.",
                        " It means that for a value of -10, the body temperature of the player is reduced by 10.")
                .defineInRange("Wetness Modifier", -10.0, -1000, 1000);
        wetnessTickTimer = builder
                .comment(" How frequently the wetness is modified.",
                        " By default, every 10 ticks, the wetness will either increase or decrease, based on the conditions.")
                .defineInRange("Wetness Tick Timer", 10, 1, 100000);
        wetnessDecrease = builder
                .comment(" How much the wetness decrease when out of water.")
                .defineInRange("Wetness Decrease", -3, -1000, 0);
        wetnessRainIncrease = builder
                .comment(" How much the wetness increase when under rain.")
                .defineInRange("Wetness Under Rain Increase", 7, 0, 1000);
        wetnessFluidIncrease = builder
                .comment(" How much the wetness increase when the player is in a fluid, scale by the amount of fluid in the block.",
                        " The defined value is for a full block of fluid, and goes up to 2 times this value when fully immerge.")
                .defineInRange("Wetness In Fluid Increase", 10, 0, 1000);
        builder.pop();

        builder.push("huddling");
        playerHuddlingModifier = builder
                .comment(" How much nearby players increase the ambient temperature by.", " Note that this value stacks!")
                .defineInRange("Player Huddling Modifier", 0.5, -1000, 1000);
        playerHuddlingRadius = builder
                .comment(" The radius, in blocks, around which players will add to each other's temperature.")
                .defineInRange("Player Huddling Radius", 1, 0, 10);
        builder.pop();

        builder.push("biomes");
        biomeTemperatureMultiplier = builder
                .comment(" How much a biome's temperature effects are multiplied.")
                .defineInRange("Biome Temperature Multiplier", 18.0d, 0.0d, 1000);
        biomeEffectsEnabled = builder
                .comment(" Whether biomes will have an effect on a player's temperature.")
                .define("Biomes affect Temperature", true);
        biomeDrynessMultiplier = builder
                .comment(" How much hot biome's dryness will make nights really cold.",
                        " Affects only dry (minecraft down fall <0.2) and hot biome.",
                        " 1 means no dryness effect; 0.5 means the biome temp will be divided by 2 at the middle of the night.")
                .defineInRange("Biome's Dryness Multiplier", 0.2d, 0, 1);
        builder.pop();

        builder.comment(" The underground effect starts apply at Start Distance to the world surface.",
                " The underground effect will linearly apply a multiplier on the biome temperature, and averages the time and season temperature effects.").push("underground");
        undergroundBiomeTemperatureMultiplier = builder
                .comment(" How much a biomes temperature effects are multiplied when player is underground")
                .defineInRange("Underground Biome Temperature Multiplier", 0.8d, 0.0d, 1000);
        undergroundEffectStartDistanceToWS = builder
                .comment(" Distance to the World Surface where underground effect will start to be applied.",
                        " Smaller distance, no underground effect are applied.")
                .defineInRange("Start Distance To World Surface For Underground Effect", 10, 0, 400);
        undergroundEffectEndDistanceToWS = builder
                .comment(" Distance to the World Surface where underground effect will be maximal.",
                        " Bigger distance, the underground effect is maximal. Between the Start and End Distance, the increase of underground effect is linear.")
                .defineInRange("End Distance To World Surface For Underground Effect", 16, 0, 400);
        builder.pop();

        builder.push("weather");
        rainTemperatureModifier = builder
                .comment(" How much of an effect rain has on temperature.",
                        " It means that for a value of -2, the body temperature of the player is reduced by 2.")
                .defineInRange("Rain Temperature Modifier", -2.0, -1000, 1000);
        snowTemperatureModifier = builder
                .comment(" How much of an effect snow has on temperature.",
                        " It means that for a value of -6, the body temperature of the player is reduced by 6.")
                .defineInRange("Snow Temperature Modifier", -6.0, -1000, 1000);
        builder.pop();

        builder.comment(" Freeze effect increases while inside snow powder.").push("freeze");
        vanillaFreezeEnabled = builder
                .comment(" If enabled, the player suffers vanilla freeze when inside powder snow.")
                .define("Vanilla Freeze Enabled", false);
        maxFreezeTemperatureModifier = builder
                .comment(" How much of an effect freeze has on temperature when reaching maximum tick time. Starts at 0 and increases linearly.")
                .defineInRange("Max Freeze Temperature Modifier", -10.0, -1000, 1000);
        maxFreezeEffectTick = builder
                .comment(" How long in tick before freeze modifier reaches its maximum effect.")
                .defineInRange("Max Freeze Effect Tick", 400, 0, 100000);
        builder.pop();

        builder.push("time");
        timeModifier = builder
                .comment(" How much Time has effect on Temperature.",
                        " Maximum effect at noon (positive) and midnight (negative), following a sinusoidal")
                .defineInRange("Time Based Temperature Modifier", 2.0d, 0.0d, Double.POSITIVE_INFINITY);
        biomeTimeMultiplier = builder
                .comment(" How strongly time in extreme temperature biomes affect player's temperature.",
                        " Extreme temperature biomes (like snowy taiga, deserts, ...) will multiply the time based temperature by this value, while temperate biome won't be affected by this value, following a linear.")
                .defineInRange("Biome Time Multiplier", 1.75d, 1.0d, Double.POSITIVE_INFINITY);
        shadeTimeModifier = builder
                .comment(" Staying in the shade or during cloudy weather will reduce player's temperature by this amount based on time of the day (maximum effect at noon, following sinusoidal).",
                        " It means that for a value of -6, the body temperature of the player is reduced by 6.",
                        " Only effective when reaching the threshold and during day time!")
                .defineInRange("Shade Time Modifier", -6.0, -1000, 1000);
        shadeTimeModifierThreshold = builder
                .comment(" Defines when the biome temperature added by the season temperature (if seasons mod loaded) will trigger a shade effect.")
                .defineInRange("Shade Time Modifier Threshold", 9.0, 0, 10000);
        builder.pop();

        builder.comment(" Temperature coat adds temperature effects on armors by using the sewing table.",
                        " Adaptive means the coating will maintain the player's temperature temperate.")
                .push("coat");

        builder.comment(" Add a heating resistance on armors.").push("heating");
        heatingCoat1Modifier = builder.defineInRange("Heating Coat I", 2.0d, 0, 1000.0d);
        heatingCoat2Modifier = builder.defineInRange("Heating Coat II", 3.0d, 0, 1000.0d);
        heatingCoat3Modifier = builder.defineInRange("Heating Coat III", 4.0d, 0, 1000.0d);
        builder.pop();

        builder.comment(" Add a cooling resistance on armors.").push("cooling");
        coolingCoat1Modifier = builder.defineInRange("Cooling Coat I", 2.0d, 0, 1000.0d);
        coolingCoat2Modifier = builder.defineInRange("Cooling Coat II", 3.0d, 0, 1000.0d);
        coolingCoat3Modifier = builder.defineInRange("Cooling Coat III", 4.0d, 0, 1000.0d);
        builder.pop();

        builder.comment(" Add a temperature resistance on armors that can both heat and cool the player.")
                .push("thermal");
        thermalCoat1Modifier = builder.defineInRange("Thermal Coat I", 2.0d, 0, 1000.0d);
        thermalCoat2Modifier = builder.defineInRange("Thermal Coat II", 3.0d, 0, 1000.0d);
        thermalCoat3Modifier = builder.defineInRange("Thermal Coat III", 4.0d, 0, 1000.0d);
        builder.pop();
        builder.pop();

        builder.push("advanced");
        tempInfluenceMaximumDist = builder
                .comment(" Maximum influence distance, in blocks, where thermal sources will have an effect on temperature.")
                .defineInRange("Temperature Influence Maximum Distance", 20, 1, 40);
        tempInfluenceUpDistMultiplier = builder
                .comment(" How strongly influence distance above the player is reduced for thermal sources to have an effect on temperature.")
                .comment(" Example max dist is 10, up mult is 0.75 -> max distance is 10 * 0.75 = 7.5 blocks above the player.",
                        " Logic is that heat goes up, the strength of the heat source above the player is decreased faster with distance.")
                .defineInRange("Temperature Influence Up Distance Multiplier", 0.75, 0.0, 1.0);
        tempInfluenceInWaterDistMultiplier = builder
                .comment(" How strongly influence distance in water is reduced for thermal sources to have an effect on temperature.",
                        " The under water maximum distance is defined as the maximum distance * this value")
                .defineInRange("Temperature Influence In Water Distance Multiplier", 0.25, 0.0, 1.0);
        tempInfluenceOutsideDistMultiplier = builder
                .comment(" How strongly influence distance outside a structure is reduced for thermal sources to have an effect on temperature.",
                        " The outside maximum distance is defined as the maximum distance * this value")
                .defineInRange("Temperature Influence Outside Distance Multiplier", 0.5, 0.0, 1.0);
        builder
                .comment(" The player's temperature will be adjusted at each temperature tick time,",
                        " by an amount of temperature defined between the minimum and the maximum temperature modification adjusted linearly.")
                .push("temperature-modification");
        tempTickTime = builder
                .comment(" Amount of time in ticks between 2 player temperature modification. The bigger is this value, the more time it takes between temperature adjustments.")
                .defineInRange("Temperature Tick Time", 20, 5, Integer.MAX_VALUE);
        maxTemperatureModification = builder
                .comment(" Maximum amount of temperature the player's temperature can be modified at each temperature tick time.",
                        " Correspond to the amount of temperature given when temperature difference is maximum, meaning 40.")
                .defineInRange("Maximum Temperature Modification", 1, 0.1, Integer.MAX_VALUE);
        minTemperatureModification = builder
                .comment(" Minimum amount of temperature the player's temperature can be modified at each temperature tick time.",
                        " Correspond to the amount of temperature given when there is no temperature difference")
                .defineInRange("Minimum Temperature Modification", 0.2, 0.1, Integer.MAX_VALUE);
        builder.pop();
        builder.pop();

        builder.push("integration");
        builder.comment(" If TerraFirmaCraft is installed, then biome, time, season (if serene seasons installed) and altitude modifiers will be disabled, and TerraFirmaCraft calculation used instead.",
                " All other modifiers remain to calculate Player temperature.").push("terrafirmacraft");
        tfcItemHeatMultiplier = builder
                .comment(" How much the heat of the item provided by TerraFirmaCraft is multiplied. 0 deactivates the impact on temperature.")
                .defineInRange("TerraFirmaCraft Item Heat Multiplier", 0.01d, 0, 1000);
        tfcTemperatureMultiplier = builder
                .comment(" How much the temperature given from TerraFirmaCraft is multiplied. 0 deactivates the impact on temperature.")
                .defineInRange("TerraFirmaCraft Temperature Multiplier", 1.0d, 0, 1000);
        builder.pop();
        builder.pop();
    }
}
