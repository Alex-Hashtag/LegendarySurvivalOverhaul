package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;

public interface IBodyPartResistanceData
{

    IBodyPartResistanceData bodyResistance(float resistanceValue);

    IBodyPartResistanceData headResistance(float resistanceValue);

    IBodyPartResistanceData chestResistance(float resistanceValue);

    IBodyPartResistanceData rightArmResistance(float resistanceValue);

    IBodyPartResistanceData leftArmResistance(float resistanceValue);

    IBodyPartResistanceData legsResistance(float resistanceValue);

    IBodyPartResistanceData feetResistance(float resistanceValue);

    JsonObject build();
}
