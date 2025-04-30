package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;

public interface IBodyPartsDamageSourceManager {
    JsonBodyPartsDamageSource get(String damageSourceRegistryName);
}
