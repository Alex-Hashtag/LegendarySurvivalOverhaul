package sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.MalusBodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Attachment that handles body damage for a player.
 */
public class BodyDamageAttachment implements IBodyDamageAttachment, INBTSerializable<CompoundTag>
{
    // Saved data
    private Map<BodyPartEnum, BodyPart> bodyParts;
    private int headacheTimer;
    private int expectedBrokenHearts;
    private float healingTickTimer;
    private float proportionalRegenTimer;
    private float customHealthRegenTimer;

    // Unsaved data
    private MobEffectInstance headacheEffect;
    private boolean hasFirstAidSupplies;
    private boolean hasFirstAidSuppliesBoosted;
    private MobEffectInstance passiveLimbRegenerationEffects;
    private boolean passiveLimbRegenerationEnabled;
    private int oldExpectedBrokenHearts;
    private int updateTickTimer; // Update immediately first time around
    private float playerMaxHealth;
    private boolean manualDirty;
    private int packetTimer;
    private Map<MobEffect, Integer> malus;
    private int healthBlinkTimer;

    public BodyDamageAttachment()
    {
        this.init();
    }

    public void init()
    {
        this.headacheEffect = null;
        this.hasFirstAidSupplies = false;
        this.hasFirstAidSuppliesBoosted = false;
        this.passiveLimbRegenerationEffects = null;
        this.updateTickTimer = 20;
        this.headacheTimer = 0;
        this.expectedBrokenHearts = 0;
        this.playerMaxHealth = 0;
        this.manualDirty = false;
        this.healingTickTimer = 0;
        this.proportionalRegenTimer = 0;
        this.customHealthRegenTimer = 0;
        this.healthBlinkTimer = 0;

        this.bodyParts = new HashMap<>();
        this.malus = new HashMap<>();

        this.bodyParts.put(BodyPartEnum.HEAD, new BodyPart(BodyPartEnum.HEAD, (float) Config.Baked.headPartHealth));
        this.bodyParts.put(BodyPartEnum.RIGHT_ARM, new BodyPart(BodyPartEnum.RIGHT_ARM, (float) Config.Baked.armsPartHealth));
        this.bodyParts.put(BodyPartEnum.LEFT_ARM, new BodyPart(BodyPartEnum.LEFT_ARM, (float) Config.Baked.armsPartHealth));
        this.bodyParts.put(BodyPartEnum.CHEST, new BodyPart(BodyPartEnum.CHEST, (float) Config.Baked.chestPartHealth));
        this.bodyParts.put(BodyPartEnum.RIGHT_LEG, new BodyPart(BodyPartEnum.RIGHT_LEG, (float) Config.Baked.legsPartHealth));
        this.bodyParts.put(BodyPartEnum.RIGHT_FOOT, new BodyPart(BodyPartEnum.RIGHT_FOOT, (float) Config.Baked.feetPartHealth));
        this.bodyParts.put(BodyPartEnum.LEFT_LEG, new BodyPart(BodyPartEnum.LEFT_LEG, (float) Config.Baked.legsPartHealth));
        this.bodyParts.put(BodyPartEnum.LEFT_FOOT, new BodyPart(BodyPartEnum.LEFT_FOOT, (float) Config.Baked.feetPartHealth));

        // Initialize maxHealth for STATIC mode only
        // DYNAMIC mode will initialize on first tick when player max health is known
        if (Config.Baked.bodyPartHealthMode != EnumUtil.bodyPartHealthMode.DYNAMIC)
        {
            for (BodyPart part : this.bodyParts.values())
            {
                part.setMaxHealth(part.getHealthMultiplier());
            }
        }
    }

    @Override
    public void setManualDirty()
    {
        this.manualDirty = true;
    }

    @Override
    public boolean isDirty()
    {
        for (BodyPart bodyPart : this.bodyParts.values())
        {
            if (bodyPart.isDirty())
                return true;
        }
        return manualDirty || (this.expectedBrokenHearts != this.oldExpectedBrokenHearts);
    }

    @Override
    public void setClean()
    {
        for (BodyPart bodyPart : this.bodyParts.values())
        {
            bodyPart.setClean();
        }
        this.manualDirty = false;
        this.oldExpectedBrokenHearts = this.expectedBrokenHearts;
    }

