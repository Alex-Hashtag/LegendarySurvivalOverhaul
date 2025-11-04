package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonArray;

public interface ITemperatureConsumableBlockDataHolder
{

    ITemperatureConsumableBlockDataHolder addTemperature(ITemperatureConsumableBlockData temperatureData);

    JsonArray build();
}
