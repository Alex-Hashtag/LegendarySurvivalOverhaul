package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;

public class JsonBodyPartResistance
{
    public static final Codec<JsonBodyPartResistance> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.FLOAT.optionalFieldOf("body_resistance", 0.0f).forGetter(c -> c.bodyResistance),
            Codec.FLOAT.optionalFieldOf("head_resistance", 0.0f).forGetter(c -> c.headResistance),
            Codec.FLOAT.optionalFieldOf("chest_resistance", 0.0f).forGetter(c -> c.chestResistance),
            Codec.FLOAT.optionalFieldOf("right_arm_resistance", 0.0f).forGetter(c -> c.rightArmResistance),
            Codec.FLOAT.optionalFieldOf("left_arm_resistance", 0.0f).forGetter(c -> c.leftArmResistance),
            Codec.FLOAT.optionalFieldOf("legs_resistance", 0.0f).forGetter(c -> c.legsResistance),
            Codec.FLOAT.optionalFieldOf("feet_resistance", 0.0f).forGetter(c -> c.feetResistance)
    ).apply(inst, JsonBodyPartResistance::new));

    public float bodyResistance;
    public float headResistance;
    public float chestResistance;
    public float rightArmResistance;
    public float leftArmResistance;
    public float legsResistance;
    public float feetResistance;

    public JsonBodyPartResistance(float bodyResistance, float headResistance, float chestResistance,
                                  float rightArmResistance, float leftArmResistance,
                                  float legsResistance, float feetResistance)
    {
        this.bodyResistance = bodyResistance;
        this.headResistance = headResistance;
        this.chestResistance = chestResistance;
        this.rightArmResistance = rightArmResistance;
        this.leftArmResistance = leftArmResistance;
        this.legsResistance = legsResistance;
        this.feetResistance = feetResistance;
    }

    public float getBodyPartResistance(BodyPartEnum bodyPartEnum)
    {
        return switch (bodyPartEnum)
        {
            case HEAD -> headResistance;
            case CHEST -> chestResistance;
            case RIGHT_ARM -> rightArmResistance;
            case LEFT_ARM -> leftArmResistance;
            case RIGHT_LEG, LEFT_LEG -> legsResistance;
            case RIGHT_FOOT, LEFT_FOOT -> feetResistance;
        };
    }
}
