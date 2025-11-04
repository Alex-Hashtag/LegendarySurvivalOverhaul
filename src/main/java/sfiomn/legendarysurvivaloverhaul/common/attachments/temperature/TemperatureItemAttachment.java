package sfiomn.legendarysurvivaloverhaul.common.attachments.temperature;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureItemAttachment;
import sfiomn.legendarysurvivaloverhaul.registry.DataComponentRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.DataComponentRegistry.TemperatureData;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

public class TemperatureItemAttachment implements ITemperatureItemAttachment, INBTSerializable<CompoundTag>
{
    private final ItemStack itemStack;

    public TemperatureItemAttachment(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    // Legacy constructor for backward compatibility (creates a detached instance)
    public TemperatureItemAttachment()
    {
        this.itemStack = ItemStack.EMPTY;
    }

    private TemperatureData getData()
    {
        if (itemStack.isEmpty())
        {
            return new TemperatureData();
        }
        TemperatureData data = itemStack.get(DataComponentRegistry.TEMPERATURE_DATA.get());
        return data != null ? data : new TemperatureData();
    }

    private void setData(TemperatureData data)
    {
        if (!itemStack.isEmpty())
        {
            itemStack.set(DataComponentRegistry.TEMPERATURE_DATA.get(), data);
        }
    }

    @Override
    public boolean shouldUpdate(long currentTick)
    {
        return getData().shouldUpdate(currentTick);
    }

    @Override
    public void updateWorldTemperature(Level world, Entity holder, long currentTick)
    {
        float newTemperature = WorldUtil.calculateClientWorldEntityTemperature(world, holder);
        setData(new TemperatureData(newTemperature, currentTick));
    }

    @Override
    public float getWorldTemperatureLevel()
    {
        return getData().temperature();
    }

    @Override
    public void setWorldTemperatureLevel(float temperature)
    {
        TemperatureData current = getData();
        setData(current.withTemperature(temperature));
    }

    // NBT (for legacy compatibility)
    public CompoundTag writeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("temperature", getWorldTemperatureLevel());
        return tag;
    }

    public void readNBT(CompoundTag tag)
    {
        if (tag.contains("temperature"))
        {
            setWorldTemperatureLevel(tag.getFloat("temperature"));
        }
    }

    // INBTSerializable
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        return writeNBT();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
    {
        readNBT(nbt);
    }
}
