package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableBlockData;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.HashMap;
import java.util.Map;

public class TemperatureConsumableBlockData implements ITemperatureConsumableBlockData
{
    private final Map<String, String> properties;
    private TemporaryModifierGroupEnum group;
    private int temperatureLevel;
    private int durationInTick;

    public TemperatureConsumableBlockData()
    {
        properties = new HashMap<>();
    }

    public TemperatureConsumableBlockData(TemperatureConsumableBlockData copy)
    {
        group = copy.group;
        temperatureLevel = copy.temperatureLevel;
        durationInTick = copy.durationInTick;
        properties = Map.copyOf(copy.properties);
    }

    @Override
    public ITemperatureConsumableBlockData group(TemporaryModifierGroupEnum group)
    {
        this.group = group;
        return this;
    }

    @Override
    public ITemperatureConsumableBlockData temperatureLevel(int temperatureLevel)
    {
        this.temperatureLevel = temperatureLevel;
        return this;
    }

    @Override
    public ITemperatureConsumableBlockData duration(int durationInTick)
    {
        this.durationInTick = durationInTick;
        return this;
    }

    @Override
    public ITemperatureConsumableBlockData addProperty(String propertyName, String propertyValue)
    {
        properties.put(propertyName, propertyValue);
        return this;
    }

    @Override
    public ITemperatureConsumableBlockData copy()
    {
        return new TemperatureConsumableBlockData(this);
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("group", this.group.name());
        json.addProperty("temperature_level", this.temperatureLevel);
        json.addProperty("duration", this.durationInTick);

        JsonObject jsonProperties = new JsonObject();
        properties.forEach(jsonProperties::addProperty);
        json.add("properties", jsonProperties);

        return json;
    }
}
