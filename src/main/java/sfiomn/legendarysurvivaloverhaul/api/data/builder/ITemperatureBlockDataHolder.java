package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonArray;

public interface ITemperatureBlockDataHolder
{

    ITemperatureBlockDataHolder addTemperature(ITemperatureBlockData temperatureData);

    JsonArray build();
}
