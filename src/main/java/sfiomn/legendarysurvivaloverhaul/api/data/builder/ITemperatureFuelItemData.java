package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;

public interface ITemperatureFuelItemData
{

    ITemperatureFuelItemData thermalType(ThermalTypeEnum thermalType);

    ITemperatureFuelItemData duration(int durationInTicks);

    JsonObject build();
}
