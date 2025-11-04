package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBiomeOverride;

public interface ITemperatureBiomeManager
{
    JsonTemperatureBiomeOverride get(ResourceLocation biomeRegistryName);
}
