package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperature;

public interface ITemperatureDimensionManager {
    JsonTemperature get(ResourceLocation dimensionRegistryName);
}
