package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;

public interface IBodyResistanceItemManager
{
    JsonBodyPartResistance get(ResourceLocation itemRegistryName);
}
