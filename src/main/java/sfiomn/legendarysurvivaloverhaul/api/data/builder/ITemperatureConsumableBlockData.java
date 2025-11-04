package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

public interface ITemperatureConsumableBlockData
{

    ITemperatureConsumableBlockData group(TemporaryModifierGroupEnum group);

    ITemperatureConsumableBlockData temperatureLevel(int temperatureLevel);

    ITemperatureConsumableBlockData duration(int durationInTick);

    ITemperatureConsumableBlockData addProperty(String propertyName, String propertyValue);

    ITemperatureConsumableBlockData copy();

    JsonObject build();
}
