package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.health.IHealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.UUID;

public class HealthUtilInternal implements IHealthUtil {

    public static final UUID HEALTH_ATTRIBUTE_UUID = UUID.fromString("b158dbba-c193-4301-9dfd-82c4347b2cf4");
    public static final UUID INITIAL_PERMANENT_HEART_ATTRIBUTE_UUID = UUID.fromString("81e7fd2b-90d2-4673-84f6-8bd343fd7c5e");
    public static final UUID INITIAL_BROKEN_HEART_RESILIENCE_ATTRIBUTE_UUID = UUID.fromString("eb13fc3b-cc33-4716-a0b0-9f4cdd7704ba");

    public static final AttributeBuilder HEALTH_ATTRIBUTE = new AttributeBuilder(Attributes.MAX_HEALTH, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".max_health");
    public static final AttributeBuilder PERMANENT_HEART_ATTRIBUTE = new AttributeBuilder(AttributeRegistry.PERMANENT_HEART.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".permanent_heart");
    public static final AttributeBuilder BROKEN_HEART_RESILIENCE_ATTRIBUTE = new AttributeBuilder(AttributeRegistry.BROKEN_HEART_RESILIENCE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".broken_heart_resilience");

    @Override
    public void updatePlayerHealthAttributes(Player player)
    {
        double maxHealth = calculatePlayerMaxHealth(player);

        HEALTH_ATTRIBUTE.addModifier(player, HEALTH_ATTRIBUTE_UUID, maxHealth - 20);
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth()));
    }

    @Override
    public double calculatePlayerMaxHealth(Player player) {
        double maxHealth = Config.Baked.initialHealth;

        if (Config.Baked.healthOverhaulEnabled) {
            HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);
            maxHealth += healthCapability.getAdditionalHealth();

            if (Config.Baked.localizedBodyDamageEnabled && healthCapability.getBrokenHearts() > 0) {
                int minhHearthLimitWithBrokenHearth = (int) player.getAttributeValue(AttributeRegistry.BROKEN_HEART_RESILIENCE.get());
                maxHealth -= Mth.clamp(maxHealth - minhHearthLimitWithBrokenHearth * 2, 0, healthCapability.getBrokenHearts() * 2);
            }
        }
        return maxHealth;
    }

    @Override
    public void initializeHealthAttributes(Player player) {
        PERMANENT_HEART_ATTRIBUTE.addModifier(player, INITIAL_PERMANENT_HEART_ATTRIBUTE_UUID, Config.Baked.permanentHearts - 1);
        BROKEN_HEART_RESILIENCE_ATTRIBUTE.addModifier(player, INITIAL_BROKEN_HEART_RESILIENCE_ATTRIBUTE_UUID, Config.Baked.resilientHeartsWithBrokenHearts - 1);
    }

    @Override
    public float hurtPlayer(Player player, float damageValue) {
        HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);

        float shieldValue = healthCapability.getShieldHealth();
        healthCapability.addShieldHealth(-damageValue);
        damageValue -= shieldValue;

        return Math.max(damageValue - shieldValue, 0);
    }

    @Override
    public void loseHearth(Player player, int amountLost) {
        HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);

        int minhHearthLimit = (int) player.getAttributeValue(AttributeRegistry.PERMANENT_HEART.get());

        healthCapability.setAdditionalHealth(Math.max(minhHearthLimit * 2 - Mth.ceil(Config.Baked.initialHealth), healthCapability.getAdditionalHealth() - amountLost * 2));
        updatePlayerHealthAttributes(player);
    }
}
