package sfiomn.legendarysurvivaloverhaul.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class SeasonsConfig
{
    public final ModConfigSpec.BooleanValue sereneSeasonsEnabled;
    public final ModConfigSpec.BooleanValue ssTropicalSeasonsEnabled;
    public final ModConfigSpec.BooleanValue ssSeasonCardsEnabled;
    public final ModConfigSpec.BooleanValue ssDefaultSeasonEnabled;

    public final ModConfigSpec.DoubleValue ssEarlySpringModifier;
    public final ModConfigSpec.DoubleValue ssMidSpringModifier;
    public final ModConfigSpec.DoubleValue ssLateSpringModifier;

    public final ModConfigSpec.DoubleValue ssEarlySummerModifier;
    public final ModConfigSpec.DoubleValue ssMidSummerModifier;
    public final ModConfigSpec.DoubleValue ssLateSummerModifier;

    public final ModConfigSpec.DoubleValue ssEarlyAutumnModifier;
    public final ModConfigSpec.DoubleValue ssMidAutumnModifier;
    public final ModConfigSpec.DoubleValue ssLateAutumnModifier;

    public final ModConfigSpec.DoubleValue ssEarlyWinterModifier;
    public final ModConfigSpec.DoubleValue ssMidWinterModifier;
    public final ModConfigSpec.DoubleValue ssLateWinterModifier;

    public final ModConfigSpec.DoubleValue ssEarlyWetSeasonModifier;
    public final ModConfigSpec.DoubleValue ssMidWetSeasonModifier;
    public final ModConfigSpec.DoubleValue ssLateWetSeasonModifier;

    public final ModConfigSpec.DoubleValue ssEarlyDrySeasonModifier;
    public final ModConfigSpec.DoubleValue ssMidDrySeasonModifier;
    public final ModConfigSpec.DoubleValue ssLateDrySeasonModifier;

    public final ModConfigSpec.BooleanValue eclipticSeasonsEnabled;
    public final ModConfigSpec.ConfigValue<List<? extends Double>> esSpringModifier;
    public final ModConfigSpec.ConfigValue<List<? extends Double>> esSummerModifier;
    public final ModConfigSpec.ConfigValue<List<? extends Double>> esAutumnModifier;
    public final ModConfigSpec.ConfigValue<List<? extends Double>> esWinterModifier;

    SeasonsConfig(ModConfigSpec.Builder builder)
    {
        builder.comment(" Temperature options for the Serene Seasons integration.").push("serene-seasons");
        sereneSeasonsEnabled = builder
                .comment(" If Serene Seasons is installed, whether the seasons have an effect on the player's temperature.")
                .define("Serene Seasons Enabled", true);
        ssTropicalSeasonsEnabled = builder
                .comment(" If the tropical seasons are disabled, the normal summer-autumn-winter-spring seasons are applied.",
                        " If disabled, dry and wet seasons are applied for hot biomes.")
                .define("Tropical Seasons Enabled", false);
        ssSeasonCardsEnabled = builder
                .comment(" If season cards are enabled, season cards will appear at every season changes.")
                .define("Season Cards Enabled", false);
        ssDefaultSeasonEnabled = builder
                .comment(" If default season is enabled, when serene season defines no season effect in a biome, the normal season temperature will be applied.",
                        " If disabled, when serene season defines no season effects, no season temperature will be applied.")
                .define("Default Season Enabled", true);

        builder.comment(" Temperature modifiers per season in temperate biomes." +
                        " The value is reached at the middle of the sub season, and smoothly transition from one to another.")
                .push("temperate");
        builder.push("spring");
        ssEarlySpringModifier = builder.defineInRange("Early Spring Modifier", -3.0, -1000, 1000);
        ssMidSpringModifier = builder.defineInRange("Mid Spring Modifier", 0.0, -1000, 1000);
        ssLateSpringModifier = builder.defineInRange("Late Spring Modifier", 3.0, -1000, 1000);
        builder.pop();

        builder.push("summer");
        ssEarlySummerModifier = builder.defineInRange("Early Summer Modifier", 6.0, -1000, 1000);
        ssMidSummerModifier = builder.defineInRange("Mid Summer Modifier", 10.0, -1000, 1000);
        ssLateSummerModifier = builder.defineInRange("Late Summer Modifier", 6.0, -1000, 1000);
        builder.pop();

        builder.push("autumn");
        ssEarlyAutumnModifier = builder.defineInRange("Early Autumn Modifier", 3.0, -1000, 1000);
        ssMidAutumnModifier = builder.defineInRange("Mid Autumn Modifier", 0.0, -1000, 1000);
        ssLateAutumnModifier = builder.defineInRange("Late Autumn Modifier", -3.0, -1000, 1000);
        builder.pop();

        builder.push("winter");
        ssEarlyWinterModifier = builder.defineInRange("Early Winter Modifier", -7.0, -1000, 1000);
        ssMidWinterModifier = builder.defineInRange("Mid Winter Modifier", -12.0, -1000, 1000);
        ssLateWinterModifier = builder.defineInRange("Late Winter Modifier", -7.0, -1000, 1000);
        builder.pop();
        builder.pop();

        builder.comment(" Temperature modifiers per season in tropical biomes.").push("tropical");
        builder.push("wet-season");
        ssEarlyWetSeasonModifier = builder.defineInRange("Early Wet Season Modifier", -1.0, -1000, 1000);
        ssMidWetSeasonModifier = builder.defineInRange("Mid Wet Season Modifier", -5.0, -1000, 1000);
        ssLateWetSeasonModifier = builder.defineInRange("Late Wet Season Modifier", -1.0, -1000, 1000);
        builder.pop();

        builder.push("dry-season");
        ssEarlyDrySeasonModifier = builder.defineInRange("Early Dry Season Modifier", 3.0, -1000, 1000);
        ssMidDrySeasonModifier = builder.defineInRange("Mid Dry Season Modifier", 7.0, -1000, 1000);
        ssLateDrySeasonModifier = builder.defineInRange("Late Dry Season Modifier", 3.0, -1000, 1000);
        builder.pop();
        builder.pop();
        builder.pop();

        builder.comment(" Temperature options for the Ecliptic Seasons integration.").push("ecliptic-seasons");
        eclipticSeasonsEnabled = builder
                .comment(" If Ecliptic Seasons is installed, whether the seasons have an effect on the player's temperature.")
                .define("Ecliptic Seasons Enabled", true);

        builder.comment(" Temperature modifiers per season. Each season is subdivided in 6 sub seasons." +
                        " The value is reached at the middle of the sub season, and smoothly transition from one to another.")
                .push("temperature");
        esSpringModifier = builder.defineList("Spring Modifier", List.of(-10.0, -7.0, -5.0, -3.0, -1.0, 0.0), Config::validateDouble);
        esSummerModifier = builder.defineList("Summer Modifier", List.of(1.0, 3.0, 5.0, 7.0, 9.0, 10.0), Config::validateDouble);
        esAutumnModifier = builder.defineList("Autumn Modifier", List.of(9.0, 7.0, 5.0, 3.0, 1.0, 0.0), Config::validateDouble);
        esWinterModifier = builder.defineList("Winter Modifier", List.of(-1.0, -3.0, -5.0, -7.0, -10.0, -12.0), Config::validateDouble);
        builder.pop();
        builder.pop();
    }
}
