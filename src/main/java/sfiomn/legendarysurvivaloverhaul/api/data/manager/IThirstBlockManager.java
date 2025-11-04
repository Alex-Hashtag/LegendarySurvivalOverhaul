package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;

import java.util.List;

public interface IThirstBlockManager
{
    List<JsonThirstBlock> get(ResourceLocation blockRegistryName);

    JsonThirstBlock get(BlockState block);

    JsonThirstBlock get(FluidState fluid);
}
