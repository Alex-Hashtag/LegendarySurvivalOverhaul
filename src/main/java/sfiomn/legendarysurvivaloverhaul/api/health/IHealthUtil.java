package sfiomn.legendarysurvivaloverhaul.api.health;

import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public interface IHealthUtil
{
    void updatePlayerMaxHealthAttribute(Player player);

    double getPlayerMaxHealth(Player player);

    double getPlayerStableMaxHealth(Player player);

    int getEffectiveBrokenHearts(Player player);

    void initializeHealthAttributes(Player player);

    float hurtPlayer(Player player, float damageValue);

    void loseHearth(Player player, int amountLost);

    void updateBrokenHearts(Player player, UUID attributeUuid, int brokenHearts);
}
