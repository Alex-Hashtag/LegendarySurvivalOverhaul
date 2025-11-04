package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IBodyPartResistanceData;

public class BodyPartResistanceData implements IBodyPartResistanceData
{
    private float bodyResistance;
    private float headResistance;
    private float chestResistance;
    private float rightArmResistance;
    private float leftArmResistance;
    private float legsResistance;
    private float feetResistance;

    public BodyPartResistanceData()
    {
    }

    @Override
    public IBodyPartResistanceData bodyResistance(float resistanceValue)
    {
        bodyResistance = resistanceValue;
        return this;
    }

    @Override
    public IBodyPartResistanceData headResistance(float resistanceValue)
    {
        headResistance = resistanceValue;
        return this;
    }

    @Override
    public IBodyPartResistanceData chestResistance(float resistanceValue)
    {
        chestResistance = resistanceValue;
        return this;
    }

    @Override
    public IBodyPartResistanceData rightArmResistance(float resistanceValue)
    {
        rightArmResistance = resistanceValue;
        return this;
    }

    @Override
    public IBodyPartResistanceData leftArmResistance(float resistanceValue)
    {
        leftArmResistance = resistanceValue;
        return this;
    }

    @Override
    public IBodyPartResistanceData legsResistance(float resistanceValue)
    {
        legsResistance = resistanceValue;
        return this;
    }

    @Override
    public IBodyPartResistanceData feetResistance(float resistanceValue)
    {
        feetResistance = resistanceValue;
        return this;
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("body_resistance", this.bodyResistance);
        json.addProperty("head_resistance", this.headResistance);
        json.addProperty("chest_resistance", this.chestResistance);
        json.addProperty("right_arm_resistance", this.rightArmResistance);
        json.addProperty("left_arm_resistance", this.leftArmResistance);
        json.addProperty("legs_resistance", this.legsResistance);
        json.addProperty("feet_resistance", this.feetResistance);
        return json;
    }
}
