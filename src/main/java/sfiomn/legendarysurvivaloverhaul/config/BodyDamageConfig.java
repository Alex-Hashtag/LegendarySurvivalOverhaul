package sfiomn.legendarysurvivaloverhaul.config;

import net.minecraftforge.common.ForgeConfigSpec;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

import java.util.Collections;
import java.util.List;

public class BodyDamageConfig
{
	public final ForgeConfigSpec.BooleanValue localizedBodyDamageEnabled;
	public final ForgeConfigSpec.DoubleValue headCriticalShotMultiplier;
	public final ForgeConfigSpec.DoubleValue bodyDamageMultiplier;
	public final ForgeConfigSpec.DoubleValue bodyHealthRatioRecoveredFromSleep;
	public final ForgeConfigSpec.DoubleValue bodyHealingFoodExhaustion;
	public final ForgeConfigSpec.IntValue minFoodOnBodyHealing;
	public final ForgeConfigSpec.IntValue painkillerAddictionDuration;

	public final ForgeConfigSpec.BooleanValue passiveLimbRegenerationOnFullHealth;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> passiveLimbRegenerationEffects;
	public final ForgeConfigSpec.BooleanValue passiveLimbRegenerationAmplificationEnabled;
	public final ForgeConfigSpec.DoubleValue passiveLimbHealthRegenerated;
	public final ForgeConfigSpec.IntValue passiveLimbRegenerationTickTimer;

	public final ForgeConfigSpec.DoubleValue proportionalLimbRegenMinThreshold;
	public final ForgeConfigSpec.DoubleValue proportionalLimbRegenMinHealValue;
	public final ForgeConfigSpec.DoubleValue proportionalLimbRegenMaxHealValue;
	public final ForgeConfigSpec.IntValue proportionalLimbRegenTickRate;
	public final ForgeConfigSpec.DoubleValue proportionalLimbRegenIncreaseRate;

	public final ForgeConfigSpec.DoubleValue firstAidSuppliesLimbHealthRegenerated;
	public final ForgeConfigSpec.EnumValue<EnumUtil.limbRegenerationMode> firstAidSuppliesLimbRegenerationMode;
	public final ForgeConfigSpec.BooleanValue firstAidSuppliesHealingOverflow;
	public final ForgeConfigSpec.IntValue firstAidSuppliesTickTimer;
	public final ForgeConfigSpec.BooleanValue firstAidSuppliesExhaustsFood;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> firstAidSuppliesBoostedOnEffects;
	public final ForgeConfigSpec.DoubleValue firstAidSuppliesBoostedTickTimerMultiplier;

	public final ForgeConfigSpec.DoubleValue recoveryEffectHealingValue;
	public final ForgeConfigSpec.IntValue healingHerbsUseTime;
	public final ForgeConfigSpec.IntValue plasterUseTime;
	public final ForgeConfigSpec.IntValue bandageUseTime;
	public final ForgeConfigSpec.IntValue tonicUseTime;
	public final ForgeConfigSpec.IntValue medkitUseTime;
	public final ForgeConfigSpec.IntValue morphineUseTime;
	public final ForgeConfigSpec.IntValue morphinePainkillerTickDuration;

	public final ForgeConfigSpec.EnumValue<EnumUtil.bodyPartHealthMode> bodyPartHealthMode;
	public final ForgeConfigSpec.DoubleValue headPartHealth;
	public final ForgeConfigSpec.DoubleValue armsPartHealth;
	public final ForgeConfigSpec.DoubleValue chestPartHealth;
	public final ForgeConfigSpec.DoubleValue legsPartHealth;
	public final ForgeConfigSpec.DoubleValue feetPartHealth;

	public final ForgeConfigSpec.ConfigValue<List<? extends String>> headPartEffects;
	public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> headPartEffectAmplifiers;
	public final ForgeConfigSpec.ConfigValue<List<? extends Double>> headPartEffectThresholds;

	public final ForgeConfigSpec.ConfigValue<List<? extends String>> armsPartEffects;
	public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> armsPartEffectAmplifiers;
	public final ForgeConfigSpec.ConfigValue<List<? extends Double>> armsPartEffectThresholds;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> bothArmsPartEffects;
	public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> bothArmsPartEffectAmplifiers;
	public final ForgeConfigSpec.ConfigValue<List<? extends Double>> bothArmsPartEffectThresholds;

