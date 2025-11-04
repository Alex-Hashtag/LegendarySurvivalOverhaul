package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;

public interface IBodyPartsDamageSourceManager
{
    JsonBodyPartsDamageSource get(String damageSourceRegistryName);
}
