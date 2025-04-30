package sfiomn.legendarysurvivaloverhaul.api.wetness;

import net.minecraft.world.entity.player.Player;

public interface IWetnessUtil
{
	public void addWetness(Player player, int wetness);

	public void deactivateWetness(Player player);

	public void activateWetness(Player player);

	public boolean isWetnessActive(Player player);
}
