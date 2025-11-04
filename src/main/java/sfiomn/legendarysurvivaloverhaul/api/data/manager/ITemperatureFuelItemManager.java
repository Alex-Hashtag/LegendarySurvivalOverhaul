package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureFuelItem;

public interface ITemperatureFuelItemManager
{
    JsonTemperatureFuelItem get(ResourceLocation itemRegistryName);
}
