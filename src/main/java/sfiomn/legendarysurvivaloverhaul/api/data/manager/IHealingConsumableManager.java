package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;

public interface IHealingConsumableManager
{
    JsonHealingConsumable get(ResourceLocation itemRegistryName);
}
