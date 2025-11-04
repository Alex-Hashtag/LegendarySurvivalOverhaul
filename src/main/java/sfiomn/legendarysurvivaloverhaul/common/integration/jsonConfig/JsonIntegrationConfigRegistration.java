package sfiomn.legendarysurvivaloverhaul.common.integration.jsonConfig;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.config.JsonFileName;

import java.io.File;
import java.util.Map;

import static sfiomn.legendarysurvivaloverhaul.config.json_old.JsonConfigRegistration.*;

public class JsonIntegrationConfigRegistration
{
    public static void init(File configDir)
    {
        registerIntegrationDefaults(configDir);

        processAllIntegrationsJson(configDir);

        //deleteAllIntegrationsJson(configDir);
        //writeAllIntegrationsToJson(configDir);
    }

    public static void registerIntegrationDefaults(File configDir)
    {
        if (LegendarySurvivalOverhaul.originsLoaded)
        {
            JsonIntegrationConfig.registerDefaultOriginsTemperature("origins:blazeborn", -5, 15, -2, 0);
            JsonIntegrationConfig.registerDefaultOriginsTemperature("origins:merling", 0, 0, 3, 0);
            JsonIntegrationConfig.registerDefaultOriginsTemperature("origins:arachnid", 0, -5, 0, 0);
            JsonIntegrationConfig.registerDefaultOriginsTemperature("origins:feline", 0, 0, 0, 2);
            JsonIntegrationConfig.registerDefaultOriginsTemperature("origins:enderian", 0, 0, 10, 0);
        }
    }

    public static void writeAllIntegrationsToJson(File jsonDir)
    {
        if (LegendarySurvivalOverhaul.originsLoaded)
            manuallyWriteToJson(JsonFileName.ORIGINS_TEMP, JsonIntegrationConfig.originsTemperatures, jsonDir);
    }

    public static void deleteAllIntegrationsJson(File jsonDir)
    {
        deleteJson(JsonFileName.ORIGINS_TEMP, jsonDir);
    }

    public static void processAllIntegrationsJson(File jsonDir)
    {
        // Temperature
        Map<String, JsonTemperatureResistance> jsonOriginsTemperatures = processJson(JsonFileName.ORIGINS_TEMP, jsonDir);

        if (jsonOriginsTemperatures != null)
        {
            // remove default item config
            //JsonIntegrationConfig.originsTemperatures.clear();
            LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonOriginsTemperatures.size() + " origins temperature values from JSON");
            for (Map.Entry<String, JsonTemperatureResistance> entry : jsonOriginsTemperatures.entrySet())
            {
                JsonIntegrationConfig.registerOriginsTemperature(entry.getKey(), entry.getValue().temperature, entry.getValue().heatResistance, entry.getValue().coldResistance, entry.getValue().thermalResistance);
            }
        }
    }
}
