package sfiomn.legendarysurvivaloverhaul.common.integration.crayfish;

import com.mrcrayfish.furniture.refurbished.blockentity.BasinBlockEntity;
import com.mrcrayfish.furniture.refurbished.blockentity.KitchenSinkBlockEntity;
import com.mrcrayfish.furniture.refurbished.blockentity.fluid.FluidContainer;
import com.mrcrayfish.furniture.refurbished.blockentity.fluid.IFluidContainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;

public class CrayfishFurnitureUtil {

    public static InteractionResult tryFillCanteenFromSinkOrBasin(Level level, BlockPos pos, Player player, ItemStack canteen) {
        if (!LegendarySurvivalOverhaul.crayfishFurnitureLoaded) {
            return InteractionResult.PASS;
        }

        try {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) {
                return InteractionResult.PASS;
            }

            // Check if it's a Kitchen Sink or Basin
            boolean isKitchenSink = blockEntity instanceof KitchenSinkBlockEntity;
            boolean isBasin = blockEntity instanceof BasinBlockEntity;
            
            if (!isKitchenSink && !isBasin) {
                return InteractionResult.PASS;
            }

            if (!(blockEntity instanceof IFluidContainerBlock fluidContainerBlock)) {
                return InteractionResult.PASS;
            }

            FluidContainer container = fluidContainerBlock.getFluidContainer();
            if (container == null) {
                return InteractionResult.PASS;
            }

            long storedAmount = container.getStoredAmount();

            // Ensure container has water and is not empty
            if (container.isEmpty()) {
                return InteractionResult.PASS;
            }
            
            if (!container.getStoredFluid().isSame(Fluids.WATER)) {
                return InteractionResult.PASS;
            }
            
            if (storedAmount <= 0) {
                return InteractionResult.PASS;
            }

            if (!(canteen.getItem() instanceof CanteenItem canteenItem)) {
                return InteractionResult.PASS;
            }

            if (!canteenItem.canFill(canteen, level)) {
                return InteractionResult.PASS;
            }

            long bottleCapacity = FluidContainer.BOTTLE_CAPACITY;

            if (storedAmount >= bottleCapacity) {
                if (!level.isClientSide) {
                    container.pull(bottleCapacity, false);
                    canteenItem.fill(canteen, level);
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playSound(SoundEvents.BOTTLE_FILL, 1.0F, 1.0F);
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                LegendarySurvivalOverhaul.LOGGER.debug("Not enough fluid in container: {} < {}", storedAmount, bottleCapacity);
            }

            return InteractionResult.PASS;
        } catch (NoClassDefFoundError e) {
            LegendarySurvivalOverhaul.LOGGER.error("Crayfish Furniture classes not found - mod may not be installed", e);
            LegendarySurvivalOverhaul.crayfishFurnitureLoaded = false;
            return InteractionResult.PASS;
        } catch (Exception e) {
            LegendarySurvivalOverhaul.LOGGER.error("Error occurred while trying to fill canteen from Crayfish furniture", e);
            return InteractionResult.PASS;
        }
    }

    public static InteractionResult tryEmptyCanteenIntoSinkOrBasin(Level level, BlockPos pos, Player player, ItemStack canteen) {
        if (!LegendarySurvivalOverhaul.crayfishFurnitureLoaded) {
            return InteractionResult.PASS;
        }

        try {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) {
                return InteractionResult.PASS;
            }

            // Check if it's a Kitchen Sink or Basin
            boolean isKitchenSink = blockEntity instanceof KitchenSinkBlockEntity;
            boolean isBasin = blockEntity instanceof BasinBlockEntity;
            
            if (!isKitchenSink && !isBasin) {
                return InteractionResult.PASS;
            }

            if (!(blockEntity instanceof IFluidContainerBlock fluidContainerBlock)) {
                return InteractionResult.PASS;
            }

            FluidContainer container = fluidContainerBlock.getFluidContainer();
            if (container == null) {
                return InteractionResult.PASS;
            }

            if (!(canteen.getItem() instanceof CanteenItem canteenItem)) {
                return InteractionResult.PASS;
            }

            // Check if canteen has water to empty
            if (!CanteenItem.canDrink(canteen)) {
                return InteractionResult.PASS;
            }

            // Check if container has room
            long currentAmount = container.getStoredAmount();
            long maxCapacity = container.getCapacity();
            long bottleCapacity = FluidContainer.BOTTLE_CAPACITY;

            if (currentAmount + bottleCapacity > maxCapacity) {
                return InteractionResult.PASS;
            }

            // Empty canteen into container
            if (!level.isClientSide) {
                container.push(Fluids.WATER, bottleCapacity, false);
                CanteenItem.shrinkCapacity(canteen);
            }

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.playSound(SoundEvents.BOTTLE_EMPTY, 1.0F, 1.0F);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);

        } catch (NoClassDefFoundError e) {
            LegendarySurvivalOverhaul.LOGGER.error("Crayfish Furniture classes not found - mod may not be installed", e);
            LegendarySurvivalOverhaul.crayfishFurnitureLoaded = false;
            return InteractionResult.PASS;
        } catch (Exception e) {
            LegendarySurvivalOverhaul.LOGGER.error("Error occurred while trying to empty canteen into Crayfish furniture", e);
            return InteractionResult.PASS;
        }
    }

    /**
     * Checks if the given BlockState is a Kitchen Sink or Basin from Crayfish Furniture mod.
     * Used to determine if a block should be handled by useOn() method.
     */
    public static boolean isSinkOrBasin(BlockState blockState) {
        if (!LegendarySurvivalOverhaul.crayfishFurnitureLoaded) {
            return false;
        }
        
        try {
            // Check by class name to avoid direct references that might not exist
            String blockClassName = blockState.getBlock().getClass().getName();
            return blockClassName.contains("KitchenSinkBlock") || blockClassName.contains("BasinBlock");
        } catch (Exception e) {
            return false;
        }
    }
}
