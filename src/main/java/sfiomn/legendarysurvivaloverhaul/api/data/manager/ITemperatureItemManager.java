package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;

public interface ITemperatureItemManager
{
    JsonTemperatureResistance get(ResourceLocation itemRegistryName);
}
