package sfiomn.legendarysurvivaloverhaul.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;

public class SewingTableBlock extends HorizontalDirectionalBlock implements MenuProvider
{

    // --- 1) Required codec for 1.20.3+ ---
    public static final MapCodec<SewingTableBlock> CODEC = simpleCodec(SewingTableBlock::new);
    private static final Component CONTAINER_TITLE =
            Component.translatable("container." + LegendarySurvivalOverhaul.MOD_ID + ".sewing_table").withStyle();
    private static final VoxelShape BASE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape X_TOP = Block.box(6.0D, 1.0D, 2.0D, 10.0D, 10.0D, 14.0D);
    private static final VoxelShape Z_TOP = Block.box(2.0D, 1.0D, 6.0D, 14.0D, 10.0D, 10.0D);
    private static final VoxelShape X_AABB = Shapes.or(BASE, X_TOP);
    private static final VoxelShape Z_AABB = Shapes.or(BASE, Z_TOP);
    public SewingTableBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    // --- 2) Properties + ctor used by codec & registry ---
    public static Properties getProperties()
    {
        return Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(4.0f, 10.0f)
                .noOcclusion();
    }

    @Override
    public MapCodec<SewingTableBlock> codec()
    {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_AABB : Z_AABB;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state,
                                                     Level level,
                                                     BlockPos pos,
                                                     Player player,
                                                     BlockHitResult hit)
    {
        if (level.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        player.openMenu(state.getMenuProvider(level, pos));
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack,
                                                    BlockState state,
                                                    Level level,
                                                    BlockPos pos,
                                                    Player player,
                                                    InteractionHand hand,
                                                    BlockHitResult hit)
    {
        // If you don't want items to do anything special, delegate to the empty-hand handler:
        return this.useWithoutItem(state, level, pos, player, hit) == InteractionResult.CONSUME
                ? ItemInteractionResult.SUCCESS
                : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }


    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        return new SimpleMenuProvider(
                (windowId, inv, player) -> new SewingTableContainer(windowId, inv, ContainerLevelAccess.create(level, pos)),
                CONTAINER_TITLE
        );
    }

    @Override
    public @NotNull Component getDisplayName()
    {
        return CONTAINER_TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
    {
        return new SewingTableContainer(windowId, playerInventory,
                ContainerLevelAccess.create(player.level(), player.blockPosition()));
    }
}
