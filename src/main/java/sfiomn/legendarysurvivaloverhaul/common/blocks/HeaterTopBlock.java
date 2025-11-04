package sfiomn.legendarysurvivaloverhaul.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class HeaterTopBlock extends HorizontalDirectionalBlock
{

    // Required codec for 1.20.3+ blocks
    public static final MapCodec<HeaterTopBlock> CODEC = simpleCodec(HeaterTopBlock::new);
    private static final VoxelShape BASE = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 2.0d, 16.0d);
    private static final VoxelShape TUBE = Block.box(4.0d, 2.0d, 4.0d, 12.0d, 14.0d, 12.0d);
    private static final VoxelShape TOP = Block.box(3.0d, 14.0d, 3.0d, 13.0d, 16.0d, 13.0d);
    private static final VoxelShape XZ_AXIS_AABB = Shapes.or(BASE, TUBE, TOP);
    // Constructor that the codec (and your registry) will call
    public HeaterTopBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    // Convenience factory for your registry call-site
    public static Properties getProperties()
    {
        return Properties
                .of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3f, 10f)
                .noOcclusion();
    }

    @Override
    public MapCodec<HeaterTopBlock> codec()
    {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return XZ_AXIS_AABB;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state,
                                                     Level level,
                                                     BlockPos pos,
                                                     Player player,
                                                     BlockHitResult hit)
    {
        BlockState bottomState = level.getBlockState(pos.below());
        if (bottomState.is(BlockRegistry.HEATER.get()) && bottomState.getBlock() instanceof HeaterBaseBlock heater)
        {
            // Delegate empty-hand interaction to the bottom block
            return heater.useWithoutItem(bottomState, level, pos.below(), player, hit);
        }
        // Let vanilla/other handlers try
        return InteractionResult.PASS;
    }

    public @NotNull ItemInteractionResult useItemOn(ItemStack stack,
                                                    BlockState state,
                                                    Level level,
                                                    BlockPos pos,
                                                    Player player,
                                                    InteractionHand hand,
                                                    BlockHitResult hit)
    {
        BlockState bottomState = level.getBlockState(pos.below());
        if (bottomState.is(BlockRegistry.HEATER.get()) && bottomState.getBlock() instanceof HeaterBaseBlock heater)
        {
            // Delegate item-in-hand interaction to the bottom block
            return heater.onTopUseItem(stack, bottomState, level, pos.below(), player, hand, hit);
        }
        // Tell Minecraft to continue with default item/block behavior
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        super.onRemove(state, level, pos, newState, isMoving);
        if (!state.is(newState.getBlock()) && level.getBlockState(pos.below()).getBlock() instanceof HeaterBaseBlock)
        {
            level.removeBlock(pos.below(), false);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        // Use getOpposite() if you want the block to face the player on placement
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }
}
