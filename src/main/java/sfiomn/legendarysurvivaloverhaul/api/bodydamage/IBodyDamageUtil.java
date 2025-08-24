package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IBodyDamageUtil
{
    void applyConsumableHealing(Player player, ItemStack itemStack, boolean itemAlreadyConsumed);

    List<Pair<MobEffect, Integer>> getEffects(MalusBodyPartEnum bodyPart, float headHealthRatio);

    MobEffectInstance getPlayerPassiveLimbRegenerationEffect(Player player);

    boolean hasPlayerFirstAidSuppliesBoostingEffect(Player player);

    void applyHealingTimeBodyPart(Player player, BodyPartEnum bodyPartEnum, float healingValue, int healingTime);

    void healBodyPart(Player player, BodyPartEnum bodyPartEnum, float healingValue);

    void hurtBodyPart(Player player, BodyPartEnum bodyPartEnum, float damageValue);

    void balancedHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue);

    void randomHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue);

    float getHealthRatio(Player player, BodyPartEnum bodyPartEnum);

    float getTotalRemainingHealing(Player player, BodyPartEnum bodyPartEnum);

    float getMaxHealth(Player player, BodyPartEnum bodyPartEnum);

    void updatePlayerBrokenHeartAttribute(Player player);
}
