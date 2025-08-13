package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.health.IHealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Objects;
import java.util.UUID;

public class HealthUtilInternal implements IHealthUtil {

    public static final UUID HEALTH_ATTRIBUTE_UUID = UUID.fromString("b158dbba-c193-4301-9dfd-82c4347b2cf4");
    public static final UUID INITIAL_PERMANENT_HEART_ATTRIBUTE_UUID = UUID.fromString("81e7fd2b-90d2-4673-84f6-8bd343fd7c5e");
    public static final UUID INITIAL_BROKEN_HEART_RESILIENCE_ATTRIBUTE_UUID = UUID.fromString("eb13fc3b-cc33-4716-a0b0-9f4cdd7704ba");

    public static final AttributeBuilder HEALTH_ATTRIBUTE = new AttributeBuilder(Attributes.MAX_HEALTH, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".max_health");
    public static final AttributeBuilder BROKEN_HEART_ATTRIBUTE = new AttributeBuilder(AttributeRegistry.BROKEN_HEART.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".broken_heart");
    public static final AttributeBuilder PERMANENT_HEART_ATTRIBUTE = new AttributeBuilder(AttributeRegistry.PERMANENT_HEART.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".permanent_heart");
    public static final AttributeBuilder BROKEN_HEART_RESILIENCE_ATTRIBUTE = new AttributeBuilder(AttributeRegistry.BROKEN_HEART_RESILIENCE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".broken_heart_resilience");

    @Override
    public void updatePlayerMaxHealthAttribute(Player player)
    {
        double maxHealth = getPlayerMaxHealth(player);

        double baseHealth = 20;
        if (player.getAttribute(Attributes.MAX_HEALTH) != null)
            baseHealth = Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).getBaseValue();

        baseHealth += getMaxHealthFromModifiers(player);

        HEALTH_ATTRIBUTE.addModifier(player, HEALTH_ATTRIBUTE_UUID, maxHealth - baseHealth);
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth()));
    }

    @Override
    public double getPlayerMaxHealth(Player player) {
        double maxHealth = Config.Baked.initialHealth;
        maxHealth += getMaxHealthFromModifiers(player);

        if (Config.Baked.healthOverhaulEnabled) {
            HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);
            float additionalHealth = healthCapability.getAdditionalHealth();
            int minhHearthLimitWithBrokenHearth = (int) player.getAttributeValue(AttributeRegistry.BROKEN_HEART_RESILIENCE.get());

            maxHealth += additionalHealth - Mth.clamp(((int) player.getAttributeValue(AttributeRegistry.BROKEN_HEART.get())) * 2, 0, maxHealth + additionalHealth - minhHearthLimitWithBrokenHearth * 2);
        }
        return maxHealth;
    }

    @Override
    public double getPlayerStableMaxHealth(Player player) {
        double maxHealth = Config.Baked.initialHealth;
        maxHealth += getMaxHealthFromModifiers(player);

        if (Config.Baked.healthOverhaulEnabled) {
            HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);
            maxHealth += healthCapability.getAdditionalHealth();
        }
        return maxHealth;
    }

    @Override
    public int getEffectiveBrokenHearts(Player player) {
        if (!Config.Baked.healthOverhaulEnabled)
            return 0;

        double maxHealth = getPlayerStableMaxHealth(player);
        int brokenHearts = (int) player.getAttributeValue(AttributeRegistry.BROKEN_HEART.get());
        int resilientHearts = (int) player.getAttributeValue(AttributeRegistry.BROKEN_HEART_RESILIENCE.get());
        return Mth.clamp(brokenHearts, 0, (int) Math.ceil(maxHealth / 2.0) - resilientHearts);
    }

    @Override
    public void initializeHealthAttributes(Player player) {
        PERMANENT_HEART_ATTRIBUTE.addModifier(player, INITIAL_PERMANENT_HEART_ATTRIBUTE_UUID, Config.Baked.permanentHearts - 1);
        BROKEN_HEART_RESILIENCE_ATTRIBUTE.addModifier(player, INITIAL_BROKEN_HEART_RESILIENCE_ATTRIBUTE_UUID, Config.Baked.resilientHeartsWithBrokenHearts - 1);
    }

    @Override
    public float hurtPlayer(Player player, float damageValue) {
        HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);

        float shieldValue = Math.max(healthCapability.getShieldHealth(), 0);
        healthCapability.addShieldHealth(-damageValue);

        return Math.max(damageValue - shieldValue, 0);
    }

    @Override
    public void loseHearth(Player player, int amountLost) {
        HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);

        int minhHearthLimit = (int) player.getAttributeValue(AttributeRegistry.PERMANENT_HEART.get());
        // max losable heart amount = max player health - minhHearthLimit
        int actuallyLostHearts = Math.min((int) Math.ceil(getPlayerStableMaxHealth(player) / 2.0) - minhHearthLimit, amountLost);

        if (actuallyLostHearts > 0) {
            healthCapability.setAdditionalHealth(healthCapability.getAdditionalHealth() - actuallyLostHearts * 2);
        }
        updatePlayerMaxHealthAttribute(player);
    }

    @Override
    public void updateBrokenHearts(Player player, UUID attributeUuid, int brokenHearts) {
        BROKEN_HEART_ATTRIBUTE.addModifier(player, attributeUuid, brokenHearts);
        updatePlayerMaxHealthAttribute(player);
    }

    private double getMaxHealthFromModifiers(Player player) {
        double maxHealthModified = 0;

        if (player.getAttribute(Attributes.MAX_HEALTH) == null)
            return maxHealthModified;

        for (AttributeModifier attributeModifier: Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).getModifiers()) {
            if (!attributeModifier.getId().equals(HEALTH_ATTRIBUTE_UUID))
                maxHealthModified += attributeModifier.getAmount();
        }
        return maxHealthModified;
    }
}
