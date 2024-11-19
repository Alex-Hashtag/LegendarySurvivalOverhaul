package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.Tags;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

public class SunFernGoldBlock extends BushBlock implements IPlantable {

    public static final Properties properties = getProperties();

    public SunFernGoldBlock() {
        super(properties);
    }

    public static Properties getProperties() {
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
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockReader, BlockPos blockPos) {
        return blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.DIRT) || blockState.is(Blocks.COARSE_DIRT) || blockState.is(Blocks.PODZOL) || blockState.is(Blocks.FARMLAND) || blockState.is(Tags.Blocks.SAND);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        double offsetX = (2 * rand.nextFloat() - 1) * 0.3F;
        double offsetZ = (2 * rand.nextFloat() - 1) * 0.3F;

        double x = pos.getX() + 0.5D + offsetX;
        double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
        double z = pos.getZ() + 0.5D + offsetZ;

        if (level.getGameTime() % 3 == 0)
            level.addParticle(ParticleTypeRegistry.SUN_FERN_BLOSSOM.get(), x, y, z, 0.04D, 0.01D, 0.04D);
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.DESERT;
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return defaultBlockState();
    }
}
