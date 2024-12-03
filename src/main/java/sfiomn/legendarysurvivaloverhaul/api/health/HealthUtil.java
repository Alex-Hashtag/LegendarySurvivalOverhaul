package sfiomn.legendarysurvivaloverhaul.api.health;

import net.minecraft.world.entity.player.Player;

public class HealthUtil
{
	public static IHealthUtil internal;

	/**
	 * Update the player max health
	 *
	 * @param player The player for which update the max health based on the additional health
	 */
	public static void updatePlayerMaxHealthAttribute(Player player) {
		internal.updatePlayerHealthAttributes(player);
	}

	/**
	 * Initialize the player broken heart resilience & permanent hearts attributes with the config values
	 *
	 * @param player The player for which we initialize the health attributes
	 */
	public static void initializeHealthAttributes(Player player) {
		internal.initializeHealthAttributes(player);
	}

	/**
	 * Process damage value to health overhaul system
	 *
	 * @param player The player being hurt
	 * @param damageValue damage inflicted to the player
	 * @return remaining damage to propagate to normal health
	 */
	public static float hurtPlayer(Player player, float damageValue) {
		return internal.hurtPlayer(player, damageValue);
	}

	/**
	 * Lose heart to the player, meaning the player's max health will be permanently reduced
	 * Use this method to take into account the minimum hearth a player should keep
	 *
	 * @param player The player for which the max health is reduced
	 * @param amountLost Amount of Heart lost
	 */
	public static void loseHearth(Player player, int amountLost) {
		internal.loseHearth(player, amountLost);
	}
}