    @Override
    public int getPacketTimer()
    {
        return this.packetTimer;
    }
    
    @Override
    public int getHealthBlinkTimer()
    {
        return this.healthBlinkTimer;
    }

    @Override
    public void tickUpdate(Player player, Level level, boolean isStart)
    {
        if (isStart)
        {
            this.packetTimer++;
            return;
        }

        if (updateTickTimer++ >= 19)
        {
            updateTickTimer = 0;
            double playerMaxHealthCheckUpdate = HealthUtil.getPlayerStableMaxHealth(player);

            if (Config.Baked.bodyPartHealthMode == EnumUtil.bodyPartHealthMode.DYNAMIC &&
                    playerMaxHealth != playerMaxHealthCheckUpdate &&
                    playerMaxHealthCheckUpdate > 0)  // Don't update if player health is invalid
            {
                playerMaxHealth = (float) playerMaxHealthCheckUpdate;
                updateBodyPartDynamicMaxHealth(playerMaxHealth);
            }

            // Refresh all the malus a player should have
            Map<MobEffect, Integer> newMalus = new HashMap<>();
            for (MalusBodyPartEnum malusBodyPart : MalusBodyPartEnum.values())
            {
                List<Pair<MobEffect, Integer>> malusEffects = new ArrayList<>();
                if (!player.hasEffect(MobEffectRegistry.PAINKILLER))
                    malusEffects = BodyDamageUtil.getEffects(malusBodyPart, getHealthRatioForMalusBodyPart(malusBodyPart));

                for (Pair<MobEffect, Integer> malusEffect : malusEffects)
                {
                    Integer alreadyAppliedEffectAmplifier = newMalus.get(malusEffect.getLeft());
                    if (alreadyAppliedEffectAmplifier == null || alreadyAppliedEffectAmplifier < malusEffect.getRight())
                        newMalus.put(malusEffect.getLeft(), malusEffect.getRight());
                }
            }

            // Clean old effects that shouldn't be applied anymore
            for (Map.Entry<MobEffect, Integer> bodyPartMalusEffect : this.malus.entrySet())
            {
                MobEffect oldEffect = bodyPartMalusEffect.getKey();
                Holder<MobEffect> effectHolder = net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(oldEffect);
                MobEffectInstance playerOldEffect = player.getEffect(effectHolder);
                if (playerOldEffect != null && (!newMalus.containsKey(oldEffect) || playerOldEffect.getAmplifier() > bodyPartMalusEffect.getValue()))
                {
                    player.removeEffect(effectHolder);
                    if (oldEffect == MobEffectRegistry.HEADACHE.get())
                        player.removeEffect(MobEffects.BLINDNESS);
                }
            }

            this.malus = newMalus;

            // Assign all malus effect to the player
            for (Map.Entry<MobEffect, Integer> malusEffect : this.malus.entrySet())
            {
                Holder<MobEffect> effectHolder = net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(malusEffect.getKey());
                player.addEffect(new MobEffectInstance(effectHolder, -1, malusEffect.getValue(), false, false, true));
            }

            // Heal each body limb of the player
            for (Map.Entry<BodyPartEnum, BodyPart> bodyPartPair : this.bodyParts.entrySet())
            {
                BodyPart bodyPart = bodyPartPair.getValue();
                if (bodyPart.getRemainingHealingTicks() > 0)
                {
                    LegendarySurvivalOverhaul.LOGGER.info("[ITEM HEALING] {} has {} healing ticks remaining, healing {} per tick", 
                        bodyPartPair.getKey(), bodyPart.getRemainingHealingTicks(), bodyPart.getHealingPerTicks());
                    int healingTick = Math.min(20, bodyPart.getRemainingHealingTicks());
                    healWithFoodExhaustion(player, bodyPartPair.getKey(), healingTick * bodyPart.getHealingPerTicks());
                    if (bodyPart.isMaxHealth())
                        bodyPart.reduceRemainingHealingTicks(bodyPart.getRemainingHealingTicks());
                    else
                        bodyPart.reduceRemainingHealingTicks(healingTick);
                }
            }
            // Always refresh broken hearts each second to keep HUD in sync (and after potential limb state changes)
            updateBrokenHearts(player);
        }

        if (updateTickTimer % 10 == 0 && updateTickTimer != 0)
        {
            this.headacheEffect = player.getEffect(MobEffectRegistry.HEADACHE);
            this.hasFirstAidSupplies = CuriosUtil.isCurioItemEquipped(player, ItemRegistry.FIRST_AID_SUPPLIES.get());
            if (hasFirstAidSupplies)
            {
                this.hasFirstAidSuppliesBoosted = BodyDamageUtil.hasPlayerFirstAidSuppliesBoostingEffect(player);
            } else
            {
                this.passiveLimbRegenerationEffects = BodyDamageUtil.getPlayerPassiveLimbRegenerationEffect(player);
                this.passiveLimbRegenerationEnabled = this.passiveLimbRegenerationEffects != null;
            }
        }

        if (this.headacheEffect != null)
        {
            if (this.headacheTimer-- < 0)
            {
                applyHeadache(player, this.headacheEffect.getAmplifier());
            }
        } else
        {
            this.headacheTimer = 0;
        }

        if (this.hasFirstAidSupplies)
        {
            boolean boostedHealingTickTimer = false;
            if (Config.Baked.firstAidSuppliesBoostedTickTimerMultiplier < 1)
            {
                boostedHealingTickTimer = this.hasFirstAidSuppliesBoosted;
            }
            healingTickTimer += boostedHealingTickTimer ? MathUtil.round((float) (1 / Config.Baked.firstAidSuppliesBoostedTickTimerMultiplier), 2) : 1;
            if (healingTickTimer >= Config.Baked.firstAidSuppliesTickTimer)
            {
                healingTickTimer = 0;
                LegendarySurvivalOverhaul.LOGGER.info("[FIRST AID HEALING] Healing most damaged limb");

                healMostDamaged(player,
                        Config.Baked.limbRegenerationMode,
                        (float) Config.Baked.firstAidSuppliesLimbHealthRegenerated,
                        Config.Baked.firstAidSuppliesHealingOverflow,
                        Config.Baked.firstAidSuppliesExhaustsFood);
                updateBrokenHearts(player);
            }
        } else if (this.passiveLimbRegenerationEnabled)
        {
            int passiveTickTimer = Config.Baked.passiveLimbRegenerationTickTimer;
            if (this.passiveLimbRegenerationEffects != null && Config.Baked.passiveLimbRegenerationAmplificationEnabled)
                passiveTickTimer = Config.Baked.passiveLimbRegenerationTickTimer >> this.passiveLimbRegenerationEffects.getAmplifier();

            if (healingTickTimer++ >= passiveTickTimer)
            {
                healingTickTimer = 0;
                LegendarySurvivalOverhaul.LOGGER.info("[PASSIVE REGEN HEALING] Healing most damaged limb");

                healMostDamaged(player,
                        EnumUtil.limbRegenerationMode.SIMPLE,
                        (float) Config.Baked.passiveLimbHealthRegenerated,
                        true,
                        true);
                updateBrokenHearts(player);
            }
        } else
        {
            healingTickTimer = 0;
        }
        
        // Proportional limb regeneration system
        if (Config.Baked.proportionalLimbRegenTickRate > 0 && !this.hasFirstAidSupplies)
        {
            if (proportionalRegenTimer++ >= Config.Baked.proportionalLimbRegenTickRate)
            {
                proportionalRegenTimer = 0;
                applyProportionalLimbRegeneration(player);
            }
        } else
        {
            proportionalRegenTimer = 0;
        }
        
        // Custom health regeneration when natural regen is off
        if (!Config.Baked.naturalRegenerationEnabled && Config.Baked.customHealthRegenEnabled)
        {
            if (customHealthRegenTimer++ >= Config.Baked.customHealthRegenTickRate)
            {
                customHealthRegenTimer = 0;
                applyCustomHealthRegeneration(player);
            }
        } else
        {
            customHealthRegenTimer = 0;
        }
        
        // Decrement health blink timer
        if (this.healthBlinkTimer > 0)
        {
            this.healthBlinkTimer--;
        }
    }

