package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonArray;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableDataHolder;

import java.util.ArrayList;
import java.util.List;

public class TemperatureConsumableDataHolder implements ITemperatureConsumableDataHolder
{
    private final List<ITemperatureConsumableData> temperatureDataList;

    public TemperatureConsumableDataHolder()
    {
        temperatureDataList = new ArrayList<>();
    }

    @Override
    public ITemperatureConsumableDataHolder addTemperature(ITemperatureConsumableData temperatureData)
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
