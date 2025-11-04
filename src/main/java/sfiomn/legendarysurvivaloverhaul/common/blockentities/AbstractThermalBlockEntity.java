package sfiomn.legendarysurvivaloverhaul.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureFuelItem;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.common.blocks.ThermalBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractThermalBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible
{
    public static final int SLOT_COUNT = 4;
    protected final ContainerData dataAccess;
    private final ThermalTypeEnum thermalType;
    protected NonNullList<ItemStack> items;
    private int fuelTime;
    private int fuelDuration;
    private int tickCount;

    public AbstractThermalBlockEntity(BlockEntityType<?> blockEntityType, ThermalTypeEnum thermalType, BlockPos pos, BlockState state)
    {
        super(blockEntityType, pos, state);
        this.items = NonNullList.withSize(AbstractThermalBlockEntity.SLOT_COUNT, ItemStack.EMPTY);
        this.tickCount = 0;
        this.fuelTime = 0;
        this.thermalType = thermalType;
        this.dataAccess = new ContainerData()
        {
            public int get(int index)
            {
                return switch (index)
                {
                    case 0 -> AbstractThermalBlockEntity.this.fuelTime;
                    case 1 -> AbstractThermalBlockEntity.this.fuelDuration;
                    default -> 0;
                };
            }

            public void set(int index, int value)
            {
                switch (index)
                {
                    case 0 -> AbstractThermalBlockEntity.this.fuelTime = value;
                    case 1 -> AbstractThermalBlockEntity.this.fuelDuration = value;
                }

            }

            public int getCount()
            {
                return 2;
            }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractThermalBlockEntity entity)
    {
        if (level == null)
        {
            return;
        }

        if (!entity.isPowered())
        {
            // Remove LIT state if unpowered
            if (state.getValue(ThermalBlock.LIT) && !level.isClientSide)
            {
                level.setBlockAndUpdate(pos, state.setValue(ThermalBlock.LIT, false));
                entity.setChanged();
            }
            return;
        }


        // Put LIT state if powered & fuel already there
        if (entity.isLit() && !state.getValue(ThermalBlock.LIT) && !level.isClientSide)
        {
            level.setBlockAndUpdate(pos, state.setValue(ThermalBlock.LIT, entity.isLit()));
            entity.setChanged();
        }


        if (entity.tickCount > 20)
        {
            entity.tickCount = 0;

            boolean initiallyLit = entity.isLit();
            boolean needRefresh = false;
            if (entity.isLit())
            {
                entity.consumeFuel();
            }

            if (!entity.isLit() && !level.isClientSide)
            {
                entity.refillFuel();
                if (entity.isLit())
                {
                    level.sendBlockUpdated(pos, state, state, 2);
                    needRefresh = true;
                }
            }

            if (initiallyLit != entity.isLit() && !level.isClientSide)
            {
                level.setBlockAndUpdate(pos, state.setValue(ThermalBlock.LIT, entity.isLit()));
                needRefresh = true;
            }

            if (needRefresh)
            {
                entity.setChanged();
            }
        }

        entity.tickCount++;
    }

    @Override
    public abstract @NotNull Component getDefaultName();

    @Override
    public abstract @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory);

    private void consumeFuel()
    {
        this.fuelTime -= 20;
    }

    public void refillFuel()
    {
        for (int i = 0; i < getContainerSize(); i++)
        {
            if (getItem(i).getCount() > 0 && this.isItemValid(getItem(i).getItem()))
            {
                int fuelValue = getFuelDuration(items.get(i));
                if (fuelValue > 0)
                {
                    this.fuelTime = fuelValue;
                    this.fuelDuration = fuelValue;
                    getItem(i).shrink(1);
                    return;
                }
            }
        }
    }

    public boolean isItemValid(Item item)
    {
        ResourceLocation registryNameItem = BuiltInRegistries.ITEM.getKey(item);
        JsonTemperatureFuelItem fuelInfo = TemperatureDataManager.getFuelItem(registryNameItem);
        return fuelInfo != null && fuelInfo.thermalType == thermalType && fuelInfo.duration > 0;
    }

    public int getFuelDuration(ItemStack item)
    {
        ResourceLocation registryNameItem = BuiltInRegistries.ITEM.getKey(item.getItem());
        JsonTemperatureFuelItem fuelInfo = TemperatureDataManager.getFuelItem(registryNameItem);
        return fuelInfo != null ? fuelInfo.duration : 0;
    }

    public boolean isLit()
    {
        if (this.level != null)
        {
            return this.fuelTime > 0;
        }
        return false;
    }

    public boolean isPowered()
    {
        if (this.level != null)
        {
            return this.level.hasNeighborSignal(this.worldPosition);
        }
        return false;
    }

    public void fillStackedContents(@NotNull StackedContents p_58342_)
    {
        for (ItemStack itemstack : this.items)
        {
            p_58342_.accountStack(itemstack);
        }
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction)
    {
        return new int[]{0, 1, 2, 3};
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack)
    {
        return isItemValid(itemStack.getItem());
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack itemStack, @Nullable Direction direction)
    {
        return this.canPlaceItem(slot, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, @NotNull Direction direction)
    {
        return slot < SLOT_COUNT;
    }

    @Override
    public int getContainerSize()
    {
        return this.items.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.items)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getItem(int slot)
    {
        return this.items.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Nonnull
    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player)
    {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent()
    {
        this.items.clear();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.loadAdditional(tag, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, provider);
        this.fuelTime = tag.getInt("fuelTime");
        this.fuelDuration = tag.getInt("fuelDuration");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.saveAdditional(tag, provider);
        tag.putInt("fuelTime", this.fuelTime);
        tag.putInt("fuelDuration", this.fuelDuration);
        ContainerHelper.saveAllItems(tag, this.items, provider);
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items)
    {
        this.items = items;
    }
}
