package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;

import java.util.List;

public interface IThirstBlockManager {
    List<JsonThirstBlock> get(ResourceLocation blockRegistryName);
}
