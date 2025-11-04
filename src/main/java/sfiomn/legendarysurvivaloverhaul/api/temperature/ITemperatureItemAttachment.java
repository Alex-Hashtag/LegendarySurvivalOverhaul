package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface ITemperatureItemAttachment
{
    float getWorldTemperatureLevel();

    void setWorldTemperatureLevel(float temperature);

    boolean shouldUpdate(long currentTick);

    void updateWorldTemperature(Level world, Entity holder, long currentTick);
}
