package sfiomn.legendarysurvivaloverhaul.api.bodydamage.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbConditions;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbPenalty;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbRegion;

/**
 * Posted on {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS} when a player's limb conditions change,
 * on the server when the limb data is synced and on the client when it's received. <br>
 * Use it for one-time reactions (sound when a limb breaks, attribute modifiers to add or remove),
 * instead of checking the limbs every tick. Conditions are the packed ints of {@link LimbConditions}.
 * <p>
 * A new player entity (login, respawn) starts from {@link LimbConditions#NONE}, so its active penalties are announced as gained again.
 */
public class LimbConditionsChangedEvent extends Event
{
    private final Player player;
    private final int oldConditions;
    private final int newConditions;

    public LimbConditionsChangedEvent(Player player, int oldConditions, int newConditions)
    {
        this.player = player;
        this.oldConditions = oldConditions;
        this.newConditions = newConditions;
    }

    public Player getPlayer()
    {
        return player;
    }

    public boolean isClientSide()
    {
        return player.level().isClientSide;
    }

    public int getOldConditions()
    {
        return oldConditions;
    }

    public int getNewConditions()
    {
        return newConditions;
    }

    public boolean gained(LimbPenalty penalty)
    {
        return LimbConditions.gained(oldConditions, newConditions, penalty);
    }

    public boolean lost(LimbPenalty penalty)
    {
        return LimbConditions.lost(oldConditions, newConditions, penalty);
    }

    public boolean penaltiesChanged()
    {
        return LimbConditions.penaltiesChanged(oldConditions, newConditions);
    }

    /**
     * @return true if the region got worse, e.g. a limb just broke
     */
    public boolean worsened(LimbRegion region)
    {
        return LimbConditions.getSeverity(newConditions, region) > LimbConditions.getSeverity(oldConditions, region);
    }
}
