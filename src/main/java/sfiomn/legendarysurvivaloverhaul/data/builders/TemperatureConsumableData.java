package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableBlockData;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureConsumableData;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

public class TemperatureConsumableData implements ITemperatureConsumableData
{
    private TemporaryModifierGroupEnum group;
    private int temperatureLevel;
    private int durationInTick;

    public TemperatureConsumableData()
    {
    }

    public TemperatureConsumableData(TemperatureConsumableData copy)
    {
        group = copy.group;
        temperatureLevel = copy.temperatureLevel;
        durationInTick = copy.durationInTick;
    }

    @Override
    public ITemperatureConsumableData group(TemporaryModifierGroupEnum group)
    {
        this.group = group;
        return this;
    }

    @Override
    public ITemperatureConsumableData temperatureLevel(int temperatureLevel)
    {
        this.temperatureLevel = temperatureLevel;
        return this;
    }

    @Override
    public ITemperatureConsumableData duration(int durationInTick)
    {
        this.durationInTick = durationInTick;
        return this;
    }

    @Override
    public ITemperatureConsumableBlockData asBlock()
    {
        return new TemperatureConsumableBlockData().duration(this.durationInTick).temperatureLevel(this.temperatureLevel).group(this.group);
    }

    @Override
    public ITemperatureConsumableData copy()
    {
        return new TemperatureConsumableData(this);
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("group", this.group.name());
        json.addProperty("temperature_level", this.temperatureLevel);
        json.addProperty("duration", this.durationInTick);

        return json;
    }
}
