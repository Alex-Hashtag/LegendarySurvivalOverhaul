package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;

public interface ITemperatureDimensionData {

    ITemperatureDimensionData temperature(float temperatureValue);
    ITemperatureDimensionData seaLevelHeight(int seaLevelHeight);
    ITemperatureDimensionData hasAltitude();
    ITemperatureDimensionData temperatureTimeCycleTicks(int cycleDurationTicks);

    JsonObject build();
}
