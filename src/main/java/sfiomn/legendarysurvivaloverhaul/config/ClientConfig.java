package sfiomn.legendarysurvivaloverhaul.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

public class ClientConfig
{
    public final ModConfigSpec.BooleanValue foodSaturationDisplayed;
    public final ModConfigSpec.BooleanValue showVanillaBarAnimationOverlay;

    public final ModConfigSpec.EnumValue<EnumUtil.temperatureDisplayMode> temperatureDisplayMode;
    public final ModConfigSpec.IntValue temperatureDisplayOffsetX;
    public final ModConfigSpec.IntValue temperatureDisplayOffsetY;
    public final ModConfigSpec.IntValue bodyTemperatureDisplayOffsetX;
    public final ModConfigSpec.IntValue bodyTemperatureDisplayOffsetY;
    public final ModConfigSpec.BooleanValue heatTemperatureOverlay;
    public final ModConfigSpec.BooleanValue coldTemperatureOverlay;
    public final ModConfigSpec.BooleanValue breathingSoundEnabled;
    public final ModConfigSpec.DoubleValue coldBreathEffectThreshold;
    public final ModConfigSpec.BooleanValue renderTemperatureInFahrenheit;

    public final ModConfigSpec.IntValue wetnessIndicatorOffsetX;
    public final ModConfigSpec.IntValue wetnessIndicatorOffsetY;

    public final ModConfigSpec.IntValue bodyDamageIndicatorOffsetX;
    public final ModConfigSpec.IntValue bodyDamageIndicatorOffsetY;
    public final ModConfigSpec.DoubleValue bodyDamageIndicatorRenderHealthLimit;

    public final ModConfigSpec.IntValue seasonCardsDisplayOffsetX;
    public final ModConfigSpec.IntValue seasonCardsDisplayOffsetY;
    public final ModConfigSpec.IntValue seasonCardsSpawnDimensionDelayInTicks;
    public final ModConfigSpec.IntValue seasonCardsDisplayTimeInTicks;
    public final ModConfigSpec.IntValue seasonCardsFadeInInTicks;
    public final ModConfigSpec.IntValue seasonCardsFadeOutInTicks;

    public final ModConfigSpec.BooleanValue showHydrationTooltip;
    public final ModConfigSpec.BooleanValue mergeHydrationAndSaturationTooltip;
    public final ModConfigSpec.BooleanValue hydrationSaturationDisplayed;
    public final ModConfigSpec.BooleanValue hydrationExhaustionDisplayed;
    public final ModConfigSpec.BooleanValue lowHydrationEffect;
    public final ModConfigSpec.BooleanValue showHydrationBar;
    public final ModConfigSpec.BooleanValue showDrinkPreview;
    public final ModConfigSpec.IntValue hydrationBarOffsetX;
    public final ModConfigSpec.IntValue hydrationBarOffsetY;

    public final ModConfigSpec.BooleanValue appendBrokenShieldHeartsToHealthBar;

