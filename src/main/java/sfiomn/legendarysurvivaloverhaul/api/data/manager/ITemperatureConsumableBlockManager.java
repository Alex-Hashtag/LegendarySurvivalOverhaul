package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumableBlock;

import java.util.List;

public interface ITemperatureConsumableBlockManager
{
    List<JsonTemperatureConsumableBlock> get(ResourceLocation consumableRegistryName);
}
