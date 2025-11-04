package sfiomn.legendarysurvivaloverhaul.common.integration.jsonConfig;

import com.google.common.collect.Maps;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.temperature.JsonTemperatureResistance;

import java.util.Map;

public class JsonIntegrationConfig
{
    public static Map<String, JsonTemperatureResistance> originsTemperatures = Maps.newHashMap();
    public static Map<String, JsonTemperatureResistance> defaultOriginsTemperatures = Maps.newHashMap();

    public static void registerDefaultOriginsTemperature(String registryName, float temperature, float heatResistance, float coldResistance, float thermalResistance)
    {
        if (!defaultOriginsTemperatures.containsKey(registryName))
            defaultOriginsTemperatures.put(registryName, new JsonTemperatureResistance(temperature, heatResistance, coldResistance, thermalResistance));
    }

    public static void registerOriginsTemperature(String registryName, float temperature, float heatResistance, float coldResistance, float thermalResistance)
    {
        if (!originsTemperatures.containsKey(registryName))
            originsTemperatures.put(registryName, new JsonTemperatureResistance(temperature, heatResistance, coldResistance, thermalResistance));
    }
}
