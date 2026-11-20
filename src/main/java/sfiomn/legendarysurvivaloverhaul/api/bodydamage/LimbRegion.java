package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

/**
 * Group of limbs sharing the same penalties. <br>
 * A region's severity is the damage of its most damaged limb, so a single broken leg is enough for the legs penalties.
 */
public enum LimbRegion
{
    HEAD(BodyPartEnum.HEAD),
    TORSO(BodyPartEnum.CHEST),
    ARMS(BodyPartEnum.RIGHT_ARM, BodyPartEnum.LEFT_ARM),
    LEGS(BodyPartEnum.RIGHT_LEG, BodyPartEnum.LEFT_LEG),
    FEET(BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_FOOT);

    private static final LimbRegion[] VALUES = values();

    static
    {
        if (VALUES.length * LimbConditions.SEVERITY_BITS != LimbConditions.PENALTY_OFFSET)
            throw new IllegalStateException("LimbConditions.PENALTY_OFFSET must match the number of limb regions");
    }

    private final BodyPartEnum[] bodyParts;

    LimbRegion(BodyPartEnum... bodyParts)
    {
        this.bodyParts = bodyParts;
    }

    public BodyPartEnum[] getBodyParts()
    {
        return bodyParts;
    }

    public static LimbRegion[] cachedValues()
    {
        return VALUES;
    }

    public static LimbRegion of(BodyPartEnum bodyPart)
    {
        return switch (bodyPart)
        {
            case HEAD -> HEAD;
            case CHEST -> TORSO;
            case RIGHT_ARM, LEFT_ARM -> ARMS;
            case RIGHT_LEG, LEFT_LEG -> LEGS;
            case RIGHT_FOOT, LEFT_FOOT -> FEET;
        };
    }
}
