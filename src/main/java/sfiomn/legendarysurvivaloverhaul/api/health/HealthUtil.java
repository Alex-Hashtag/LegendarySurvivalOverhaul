package sfiomn.legendarysurvivaloverhaul.api.health;

import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class HealthUtil
{
    public static IHealthUtil internal;

    /**
     * Update the player max health
     *
     * @param player The player for which update the max health based on the additional health
     */
    public static void updatePlayerMaxHealthAttribute(Player player)
    {
        internal.updatePlayerMaxHealthAttribute(player);
    }

    /**
     * Get the player max health based on broken hearts, additional health and initial health
     *
     * @param player The player for which the max health is calculated
     * @return player max health
     */
    public static double getPlayerMaxHealth(Player player)
    {
        return internal.getPlayerMaxHealth(player);
    }

    /**
     * Get the player max health based only on additional health and initial health; without the impact of the broken hearts
     *
     * @param player The player for which the stable max health is calculated
     * @return player stable max health, meaning without the impact of broken hearts
     */
    public static double getPlayerStableMaxHealth(Player player)
    {
        return internal.getPlayerStableMaxHealth(player);
    }

    /**
     * Get the player effective broken hearts, taking into account the broken hearts resiliency
     *
     * @param player The player that is inflicted by broken hearts
     * @return number of broken hearts actually impacting the player
     */
    public static int getEffectiveBrokenHearts(Player player)
    {
        return internal.getEffectiveBrokenHearts(player);
    }

    /**
     * Initialize the player broken heart resilience & permanent hearts attributes with the config values
     *
     * @param player The player for which we initialize the health attributes
     */
    public static void initializeHealthAttributes(Player player)
    {
        internal.initializeHealthAttributes(player);
    }

    /**
     * Process damage value to health overhaul system
     *
     * @param player      The player being hurt
     * @param damageValue damage inflicted to the player
     * @return remaining damage to propagate to normal health
     */
    public static float hurtPlayer(Player player, float damageValue)
    {
        return internal.hurtPlayer(player, damageValue);
    }

    /**
     * Lose heart to the player, meaning the player's max health will be permanently reduced
     * Use this method to take into account the minimum hearth a player should keep
     *
     * @param player     The player for which the max health is reduced
     * @param amountLost Amount of Heart lost
     */
    public static void loseHearth(Player player, int amountLost)
    {
        internal.loseHearth(player, amountLost);
    }

    /**
     * Update the player broken hearts amount, based on the specified UUID
     * Modifications done on the same UUID are overwritten
     *
     * @param player        The player for which the broken hearts amount is updated
     * @param attributeUuid Uuid used to identify the modification
     * @param brokenHearts  The new amount of broken hearts
     */
    public static void updateBrokenHearts(Player player, UUID attributeUuid, int brokenHearts)
    {
        internal.updateBrokenHearts(player, attributeUuid, brokenHearts);
    }
}
