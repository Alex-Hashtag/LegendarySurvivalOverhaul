package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

public interface ITemperatureConsumableData
{

    ITemperatureConsumableData group(TemporaryModifierGroupEnum group);

    ITemperatureConsumableData temperatureLevel(int temperatureLevel);

    ITemperatureConsumableData duration(int durationInTick);

    ITemperatureConsumableBlockData asBlock();

    ITemperatureConsumableData copy();

    JsonObject build();
}
