package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonArray;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableBlockData;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableBlockDataHolder;

import java.util.ArrayList;
import java.util.List;

public class TemperatureConsumableBlockDataHolder implements ITemperatureConsumableBlockDataHolder
{
    private final List<ITemperatureConsumableBlockData> temperatureDataList;

    public TemperatureConsumableBlockDataHolder()
    {
        temperatureDataList = new ArrayList<>();
    }

    @Override
    public ITemperatureConsumableBlockDataHolder addTemperature(ITemperatureConsumableBlockData temperatureData)
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
