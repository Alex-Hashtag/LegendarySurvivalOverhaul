package sfiomn.legendarysurvivaloverhaul.config;

import net.minecraftforge.common.ForgeConfigSpec;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

public class HealthConfig
{
	public final ForgeConfigSpec.BooleanValue healthOverhaulEnabled;
	public final ForgeConfigSpec.DoubleValue initialHealth;
	public final ForgeConfigSpec.BooleanValue naturalRegenerationEnabled;
	public final ForgeConfigSpec.DoubleValue healthRatioRecoveredFromSleep;
	public final ForgeConfigSpec.DoubleValue maxAdditionalHealth;
	public final ForgeConfigSpec.DoubleValue maxShieldHealth;
	public final ForgeConfigSpec.BooleanValue absorptionEffectOverride;
	public final ForgeConfigSpec.IntValue heartsLostOnDeath;
	public final ForgeConfigSpec.IntValue permanentHearts;
	public final ForgeConfigSpec.IntValue resilientHeartsWithBrokenHearts;
	public final ForgeConfigSpec.DoubleValue brokenHeartsPerInjuredLimb;
	public final ForgeConfigSpec.EnumValue<EnumUtil.brokenHeartsPerInjuredLimbMode> brokenHeartsPerInjuredLimbMode;

	public final ForgeConfigSpec.BooleanValue customHealthRegenEnabled;
	public final ForgeConfigSpec.DoubleValue customHealthRegenRate;
	public final ForgeConfigSpec.IntValue customHealthRegenTickRate;
	public final ForgeConfigSpec.DoubleValue customHealthRegenFoodExhaustion;

	HealthConfig(ForgeConfigSpec.Builder builder)
	{
		healthOverhaulEnabled = builder
				.comment(" Whether the overhaul health system is enabled.")
				.define("Health Overhaul Enabled", true);
		initialHealth = builder
				.comment(" How much health player will have initially.")
				.defineInRange("Initial Player Health", 20.0, 1.0, 10000.0);
		maxAdditionalHealth = builder
				.comment(" How much of Additional Health a player can accumulate. 2 Heath means a full heart.")
				.defineInRange("Maximum Additional Health", 20.0, 0.0, 10000.0);
		healthRatioRecoveredFromSleep = builder
				.comment(" How much health ratio are recovered from bed sleeping.")
				.defineInRange("Health Ratio Recovered", 1.0d, 0.0d, 1.0d);

		builder.push("regeneration");
		naturalRegenerationEnabled = builder
				.comment(" If enabled, the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)")
				.define("Natural Regeneration Enabled", false);
		customHealthRegenEnabled = builder
				.comment(" Enable custom health regeneration when natural regen is off. Consumes saturation and hunger.")
				.define("Custom Health Regen Enabled", true);
		customHealthRegenRate = builder
				.comment(" Amount of health to regenerate per tick rate.")
				.defineInRange("Custom Health Regen Rate", 1.0, 0, 1000);
		customHealthRegenTickRate = builder
				.comment(" How often in ticks health regenerates. 20 ticks = 1s")
				.defineInRange("Custom Health Regen Tick Rate", 200, 1, 10000);
		customHealthRegenFoodExhaustion = builder
				.comment(" Food exhaustion per health point regenerated.")
				.defineInRange("Custom Health Regen Food Exhaustion", 6.0, 0, 100);
		builder.pop();

		builder.push("shield-health");
		maxShieldHealth = builder
				.comment(" How much of Shield Health a player can accumulate. 2 Shield Heath means a full shield.",
						" Shield Health are lost when the player suffers damages and can't regenerate. Works similarly as the Minecraft Absorption.")
				.defineInRange("Maximum Shield Health", 20.0, 1.0, 10000.0);
		absorptionEffectOverride = builder
				.comment(" Override the absorption effect by a shield health increase of 2.",
						" The absorption is typically given by the Golden Apple.")
				.define("Absorption Effect Override", true);
		builder.pop();

		builder.push("heart-loss");
		heartsLostOnDeath = builder
				.comment(" The number of Hearts lost on death.")
				.defineInRange("Hearts Lost On Death", 0, 0, 10000);
		permanentHearts = builder
				.comment(" The number of Hearts below which player can't lose hearts upon death.",
						" The hearts below this limit are de facto Permanent Hearts.")
				.defineInRange("Permanent Hearts", 10, 1, 10000);
		builder.pop();

		builder.comment(" Broken Hearts are an interaction with the localized body damage feature. Enables both feature to have it.",
						" Broken Hearts are lost hearts when a player's limb is severely injured and it can be recovered by healing the injured limb.")
				.push("broken-hearts");
		resilientHeartsWithBrokenHearts = builder
				.comment(" The Resilient Hearts is the number of heart below which Broken Hearts can no longer be added.",
						" By default, the player has 2 resilient heart, meaning no matter the amount of broken hearts, the player won't go below 2 hearts.")
				.defineInRange("Minimum Amount Of Player's Heart With Broken Hearts (Broken Heart Resilience)", 2, 1, 10000);
		brokenHeartsPerInjuredLimb = builder
				.comment(" Amount of Broken Hearts added per limbs fully injured.")
				.defineInRange("Added Broken Hearts Per Injured Limb", 0.1, 0, 10000);
		brokenHeartsPerInjuredLimbMode = builder
				.comment(" How broken hearts inflicted per injured limbs are calculated. The total amount will be round down to have an integer amount of broken hearts.",
						" For example, if the amount per injured limb is 0.1 with mode Player Dynamic and the player has 3 limbs injured, the total amount is 3 * (0.1 * 20), 20 being the default player max health, so 6 broken hearts will be inflicted.",
						" Accepted values are as follows:",
						"   SIMPLE - The broken heart amount is a fixed value defined in Broken Hearts Per Injured Limb.",
						"   PLAYER_DYNAMIC - The broken heart amount is a percentage value of the player max health using the percentage value defined in Broken Hearts Per Injured Limb.",
						"   LIMB_DYNAMIC - The broken heart amount is a percentage value of the injured limb max health using the percentage value defined in Broken Hearts Per Injured Limb.",
						" Any other value will default to SIMPLE.")
				.defineEnum("Broken Hearts Per Injured Limb Mode", EnumUtil.brokenHeartsPerInjuredLimbMode.PLAYER_DYNAMIC);
		builder.pop();
	}
}
