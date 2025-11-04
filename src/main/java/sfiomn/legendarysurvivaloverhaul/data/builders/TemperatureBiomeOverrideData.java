package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureBiomeOverrideData;

public class TemperatureBiomeOverrideData implements ITemperatureBiomeOverrideData
{
    private float temperature;
    private boolean isDry;

    public TemperatureBiomeOverrideData()
    {
    }

    @Override
    public ITemperatureBiomeOverrideData temperature(float temperatureValue)
    {
        this.temperature = temperatureValue;
        return this;
    }

    @Override
    public ITemperatureBiomeOverrideData isDry(boolean isDry)
    {
        this.isDry = isDry;
        return this;
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("temperature", this.temperature);
        json.addProperty("is_dry", this.isDry);
        return json;
    }
}
