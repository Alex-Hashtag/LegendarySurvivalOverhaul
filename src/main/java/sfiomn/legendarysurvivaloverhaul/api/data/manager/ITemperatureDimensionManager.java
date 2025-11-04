package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureDimension;

public interface ITemperatureDimensionManager
{
    JsonTemperatureDimension get(ResourceLocation dimensionRegistryName);
}
