package sfiomn.legendarysurvivaloverhaul.common.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.AbstractThermalBlockEntity;

public abstract class AbstractThermalContainer extends AbstractContainerMenu
{

    public final AbstractThermalBlockEntity blockEntity;
    public final ThermalTypeEnum thermalType;
    public final Level level;
    public final ContainerData dataAccess;

    // Accept Supplier<MenuType<?>> so it works with DeferredHolder (and also RegistryObject if you kept it)
    public AbstractThermalContainer(int windowId,
                                    Inventory playerInventory,
                                    AbstractThermalBlockEntity be,
                                    ContainerData dataAccess,
                                    DeferredHolder<MenuType<?>, ? extends MenuType<?>> menuTypeSupplier,
                                    ThermalTypeEnum thermalType)
    {
        super(menuTypeSupplier.get(), windowId);
        checkContainerSize(playerInventory, 4);
        this.thermalType = thermalType;
        this.blockEntity = be;
        this.level = playerInventory.player.level();
        this.dataAccess = dataAccess;

        // Add thermal slots FIRST (slots 0-3) so quickMoveStack works correctly
        addSlot(addThermalSlot(be, 0, 14, 32));
        addSlot(addThermalSlot(be, 1, 34, 32));
        addSlot(addThermalSlot(be, 2, 14, 52));
        addSlot(addThermalSlot(be, 3, 34, 52));

        // Then add player inventory (slots 4+)
        layoutPlayerInventorySlots(playerInventory, 8, 84);

        addDataSlots(dataAccess);
    }

    private Slot addThermalSlot(AbstractThermalBlockEntity be, int index, int posX, int posY)
    {
        return new Slot(be, index, posX, posY)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return be.isItemValid(stack.getItem());
            }
        };
    }

    public ThermalTypeEnum getThermalType()
    {
        return this.thermalType;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isPowered()
    {
        return this.blockEntity.isPowered();
    }

    @OnlyIn(Dist.CLIENT)
    public float getFuelTimeScale()
    {
        if (this.dataAccess.get(1) != 0)
        {
            return (float) this.dataAccess.get(0) / this.dataAccess.get(1);
        } else
        {
            return 0.0f;
        }
    }

    private int addSlotRange(Inventory playerInventory, int index, int x, int y, int amount, int dx)
    {
        for (int i = 0; i < amount; i++)
        {
            this.addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Inventory playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy)
    {
        for (int j = 0; j < verAmount; j++)
        {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(Inventory playerInventory, int leftCol, int topRow)
    {
        int lastIndex = addSlotRange(playerInventory, 0, leftCol, topRow + 58, 9, 18);
        addSlotBox(playerInventory, lastIndex, leftCol, topRow, 9, 18, 3, 18);
    }

    //  0 - 3 = TileInventory slots (map to TileEntity slots 0 - 3)
    //  4 - 12 = hotbar slots (InventoryPlayer slots 0 - 8)
    //  13 - 39 = player inventory slots (InventoryPlayer slots 9 - 35)
    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < AbstractThermalBlockEntity.SLOT_COUNT)
            {
                if (!this.moveItemStackTo(itemstack1, AbstractThermalBlockEntity.SLOT_COUNT + 9, this.slots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
            } else if (index < this.slots.size())
            {
                if (blockEntity.isItemValid(itemstack1.getItem()))
                {
                    if (!this.moveItemStackTo(itemstack1, 0, AbstractThermalBlockEntity.SLOT_COUNT, false))
                    {
                        return ItemStack.EMPTY;
                    }
                } else if (index < AbstractThermalBlockEntity.SLOT_COUNT + 9)
                {
                    if (!this.moveItemStackTo(itemstack1, AbstractThermalBlockEntity.SLOT_COUNT + 9, this.slots.size(), false))
                    {
                        return ItemStack.EMPTY;
                    }
                } else
                {
                    if (!this.moveItemStackTo(itemstack1, AbstractThermalBlockEntity.SLOT_COUNT, AbstractThermalBlockEntity.SLOT_COUNT + 9, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            } else
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            } else
            {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }
}
