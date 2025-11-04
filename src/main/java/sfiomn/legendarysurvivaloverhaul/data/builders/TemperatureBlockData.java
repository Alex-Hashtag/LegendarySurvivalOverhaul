package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureBlockData;

import java.util.HashMap;
import java.util.Map;

public class TemperatureBlockData implements ITemperatureBlockData
{
    private final Map<String, String> properties;
    private float temperature;

    public TemperatureBlockData()
    {
        properties = new HashMap<>();
    }

    public TemperatureBlockData(TemperatureBlockData copy)
    {
        temperature = copy.temperature;
        properties = Map.copyOf(copy.properties);
    }

    @Override
    public ITemperatureBlockData temperature(float temperatureValue)
    {
        temperature = temperatureValue;
        return this;
    }

    @Override
    public ITemperatureBlockData addProperty(String propertyName, String propertyValue)
    {
        properties.put(propertyName, propertyValue);
        return this;
    }

    @Override
    public ITemperatureBlockData copy()
    {
        return new TemperatureBlockData(this);
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("temperature", this.temperature);

        JsonObject jsonProperties = new JsonObject();
        properties.forEach(jsonProperties::addProperty);
        json.add("properties", jsonProperties);
        return json;
    }
}