    private void applyHeadache(Player player, int amplifier)
    {
        player.level().playLocalSound(player.blockPosition(), SoundRegistry.HEADACHE_HEARTBEAT.get(), SoundSource.PLAYERS, 1.f, 1.0f, false);

        int blindnessTime = (40 + player.getRandom().nextInt(100)) * Math.min(amplifier + 1, 4);
        this.headacheTimer = blindnessTime + Math.round((float) (200 + player.getRandom().nextInt(400)) / (float) Math.min(amplifier + 1, 4));
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, blindnessTime, 0, false, false, true));
    }

    private void healMostDamaged(Player player, EnumUtil.limbRegenerationMode healingMode, float healingValue, boolean overflow, boolean exhaustFood)
    {
        BodyPart mostDamaged = getLowestHealthBodyPart();
        float damage = mostDamaged.getDamage();

        float actualHealingValue;
        if (healingMode == EnumUtil.limbRegenerationMode.PLAYER_DYNAMIC)
        {
            actualHealingValue = MathUtil.ceil(healingValue * player.getMaxHealth(), 2);
        } else if (healingMode == EnumUtil.limbRegenerationMode.LIMB_DYNAMIC)
        {
            actualHealingValue = MathUtil.ceil(healingValue * mostDamaged.getMaxHealth(), 2);
        } else
        {
            actualHealingValue = healingValue;
        }

        BiConsumer<BodyPartEnum, Float> healingFunction = (part, amount) ->
        {
            if (exhaustFood)
            {
                healWithFoodExhaustion(player, part, amount);
            } else
            {
                heal(part, amount);
            }
        };

        healingFunction.accept(mostDamaged.getBodyPartEnum(), actualHealingValue);
        float overflowHealing = actualHealingValue - damage;

        if (overflow && overflowHealing > 0.0f && healingMode != EnumUtil.limbRegenerationMode.LIMB_DYNAMIC)
        {
            mostDamaged = getLowestHealthBodyPart();
            healingFunction.accept(mostDamaged.getBodyPartEnum(), overflowHealing);
        }
    }

    private BodyPart getLowestHealthBodyPart()
    {
        return bodyParts.entrySet()
                .stream()
                .min((entry1, entry2) -> Float.compare(getBodyPartHealthRatio(entry1.getKey()), getBodyPartHealthRatio(entry2.getKey())))
                .map(Map.Entry::getValue)
                .orElse(null); // or throw an exception if you prefer
    }
    
    private void applyProportionalLimbRegeneration(Player player)
    {
        double stableMaxHealth = HealthUtil.getPlayerStableMaxHealth(player);
        double currentHealth = player.getHealth();
        double effectiveBrokenHearts = HealthUtil.getEffectiveBrokenHearts(player) * 2.0;
        double adjustedMaxHealth = stableMaxHealth - effectiveBrokenHearts;
        
        if (adjustedMaxHealth <= 0) return;
        
        double healthRatio = currentHealth / adjustedMaxHealth;
        healthRatio = Math.max(0, Math.min(1, healthRatio));
        
        if (healthRatio < Config.Baked.proportionalLimbRegenMinThreshold)
        {
            return;
        }
        
        double normalizedRatio = (healthRatio - Config.Baked.proportionalLimbRegenMinThreshold) / (1.0 - Config.Baked.proportionalLimbRegenMinThreshold);
        normalizedRatio = Math.pow(normalizedRatio, Config.Baked.proportionalLimbRegenIncreaseRate);
        
        double healAmount = Config.Baked.proportionalLimbRegenMinHealValue + 
                (Config.Baked.proportionalLimbRegenMaxHealValue - Config.Baked.proportionalLimbRegenMinHealValue) * normalizedRatio;
        
        if (healAmount <= 0) return;
        
        float totalDamage = 0;
        for (BodyPart bodyPart : this.bodyParts.values())
        {
            if (bodyPart.getDamage() < bodyPart.getMaxHealth())
            {
                totalDamage += bodyPart.getDamage();
            }
        }
        
        if (totalDamage <= 0) return;
        
        boolean healedAtMaxHealth = currentHealth >= stableMaxHealth - 0.01;
        
        if (healedAtMaxHealth)
        {
            player.level().playSound(null, player.blockPosition(), SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 0.3f, 1.5f);
            this.healthBlinkTimer = 40; // Blink for 2 seconds
            this.setManualDirty(); // Force packet sync
        }
        
        for (Map.Entry<BodyPartEnum, BodyPart> entry : this.bodyParts.entrySet())
        {
            BodyPart bodyPart = entry.getValue();
            float damage = bodyPart.getDamage();
            
            if (damage >= bodyPart.getMaxHealth())
            {
                continue;
            }
            
            float proportionalHeal = (float) ((damage / totalDamage) * healAmount);
            healWithFoodExhaustion(player, entry.getKey(), proportionalHeal);
        }
    }
    
    private void applyCustomHealthRegeneration(Player player)
    {
        if (player.getFoodData().getFoodLevel() <= 0) return;
        
        double totalLimbHealthRatio = 0;
        for (BodyPart bodyPart : this.bodyParts.values())
        {
            totalLimbHealthRatio += (bodyPart.getMaxHealth() - bodyPart.getDamage()) / bodyPart.getMaxHealth();
        }
        totalLimbHealthRatio /= this.bodyParts.size();
        
        double maxHealthToRegen = player.getMaxHealth() * totalLimbHealthRatio;
        
        if (player.getHealth() >= maxHealthToRegen - 0.01) return;
        
        float healthToHeal = (float) Math.min(Config.Baked.customHealthRegenRate, maxHealthToRegen - player.getHealth());
        
        if (healthToHeal > 0)
        {
            player.heal(healthToHeal);
            player.getFoodData().addExhaustion((float) (healthToHeal * Config.Baked.customHealthRegenFoodExhaustion));
        }
    }

    @Override
    public void updateBrokenHearts(Player player)
    {
        if (Config.Baked.brokenHeartsPerInjuredLimb == 0)
            return;

        double expectedBrokenHearts = 0;
        int brokenLimbCount = 0;
        for (Map.Entry<BodyPartEnum, BodyPart> bodyPartPair : this.bodyParts.entrySet())
        {
            BodyPart bodyPart = bodyPartPair.getValue();
            if (Config.Baked.healthOverhaulEnabled && bodyPart.getDamage() >= bodyPart.getMaxHealth())
            {
                brokenLimbCount++;
                expectedBrokenHearts += switch (Config.Baked.brokenHeartsPerInjuredLimbMode)
                {
                    case PLAYER_DYNAMIC:
                        yield Config.Baked.brokenHeartsPerInjuredLimb * this.playerMaxHealth / 2.0;
                    case LIMB_DYNAMIC:
                        yield Config.Baked.brokenHeartsPerInjuredLimb * bodyPart.getMaxHealth() / 2.0;
                    default:
                        yield Config.Baked.brokenHeartsPerInjuredLimb / 2.0;
                };
            }
        }

        this.expectedBrokenHearts = (int) expectedBrokenHearts;
        BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
    }

    @Override
    public boolean isWoundedBelow(float healthPercent)
    {
        for (BodyPart part : this.bodyParts.values())
        {
            if (getBodyPartHealthRatio(part.getBodyPartEnum()) < healthPercent)
                return true;
        }
        return false;
    }

    @Override
    public int getExpectedBrokenHearts()
    {
        return this.expectedBrokenHearts;
    }

    @Override
    public float getBodyPartDamage(BodyPartEnum part)
    {
        return this.bodyParts.get(part).getDamage();
    }

    @Override
    public float getBodyPartMaxHealth(BodyPartEnum part)
    {
        return this.bodyParts.get(part).getMaxHealth();
    }

    @Override
    public void setBodyPartDamage(BodyPartEnum part, float damageValue)
    {
        this.bodyParts.get(part).setDamage(damageValue);
    }

    @Override
    public void setBodyPartMaxHealth(BodyPartEnum part, float maxHealthValue)
    {
        this.bodyParts.get(part).setMaxHealth(maxHealthValue);
    }

    @Override
    public void healWithFoodExhaustion(Player player, BodyPartEnum part, float healingValue)
    {
        float exceedHeal = Math.max(healingValue - this.bodyParts.get(part).getDamage(), 0);
        this.heal(part, healingValue);
        if (Config.Baked.bodyHealingFoodExhaustion > 0 && player.getFoodData().getFoodLevel() > Config.Baked.minFoodOnBodyHealing)
        {
            player.getFoodData().addExhaustion((float) ((healingValue - exceedHeal) * Config.Baked.bodyHealingFoodExhaustion));
        }
    }

    @Override
    public void heal(BodyPartEnum part, float healingValue)
    {
        this.bodyParts.get(part).heal(healingValue);
    }

    @Override
    public void hurt(BodyPartEnum part, float damageValue)
    {
        this.bodyParts.get(part).hurt(damageValue);
    }

    @Override
    public void applyHealingTime(BodyPartEnum part, int healingTicks, float healingPerTick)
    {
        this.bodyParts.get(part).setHealing(healingTicks, healingPerTick);
    }

    @Override
    public float getBodyPartHealthRatio(BodyPartEnum part)
    {
        BodyPart bodyPart = this.bodyParts.get(part);
        float maxHealth = bodyPart.getMaxHealth();
        float damage = bodyPart.getDamage();

        // Safety check: if maxHealth is 0 (uninitialized), assume healthy
        if (maxHealth <= 0)
        {
            return 1.0f;
        }

        float ratio = MathUtil.round((maxHealth - damage) / maxHealth, 2);


        return ratio;
    }

    @Override
    public int getRemainingHealingTicks(BodyPartEnum part)
    {
        return this.bodyParts.get(part).getRemainingHealingTicks();
    }

    @Override
    public float getHealingPerTicks(BodyPartEnum part)
    {
        return this.bodyParts.get(part).getHealingPerTicks();
    }

    @Override
    public float getHealthRatioForMalusBodyPart(MalusBodyPartEnum part)
    {
        return switch (part)
        {
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

    private void updateBodyPartDynamicMaxHealth(float maxHealth)
    {
        if (maxHealth <= 0) return;  // Safety check: don't update with invalid health values

        for (BodyPart bodyPart : this.bodyParts.values())
        {
            float newMaxHealth = Math.round(bodyPart.getHealthMultiplier() * maxHealth * 100) / 100.0f;
            float oldMaxHealth = bodyPart.getMaxHealth();
            float oldDamage = bodyPart.getDamage();
            
            bodyPart.setMaxHealth(newMaxHealth);
            if (oldMaxHealth != 0 && oldMaxHealth != newMaxHealth)
            {
                // If limb is fully broken (damage >= maxHealth), keep it fully broken
                if (oldDamage >= oldMaxHealth)
                {
                    bodyPart.setDamage(newMaxHealth);
                }
                else
                {
                    // Scale damage proportionally to maintain the same health ratio
                    float damageRatio = oldDamage / oldMaxHealth;
                    float newDamage = newMaxHealth * damageRatio;
                    bodyPart.setDamage(newDamage);
                }
            }
        }
    }

    public CompoundTag writeNBT()
    {
        CompoundTag compound = new CompoundTag();
        for (BodyPart bodyPart : this.bodyParts.values())
        {
            compound = bodyPart.writeNbt(compound);
        }
        compound.putInt("headacheTimer", this.headacheTimer);
        compound.putInt("expectedBrokenHearts", this.expectedBrokenHearts);
        compound.putFloat("healingTickTimer", this.healingTickTimer);
        compound.putFloat("proportionalRegenTimer", this.proportionalRegenTimer);
        compound.putFloat("customHealthRegenTimer", this.customHealthRegenTimer);

        return compound;
    }

    public void readNBT(CompoundTag compound)
    {
        this.init();
        for (BodyPart bodyPart : this.bodyParts.values())
        {
            bodyPart.readNBT(compound);
        }

        this.headacheTimer = compound.getInt("headacheTimer");
        this.expectedBrokenHearts = compound.getInt("expectedBrokenHearts");
        this.healingTickTimer = compound.getFloat("healingTickTimer");
        this.proportionalRegenTimer = compound.contains("proportionalRegenTimer") ? compound.getFloat("proportionalRegenTimer") : 0;
        this.customHealthRegenTimer = compound.contains("customHealthRegenTimer") ? compound.getFloat("customHealthRegenTimer") : 0;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        return writeNBT();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
    {
        readNBT(nbt);
    }
}