    ClientConfig(ModConfigSpec.Builder builder)
    {

        builder.comment(" Options related to the heads up display.",
                        " These options will automatically update upon being saved.")
                .push("hud");
        builder.push("general");

        foodSaturationDisplayed = builder
                .comment(" If enabled, the food saturation will be rendered on the Food Bar while the player suffers Cold Hunger Effect (secondary temperature effect).")
                .define("Show Food Saturation Bar", true);

        showVanillaBarAnimationOverlay = builder
                .comment(" Whether the vanilla animation of the Food bar and Hydration bar is rendered. The bar shakes more the lower they are.",
                        " This mod render a new food bar as a secondary effect of a cold temperature.",
                        " Disable this animation if the temperature secondary effect is enabled to allow a compatibility with other mods rendering the food bar (for example Appleskin).")
                .define("Show Vanilla Bar Animation Overlay", true);
        builder.pop();

        builder.push("temperature");
        temperatureDisplayMode = builder
                .comment(" How temperature is displayed. Accepted values are as follows:",
                        "    SYMBOL - Display the player's current temperature as a symbol above the hotbar.",
                        "    NONE - Disable the temperature indicator.")
                .defineEnum("Temperature Display Mode", EnumUtil.temperatureDisplayMode.SYMBOL);
        temperatureDisplayOffsetX = builder
                .comment(" The X and Y offset of the temperature indicator. Set both to 0 for no offset.")
                .defineInRange("Temperature Display X Offset", 0, -10000, 10000);
        temperatureDisplayOffsetY = builder
                .defineInRange("Temperature Display Y Offset", 0, -10000, 10000);
        bodyTemperatureDisplayOffsetX = builder
                .comment(" The X and Y offset of the body temperature, shown when thermometer is equipped as a bauble (needs the curios mod).",
                        " Set both to 0 for no offset.")
                .defineInRange("Body Temperature Display X Offset", 0, -10000, 10000);
        bodyTemperatureDisplayOffsetY = builder
                .defineInRange("Body Temperature Display Y Offset", 0, -10000, 10000);
        heatTemperatureOverlay = builder
                .comment(" If enabled, player will see a foggy effect when the heat is high.")
                .define("Heat Temperature Overlay", true);
        coldTemperatureOverlay = builder
                .comment(" If enabled, player will see a frost effect when the cold is low.")
                .define("Cold Temperature Overlay", true);
        breathingSoundEnabled = builder
                .comment(" If enabled, breathing sound can be heard while player faces harsh temperatures.")
                .define("Breathing Sound Enabled", true);
        coldBreathEffectThreshold = builder
                .comment(" Temperature threshold below which a cold breath effect is rendered by the player. -1000 disable the feature.")
                .defineInRange("Cold Breath Temperature Threshold", 10.0, -1000, 1000);
        renderTemperatureInFahrenheit = builder
                .comment(" If enabled, render the temperature values in Fahrenheit.")
                .define("Temperature In Fahrenheit", false);
        builder.push("wetness");
        wetnessIndicatorOffsetX = builder
                .comment(" The X and Y offset of the wetness indicator. Set both to 0 for no offset.")
                .defineInRange("Wetness Indicator X Offset", 0, -10000, 10000);
        wetnessIndicatorOffsetY = builder
                .defineInRange("Wetness Indicator Y Offset", 0, -10000, 10000);
        builder.pop();
        builder.pop();

        builder.pop();

        builder.push("body-damage");
        bodyDamageIndicatorOffsetX = builder
                .comment(" The X and Y offset of the body damage indicator. Set both to 0 for no offset.", " By default, render next to the inventory bar.")
                .defineInRange("Body Damage Indicator X Offset", 0, -10000, 10000);
        bodyDamageIndicatorOffsetY = builder.defineInRange("Body Damage Indicator Y Offset", 0, -10000, 10000);
        bodyDamageIndicatorRenderHealthLimit = builder
                .comment(" Limb health threshold below which the body damage indicator is rendered.", " If set to 1.1, the body damage indicator is always rendered.", " If set to 1.0, the body damage indicator is rendered as soon as a limb is wounded.")
                .defineInRange("Body Damage Indicator Limb Health Threshold", 1.0, 0.0, 1.1);
        builder.pop();

        builder.push("season-cards");
        seasonCardsDisplayOffsetX = builder
                .comment(" The X and Y offset of the season cards. Set both to 0 for no offset.", " By default, render first top quarter vertically and centered horizontally.")
                .defineInRange("Season Cards Display X Offset", 0, -10000, 10000);
        seasonCardsDisplayOffsetY = builder
                .defineInRange("Season Cards Display Y Offset", 0, -10000, 10000);
        seasonCardsSpawnDimensionDelayInTicks = builder
                .comment(" The delay before rendering the season card at first player spawn or player dimension change.")
                .defineInRange("Season Cards Delay In Ticks", 80, 0, Integer.MAX_VALUE);
        seasonCardsDisplayTimeInTicks = builder
                .comment(" The display time in ticks that the season card will be fully rendered.")
                .defineInRange("Season Cards Display Time In Ticks", 40, 0, Integer.MAX_VALUE);
        seasonCardsFadeInInTicks = builder
                .comment(" The fade in time in ticks that the season card will appear.")
                .defineInRange("Season Cards Fade in In Ticks", 20, 0, Integer.MAX_VALUE);
        seasonCardsFadeOutInTicks = builder
                .comment(" The fade out time in ticks that the season card will disappear.")
                .defineInRange("Season Cards Fade Out In Ticks", 20, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.push("thirst");

        builder.push("tooltip");
        showHydrationTooltip = builder
                .comment(" If enabled, show the hydration values in the item tooltip.")
                .define("Show Hydration Tooltip", true);
        mergeHydrationAndSaturationTooltip = builder
                .comment(" If enabled, show the hydration and the saturation values on the same line in the tooltip.")
                .define("Merge Hydration And Saturation Tooltip", true);
        builder.pop();

        hydrationSaturationDisplayed = builder
                .comment(" Whether the Hydration Saturation is displayed or not.")
                .define("Render the hydration saturation", true);
        hydrationExhaustionDisplayed = builder
                .comment(" Whether the Hydration Exhaustion is displayed or not (grey bar behind the hydration bar).")
                .define("Render the hydration exhaustion", true);
        lowHydrationEffect = builder
                .comment(" If enabled, player's vision will become blurry when running low on hydration.")
                .define("Low Hydration Effect", true);
        builder.push("hydration-bar");
        showHydrationBar = builder
                .comment(" If enabled, the hydration bar will be displayed.")
                .define("Show Hydration Bar", true);
        showDrinkPreview = builder
                .comment(" If enabled, a preview on hydration bar will be displayed if player holds a drink item.")
                .define("Show Drink Preview", true);
        hydrationBarOffsetX = builder
                .comment(" How much the hydration bar is moved to the right.")
                .defineInRange("Hydration Bar Offset X", 0, -10000, 10000);
        hydrationBarOffsetY = builder
                .comment(" How much the hydration bar is moved to the bottom.")
                .defineInRange("Hydration Bar Offset Y", 0, -10000, 10000);
        builder.pop();
        builder.pop();

        builder.push("health-overhaul");
        appendBrokenShieldHeartsToHealthBar = builder
                .comment(" If enabled, will try to append the broken and shield hearts at the end of the Health Bar.",
                        " A compat is made with overflowing-bars mod.")
                .define("Append Broken/Shield Hearts To Health Bar", true);
        builder.pop();
    }
}
