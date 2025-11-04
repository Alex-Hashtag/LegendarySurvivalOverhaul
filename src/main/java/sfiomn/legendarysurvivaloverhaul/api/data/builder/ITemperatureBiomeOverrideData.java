package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;

public interface ITemperatureBiomeOverrideData
{

    ITemperatureBiomeOverrideData temperature(float temperatureValue);

    ITemperatureBiomeOverrideData isDry(boolean isDry);

    JsonObject build();
}
