package sfiomn.legendarysurvivaloverhaul.api.wetness;

import net.minecraft.world.entity.player.Player;

public interface IWetnessUtil
{
    void addWetness(Player player, int wetness);

    void deactivateWetness(Player player);

    void activateWetness(Player player);

    boolean isWetnessActive(Player player);
}
