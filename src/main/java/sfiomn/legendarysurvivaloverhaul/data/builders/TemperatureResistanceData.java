package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureResistanceData;

public class TemperatureResistanceData implements ITemperatureResistanceData
{
    private float temperature;
    private float heatResistance;
    private float coldResistance;
    private float thermalResistance;

    public TemperatureResistanceData()
    {
    }

    @Override
    public ITemperatureResistanceData temperature(float temperatureValue)
    {
        temperature = temperatureValue;
        return this;
    }

    @Override
    public ITemperatureResistanceData heatResistance(float heatResistanceValue)
    {
        heatResistance = heatResistanceValue;
        return this;
    }

    @Override
    public ITemperatureResistanceData coldResistance(float coldResistanceValue)
    {
        coldResistance = coldResistanceValue;
        return this;
    }

    @Override
    public ITemperatureResistanceData thermalResistance(float thermalResistanceValue)
    {
        thermalResistance = thermalResistanceValue;
        return this;
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("temperature", this.temperature);
        json.addProperty("heat_resistance", this.heatResistance);
        json.addProperty("cold_resistance", this.coldResistance);
        json.addProperty("thermal_resistance", this.thermalResistance);
        return json;
    }
}
