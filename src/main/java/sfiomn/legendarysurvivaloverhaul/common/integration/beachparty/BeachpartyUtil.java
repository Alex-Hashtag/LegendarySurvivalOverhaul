package sfiomn.legendarysurvivaloverhaul.common.integration.beachparty;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.block.BeachParasolBlock;
import net.satisfy.beachparty.core.block.HoodedBeachChair;
import net.satisfy.beachparty.core.entity.ChairEntity;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import javax.annotation.Nonnull;

public class BeachpartyUtil
{

    public static boolean isUnderParasol(Level level, Player player, BlockPos pos)
    {
        if (LegendarySurvivalOverhaul.beachpartyLoaded)
        {

            if (player != null && player.getVehicle() != null && player.getVehicle() instanceof ChairEntity)
            {
                BlockPos checkPos = player.blockPosition().offset(0, 1, 0);
                BlockState blockState = level.getBlockState(checkPos);
                return blockState.getBlock() instanceof HoodedBeachChair;
            } else
            {
                BlockPos checkPos = pos.offset(0, 1, 0);
                BlockState blockState = level.getBlockState(checkPos);
                if (blockState.getBlock() instanceof BeachParasolBlock)
                {
                    return blockState.hasProperty(BeachParasolBlock.OPEN) ? blockState.getValue(BeachParasolBlock.OPEN) : false;
                }
            }
        }
        return false;
    }

    public static boolean canProvideShade(@Nonnull ResourceLocation itemRegistryName)
    {
        return LegendarySurvivalOverhaul.beachpartyLoaded && (itemRegistryName.toString().equals("beachparty:hooded_beach_chair") || itemRegistryName.toString().equals("beachparty:beach_parasol"));
    }
}
