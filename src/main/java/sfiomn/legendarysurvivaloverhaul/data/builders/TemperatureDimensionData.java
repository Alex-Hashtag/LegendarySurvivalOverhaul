package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureDimensionData;

public class TemperatureDimensionData implements ITemperatureDimensionData
{
    private float temperature;
    private int seaLevelHeight;
    private boolean hasAltitude;

    public TemperatureDimensionData()
    {
    }

    @Override
    public ITemperatureDimensionData temperature(float temperatureValue)
    {
        temperature = temperatureValue;
        return this;
    }

    @Override
    public ITemperatureDimensionData seaLevelHeight(int seaLevelHeightValue)
    {
        seaLevelHeight = seaLevelHeightValue;
        return this;
    }

    @Override
    public ITemperatureDimensionData hasAltitude()
    {
        hasAltitude = true;
        return this;
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("temperature", this.temperature);
        json.addProperty("sea_level_height", this.seaLevelHeight);
        json.addProperty("has_altitude", this.hasAltitude);
        return json;
    }
}
