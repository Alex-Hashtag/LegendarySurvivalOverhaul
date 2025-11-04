package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;

public interface ITemperatureBlockData
{

    ITemperatureBlockData temperature(float temperatureValue);

    ITemperatureBlockData addProperty(String propertyName, String propertyValue);

    ITemperatureBlockData copy();

    JsonObject build();
}
