package sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage;

import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.*;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.*;


public class BodyDamageCapability implements IBodyDamageCapability
{
	// Saved data
	private Map<BodyPartEnum, BodyPart> bodyParts;
	private int headacheTimer;
	private int expectedBrokenHearts;

	// Unsaved data
	private int oldExpectedBrokenHearts;
	private int updateTickTimer; // Update immediately first time around
	private float playerMaxHealth;
	private boolean manualDirty;
	private int packetTimer;
	private List<Triple<MalusBodyPartEnum, MobEffect, Integer>> malus;

	public BodyDamageCapability()
	{
		this.init();
	}

	public void init()
	{
		this.updateTickTimer = 20;
		this.headacheTimer = 0;
		this.expectedBrokenHearts = 0;
		this.playerMaxHealth = 0;
		this.manualDirty = false;

		this.bodyParts = new HashMap<>();
		this.malus = new ArrayList<>();

		this.bodyParts.put(BodyPartEnum.HEAD, new BodyPart(BodyPartEnum.HEAD, (float) Config.Baked.headPartHealth));
		this.bodyParts.put(BodyPartEnum.RIGHT_ARM, new BodyPart(BodyPartEnum.RIGHT_ARM, (float) Config.Baked.armsPartHealth));
		this.bodyParts.put(BodyPartEnum.LEFT_ARM, new BodyPart(BodyPartEnum.LEFT_ARM, (float) Config.Baked.armsPartHealth));
		this.bodyParts.put(BodyPartEnum.CHEST, new BodyPart(BodyPartEnum.CHEST, (float) Config.Baked.chestPartHealth));
		this.bodyParts.put(BodyPartEnum.RIGHT_LEG, new BodyPart(BodyPartEnum.RIGHT_LEG, (float) Config.Baked.legsPartHealth));
		this.bodyParts.put(BodyPartEnum.RIGHT_FOOT, new BodyPart(BodyPartEnum.RIGHT_FOOT, (float) Config.Baked.feetPartHealth));
		this.bodyParts.put(BodyPartEnum.LEFT_LEG, new BodyPart(BodyPartEnum.LEFT_LEG, (float) Config.Baked.legsPartHealth));
		this.bodyParts.put(BodyPartEnum.LEFT_FOOT, new BodyPart(BodyPartEnum.LEFT_FOOT, (float) Config.Baked.feetPartHealth));

		if (!Config.Baked.bodyPartHealthMode.equals("DYNAMIC")) {
			for (BodyPart part: this.bodyParts.values()) {
				part.setMaxHealth(part.getHealthMultiplier());
			}
		}
	}

	@Override
	public void setManualDirty() {
		this.manualDirty = true;
	}

	@Override
	public boolean isDirty() {
		for (BodyPart bodyPart: this.bodyParts.values()) {
			if (bodyPart.isDirty())
				return true;
		}
		return manualDirty || (this.expectedBrokenHearts != this.oldExpectedBrokenHearts);
	}

	@Override
	public void setClean() {
		for (BodyPart bodyPart: this.bodyParts.values()) {
			bodyPart.setClean();
		}
		this.manualDirty = false;
		this.oldExpectedBrokenHearts = this.expectedBrokenHearts;
	}

	@Override
	public int getPacketTimer() {
		return this.packetTimer;
	}

