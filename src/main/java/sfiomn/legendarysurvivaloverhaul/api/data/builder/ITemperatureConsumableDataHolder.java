package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonArray;

public interface ITemperatureConsumableDataHolder
{

    ITemperatureConsumableDataHolder addTemperature(ITemperatureConsumableData temperatureData);

    JsonArray build();
}
