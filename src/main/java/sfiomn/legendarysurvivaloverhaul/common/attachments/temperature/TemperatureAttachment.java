package sfiomn.legendarysurvivaloverhaul.common.attachments.temperature;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureAttachment;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Code adapted from 
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/capability/TemperatureAttachment.java

public class TemperatureAttachment implements ITemperatureAttachment, INBTSerializable<CompoundTag>
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

    public TemperatureAttachment()
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

            // Apply temperature effects based on current temperature
            applyTemperatureEffects(player, tempEnum);
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
        player.setYBodyRot(player.getYRot() + (float) (Math.cos((double) player.tickCount * 3.25D) * 1.25663706144));
    }

    private void applyTemperatureEffects(Player player, TemperatureEnum tempEnum)
    {
        // Ensure effects stick for at least 5 seconds and are refreshed when close to expiring
        // Duration in ticks
        int effectDuration = 100;            // 5 seconds
        int refreshThreshold = 40;           // 2 seconds
        
        // Apply or remove FROSTBITE effect
        if (tempEnum == TemperatureEnum.FROSTBITE && Config.Baked.dangerousColdTemperature)
        {
            if (!FrostbiteEffect.playerIsImmuneToFrost(player))
            {
                var current = player.getEffect(MobEffectRegistry.FROSTBITE);
                if (current == null || current.getDuration() <= refreshThreshold)
                {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.FROSTBITE, effectDuration, 0, false, false, true));
                }
            }
        }
        else
        {
            if (player.hasEffect(MobEffectRegistry.FROSTBITE))
            {
                player.removeEffect(MobEffectRegistry.FROSTBITE);
            }
        }

        // Apply or remove COLD_HUNGER effect
        if ((tempEnum == TemperatureEnum.COLD || tempEnum == TemperatureEnum.FROSTBITE) && Config.Baked.coldTemperatureSecondaryEffects)
        {
            if (!FrostbiteEffect.playerIsImmuneToFrost(player))
            {
                var current = player.getEffect(MobEffectRegistry.COLD_HUNGER);
                if (current == null || current.getDuration() <= refreshThreshold)
                {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.COLD_HUNGER, effectDuration, 0, false, false, true));
                }
            }
        }
        else
        {
            if (player.hasEffect(MobEffectRegistry.COLD_HUNGER))
            {
                player.removeEffect(MobEffectRegistry.COLD_HUNGER);
            }
        }

        // Apply or remove HEAT_STROKE effect
        if (tempEnum == TemperatureEnum.HEAT_STROKE && Config.Baked.dangerousHeatTemperature)
        {
            if (!HeatStrokeEffect.playerIsImmuneToHeat(player))
            {
                var current = player.getEffect(MobEffectRegistry.HEAT_STROKE);
                if (current == null || current.getDuration() <= refreshThreshold)
                {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.HEAT_STROKE, effectDuration, 0, false, false, true));
                }
            }
        }
        else
        {
            if (player.hasEffect(MobEffectRegistry.HEAT_STROKE))
            {
                player.removeEffect(MobEffectRegistry.HEAT_STROKE);
            }
        }

        // Apply or remove HEAT_THIRST effect
        if ((tempEnum == TemperatureEnum.HOT || tempEnum == TemperatureEnum.HEAT_STROKE) && Config.Baked.heatTemperatureSecondaryEffects)
        {
            if (!HeatStrokeEffect.playerIsImmuneToHeat(player))
            {
                var current = player.getEffect(MobEffectRegistry.HEAT_THIRST);
                if (current == null || current.getDuration() <= refreshThreshold)
                {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.HEAT_THIRST, effectDuration, 0, false, false, true));
                }
            }
        }
        else
        {
            if (player.hasEffect(MobEffectRegistry.HEAT_THIRST))
            {
                player.removeEffect(MobEffectRegistry.HEAT_THIRST);
            }
        }
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