	@Override
	public void tickUpdate(Player player, Level level, TickEvent.Phase phase)
	{
		if(phase == TickEvent.Phase.START) {
			this.packetTimer++;
			return;
		};

		if (updateTickTimer++ >= 20) {
			updateTickTimer = 0;
			float playerMaxHealthCheckUpdate = player.getMaxHealth();
			if (Config.Baked.healthOverhaulEnabled) {
				int brokenHearts = (int) (player.getAttributeValue(AttributeRegistry.BROKEN_HEART.get()));
				int minhHearthLimitWithBrokenHearth = (int) player.getAttributeValue(AttributeRegistry.BROKEN_HEART_RESILIENCE.get());
				float additionalHealth = CapabilityUtil.getHealthCapability(player).getAdditionalHealth();
				playerMaxHealthCheckUpdate += Math.min(brokenHearts * 2, 20 + additionalHealth - minhHearthLimitWithBrokenHearth * 2);
			}

			if (Config.Baked.bodyPartHealthMode.equals("DYNAMIC") && playerMaxHealth != playerMaxHealthCheckUpdate) {
				playerMaxHealth = playerMaxHealthCheckUpdate;
				updateDynamicMaxHealth(playerMaxHealth);
			}

			// Refresh all the malus a player should have
			List<Triple<MalusBodyPartEnum, MobEffect, Integer>> newMalus = new ArrayList<>();
			for (MalusBodyPartEnum malusBodyPart: MalusBodyPartEnum.values()) {
				List<Pair<MobEffect, Integer>> malusEffects = new ArrayList<>();
				if (!player.hasEffect(MobEffectRegistry.PAINKILLER.get()))
					malusEffects = BodyDamageUtil.getEffects(malusBodyPart, getHealthRatioForMalusBodyPart(malusBodyPart));
				for (Triple<MalusBodyPartEnum, MobEffect, Integer> bodyPartMalusEffect: this.malus) {
					if (bodyPartMalusEffect.getLeft() == malusBodyPart) {
						Pair<MobEffect, Integer> oldEffect = Pair.of(bodyPartMalusEffect.getMiddle(), bodyPartMalusEffect.getRight());
						if (!malusEffects.contains(oldEffect)) {
							player.removeEffect(oldEffect.getLeft());
							if (oldEffect.getLeft() == MobEffectRegistry.HEADACHE.get())
								player.removeEffect(MobEffects.BLINDNESS);
						}
					}
				}
				for (Pair<MobEffect, Integer> malusEffect: malusEffects) {
					newMalus.add(Triple.of(malusBodyPart, malusEffect.getLeft(), malusEffect.getRight()));
				}
			}

			this.malus = newMalus;

			// Assign all malus effect to the player
			for (Triple<MalusBodyPartEnum, MobEffect, Integer> malusEffect: this.malus) {
				if (!player.hasEffect(malusEffect.getMiddle()))
					player.addEffect(new MobEffectInstance(malusEffect.getMiddle(), -1, malusEffect.getRight(), false, false, true));
			}

			// Heal each body limb of the player
			int expectedBrokenHearts = 0;
			for (Map.Entry<BodyPartEnum, BodyPart> bodyPartPair: this.bodyParts.entrySet()) {
				BodyPart bodyPart = bodyPartPair.getValue();
				if (bodyPart.getRemainingHealingTicks() > 0) {
					int healingTick = Math.min(20, bodyPart.getRemainingHealingTicks());
					float healingValue = healingTick * bodyPart.getHealingPerTicks();
					this.heal(bodyPartPair.getKey(), healingValue);
					if (Config.Baked.bodyHealingFoodExhaustion > 0 && player.getFoodData().getFoodLevel() > Config.Baked.minFoodOnBodyHealing) {
						player.getFoodData().addExhaustion((float) (healingValue * Config.Baked.bodyHealingFoodExhaustion));
					}
					if (bodyPart.isMaxHealth())
						bodyPart.reduceRemainingHealingTicks(bodyPart.getRemainingHealingTicks());
					else
						bodyPart.reduceRemainingHealingTicks(healingTick);
				}

				if (Config.Baked.healthOverhaulEnabled && bodyPart.getDamage() == bodyPart.getMaxHealth()) {
					expectedBrokenHearts += Config.Baked.brokenHeartsPerInjuredLimb;
				}
			}

			this.expectedBrokenHearts = expectedBrokenHearts;
		}

		if (player.hasEffect(MobEffectRegistry.HEADACHE.get())) {
			if (this.headacheTimer-- < 0) {
				applyHeadache(player, Objects.requireNonNull(player.getEffect(MobEffectRegistry.HEADACHE.get())).getAmplifier());
			}
		} else {
			this.headacheTimer = 0;
		}
	}

