package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.*;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.client.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

import java.util.*;
import java.util.stream.Collectors;

public class BodyDamageUtilInternal implements IBodyDamageUtil
{
    public static final UUID BROKEN_HEART_ATTRIBUTE_UUID = UUID.fromString("2e3cede5-3c18-45c2-8a46-31b89fb9c027");
    public static final AttributeBuilder BODY_RESISTANCE = new AttributeBuilder(AttributeRegistry.BODY_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "body_resistance"));
    public static final AttributeBuilder HEAD_RESISTANCE = new AttributeBuilder(AttributeRegistry.HEAD_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "head_temperature"));
    public static final AttributeBuilder CHEST_RESISTANCE = new AttributeBuilder(AttributeRegistry.CHEST_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "chest_temperature"));
    public static final AttributeBuilder RIGHT_ARM_RESISTANCE = new AttributeBuilder(AttributeRegistry.RIGHT_ARM_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "right_arm_resistance"));
    public static final AttributeBuilder LEFT_ARM_RESISTANCE = new AttributeBuilder(AttributeRegistry.LEFT_ARM_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "left_arm_resistance"));
    public static final AttributeBuilder LEGS_RESISTANCE = new AttributeBuilder(AttributeRegistry.LEGS_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "legs_resistance"));
    public static final AttributeBuilder FEET_RESISTANCE = new AttributeBuilder(AttributeRegistry.FEET_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "feet_resistance"));
    public static final Map<BodyPartEnum, AttributeBuilder> bodyPartResistanceAttribute = new HashMap<>();
    public static final Map<EquipmentSlot, UUID> equipmentSlotBodyResistanceUuid = new HashMap<>();
    private static final List<Holder<MobEffect>> firstAidSuppliesBoostingEffects = new ArrayList<>();
    private static final List<Holder<MobEffect>> passiveLimbRegenerationEffects = new ArrayList<>();
    private static final Map<MalusBodyPartEnum, Map<Float, Pair<MobEffect, Integer>>> bodyPartMalusEffects = new HashMap<>();

    static
    {
        bodyPartResistanceAttribute.put(BodyPartEnum.HEAD, HEAD_RESISTANCE);
        bodyPartResistanceAttribute.put(BodyPartEnum.CHEST, CHEST_RESISTANCE);
        bodyPartResistanceAttribute.put(BodyPartEnum.RIGHT_ARM, RIGHT_ARM_RESISTANCE);
        bodyPartResistanceAttribute.put(BodyPartEnum.LEFT_ARM, LEFT_ARM_RESISTANCE);
        bodyPartResistanceAttribute.put(BodyPartEnum.RIGHT_LEG, LEGS_RESISTANCE);
        bodyPartResistanceAttribute.put(BodyPartEnum.LEFT_LEG, LEGS_RESISTANCE);
        bodyPartResistanceAttribute.put(BodyPartEnum.RIGHT_FOOT, FEET_RESISTANCE);
        bodyPartResistanceAttribute.put(BodyPartEnum.LEFT_FOOT, FEET_RESISTANCE);

        equipmentSlotBodyResistanceUuid.put(EquipmentSlot.HEAD, UUID.fromString("e1d9eda1-f904-4ddd-8a9f-720fa592117e"));
        equipmentSlotBodyResistanceUuid.put(EquipmentSlot.CHEST, UUID.fromString("0b743835-b4aa-4c08-aa13-f3646730a749"));
        equipmentSlotBodyResistanceUuid.put(EquipmentSlot.MAINHAND, UUID.fromString("c18151f4-cb03-4df9-994d-e4decbb60b6e"));
        equipmentSlotBodyResistanceUuid.put(EquipmentSlot.OFFHAND, UUID.fromString("0fba64c7-8863-4a17-9458-7bbce1518bf0"));
        equipmentSlotBodyResistanceUuid.put(EquipmentSlot.LEGS, UUID.fromString("e024cc3d-cd24-4bcf-b7a8-f2272e44ac2d"));
        equipmentSlotBodyResistanceUuid.put(EquipmentSlot.FEET, UUID.fromString("53523212-2ebc-4f6b-9044-7f9a1fa96852"));
    }

    public BodyDamageUtilInternal()
    {
    }

    public static void initMalusConfig()
    {
        for (MalusBodyPartEnum malus : MalusBodyPartEnum.values())
        {
            Map<Float, Pair<MobEffect, Integer>> malusEffects = new HashMap<>();
            if (malus.effects.size() != malus.amplifiers.size() || malus.effects.size() != malus.thresholds.size())
            {
                LegendarySurvivalOverhaul.LOGGER.debug("{} effects, amplifiers and thresholds elements number doesn't match. The last elements won't be used.", malus.name());
            }

            for (int i = 0; i < malus.effects.size(); i++)
            {
                MobEffect malusEffect = BuiltInRegistries.MOB_EFFECT
                        .getOptional(ResourceLocation.parse(malus.effects.get(i)))
                        .orElse(null);
                int malusAmplifier;
                float malusThreshold;
                if (malusEffect == null)
                {
                    LegendarySurvivalOverhaul.LOGGER.debug("Unknown effect {} for {}", malus.effects.get(i), malus.name());
                    continue;
                }
                try
                {
                    malusAmplifier = Math.abs(malus.amplifiers.get(i));
                } catch (IndexOutOfBoundsException e)
                {
                    LegendarySurvivalOverhaul.LOGGER.debug("No amplifier defined for effect {} in {}", malus.effects.get(i), malus.name());
                    continue;
                }
                try
                {
                    malusThreshold = (float) Mth.clamp(malus.thresholds.get(i), 0.0f, 1.0f);
                } catch (IndexOutOfBoundsException e)
                {
                    LegendarySurvivalOverhaul.LOGGER.debug("No threshold defined for effect {} in {}", malus.thresholds.get(i), malus.name());
                    continue;
                }
                malusEffects.put(malusThreshold, Pair.of(malusEffect, malusAmplifier));
            }
            bodyPartMalusEffects.put(malus, malusEffects);
        }
    }

    public static void initLimbEffects()
    {
        for (String effectRegistryName : Config.Baked.firstAidSuppliesBoostedOnEffects)
        {
            if (ResourceLocation.tryParse(effectRegistryName) == null)
                LegendarySurvivalOverhaul.LOGGER.info("First Aid Supplies boosting effect : not valid effect registry name : {}", effectRegistryName);

            MobEffect boostingEffect = BuiltInRegistries.MOB_EFFECT
                    .getOptional(ResourceLocation.parse(effectRegistryName))
                    .orElse(null);
            if (boostingEffect == null)
            {
                LegendarySurvivalOverhaul.LOGGER.info("Unknown effect {}", effectRegistryName);
                continue;
            }
            firstAidSuppliesBoostingEffects.add(Holder.direct(boostingEffect));
        }

        for (String effectRegistryName : Config.Baked.passiveLimbRegenerationEffects)
        {
            if (ResourceLocation.tryParse(effectRegistryName) == null)
                LegendarySurvivalOverhaul.LOGGER.info("Limb Regeneration Effect : not valid effect registry name : {}", effectRegistryName);

            MobEffect regenerationEffect = BuiltInRegistries.MOB_EFFECT
                    .getOptional(ResourceLocation.parse(effectRegistryName))
                    .orElse(null);
            if (regenerationEffect == null)
            {
                LegendarySurvivalOverhaul.LOGGER.info("Unknown effect {}", effectRegistryName);
                continue;
            }
            passiveLimbRegenerationEffects.add(Holder.direct(regenerationEffect));
        }
    }

    @Override
    public void applyConsumableHealing(Player player, ItemStack itemStack, boolean itemAlreadyConsumed)
    {
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        JsonHealingConsumable jsonConsumableHeal = BodyDamageDataManager.getHealingItem(itemRegistryName);

        if (jsonConsumableHeal == null)
            return;

        if (Config.Baked.localizedBodyDamageEnabled && jsonConsumableHeal.healingCharges > 0)
        {
            if (player.level().isClientSide && Minecraft.getInstance().screen == null)
                ClientHooks.openBodyHealthScreen(player, player.getUsedItemHand(), itemAlreadyConsumed,
                        jsonConsumableHeal.healingCharges, jsonConsumableHeal.healingValue, jsonConsumableHeal.healingTime);
        } else
        {
            if (Config.Baked.localizedBodyDamageEnabled)
            {
                for (BodyPartEnum bodyPart : BodyPartEnum.values())
                {
                    BodyDamageUtil.applyHealingTimeBodyPart(player, bodyPart, jsonConsumableHeal.healingValue, jsonConsumableHeal.healingTime);
                }
            }

            if (jsonConsumableHeal.recoveryEffectDuration > 0)
            {
                player.addEffect(new MobEffectInstance(MobEffectRegistry.RECOVERY, jsonConsumableHeal.recoveryEffectDuration, jsonConsumableHeal.recoveryEffectAmplifier, false, false, true));
            }

            player.level().playSound(null, player, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            if (!itemAlreadyConsumed)
                itemStack.shrink(1);
        }
    }

    @Override
    public MobEffectInstance getPlayerPassiveLimbRegenerationEffect(Player player)
    {
        MobEffectInstance passiveLimbRegenerationEffect = null;
        for (Holder<MobEffect> effect : passiveLimbRegenerationEffects)
        {
            MobEffectInstance regenerationEffect = player.getEffect(effect);
            if (regenerationEffect != null)
            {
                if (passiveLimbRegenerationEffect == null ||
                        regenerationEffect.getAmplifier() > passiveLimbRegenerationEffect.getAmplifier())
                {
                    passiveLimbRegenerationEffect = regenerationEffect;
                }
            }
        }
        return passiveLimbRegenerationEffect;
    }

    @Override
    public boolean hasPlayerFirstAidSuppliesBoostingEffect(Player player)
    {
        for (Holder<MobEffect> effect : firstAidSuppliesBoostingEffects)
        {
            if (player.hasEffect(effect))
                return true;
        }
        return false;
    }

    @Override
    public List<Pair<MobEffect, Integer>> getEffects(MalusBodyPartEnum bodyPart, float healthRatio)
    {
        List<Pair<MobEffect, Integer>> effects = new ArrayList<>();
        if (bodyPart == null)
            return effects;

        for (Map.Entry<Float, Pair<MobEffect, Integer>> effect : bodyPartMalusEffects.get(bodyPart).entrySet())
        {
            if (healthRatio <= effect.getKey())
            {
                effects.add(effect.getValue());
            }
        }
        return effects;
    }

    @Override
    public void applyHealingTimeBodyPart(Player player, BodyPartEnum bodyPartEnum, float healingValue, int healingTime)
    {

        if (!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return;

        IBodyDamageAttachment capability = AttachmentUtil.getBodyDamageAttachment(player);

        int remainingHealingTicks = capability.getRemainingHealingTicks(bodyPartEnum);
        float healingPerTicks = capability.getHealingPerTicks(bodyPartEnum);

        float healingValuePerTick = (healingValue + remainingHealingTicks * healingPerTicks) / (float) (healingTime + remainingHealingTicks);

        capability.applyHealingTime(bodyPartEnum, healingTime + remainingHealingTicks, healingValuePerTick);
    }

    @Override
    public void healBodyPart(Player player, BodyPartEnum bodyPartEnum, float healingValue)
    {

        if (!Config.Baked.localizedBodyDamageEnabled)
            return;

        IBodyDamageAttachment capability = AttachmentUtil.getBodyDamageAttachment(player);

        capability.heal(bodyPartEnum, healingValue);
    }

    @Override
    public void hurtBodyPart(Player player, BodyPartEnum bodyPartEnum, float damageValue)
    {

        if (!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return;

        IBodyDamageAttachment capability = AttachmentUtil.getBodyDamageAttachment(player);
        double bodyResistance = BODY_RESISTANCE.getAttribute(player).getValue();
        double limbResistance = bodyPartResistanceAttribute.get(bodyPartEnum).getAttribute(player).getValue();

        damageValue = damageValue * (float) (1 - bodyResistance - limbResistance);

        float remainingDamage = Math.max(0, damageValue - (capability.getBodyPartMaxHealth(bodyPartEnum) - capability.getBodyPartDamage(bodyPartEnum)));

        capability.hurt(bodyPartEnum, damageValue - remainingDamage);

        if (remainingDamage > 0 && !bodyPartEnum.getNeighbours().isEmpty())
        {
            remainingDamage /= (float) (1 - bodyResistance - limbResistance);
            List<BodyPartEnum> damageableBodyParts = bodyPartEnum.getNeighbours().stream().filter(bodyPart -> capability.getBodyPartHealthRatio(bodyPart) > 0).collect(Collectors.toList());
            if (!damageableBodyParts.isEmpty())
            {
                BodyPartEnum bodyPart = DamageDistributionEnum.ONE_OF.getBodyParts(player, damageableBodyParts).get(0);
                limbResistance = bodyPartResistanceAttribute.get(bodyPart).getAttribute(player).getValue();
                capability.hurt(bodyPart, remainingDamage * (float) (1 - bodyResistance - limbResistance));
            }
        }
    }

    @Override
    public void balancedHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue)
    {

        if (!Config.Baked.localizedBodyDamageEnabled || bodyParts.isEmpty())
            return;

        Collections.shuffle(bodyParts);

        for (BodyPartEnum bodyPart : bodyParts)
        {
            hurtBodyPart(player, bodyPart, damageValue / bodyParts.size());
        }
    }

    @Override
    public void randomHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || bodyParts.isEmpty())
            return;
        int bodyPartIndex = bodyParts.size() == 1 ? 0 : player.getRandom().nextInt(bodyParts.size() - 1);

        hurtBodyPart(player, bodyParts.get(bodyPartIndex), damageValue);
    }

    @Override
    public float getHealthRatio(Player player, BodyPartEnum bodyPartEnum)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return 0.0f;

        IBodyDamageAttachment capability = AttachmentUtil.getBodyDamageAttachment(player);

        return capability.getBodyPartHealthRatio(bodyPartEnum);
    }

    @Override
    public float getTotalRemainingHealing(Player player, BodyPartEnum bodyPartEnum)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return 0.0f;

        IBodyDamageAttachment capability = AttachmentUtil.getBodyDamageAttachment(player);

        return capability.getRemainingHealingTicks(bodyPartEnum) * capability.getHealingPerTicks(bodyPartEnum);
    }

    @Override
    public float getMaxHealth(Player player, BodyPartEnum bodyPartEnum)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return 0.0f;

        IBodyDamageAttachment capability = AttachmentUtil.getBodyDamageAttachment(player);

        return capability.getBodyPartMaxHealth(bodyPartEnum);
    }

    @Override
    public void updatePlayerBrokenHeartAttribute(Player player)
    {
        int expectedBrokenHearts = 0;
        if (Config.Baked.localizedBodyDamageEnabled && Config.Baked.healthOverhaulEnabled)
        {
            IBodyDamageAttachment capability = AttachmentUtil.getBodyDamageAttachment(player);

            expectedBrokenHearts = capability.getExpectedBrokenHearts();
        }

        HealthUtil.updateBrokenHearts(player, BROKEN_HEART_ATTRIBUTE_UUID, expectedBrokenHearts);
    }
}
