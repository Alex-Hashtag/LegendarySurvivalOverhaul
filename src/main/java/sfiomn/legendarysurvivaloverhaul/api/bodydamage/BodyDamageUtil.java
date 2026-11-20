package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;

import java.util.List;

public class BodyDamageUtil
{
    public static IBodyDamageUtil internal;

    /**
     * Apply to the player the limb healing behavior associated to the provided item registry name. <br>
     * Items healing a limited number of limbs open the body health screen on the client, and wait on the server for the player to choose the limbs.
     *
     * @param player              Player to which limbs will be healed
     * @param itemStack           Item stack that has a limb healing behavior
     * @param itemAlreadyConsumed Whether the item has been already consumed or not
     */
    public static void applyConsumableHealing(Player player, ItemStack itemStack, boolean itemAlreadyConsumed)
    {
        internal.applyConsumableHealing(player, itemStack, itemAlreadyConsumed);
    }

    /**
     * Check if the healing consumable would have any effect on the player.
     *
     * @param player            player using the item
     * @param healingConsumable healing behavior of the item
     */
    public static boolean canUseHealingConsumable(Player player, JsonHealingConsumable healingConsumable)
    {
        return internal.canUseHealingConsumable(player, healingConsumable);
    }

    /**
     * Heal the body part chosen by the player with the healing item they just used. <br>
     * Server side only, does nothing if there is no pending healing item.
     *
     * @param player       player to heal
     * @param bodyPartEnum body part chosen by the player
     */
    public static void healBodyPartWithHealingSession(Player player, BodyPartEnum bodyPartEnum)
    {
        internal.healBodyPartWithHealingSession(player, bodyPartEnum);
    }

    /**
     * Restore limb hearts of a player's body limb. <br>
     *
     * @param player        player to heal
     * @param bodyPart      bodyPart enum to heal
     * @param limbHearts    limb hearts restored
     * @param canHealBroken whether a broken limb can be healed
     * @return limb hearts actually restored
     */
    public static int healBodyPart(Player player, BodyPartEnum bodyPart, int limbHearts, boolean canHealBroken)
    {
        return internal.healBodyPart(player, bodyPart, limbHearts, canHealBroken);
    }

    /**
     * Give limb shields to random limbs without shield, the most damaged limbs being more likely to be chosen.
     *
     * @param player      player receiving the shields
     * @param limbShields number of limb shields
     */
    public static void giveLimbShields(Player player, int limbShields)
    {
        internal.giveLimbShields(player, limbShields);
    }

    /**
     * Hurt a player's body limb. <br>
     * The limb loses one limb heart (or its shield) each time the damage reaches the limb damage threshold. <br>
     * If the limb is already broken, a neighbour limb is hurt instead.
     *
     * @param player      player to hurt
     * @param bodyPart    bodyPart enum to hurt
     * @param damageValue damage value inflicted to the player
     */
    public static void hurtBodyPart(Player player, BodyPartEnum bodyPart, float damageValue)
    {
        internal.hurtBodyPart(player, bodyPart, damageValue);
    }

    /**
     * Hurt a set of player's body limbs. <br>
     * Each time the damage reaches the limb damage threshold, one of the body parts loses one limb heart (or its shield).
     *
     * @param player      player to hurt
     * @param bodyParts   list of bodyPart enum that can be hurt
     * @param damageValue damage value inflicted to the player
     */
    public static void hurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue)
    {
        internal.hurtBodyParts(player, bodyParts, damageValue);
    }

    /**
     * Get the damage needed to make a limb lose one limb heart, before limb resistances. <br>
     * It is a percentage of the player max health, broken hearts removed.
     *
     * @param player player
     */
    public static float getLimbDamageThreshold(Player player)
    {
        return internal.getLimbDamageThreshold(player);
    }

    /**
     * Get the health percentage of a player's body limb. <br>
     *
     * @param player   player
     * @param bodyPart bodyPart enum
     * @return remaining limb hearts / max limb hearts
     */
    public static float getHealthRatio(Player player, BodyPartEnum bodyPart)
    {
        return internal.getHealthRatio(player, bodyPart);
    }

    /**
     * Check if a limb penalty is active on the player. Cheap: a bit test on cached limb conditions. <br>
     * On the client, prefer {@code ClientLimbConditions} for the local player in per frame code.
     *
     * @param player  player
     * @param penalty limb penalty
     */
    public static boolean hasLimbPenalty(Player player, LimbPenalty penalty)
    {
        return internal.hasLimbPenalty(player, penalty);
    }

    /**
     * Update the broken heart attribute of the player, based on the number of broken limbs. <br>
     *
     * @param player player
     */
    public static void updatePlayerBrokenHeartAttribute(Player player)
    {
        internal.updatePlayerBrokenHeartAttribute(player);
    }
}
