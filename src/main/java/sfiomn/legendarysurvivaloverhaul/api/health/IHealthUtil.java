package sfiomn.legendarysurvivaloverhaul.api.health;

import net.minecraft.world.entity.player.Player;

public interface IHealthUtil
{
    void updatePlayerHealthAttributes(Player player);

    double calculatePlayerMaxHealth(Player player);

    void initializeHealthAttributes(Player player);

    float hurtPlayer(Player player, float damageValue);

    void loseHearth(Player player, int amountLost);
}
