package sfiomn.legendarysurvivaloverhaul.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

public class CommonConfig
{
    // Core/Advanced
    public final ModConfigSpec.EnumValue<EnumUtil.DifficultyMode> difficultyMode;
    public final ModConfigSpec.IntValue routinePacketSync;

    // Misc
    public final ModConfigSpec.EnumValue<EnumUtil.CompassInfo> compassInfoMode;
    public final ModConfigSpec.BooleanValue showCoordinateOnMap;
    public final ModConfigSpec.BooleanValue hideInfoFromDebug;

    // Food
    public final ModConfigSpec.DoubleValue baseFoodExhaustion;
    public final ModConfigSpec.DoubleValue sprintingFoodExhaustion;
    public final ModConfigSpec.DoubleValue onAttackFoodExhaustion;

    CommonConfig(ModConfigSpec.Builder builder)
    {
        builder.comment(new String[]{
                " General options shared by all features.",
                " Each feature has its own config file in this folder (temperature, seasons, thirst, health, body_damage).",
                " See the data packs to customize the temperature of specific blocks, liquids, armors, etc."
        }).push("core");
        difficultyMode = builder
                .comment(" How the mod represents a challenge for the player.",
                        " Accepted values are as follows:",
                        "   PEACEFUL - Temperature doesn't harm the player and the hydration doesn't decrease.",
                        "   EASY - Temperature and Thirst won't hurt the player health below 10 hearts.",
                        "   NORMAL - Temperature and Thirst hurts the player down to 1 heart.",
                        "   HARD - Temperature and Thirst can kill the player.",
                        " Any other value will default to HARD.")
                .defineEnum("Temperature Difficulty Mode", EnumUtil.DifficultyMode.HARD);

        builder.push("advanced");
        routinePacketSync = builder
                .comment(" How often player temperature, thirst, body damage and health is regularly synced between the client and server, in ticks.",
                        " Lower values will increase accuracy at the cost of performance.")
                .defineInRange("Routine Packet Sync", 30, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.pop();

        builder.push("misc");
        compassInfoMode = builder
                .comment(" What information the compass returns when player is using it or in an item frame.")
                .defineEnum("Compass Info Mode", EnumUtil.CompassInfo.FULL);
        showCoordinateOnMap = builder
                .comment(" If enabled, use on a filled map will show destination coordinates.")
                .define("Show Coordinate On Filled Map", true);
        hideInfoFromDebug = builder
                .comment(" If enabled, information like position and direction will be hidden from the debug screen (F3).")
                .define("Hide Info From Debug", true);
        builder.pop();

        builder.comment(" Options related to the player food data").push("food");
        baseFoodExhaustion = builder
                .comment(" Food exhausted every 10 ticks. Increase the base minecraft food exhaustion.")
                .defineInRange("Base Food Exhaustion", 0.05d, 0, 1000.0D);
        sprintingFoodExhaustion = builder
                .comment(" Food exhausted every 10 ticks while sprinting in addition to the sprinting minecraft food exhaustion.")
                .defineInRange("Sprinting Food Exhaustion", 0.1d, 0, 1000.0D);
        onAttackFoodExhaustion = builder
                .comment(" Food exhausted on every attack in addition to the minecraft attack food exhaustion.")
                .defineInRange("On Attack Food Exhaustion", 0.1d, 0, 1000.0D);
        builder.pop();
    }
}
