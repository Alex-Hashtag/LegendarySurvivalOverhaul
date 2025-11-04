package sfiomn.legendarysurvivaloverhaul.api.food;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * (Don't use this!) <br>
 * Runs a tick update for the player's food capability
 *
 * @param player
 * @param world
 * @param isStart
 */
public interface IFoodAttachment
{
    void tickUpdate(Player player, Level world, boolean isStart);
}
