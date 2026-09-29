package sfiomn.legendarysurvivaloverhaul.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ThirstConfig
{
    public final ModConfigSpec.BooleanValue thirstEnabled;
    public final ModConfigSpec.BooleanValue dangerousDehydration;
    public final ModConfigSpec.BooleanValue cumulativeThirstEffectDuration;
    public final ModConfigSpec.DoubleValue dehydrationDamageScaling;
    public final ModConfigSpec.DoubleValue thirstEffectModifier;
    public final ModConfigSpec.DoubleValue baseHydrationExhaustion;
    public final ModConfigSpec.DoubleValue sprintingHydrationExhaustion;
    public final ModConfigSpec.DoubleValue onJumpHydrationExhaustion;
    public final ModConfigSpec.DoubleValue onBlockBreakHydrationExhaustion;
    public final ModConfigSpec.DoubleValue onAttackHydrationExhaustion;
    public final ModConfigSpec.IntValue canteenCapacity;
    public final ModConfigSpec.BooleanValue selfWateringCanteenEnabled;
    public final ModConfigSpec.IntValue selfWateringCanteenWetnessIncrease;
    public final ModConfigSpec.IntValue largeCanteenCapacity;
    public final ModConfigSpec.BooleanValue allowOverridePurifiedWater;
    public final ModConfigSpec.IntValue hydrationLava;
    public final ModConfigSpec.DoubleValue saturationLava;
    public final ModConfigSpec.BooleanValue glassBottleLootAfterDrink;

    // Integration
    public final ModConfigSpec.IntValue hydrationLavaBlazeborn;
    public final ModConfigSpec.DoubleValue saturationLavaBlazeborn;
    public final ModConfigSpec.DoubleValue extraThirstExhaustionShulk;
    public final ModConfigSpec.DoubleValue extraThirstExhaustionPhantom;
    public final ModConfigSpec.BooleanValue thirstEnabledIfVampire;

    ThirstConfig(ModConfigSpec.Builder builder)
    {
        thirstEnabled = builder
                .comment(" Whether the thirst system is enabled.")
                .define("Thirst Enabled", true);
        dangerousDehydration = builder
                .comment(" If enabled, players will take damage from the complete dehydration.")
                .define("Dangerous Dehydration", true);
        cumulativeThirstEffectDuration = builder
                .comment(" If enabled, each time the player receives a thirst effect, its duration will be added to the thirst effect duration if already on the player.")
                .define("Cumulative Thirst Effect Duration", true);
        dehydrationDamageScaling = builder
                .comment(" Scaling of the damages dealt when completely dehydrated. Each tick damage will be increased by this value.")
                .defineInRange("Dehydration Damage Scaling", 0.3d, 0, 1000.0d);
        thirstEffectModifier = builder
                .comment(" How much thirst exhaustion will be added every 50 ticks when the player suffers from non amplified Thirst Effect.",
                        " The player will suffer Thirst Effect from dirty water for example.")
                .defineInRange("Thirst Effect Modifier", 0.25d, 0, 1000);

        builder.push("exhaustion");
        baseHydrationExhaustion = builder
                .comment(" Hydration exhausted every 10 ticks.")
                .defineInRange("Base Hydration Exhaustion", 0.03d, 0, 1000.0d);
        sprintingHydrationExhaustion = builder
                .comment(" Hydration exhausted when sprinting, replacing the base hydration exhausted.")
                .defineInRange("Sprinting Hydration Exhaustion", 0.1d, 0, 1000.0d);
        onJumpHydrationExhaustion = builder
                .comment(" Hydration exhausted on every jump.")
                .defineInRange("On Jump Hydration Exhaustion", 0.15d, 0, 1000.0d);
        onBlockBreakHydrationExhaustion = builder
                .comment(" Hydration exhausted on every block break.")
                .defineInRange("On Block Break Hydration Exhaustion", 0.07d, 0, 1000.0d);
        onAttackHydrationExhaustion = builder
                .comment(" Hydration exhausted on every attack.")
                .defineInRange("On Attack Hydration Exhaustion", 0.3d, 0, 1000.0d);
        builder.pop();

        builder.push("canteen");
        selfWateringCanteenEnabled = builder
                .comment(" If enabled, the player can water himself by using the canteen while crouching.",
                        " This will increase the player wetness and remove fire.")
                .define("Self Watering Canteen Enabled", true);
        selfWateringCanteenWetnessIncrease = builder
                .comment(" If Self Watering Canteen and Wetness are enabled, defines how much wetness is added to the player.",
                        " Set this value to 0 to have no wetness added. By default, the maximum wetness is 400.")
                .defineInRange("Self Watering Canteen Wetness", 400, 0, 10000);
        canteenCapacity = builder
                .comment(" Capacity of the canteen used to store water.")
                .defineInRange("Canteen Capacity", 10, 0, 1000);
        largeCanteenCapacity = builder
                .comment(" Capacity of the large canteen used to store water.")
                .defineInRange("Large Canteen Capacity", 20, 0, 1000);
        allowOverridePurifiedWater = builder
                .comment(" Allow override of purified water stored in canteen with normal water.")
                .define("Allow Override Purified Water", true);
        builder.pop();

        builder.comment(" Allows drinking from lava. Can be used as bauble.").push("nether_chalice");
        hydrationLava = builder
                .comment(" Amount of hydration recovered when drinking from lava.")
                .defineInRange("Lava Hydration", 6, 0, 20);
        saturationLava = builder
                .comment(" Amount of saturation recovered when drinking from lava.")
                .defineInRange("Lava Saturation", 4.0, 0, 20);
        builder.pop();

        builder.push("juices");
        glassBottleLootAfterDrink = builder
                .comment(" Whether the player retrieves a glass bottle after drinking a juice.")
                .define("Glass Bottle Loot After Drinking A Juice", true);
        builder.pop();

        builder.push("integration");
        builder.push("origins");

        builder.comment(" Temperature won't increase while on fire",
                        " Immune to wetness",
                        " Can drink lava")
                .push("blazeborn");
        hydrationLavaBlazeborn = builder
                .comment(" Amount of hydration recovered when drinking from lava.")
                .defineInRange("Lava Hydration For Blazeborn", 3, 0, 20);
        saturationLavaBlazeborn = builder
                .comment(" Amount of saturation recovered when drinking from lava.")
                .defineInRange("Lava Saturation For Blazeborn", 1.0, 0, 20);
        builder.pop();

        builder.comment(" Thirst depletes slightly faster").push("shulk");
        extraThirstExhaustionShulk = builder
                .comment(" Amount of thirst exhaustion added every 20 ticks.")
                .defineInRange("Extra Thirst Exhaustion For Shulk", 0.1, 0, 1000);
        builder.pop();

        builder.comment(" Thirst depletes slightly faster").push("phantom");
        extraThirstExhaustionPhantom = builder
                .comment(" Amount of thirst exhaustion added every 20 ticks.")
                .defineInRange("Extra Thirst Exhaustion For Phantom", 0.1, 0, 1000);
        builder.pop();
        builder.pop();

        builder.push("vampirism");
        thirstEnabledIfVampire = builder
                .comment(" If Vampirism is installed and if thirst enabled while being a vampire, keep the thirst system in addition to the vampiric one.",
                        " If disabled, the thirst system will be disabled for vampires.")
                .define("Thirst Enabled If Vampire", false);
        builder.pop();
        builder.pop();
    }
}
