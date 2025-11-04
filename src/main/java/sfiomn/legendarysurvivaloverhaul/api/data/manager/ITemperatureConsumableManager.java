package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;

import java.util.List;

public interface ITemperatureConsumableManager
{
    List<JsonTemperatureConsumable> get(ResourceLocation consumableRegistryName);
}
