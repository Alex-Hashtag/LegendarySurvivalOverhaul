package sfiomn.legendarysurvivaloverhaul.api.wetness;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IWetnessAttachment
{
    int getWetness();

    void setWetness(int exhaustion);

    int getWetnessTickTimer();

    void setWetnessTickTimer(int tickTimer);

    void addWetness(int wetness);

    void addWetnessTickTimer(int tickTimer);

    /**
     * (Don't use this!) <br>
     * Checks if the capability needs an update
     *
     * @return boolean has thirst changed
     */
    boolean isDirty();

    /**
     * (Don't use this!) <br>
     * Sets the capability as updated
     */
    void setClean();

    /**
     * Force the synchronization server - client
     * of the thirst capability
     */
    void setDirty();

    /**
     * (Don't use this!) <br>
     * Runs a tick update for the player's thirst capability
     *
     * @param player
     * @param world
     * @param isStart
     */
    void tickUpdate(Player player, Level world, boolean isStart);


    /**
     * (Don't use this!) <br>
     * Gets the current tick of the packet timer
     *
     * @return int packetTimer
     */
    int getPacketTimer();
}
