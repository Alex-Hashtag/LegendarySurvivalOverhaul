package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Code adapted from 
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/capability/TemperatureCapability.java

public class TemperatureCapability implements ITemperatureCapability, INBTSerializable<CompoundTag>
{
    private float temperature;
    private Set<Integer> temperatureImmunities;
    private int temperatureTickTimer;
    private int freezeTickTimer;

    //Unsaved data
    private float oldTemperature;
    private float targetTemp;
    private boolean manualDirty;
    private int packetTimer;

    public TemperatureCapability()
    {
        this.init();
    }

    public void init()
    {
        this.temperature = TemperatureEnum.NORMAL.getMiddle();
        this.temperatureImmunities = new HashSet<>();
        this.temperatureTickTimer = 0;
        this.freezeTickTimer = 0;

        this.oldTemperature = 0;
        this.targetTemp = 0;
        this.manualDirty = false;
        this.packetTimer = 0;
    }

    @Override
    public float getTemperatureLevel()
    {
        return temperature;
    }

    @Override
    public void setTemperatureLevel(float temperature)
    {
        this.temperature = temperature;
    }

    @Override
    public float getTargetTemperatureLevel()
    {
        return targetTemp;
    }

    @Override
    public void setTargetTemperatureLevel(float targetTemperature)
    {
        this.targetTemp = targetTemperature;
    }

    @Override
    public int getTemperatureTickTimer()
    {
        return temperatureTickTimer;
    }

    @Override
    public void setTemperatureTickTimer(int tickTimer)
    {
        this.temperatureTickTimer = tickTimer;
    }

    @Override
    public int getFreezeTickTimer()
    {
        return freezeTickTimer;
    }

    @Override
    public void setFreezeTickTimer(int tickTimer)
    {
        this.freezeTickTimer = tickTimer;
    }

    @Override
    public void addTemperatureLevel(float temperature)
    {
        this.setTemperatureLevel(getTemperatureLevel() + temperature);
    }

    @Override
    public void addTemperatureTickTimer(int tickTimer)
    {
        this.setTemperatureTickTimer(this.getTemperatureTickTimer() + tickTimer);
    }

    @Override
    public void addFreezeTickTimer(int tickTimer)
    {
        this.setFreezeTickTimer(Mth.clamp(this.getFreezeTickTimer() + tickTimer, 0, Config.Baked.maxFreezeEffectTick));
    }

    @Override
    public void addTemperatureImmunityId(int immunityId)
    {
        this.temperatureImmunities.add(immunityId);
    }

    public void removeTemperatureImmunityId(int immunityId)
    {
        this.temperatureImmunities.remove(immunityId);
    }

    @Override
    public void tickUpdate(Player player, Level level, boolean isStart)
    {
        if (isStart)
        {
            return;
        }

        addTemperatureTickTimer(1);

        if (player.isFreezing())
            addFreezeTickTimer(1);
        else if (getFreezeTickTimer() > 0)
            addFreezeTickTimer(-1);

        if (getTemperatureTickTimer() >= Config.Baked.tempTickTime)
        {
            setTemperatureTickTimer(0);

            this.targetTemp = TemperatureUtil.getPlayerTargetTemperature(player);
            if (getTemperatureLevel() != this.targetTemp)
            {
                tickTemperature(getTemperatureLevel(), this.targetTemp);
            }

            TemperatureEnum tempEnum = getTemperatureEnum();

            if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.DEBUG_STICK)
            {
                LegendarySurvivalOverhaul.LOGGER.info(tempEnum + ", " + getTemperatureLevel() + " -> " + this.targetTemp);
            }

            // Effects application is handled elsewhere; no direct calls here on 1.21
        }
    }

    @Override
    public void tickClient(Player player, boolean isStart)
    {
        if (isStart) return;
        if (getTemperatureEnum() == TemperatureEnum.FROSTBITE && !FrostbiteEffect.playerIsImmuneToFrost(player))
        {
            shakePlayer(player);
        }
    }

    private void shakePlayer(Player player)
    {
        // PI * 0.4 = 1.25663706144
        player.setYBodyRot(player.getYRot() + (float) (Math.cos((double) player.tickCount * 3.25D) * 1.25663706144));
    }

    private void tickTemperature(float currentTemp, float destination)
    {
        float diff = Math.abs(destination - currentTemp);

        double temperatureTowards = ((diff * (Config.Baked.maxTemperatureModification - Config.Baked.minTemperatureModification)) / (TemperatureEnum.HEAT_STROKE.getUpperBound() - TemperatureEnum.FROSTBITE.getLowerBound())) + Config.Baked.minTemperatureModification;

        temperatureTowards = Math.min(temperatureTowards, diff);

        if (currentTemp > destination)
        {
            addTemperatureLevel((float) -temperatureTowards);
        } else
        {
            addTemperatureLevel((float) temperatureTowards);
        }
    }

    @Override
    public boolean isDirty()
    {
        return manualDirty || this.temperature != this.oldTemperature;
    }

    @Override
    public void setClean()
    {
        this.oldTemperature = this.temperature;
        this.manualDirty = false;
    }

    @Override
    public int getPacketTimer()
    {
        return packetTimer;
    }

    @Override
    public TemperatureEnum getTemperatureEnum()
    {
        return TemperatureEnum.get(temperature);
    }

    @Override
    public List<Integer> getTemperatureImmunities()
    {
        return new ArrayList<>(this.temperatureImmunities);
    }

    public CompoundTag writeNBT()
    {
        CompoundTag compound = new CompoundTag();

        compound.putFloat("temperature", this.getTemperatureLevel());
        compound.putFloat("targettemperature", this.getTargetTemperatureLevel());
        compound.putInt("ticktimer", this.getTemperatureTickTimer());
        compound.putInt("freezeticktimer", this.getFreezeTickTimer());
        compound.putIntArray("immunities", this.getTemperatureImmunities());

        return compound;
    }

    public void readNBT(CompoundTag compound)
    {
        this.init();
        if (compound.contains("temperature"))
            this.setTemperatureLevel(compound.getFloat("temperature"));
        if (compound.contains("targettemperature"))
            this.setTargetTemperatureLevel(compound.getFloat("targettemperature"));
        if (compound.contains("tickTimer"))
            this.setTemperatureTickTimer(compound.getInt("tickTimer"));
        if (compound.contains("freezeticktimer"))
            this.setFreezeTickTimer(compound.getInt("freezeticktimer"));
        if (compound.contains("immunities"))
            for (int immunityId : compound.getIntArray("immunities"))
                this.addTemperatureImmunityId(immunityId);
    }

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
