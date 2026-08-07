package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class SpreadPoint {
    private final BlockPos pos;
    private final Direction originDirection;
    private final double spreadCapacity;
    private final double influenceDistance;
    private final Level level;
    private final BlockState blockState;
    private final boolean loaded;
    private boolean canSeeSky;
    private boolean isWater;

    public SpreadPoint(BlockPos pos, Direction originDirection, double spreadCapacity, double influenceDistance, Level level) {
        this.pos = pos;
        this.originDirection = originDirection;
        this.spreadCapacity = spreadCapacity;
        this.influenceDistance = influenceDistance;
        this.level = level;
        //  Only read the block state if its chunk is already loaded. Level.getBlockState() on an
        //  unloaded chunk triggers a *blocking* chunk load on the server thread, stalling the tick.
        this.loaded = !level.isOutsideBuildHeight(pos) && level.hasChunkAt(pos);
        this.blockState = this.loaded ? level.getBlockState(pos) : Blocks.AIR.defaultBlockState();
        this.canSeeSky = false;
        this.isWater = false;
    }

    public double influenceDistance() {
        return influenceDistance;
    }

    public BlockPos position() {
        return pos;
    }

    public BlockState blockState() {
        return blockState;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Direction originalDirection() {
        return originDirection;
    }

    public double spreadCapacity() {
        return spreadCapacity;
    }

    public SpreadPoint spreadTo(BlockPos newBlockPos, Direction originDirection, float distance) {
        return new SpreadPoint(newBlockPos, originDirection, this.spreadCapacity - (distance * consumptionMultiplier(originDirection)), this.influenceDistance + distance, level);
    }

    public boolean isValidSpreadPoint(Direction originDirection) {
        //  Check we can spread the temperature influence in the new position meaning either AIR or a block a player can pass through
        //  Treat unloaded terrain as a barrier so the flood fill stops at the loaded boundary
        //  instead of forcing chunks to load.
        if (spreadCapacity <= 0 || !loaded) {
            return false;
        } else {
            if (blockState.isAir())
                return true;
            if (blockState.is(Blocks.WATER)) {
                this.isWater = true;
                return true;
            }

            return !blockState.isFaceSturdy(level, pos, originDirection.getOpposite());
        }
    }

    public boolean isValidSpreadDirection(Direction direction) {
        return loaded && !blockState.isFaceSturdy(level, pos, direction);
    }

    public void setCanSeeSky(boolean canSeeSky) {
        this.canSeeSky = canSeeSky;
    }

    private double consumptionMultiplier(Direction originDirection) {
        boolean upDirection = originDirection.getNormal().getY() > 0;
        double consumptionMultiplier = 1;
        if (this.isWater) {
            consumptionMultiplier *= Config.Baked.tempInfluenceInWaterDistMultiplier;
        } else if (this.canSeeSky) {
            consumptionMultiplier *= Config.Baked.tempInfluenceOutsideDistMultiplier;
        }
        if (upDirection) {
            consumptionMultiplier *= Config.Baked.tempInfluenceUpDistMultiplier;
        }
        return (1 / consumptionMultiplier);
    }
}