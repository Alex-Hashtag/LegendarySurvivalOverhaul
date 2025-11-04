package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureFuelItemData;

public class TemperatureFuelItemData implements ITemperatureFuelItemData
{
    private ThermalTypeEnum thermalType;
    private int duration;

    public TemperatureFuelItemData()
    {
    }

    @Override
    public ITemperatureFuelItemData thermalType(ThermalTypeEnum thermalType)
    {
        this.thermalType = thermalType;
        return this;
    }

    @Override
    public ITemperatureFuelItemData duration(int durationInTicks)
    {
        this.duration = durationInTicks;
        return this;
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("thermal_type", this.thermalType.name());
        json.addProperty("duration", this.duration);
        return json;
    }
}
