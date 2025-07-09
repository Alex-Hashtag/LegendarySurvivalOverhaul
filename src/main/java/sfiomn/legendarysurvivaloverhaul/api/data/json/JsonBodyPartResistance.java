package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;

public class JsonBodyPartResistance {
    public static final Codec<JsonBodyPartResistance> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.DOUBLE.fieldOf("body_resistance").forGetter(c -> c.bodyResistance),
            Codec.DOUBLE.fieldOf("head_resistance").forGetter(c -> c.headResistance),
            Codec.DOUBLE.fieldOf("chest_resistance").forGetter(c -> c.chestResistance),
            Codec.DOUBLE.fieldOf("right_arm_resistance").forGetter(c -> c.rightArmResistance),
            Codec.DOUBLE.fieldOf("left_arm_resistance").forGetter(c -> c.leftArmResistance),
            Codec.DOUBLE.fieldOf("legs_resistance").forGetter(c -> c.legsResistance),
            Codec.DOUBLE.fieldOf("feet_resistance").forGetter(c -> c.feetResistance)
    ).apply(inst, JsonBodyPartResistance::new));

    public double bodyResistance;
    public double headResistance;
    public double chestResistance;
    public double rightArmResistance;
    public double leftArmResistance;
    public double legsResistance;
    public double feetResistance;

    public JsonBodyPartResistance(double bodyResistance, double headResistance, double chestResistance,
                                  double rightArmResistance, double leftArmResistance,
                                  double legsResistance, double feetResistance) {
        this.bodyResistance = bodyResistance;
        this.headResistance = headResistance;
        this.chestResistance = chestResistance;
        this.rightArmResistance = rightArmResistance;
        this.leftArmResistance = leftArmResistance;
        this.legsResistance = legsResistance;
        this.feetResistance = feetResistance;
    }

    public double getBodyPartResistance(BodyPartEnum bodyPartEnum) {
        return switch (bodyPartEnum) {
            case HEAD -> headResistance;
            case CHEST -> chestResistance;
            case RIGHT_ARM -> rightArmResistance;
            case LEFT_ARM -> leftArmResistance;
            case RIGHT_LEG, LEFT_LEG -> legsResistance;
            case RIGHT_FOOT, LEFT_FOOT -> feetResistance;
        };
    }
}
