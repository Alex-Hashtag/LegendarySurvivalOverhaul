package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import java.util.List;

/**
 * Limb conditions derived from the limb data, packed in a single int so they cost no memory and no allocation:
 * <ul>
 *     <li>bits 0-9: severity of each {@link LimbRegion}, 2 bits per region (0 healthy to 3 broken)</li>
 *     <li>bits 10-28: one flag per active {@link LimbPenalty}</li>
 * </ul>
 * Conditions are only computed again when the limb data changes, checking a penalty is a single bit test.
 */
public final class LimbConditions
{
    public static final int NONE = 0;

    // Compile time constants, so the enums using them don't depend on this class initialization
    static final int SEVERITY_BITS = 2;
    static final int SEVERITY_MASK = (1 << SEVERITY_BITS) - 1;
    static final int PENALTY_OFFSET = 10;
    private static final int SEVERITIES_MASK = (1 << PENALTY_OFFSET) - 1;

    // Penalties enabled in the config, as a mask of penalty bits (all by default)
    private static int enabledPenalties = ~0;
    // Changes each time the enabled penalties change, so cached conditions can be computed again
    private static int configVersion = 0;

    private LimbConditions()
    {
    }

    /**
     * Compute the limb conditions from the packed limb data of {@link IBodyDamageAttachment}.
     */
    public static int compute(int limbData)
    {
        int conditions = 0;
        for (LimbRegion region : LimbRegion.cachedValues())
        {
            int severity = 0;
            for (BodyPartEnum bodyPart : region.getBodyParts())
                severity = Math.max(severity, getPenaltyDamage(limbData, bodyPart));
            conditions |= severity << (region.ordinal() * SEVERITY_BITS);
        }

        for (LimbPenalty penalty : LimbPenalty.cachedValues())
        {
            if (getSeverity(conditions, penalty.region) >= penalty.requiredSeverity)
                conditions |= penalty.bit;
        }
        return conditions & (enabledPenalties | SEVERITIES_MASK);
    }

    /**
     * Damage of the limb used for its penalties: a shield on a broken limb makes it count as having one limb heart left. <br>
     * It only changes the penalties, the limb stays broken (broken hearts, healing) and breaking the shield doesn't hurt it.
     */
    public static int getPenaltyDamage(int limbData, BodyPartEnum bodyPart)
    {
        int damage = IBodyDamageAttachment.getLimbDamage(limbData, bodyPart);
        if (damage >= IBodyDamageAttachment.MAX_LIMB_DAMAGE && IBodyDamageAttachment.hasLimbShield(limbData, bodyPart))
            return IBodyDamageAttachment.MAX_LIMB_DAMAGE - 1;
        return damage;
    }

    public static int getSeverity(int conditions, LimbRegion region)
    {
        return (conditions >> (region.ordinal() * SEVERITY_BITS)) & SEVERITY_MASK;
    }

    public static boolean has(int conditions, LimbPenalty penalty)
    {
        return (conditions & penalty.bit) != 0;
    }

    /**
     * @return true if at least one penalty is active
     */
    public static boolean hasAnyPenalty(int conditions)
    {
        return (conditions >>> PENALTY_OFFSET) != 0;
    }

    /**
     * @return true if the penalty is active in the new conditions and wasn't in the old ones
     */
    public static boolean gained(int oldConditions, int newConditions, LimbPenalty penalty)
    {
        return !has(oldConditions, penalty) && has(newConditions, penalty);
    }

    /**
     * @return true if the penalty was active in the old conditions and isn't anymore
     */
    public static boolean lost(int oldConditions, int newConditions, LimbPenalty penalty)
    {
        return has(oldConditions, penalty) && !has(newConditions, penalty);
    }

    /**
     * @return true if the penalty set differs, ignoring region severity changes that don't change any penalty
     */
    public static boolean penaltiesChanged(int oldConditions, int newConditions)
    {
        return (oldConditions >>> PENALTY_OFFSET) != (newConditions >>> PENALTY_OFFSET);
    }

    /**
     * Called when the config is baked.
     *
     * @param disabledPenalties names of the disabled {@link LimbPenalty}
     */
    public static void setDisabledPenalties(List<? extends String> disabledPenalties)
    {
        int enabled = ~0;
        for (String name : disabledPenalties)
        {
            LimbPenalty penalty = LimbPenalty.get(name);
            if (penalty != null)
                enabled &= ~penalty.bit;
        }
        enabledPenalties = enabled;
        configVersion++;
    }

    public static int getConfigVersion()
    {
        return configVersion;
    }
}