	private void applyHeadache(Player player, int amplifier) {
		player.level().playLocalSound(player.blockPosition(), SoundRegistry.HEADACHE_HEARTBEAT.get(), SoundSource.PLAYERS, 1.f, 1.0f, false);

		int blindnessTime = (40 + player.getRandom().nextInt(100)) * Math.min(amplifier + 1, 4);
		this.headacheTimer = blindnessTime + Math.round((float) (200 + player.getRandom().nextInt(400)) / (float) Math.min(amplifier + 1, 4));
		player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, blindnessTime, 0, false, false, true));
	}

	@Override
	public boolean isWoundedBelow(float healthPercent) {
		for (BodyPart part: this.bodyParts.values()) {
			if (getBodyPartHealthRatio(part.getBodyPartEnum()) < healthPercent)
				return true;
		}
		return false;
	}

	@Override
	public int getExpectedBrokenHearts() {
		return this.expectedBrokenHearts;
	}

	@Override
	public float getBodyPartDamage(BodyPartEnum part) {
		return this.bodyParts.get(part).getDamage();
	}

	@Override
	public float getBodyPartMaxHealth(BodyPartEnum part) {
		return this.bodyParts.get(part).getMaxHealth();
	}

	@Override
	public void setBodyPartDamage(BodyPartEnum part, float damageValue) {
		this.bodyParts.get(part).setDamage(damageValue);
	}

	@Override
	public void setBodyPartMaxHealth(BodyPartEnum part, float maxHealthValue) {
		this.bodyParts.get(part).setMaxHealth(maxHealthValue);
	}

	@Override
	public void heal(BodyPartEnum part, float healingValue) {
		this.bodyParts.get(part).heal(healingValue);
	}

	@Override
	public void hurt(BodyPartEnum part, float damageValue) {
		this.bodyParts.get(part).hurt(damageValue);
	}

	@Override
	public void applyHealingTime(BodyPartEnum part, int healingTicks, float healingPerTick) {
		this.bodyParts.get(part).setHealing(healingTicks, healingPerTick);
	}

	@Override
	public float getBodyPartHealthRatio(BodyPartEnum part) {
		BodyPart bodyPart = this.bodyParts.get(part);
		return MathUtil.round((bodyPart.getMaxHealth() - bodyPart.getDamage()) / bodyPart.getMaxHealth(), 2);
	}

	@Override
	public int getRemainingHealingTicks(BodyPartEnum part) {
		return this.bodyParts.get(part).getRemainingHealingTicks();
	}

	@Override
	public float getHealingPerTicks(BodyPartEnum part) {
		return this.bodyParts.get(part).getHealingPerTicks();
	}

	@Override
	public float getHealthRatioForMalusBodyPart(MalusBodyPartEnum part) {
        return switch (part) {
            case HEAD -> this.getBodyPartHealthRatio(BodyPartEnum.HEAD);
            case ARMS ->
                    Math.min(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_ARM), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_ARM));
            case BOTH_ARMS ->
                    Math.max(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_ARM), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_ARM));
            case CHEST -> this.getBodyPartHealthRatio(BodyPartEnum.CHEST);
            case LEGS ->
                    Math.min(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_LEG), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_LEG));
            case BOTH_LEGS ->
                    Math.max(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_LEG), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_LEG));
            case FEET ->
                    Math.min(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_FOOT), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_FOOT));
            case BOTH_FEET ->
                    Math.max(this.getBodyPartHealthRatio(BodyPartEnum.RIGHT_FOOT), this.getBodyPartHealthRatio(BodyPartEnum.LEFT_FOOT));
        };
	}

	private void updateDynamicMaxHealth(float maxHealth) {
		for (BodyPart bodyPart: this.bodyParts.values()) {
			bodyPart.setMaxHealth(Math.round(bodyPart.getHealthMultiplier() * maxHealth * 100) / 100.0f);
		}
	}

	public CompoundTag writeNBT()
	{
		CompoundTag compound = new CompoundTag();
		for (BodyPart bodyPart: this.bodyParts.values()) {
			compound = bodyPart.writeNbt(compound);
		}
		compound.putInt("headacheTimer", this.headacheTimer);
		compound.putInt("expectedBrokenHearts", this.expectedBrokenHearts);

		return compound;
	}

	public void readNBT(CompoundTag compound)
	{
		this.init();
		for (BodyPart bodyPart: this.bodyParts.values()) {
			bodyPart.readNBT(compound);
		}

		this.headacheTimer = compound.getInt("headacheTimer");
		this.expectedBrokenHearts = compound.getInt("expectedBrokenHearts");
	}
}
