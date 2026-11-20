package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

/**
 * Penalties given by damaged limbs. A penalty is active when its region reaches the required severity
 * (2 = fractured, 3 = broken), and can be disabled in the config. <br>
 * Penalties are flags in {@link LimbConditions}, never potion effects.
 * <p>
 * {@link Side#CLIENT} penalties are only rendered or heard by the player, {@link Side#SERVER} penalties change the gameplay.
 */
public enum LimbPenalty
{
    // Head
    MUFFLED_HEARING(LimbRegion.HEAD, 2, Side.CLIENT),
    DEAFENED_HEARING(LimbRegion.HEAD, 3, Side.CLIENT),
    EAR_RINGING(LimbRegion.HEAD, 3, Side.CLIENT),
    MOTION_BLUR(LimbRegion.HEAD, 3, Side.CLIENT),
    LAZY_MOUSE(LimbRegion.HEAD, 3, Side.CLIENT),

    // Torso
    HEARTBEAT_ON_BREAK(LimbRegion.TORSO, 2, Side.CLIENT),
    FRAGILE_TORSO(LimbRegion.TORSO, 2, Side.SERVER),
    CONSTANT_HEARTBEAT(LimbRegion.TORSO, 3, Side.CLIENT),
    VERY_FRAGILE_TORSO(LimbRegion.TORSO, 3, Side.SERVER),
    SHORTNESS_OF_BREATH(LimbRegion.TORSO, 3, Side.SERVER),

    // Arms
    SLOW_BOW_DRAW(LimbRegion.ARMS, 2, Side.SERVER),
    SLOW_EATING(LimbRegion.ARMS, 2, Side.SERVER),
    NO_SWIMMING(LimbRegion.ARMS, 3, Side.SERVER),
    SLOW_BANDAGING(LimbRegion.ARMS, 3, Side.SERVER),
    WEAK_PROJECTILES(LimbRegion.ARMS, 3, Side.SERVER),

    // Legs
    SLOW_SPRINT(LimbRegion.LEGS, 2, Side.SERVER),
    NO_SNEAKING(LimbRegion.LEGS, 3, Side.SERVER),

    // Feet
    HARD_FALLING(LimbRegion.FEET, 2, Side.SERVER),
    VERY_HARD_FALLING(LimbRegion.FEET, 3, Side.SERVER);

    public enum Side
    {
        CLIENT,
        SERVER
    }

    private static final LimbPenalty[] VALUES = values();

    static
    {
        if (LimbConditions.PENALTY_OFFSET + VALUES.length > Integer.SIZE)
            throw new IllegalStateException("Too many limb penalties to fit in the limb conditions");
    }

    public final LimbRegion region;
    public final int requiredSeverity;
    public final Side side;
    /**
     * Bit of this penalty in {@link LimbConditions}
     */
    final int bit;

    LimbPenalty(LimbRegion region, int requiredSeverity, Side side)
    {
        this.region = region;
        this.requiredSeverity = requiredSeverity;
        this.side = side;
        this.bit = 1 << (LimbConditions.PENALTY_OFFSET + this.ordinal());
    }

    public static LimbPenalty[] cachedValues()
    {
        return VALUES;
    }

    public static LimbPenalty get(String name)
    {
        for (LimbPenalty penalty : VALUES)
            if (penalty.name().equalsIgnoreCase(name))
                return penalty;
        return null;
    }
}
