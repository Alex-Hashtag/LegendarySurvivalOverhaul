package sfiomn.legendarysurvivaloverhaul.common.integration.meadow;

import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.meadow.core.registry.ObjectRegistry;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;


public class MeadowUtil
{

    public static boolean isInWoodenWaterCauldron(BlockState blockState)
    {
        return LegendarySurvivalOverhaul.meadowLoaded && blockState.is(ObjectRegistry.WOODEN_WATER_CAULDRON.get());
    }
}
