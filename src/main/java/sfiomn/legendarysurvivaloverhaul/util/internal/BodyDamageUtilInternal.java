package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbPenalty;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.client.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage.BodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries.SupplementariesUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;

import java.util.*;

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

    @Override
    public void applyConsumableHealing(Player player, ItemStack itemStack, boolean itemAlreadyConsumed)
    {
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        JsonHealingConsumable healingConsumable = BodyDamageDataManager.getHealingItem(itemRegistryName);

        if (healingConsumable == null)
            return;

        if (Config.Baked.localizedBodyDamageEnabled && healingConsumable.isLimbSelection())
        {
            // The player chooses the limbs to heal in the body health screen, the server waits for its choice
            if (player.level().isClientSide)
                ClientHooks.openBodyHealthScreen(player, player.getUsedItemHand(), healingConsumable);
            else
                AttachmentUtil.getBodyDamageAttachment(player).startHealingSession(itemRegistryName, player.getUsedItemHand(),
                        healingConsumable.healingCharges, itemAlreadyConsumed);
            return;
        }

        if (player.level().isClientSide)
            return;

        if (Config.Baked.localizedBodyDamageEnabled)
        {
            BodyDamageAttachment attachment = AttachmentUtil.getBodyDamageAttachment(player);
            for (BodyPartEnum bodyPart : BodyPartEnum.values())
                attachment.healLimb(bodyPart, healingConsumable.limbHealing, healingConsumable.healsBrokenLimbs);
            giveLimbShields(player, healingConsumable.limbShields);
            attachment.updateBrokenHearts(player);
        }

        applyRegeneration(player, healingConsumable);
        player.level().playSound(null, player, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        if (!itemAlreadyConsumed && !player.isCreative())
            itemStack.shrink(1);
    }

    private static void applyRegeneration(Player player, JsonHealingConsumable healingConsumable)
    {
        if (healingConsumable.regenerationDuration > 0)
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, healingConsumable.regenerationDuration, healingConsumable.regenerationAmplifier));
    }

    @Override
    public boolean canUseHealingConsumable(Player player, JsonHealingConsumable healingConsumable)
    {
        if (healingConsumable.regenerationDuration > 0 && player.getHealth() < player.getMaxHealth())
            return true;

        if (!Config.Baked.localizedBodyDamageEnabled)
            return false;

        BodyDamageAttachment attachment = AttachmentUtil.getBodyDamageAttachment(player);
        for (BodyPartEnum bodyPart : BodyPartEnum.values())
        {
            if (healingConsumable.limbHealing > 0 && attachment.canHealLimb(bodyPart, healingConsumable.healsBrokenLimbs))
                return true;
            if (healingConsumable.limbShields > 0 && !attachment.hasLimbShield(bodyPart))
                return true;
        }
        return false;
    }

    @Override
    public void healBodyPartWithHealingSession(Player player, BodyPartEnum bodyPartEnum)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null || player.level().isClientSide)
            return;

        BodyDamageAttachment attachment = AttachmentUtil.getBodyDamageAttachment(player);
        ResourceLocation sessionItem = attachment.getHealingSessionItem();
        if (sessionItem == null)
            return;

        JsonHealingConsumable healingConsumable = BodyDamageDataManager.getHealingItem(sessionItem);
        if (healingConsumable == null || !healingConsumable.isLimbSelection())
        {
            attachment.clearHealingSession();
            return;
        }

        if (!attachment.canHealLimb(bodyPartEnum, healingConsumable.healsBrokenLimbs))
            return;

        // The item is only consumed once the first limb is chosen, so closing the screen doesn't waste it
        if (!attachment.isHealingSessionItemConsumed())
        {
            ItemStack usedItemStack = getItemInHand(player, attachment.getHealingSessionHand());
            if (!sessionItem.equals(BuiltInRegistries.ITEM.getKey(usedItemStack.getItem())))
            {
                attachment.clearHealingSession();
                return;
            }
            if (!player.isCreative())
                usedItemStack.shrink(1);
            attachment.setHealingSessionItemConsumed();
            applyRegeneration(player, healingConsumable);
        }

        attachment.healLimb(bodyPartEnum, healingConsumable.limbHealing, healingConsumable.healsBrokenLimbs);
        attachment.useHealingSessionCharge();
        attachment.updateBrokenHearts(player);
        player.level().playSound(null, player, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    private static ItemStack getItemInHand(Player player, InteractionHand hand)
    {
        if (hand == null)
            return ItemStack.EMPTY;

        ItemStack itemStack = player.getItemInHand(hand);
        if (LegendarySurvivalOverhaul.supplementariesLoaded)
        {
            ItemStack itemStackInBasket = SupplementariesUtil.getSelectedItemInLunchBasket(itemStack);
            if (itemStackInBasket != ItemStack.EMPTY)
                return itemStackInBasket;
        }
        return itemStack;
    }

    @Override
    public int healBodyPart(Player player, BodyPartEnum bodyPartEnum, int limbHearts, boolean canHealBroken)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return 0;

        BodyDamageAttachment attachment = AttachmentUtil.getBodyDamageAttachment(player);
        int healed = attachment.healLimb(bodyPartEnum, limbHearts, canHealBroken);
        if (healed > 0)
            attachment.updateBrokenHearts(player);
        return healed;
    }

    @Override
    public void giveLimbShields(Player player, int limbShields)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || limbShields <= 0)
            return;

        BodyDamageAttachment attachment = AttachmentUtil.getBodyDamageAttachment(player);
        for (int i = 0; i < limbShields; i++)
        {
            // Limbs without shield, weighted by their damage so the most damaged limbs are more likely protected
            int totalWeight = 0;
            Map<BodyPartEnum, Integer> weights = new EnumMap<>(BodyPartEnum.class);
            for (BodyPartEnum bodyPart : BodyPartEnum.values())
            {
                if (attachment.hasLimbShield(bodyPart))
                    continue;
                int weight = 1 + attachment.getLimbDamage(bodyPart);
                weights.put(bodyPart, weight);
                totalWeight += weight;
            }

            if (totalWeight == 0)
                return;

            int roll = player.getRandom().nextInt(totalWeight);
            for (Map.Entry<BodyPartEnum, Integer> weight : weights.entrySet())
            {
                roll -= weight.getValue();
                if (roll < 0)
                {
                    attachment.setLimbShield(weight.getKey(), true);
                    break;
                }
            }
        }
    }

    @Override
    public void hurtBodyPart(Player player, BodyPartEnum bodyPartEnum, float damageValue)
    {
        if (bodyPartEnum != null)
            hurtBodyParts(player, Collections.singletonList(bodyPartEnum), damageValue);
    }

    @Override
    public void hurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || bodyParts.isEmpty() || damageValue <= 0)
            return;

        float threshold = getLimbDamageThreshold(player);
        if (threshold <= 0)
            return;

        BodyDamageAttachment attachment = AttachmentUtil.getBodyDamageAttachment(player);
        double bodyResistance = BODY_RESISTANCE.getAttribute(player).getValue();
        Set<BodyPartEnum> immuneBodyParts = EnumSet.noneOf(BodyPartEnum.class);
        float remainingDamage = damageValue;
        boolean limbHit = false;

        // Each time the damage reaches the threshold, a limb loses a limb heart (or its shield)
        while (true)
        {
            BodyPartEnum target = chooseHitBodyPart(player, attachment, bodyParts, immuneBodyParts);
            if (target == null)
                break;

            double resistance = bodyResistance + bodyPartResistanceAttribute.get(target).getAttribute(player).getValue();
            if (resistance >= 1)
            {
                immuneBodyParts.add(target);
                continue;
            }

            float damageNeeded = (float) (threshold / (1 - resistance));
            if (remainingDamage < damageNeeded)
                break;

            remainingDamage -= damageNeeded;
            attachment.hitLimb(target);
            limbHit = true;
        }

        if (limbHit)
            attachment.updateBrokenHearts(player);
    }

    /**
     * Choose a random hit body part that can still be hurt. <br>
     * If all of them are broken, the damage goes to one of their neighbours.
     */
    private static BodyPartEnum chooseHitBodyPart(Player player, BodyDamageAttachment attachment, List<BodyPartEnum> bodyParts, Set<BodyPartEnum> immuneBodyParts)
    {
        List<BodyPartEnum> hittableBodyParts = new ArrayList<>();
        for (BodyPartEnum bodyPart : bodyParts)
        {
            if (canBeHit(attachment, bodyPart, immuneBodyParts))
                hittableBodyParts.add(bodyPart);
        }

        if (hittableBodyParts.isEmpty())
        {
            for (BodyPartEnum bodyPart : bodyParts)
            {
                for (BodyPartEnum neighbour : bodyPart.getNeighbours())
                {
                    if (!hittableBodyParts.contains(neighbour) && canBeHit(attachment, neighbour, immuneBodyParts))
                        hittableBodyParts.add(neighbour);
                }
            }
        }

        if (hittableBodyParts.isEmpty())
            return null;

        return hittableBodyParts.get(player.getRandom().nextInt(hittableBodyParts.size()));
    }

    private static boolean canBeHit(BodyDamageAttachment attachment, BodyPartEnum bodyPart, Set<BodyPartEnum> immuneBodyParts)
    {
        return !immuneBodyParts.contains(bodyPart) && (attachment.hasLimbShield(bodyPart) || !attachment.isLimbBroken(bodyPart));
    }

    @Override
    public float getLimbDamageThreshold(Player player)
    {
        // Broken hearts are removed from the max health first
        double maxHealth = HealthUtil.getPlayerStableMaxHealth(player) - HealthUtil.getEffectiveBrokenHearts(player) * 2.0;
        return (float) (Math.max(maxHealth, 0) * Config.Baked.limbDamageThreshold);
    }

    @Override
    public float getHealthRatio(Player player, BodyPartEnum bodyPartEnum)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return 1.0f;

        return AttachmentUtil.getBodyDamageAttachment(player).getLimbHealthRatio(bodyPartEnum);
    }

    @Override
    public boolean hasLimbPenalty(Player player, LimbPenalty penalty)
    {
        return Config.Baked.localizedBodyDamageEnabled && AttachmentUtil.getBodyDamageAttachment(player).hasLimbPenalty(penalty);
    }

    @Override
    public void updatePlayerBrokenHeartAttribute(Player player)
    {
        int expectedBrokenHearts = 0;
        if (Config.Baked.localizedBodyDamageEnabled && Config.Baked.healthOverhaulEnabled)
            expectedBrokenHearts = AttachmentUtil.getBodyDamageAttachment(player).getExpectedBrokenHearts();

        HealthUtil.updateBrokenHearts(player, BROKEN_HEART_ATTRIBUTE_UUID, expectedBrokenHearts);
    }
}
