package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;

public interface ITemperatureData
{

    ITemperatureData temperature(float temperatureValue);

    JsonObject build();
}
