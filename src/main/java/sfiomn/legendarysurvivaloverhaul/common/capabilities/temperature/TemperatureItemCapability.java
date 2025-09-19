package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

public class TemperatureItemCapability implements ITemperatureItemCapability {
    private float temperature;
    private long updateTick;

    public TemperatureItemCapability() {
        this.init();
    }

    private void init() {
        this.temperature = TemperatureEnum.NORMAL.getMiddle();
        this.updateTick = 0;
    }

    @Override
    public boolean shouldUpdate(long currentTick) {
        return (currentTick - this.updateTick) > 10;
    }

    @Override
    public void updateWorldTemperature(Level world, Entity holder, long currentTick) {
        this.updateTick = currentTick;
        this.temperature = WorldUtil.calculateClientWorldEntityTemperature(world, holder);
    }

    @Override
    public float getWorldTemperatureLevel() {
        return this.temperature;
    }

    @Override
    public void setWorldTemperatureLevel(float temperature) {
        this.temperature = temperature;
    }

    public CompoundTag writeNBT()
    {
        CompoundTag compound = new CompoundTag();

        compound.putFloat("temperature", this.temperature);

        return compound;
    }

    public void readNBT(CompoundTag compound)
    {
        this.init();
        if (compound.contains("temperature"))
            this.setWorldTemperatureLevel(compound.getFloat("temperature"));
    }
}
