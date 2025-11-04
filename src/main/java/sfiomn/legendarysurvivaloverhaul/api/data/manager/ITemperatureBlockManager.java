package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBlock;

import java.util.List;

public interface ITemperatureBlockManager
{
    List<JsonTemperatureBlock> get(ResourceLocation blockRegistryName);
}
