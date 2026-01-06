package sfiomn.legendarysurvivaloverhaul.common.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class GoldFernFeature extends Feature<SimpleBlockConfiguration>
{
    public GoldFernFeature(Codec<SimpleBlockConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context)
    {
        SimpleBlockConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();

        BlockState state = config.toPlace().getState(context.random(), pos);

        if (context.random().nextFloat() < Config.Baked.goldFernChance)
        {
            if (state.is(BlockRegistry.ICE_FERN_CROP.get()))
            {
                state = BlockRegistry.ICE_FERN_GOLD.get().defaultBlockState();
            } else if (state.is(BlockRegistry.SUN_FERN_CROP.get()))
            {
                state = BlockRegistry.SUN_FERN_GOLD.get().defaultBlockState();
            }
        }

        if (!state.canSurvive(level, pos))
        {
            return false;
        }

        level.setBlock(pos, state, 2);
        return true;
    }
}
