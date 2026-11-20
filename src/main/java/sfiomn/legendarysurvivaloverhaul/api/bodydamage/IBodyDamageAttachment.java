package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Attachment for handling localized body damage. <br>
 * Each limb has {@link #MAX_LIMB_DAMAGE} limb hearts (healthy, bruised, fractured, broken) and can be covered by a shield.
 */
public interface IBodyDamageAttachment
{
    /**
     * Number of limb hearts a limb has. A limb with this much damage is broken.
     */
    int MAX_LIMB_DAMAGE = 3;

    /**
     * Layout of the packed limb data: 2 damage bits per limb (bits 0-15), then 1 shield bit per limb (bits 16-23).
     */
    int LIMB_DAMAGE_BITS = 2;
    int LIMB_DAMAGE_MASK = (1 << LIMB_DAMAGE_BITS) - 1;
    int LIMB_SHIELD_OFFSET = 16;

    static int getLimbDamage(int limbData, BodyPartEnum part)
    {
        return (limbData >> (part.ordinal() * LIMB_DAMAGE_BITS)) & LIMB_DAMAGE_MASK;
    }

    static boolean hasLimbShield(int limbData, BodyPartEnum part)
    {
        return (limbData & (1 << (LIMB_SHIELD_OFFSET + part.ordinal()))) != 0;
    }

    /**
     * @return number of limb hearts lost by the limb, between 0 (healthy) and {@link #MAX_LIMB_DAMAGE} (broken)
     */
    int getLimbDamage(BodyPartEnum part);

    void setLimbDamage(BodyPartEnum part, int damage);

    boolean hasLimbShield(BodyPartEnum part);

    void setLimbShield(BodyPartEnum part, boolean shield);

    boolean isLimbBroken(BodyPartEnum part);

    /**
     * @return remaining limb hearts / {@link #MAX_LIMB_DAMAGE}
     */
    float getLimbHealthRatio(BodyPartEnum part);

    /**
     * Hit the limb once: breaks its shield if it has one, otherwise the limb loses a limb heart.
     *
     * @return false if nothing happened because the limb is already broken
     */
    boolean hitLimb(BodyPartEnum part);

    /**
     * Restore limb hearts.
     *
     * @param canHealBroken whether a broken limb can be healed
     * @return number of limb hearts restored
     */
    int healLimb(BodyPartEnum part, int amount, boolean canHealBroken);

    boolean canHealLimb(BodyPartEnum part, boolean canHealBroken);

    /**
     * @return the packed {@link LimbConditions}, computed again only when the limb data changed
     */
    int getLimbConditions();

    default boolean hasLimbPenalty(LimbPenalty penalty)
    {
        return LimbConditions.has(getLimbConditions(), penalty);
    }

    default int getRegionSeverity(LimbRegion region)
    {
        return LimbConditions.getSeverity(getLimbConditions(), region);
    }

    int getExpectedBrokenHearts();

    void updateBrokenHearts(Player player);

    /**
     * Check if at least one body part has health below provided health percent
     *
     * @param healthPercent health percent of the limb
     * @return isWounded or not
     */
    boolean isWoundedBelow(float healthPercent);

    /**
     * Force the health body damage sync server - client
     */
    void setManualDirty();

    /**
     * (Don't use this!) <br>
     * Checks if the capability needs an update
     *
     * @return boolean has localized body damage changed
     */
    boolean isDirty();

    /**
     * (Don't use this!) <br>
     * Sets the capability as updated
     */
    void setClean();

    /**
     * (Don't use this!) <br>
     * Gets the current tick of the packet timer
     *
     * @return int packetTimer
     */
    int getPacketTimer();

    /**
     * (Don't use this!) <br>
     * Runs a tick update for the player's localized body damage capability
     *
     * @param player
     * @param world
     * @param isStart
     */
    void tickUpdate(Player player, Level world, boolean isStart);
}
