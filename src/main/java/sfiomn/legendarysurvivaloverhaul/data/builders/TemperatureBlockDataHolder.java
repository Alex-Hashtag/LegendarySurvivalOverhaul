package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonArray;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureBlockData;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureBlockDataHolder;

import java.util.ArrayList;
import java.util.List;

public class TemperatureBlockDataHolder implements ITemperatureBlockDataHolder
{
    private final List<ITemperatureBlockData> temperatureDataList;

    public TemperatureBlockDataHolder()
    {
        temperatureDataList = new ArrayList<>();
    }

    @Override
    public TemperatureBlockDataHolder addTemperature(ITemperatureBlockData temperatureData)
    {
        temperatureDataList.add(temperatureData.copy());
        return this;
    }

    @Override
    public JsonArray build()
    {
        JsonArray json = new JsonArray();
        temperatureDataList.forEach(t -> json.add(t.build()));
        return json;
    }
}
