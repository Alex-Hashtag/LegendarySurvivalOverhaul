package sfiomn.legendarysurvivaloverhaul.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

public class IceFernGoldBlock extends BushBlock
{

    // 1) Required for 1.20.3+ block serialization
    public static final MapCodec<IceFernGoldBlock> CODEC = simpleCodec(IceFernGoldBlock::new);

    public IceFernGoldBlock(Properties properties)
    {
        super(properties);
    }

    // 2) Properties + ctor used by codec & registry
    public static Properties getProperties()
    {
        return Properties
                .of()
                .mapColor(MapColor.PLANT)
                .randomTicks()
                .noOcclusion()
                .noCollission()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }

    @Override
    public MapCodec<IceFernGoldBlock> codec()
    {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter level, BlockPos pos)
    {
        return blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.DIRT) ||
                blockState.is(Blocks.COARSE_DIRT) || blockState.is(Blocks.PODZOL) ||
                blockState.is(Blocks.FARMLAND) || blockState.is(Blocks.SNOW_BLOCK);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand)
    {
        double offsetX = (2 * rand.nextFloat() - 1) * 0.3F;
        double offsetZ = (2 * rand.nextFloat() - 1) * 0.3F;

        double x = pos.getX() + 0.5D + offsetX;
        double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
        double z = pos.getZ() + 0.5D + offsetZ;

        if (level.getGameTime() % 3 == 0)
        {
            level.addParticle(ParticleTypeRegistry.ICE_FERN_BLOSSOM.get(), x, y, z, 0.04D, 0.01D, 0.04D);
        }
    }
}
