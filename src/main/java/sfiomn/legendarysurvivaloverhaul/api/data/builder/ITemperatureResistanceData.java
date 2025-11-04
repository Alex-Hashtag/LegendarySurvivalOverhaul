package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;

public interface ITemperatureResistanceData
{

    ITemperatureResistanceData temperature(float temperatureValue);

    ITemperatureResistanceData heatResistance(float heatResistanceValue);

    ITemperatureResistanceData coldResistance(float coldResistanceValue);

    ITemperatureResistanceData thermalResistance(float thermalResistanceValue);

    JsonObject build();
}