	public final ForgeConfigSpec.ConfigValue<List<? extends String>> chestPartEffects;
	public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> chestPartEffectAmplifiers;
	public final ForgeConfigSpec.ConfigValue<List<? extends Double>> chestPartEffectThresholds;

	public final ForgeConfigSpec.ConfigValue<List<? extends String>> legsPartEffects;
	public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> legsPartEffectAmplifiers;
	public final ForgeConfigSpec.ConfigValue<List<? extends Double>> legsPartEffectThresholds;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> bothLegsPartEffects;
	public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> bothLegsPartEffectAmplifiers;
	public final ForgeConfigSpec.ConfigValue<List<? extends Double>> bothLegsPartEffectThresholds;

	public final ForgeConfigSpec.ConfigValue<List<? extends String>> feetPartEffects;
	public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> feetPartEffectAmplifiers;
	public final ForgeConfigSpec.ConfigValue<List<? extends Double>> feetPartEffectThresholds;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> bothFeetPartEffects;
	public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> bothFeetPartEffectAmplifiers;
	public final ForgeConfigSpec.ConfigValue<List<? extends Double>> bothFeetPartEffectThresholds;

	public final ForgeConfigSpec.BooleanValue morphineSyringeApplyPainkillerAddiction;

	BodyDamageConfig(ForgeConfigSpec.Builder builder)
	{
		localizedBodyDamageEnabled = builder
				.comment(" Whether body members receive localized damages.",
						" The damageSourceBodyParts.json allows you to define for specific damage source, the damage spread across specified body parts.",
						" The damage distribution can either be ONE_OF or ALL. ALL means the damage are equally divided across all body parts.")
				.define("Localized Body Damage Enabled", true);
		headCriticalShotMultiplier = builder
				.comment(" Multiply the damage taken by the player when shot in the head without helmet.")
				.defineInRange("Headshot Multiplier", 2.0d, 1.0d, 1000.0d);
		bodyDamageMultiplier = builder
				.comment(" How much of the hurt player's damage is assigned to the body parts.")
				.defineInRange("Body Damage Multiplier", 1.0d, 0.0d, 1000.0d);
		bodyHealthRatioRecoveredFromSleep = builder
				.comment(" How much health ratio are recovered in all body parts from bed sleeping.")
				.defineInRange("Body Part Health Ratio Recovered", 1.0d, 0.0d, 1.0d);
		bodyHealingFoodExhaustion = builder
				.comment(" How much food is exhausted when a limb regenerates based on the amount of health regenerated.",
						" For each 1 health regenerated, the food is exhausted by this value.")
				.defineInRange("Body Healing Food Exhaustion", 0.5d, 0, 1000.0D);
		minFoodOnBodyHealing = builder
				.comment(" The hunger bar won't drop below this value while body is healing.",
						" Each hunger icon has a value of 2 in the hunger bar.")
				.defineInRange("Minimum Food On Body Healing", 0, 0, 1000);
		painkillerAddictionDuration = builder
				.comment(" How long in ticks is the Addiction Effect lasting. 0 deactivates the feature.",
						" The Addiction Effect prevents you from re-using the morphine item.")
				.defineInRange("Painkiller Addiction Duration", 3600, 0, 100000);

		builder.push("healing-limbs");
		builder.push("passive-regeneration");
		passiveLimbRegenerationOnFullHealth = builder
				.comment(" The limbs will be healed when the player is at max health.")
				.define("Passive Limb Regeneration On Full Health", true);
		passiveLimbRegenerationEffects = builder
				.comment(" The limbs will be healed when the player is under one of the mentioned effect, the most damaged limb first.")
				.defineListAllowEmpty("Passive Limb Regeneration On Effects", List.of("minecraft:regeneration", "farmersdelight:comfort"), Config::validateEffectName);
		passiveLimbRegenerationAmplificationEnabled = builder
				.comment(" Whether the amplification of the effect increase the limb regeneration speed.",
						" The speed increase follows the same logic as the Regeneration Effect Amplification :",
						"    Amplifier 1 -> tick timer /2, Amplifier 2 -> tick timer /4, ...")
				.define("Passive Limb Regeneration Amplification Enabled", true);
		passiveLimbHealthRegenerated = builder
				.comment(" Amount of limb health regenerated, the most damaged limb first.")
				.defineInRange("Passive Limb Health Regenerated", 0.5, 0, 1000);
		passiveLimbRegenerationTickTimer = builder
				.comment(" How fast in ticks the limbs regenerate passively. 20 ticks = 1s")
				.defineInRange("Passive Limb Regeneration Tick Timer", 200, 0, 10000);
		builder.pop();

		builder.push("proportional-regeneration");
		proportionalLimbRegenMinThreshold = builder
				.comment(" Minimum health threshold (as ratio of max health minus broken hearts) to start regenerating limbs.")
				.defineInRange("Proportional Limb Regen Min Threshold", 0.5, 0, 1);
		proportionalLimbRegenMinHealValue = builder
				.comment(" Minimum limb heal value per tick rate when at min threshold. Spread proportionally across all body parts.")
				.defineInRange("Proportional Limb Regen Min Heal Value", 0.0, 0, 1000);
		proportionalLimbRegenMaxHealValue = builder
				.comment(" Maximum limb heal value per tick rate when at full health. Spread proportionally across all body parts.")
				.defineInRange("Proportional Limb Regen Max Heal Value", 4.0, 0, 1000);
		proportionalLimbRegenTickRate = builder
				.comment(" How often in ticks the proportional regeneration occurs. 20 ticks = 1s")
				.defineInRange("Proportional Limb Regen Tick Rate", 100, 1, 10000);
		proportionalLimbRegenIncreaseRate = builder
				.comment(" Rate at which healing increases from min to max. 1.0 = linear, >1 = exponential.")
				.defineInRange("Proportional Limb Regen Increase Rate", 1.0, 0.1, 10);
		builder.pop();

		builder.comment(" The First Aid Supplies overrides the passive limb regeneration as its effects is meant to be stronger.").push("first-aid-supplies");
		firstAidSuppliesLimbHealthRegenerated = builder
				.comment(" The First Aid Supplies regenerate limb health passively, either by holding it or using Curios mod, the most damaged limb first.")
				.defineInRange("First Aid Supplies Limb Health Regenerated", 0.25, 0, 1000);
		firstAidSuppliesLimbRegenerationMode = builder
				.comment(" How a player's limb health regenerated is defined. Accepted values are as follows:",
						"   SIMPLE - The limb health regenerated is a fixed value defined in First Aid Supplies Limb Health Regenerated.",
						"   PLAYER_DYNAMIC - The limb health regenerated is a percentage value of the player max health using the percentage value defined in First Aid Supplies Limb Health Regenerated.",
						"   LIMB_DYNAMIC - The limb health regenerated is a percentage value of the limb max health using the percentage value defined in First Aid Supplies Limb Health Regenerated.",
						" Any other value will default to SIMPLE.")
				.defineEnum("First Aid Supplies Limb Regeneration Mode", EnumUtil.limbRegenerationMode.LIMB_DYNAMIC);
		firstAidSuppliesHealingOverflow = builder
				.comment(" Whether the exceeded limb health regenerated will heal the next most damaged limb.",
						" Only available for Regeneration Mode SIMPLE or PLAYER_DYNAMIC.")
				.define("First Aid Supplies Healing Overflow", false);
		firstAidSuppliesTickTimer = builder
				.comment(" How fast in ticks the First Aid Supplies will heal limbs. 20 ticks = 1s")
				.defineInRange("First Aid Supplies Tick Timer", 300, 0, 10000);
		firstAidSuppliesExhaustsFood = builder
				.comment(" Whether the First Aid Supplies exhaust food when healing limbs, such as the other healing items.")
				.define("First Aid Supplies Exhausts Food", true);
		firstAidSuppliesBoostedOnEffects = builder
				.comment(" The First Aid Supplies will heal limbs faster when the player is under one of the mentioned effect.")
				.defineListAllowEmpty("First Aid Supplies Boosted On Effects", List.of("minecraft:regeneration", "farmersdelight:comfort"), Config::validateEffectName);
		firstAidSuppliesBoostedTickTimerMultiplier = builder
				.comment(" How much the First Aid Supplies tick timer is multiplied when boosted. ",
						" A value of 1 would deactivate the speed boost. 0.5 makes the heal twice faster.")
				.defineInRange("First Aid Supplies Tick Timer Multiplier", 0.75, 0.1, 1);
		builder.pop();

		builder.push("healing-items");
		recoveryEffectHealingValue = builder
				.comment(" Amount of player health regenerated by the Recovery Effect every effect tick.",
						" For comparison, the Regeneration Effect healing amount is 1.")
				.defineInRange("Recovery Effect Healing Amount", 0.1, 0, 1000);
		healingHerbsUseTime = builder
				.comment(" Item use time in ticks.")
				.defineInRange("Healing Herbs Use Time", 20, 0, 1000);
		plasterUseTime = builder
				.comment(" Item use time in ticks.")
				.defineInRange("Plaster Use Time", 20, 0, 1000);
		bandageUseTime = builder
				.comment(" Item use time in ticks.")
				.defineInRange("Bandage Use Time", 30, 0, 1000);
		tonicUseTime = builder
				.comment(" Item use time in ticks.")
				.defineInRange("Tonic Use Time", 50, 0, 1000);
		medkitUseTime = builder
				.comment(" Item use time in ticks.")
				.defineInRange("Medkit Use Time", 50, 0, 1000);
		builder.pop();

		builder.push("morphine");
		morphineUseTime = builder
				.comment(" Item use time in ticks.")
				.defineInRange("Morphine Use Time", 30, 0, 1000);
		morphinePainkillerTickDuration = builder
				.comment(" Painkiller effect duration in ticks. This effect prevents the player to be affected by broken limbs effects.")
				.defineInRange("Morphine Painkiller Duration", 1800, 0, 10000);
		builder.pop();
		builder.pop();

		builder.push("body-parts-health");
		bodyPartHealthMode = builder
				.comment(" How a player's body part health is determined. Accepted values are as follows:",
						"   SIMPLE - Body parts will have initial fixed values. The body parts health define the health value.",
						"       In this case, if the 'headPartHeath = 10', the head will have '10' health.",
						"   DYNAMIC - Body parts will have dynamic values based on the player's max health. In this case, the body parts health is a multiplier of the player's max health.",
						"       In this case, if the 'headPartHeath = 0.3', the head will have '0.3' * 'player max health' health.",
						" Any other value will default to SIMPLE.")
				.defineEnum("Body Part Health Mode", EnumUtil.bodyPartHealthMode.DYNAMIC);
		headPartHealth = builder.defineInRange("Head Part Health", 0.4d, 0.0d, 1000.0d);
		armsPartHealth = builder.comment(" Both arms will have this health.")
				.defineInRange("Arms Part Health", 0.4d, 0.0d, 1000.0d);
		chestPartHealth = builder.defineInRange("Chest Part Health", 0.6d, 0.0d, 1000.0d);
		legsPartHealth = builder.comment(" Both legs will have this health.")
				.defineInRange("Legs Part Health", 0.6d, 0.0d, 1000.0d);
		feetPartHealth = builder.comment(" Both feet will have this health.")
				.defineInRange("Feet Part Health", 0.4d, 0.0d, 1000.0d);
		builder.pop();

		builder.push("body-parts-effects");
		builder.comment(" Each effect, threshold and amplifier lists must have the same number of items.",
						" The first effect will be triggered with the first amplifier value when the first threshold is reach.")
				.push("head");
		headPartEffects = builder
				.comment(" The list of effects that will be triggered when the head is damaged by the percentage of remaining head health defined in the thresholds.")
				.defineListAllowEmpty("Head Part Effects", List.of(LegendarySurvivalOverhaul.MOD_ID + ":headache"), Config::validateEffectName);
		headPartEffectAmplifiers = builder
				.comment(" The list of amplifiers the effect will have.",
						" 0 means the basic effect, 1 means the effect is amplified once.")
				.defineListAllowEmpty("Head Part Effect Amplifiers", List.of(0), Config::validatePositiveInt);
		headPartEffectThresholds = builder
				.comment(" The list of thresholds for which each effect will be triggered. A threshold is a percentage of remaining head health.",
						" 0 means the head is fully damaged.")
				.defineListAllowEmpty("Head Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
		builder.pop();
		builder.push("arms");
		armsPartEffects = builder.defineListAllowEmpty("Arms Part Effects", List.of("minecraft:mining_fatigue"), Config::validateEffectName);
		armsPartEffectAmplifiers = builder.defineListAllowEmpty("Arms Part Effect Amplifiers", List.of(0), Config::validatePositiveInt);
		armsPartEffectThresholds = builder.defineListAllowEmpty("Arms Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
		bothArmsPartEffects = builder
				.comment(" These effects will be triggered when both arms reach the thresholds.",
						" If a same effect is used with a higher amplifier, the higher prevails (normal Minecraft behaviour).")
				.defineListAllowEmpty("Both Arms Part Effects", List.of("minecraft:weakness"), Config::validateEffectName);
		bothArmsPartEffectAmplifiers = builder.defineListAllowEmpty("Both Arms Part Effect Amplifiers", List.of(0), Config::validatePositiveInt);
		bothArmsPartEffectThresholds = builder.defineListAllowEmpty("Both Arms Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
		builder.pop();
		builder.push("chest");
		chestPartEffects = builder.defineListAllowEmpty("Chest Part Effects", List.of(LegendarySurvivalOverhaul.MOD_ID + ":vulnerability"), Config::validateEffectName);
		chestPartEffectAmplifiers = builder.defineListAllowEmpty("Chest Part Effect Amplifier", List.of(0), Config::validatePositiveInt);
		chestPartEffectThresholds = builder.defineListAllowEmpty("Chest Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
		builder.pop();
		builder.push("legs");
		legsPartEffects = builder.defineListAllowEmpty("Legs Part Effects", List.of(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"), Config::validateEffectName);
		legsPartEffectAmplifiers = builder.defineListAllowEmpty("Legs Part Effect Amplifiers", List.of(0), Config::validatePositiveInt);
		legsPartEffectThresholds = builder.defineListAllowEmpty("Legs Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
		bothLegsPartEffects = builder.defineListAllowEmpty("Both Legs Part Effects", List.of(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"), Config::validateEffectName);
		bothLegsPartEffectAmplifiers = builder.defineListAllowEmpty("Both Legs Part Effect Amplifiers", List.of(1), Config::validatePositiveInt);
		bothLegsPartEffectThresholds = builder.defineListAllowEmpty("Both Legs Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
		builder.pop();
		builder.push("feet");
		feetPartEffects = builder.defineListAllowEmpty("Feet Part Effects", Collections.singletonList("minecraft:slowness"), Config::validateEffectName);
		feetPartEffectAmplifiers = builder.defineListAllowEmpty("Feet Part Effect Amplifiers", Collections.singletonList(0), Config::validatePositiveInt);
		feetPartEffectThresholds = builder.defineListAllowEmpty("Feet Part Effect Thresholds", Collections.singletonList(0.2), Config::validatePercentDouble);
		bothFeetPartEffects = builder.defineListAllowEmpty("Both Feet Part Effects", Collections.singletonList("minecraft:slowness"), Config::validateEffectName);
		bothFeetPartEffectAmplifiers = builder.defineListAllowEmpty("Both Feet Part Effect Amplifiers", Collections.singletonList(1), Config::validatePositiveInt);
		bothFeetPartEffectThresholds = builder.defineListAllowEmpty("Both Feet Part Effect Thresholds", Collections.singletonList(0.2), Config::validatePercentDouble);
		builder.pop();
		builder.pop();

		builder.push("integration");
		builder.push("meds_and_herbs");
		morphineSyringeApplyPainkillerAddiction = builder
				.comment(" Painkiller Addiction effect prevents the abusive usage of Painkiller effect that prevents all negative effects from limbs.",
						" Syringe Morphine share the same configuration as Morphine item")
				.define("Syringe Morphine Apply Painkiller Addiction", true);
		builder.pop();
		builder.pop();
	}
}
