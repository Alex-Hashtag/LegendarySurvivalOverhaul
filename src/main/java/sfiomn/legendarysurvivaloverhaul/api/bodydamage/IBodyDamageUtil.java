package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;

import java.util.List;

public interface IBodyDamageUtil
{
    void applyConsumableHealing(Player player, ItemStack itemStack, boolean itemAlreadyConsumed);

    boolean canUseHealingConsumable(Player player, JsonHealingConsumable healingConsumable);

    void healBodyPartWithHealingSession(Player player, BodyPartEnum bodyPartEnum);

    int healBodyPart(Player player, BodyPartEnum bodyPartEnum, int limbHearts, boolean canHealBroken);

    void giveLimbShields(Player player, int limbShields);

    void hurtBodyPart(Player player, BodyPartEnum bodyPartEnum, float damageValue);

    void hurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue);

    float getLimbDamageThreshold(Player player);

    float getHealthRatio(Player player, BodyPartEnum bodyPartEnum);

    boolean hasLimbPenalty(Player player, LimbPenalty penalty);

    void updatePlayerBrokenHeartAttribute(Player player);
}
