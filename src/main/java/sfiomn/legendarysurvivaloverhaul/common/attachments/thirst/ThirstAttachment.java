package sfiomn.legendarysurvivaloverhaul.common.attachments.thirst;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.DifficultyUtil;

public class ThirstAttachment implements IThirstAttachment, INBTSerializable<CompoundTag>
{
    public static int MAX_HYDRATION = 20;
    public static float MAX_SATURATION = 20.0f;

    private float exhaustion = 0.0f;
    private int thirst;
    private float saturation;
    private int tickTimer;
    private int damageTickTimer; // Update immediately first time around
    private int damageCounter;
    private boolean hasShownBlurWarning = false;

    //Unsaved data
    private int oldHydration;
    private float oldSaturation;
    private float oldExhaustion;
    private boolean wasSprinting;
    private Vec3 oldPos;
    private boolean dirty;
    private int packetTimer;

    public ThirstAttachment()
    {
        this.init();
    }

    public void init()
    {
        this.thirst = 20;
        this.saturation = 5.0f;
        this.tickTimer = 0;
        this.damageCounter = 0;
        this.hasShownBlurWarning = false;

        this.oldHydration = 0;
        this.oldSaturation = 0.0f;
        this.oldExhaustion = 0.0f;
        this.wasSprinting = false;
        this.oldPos = null;
        this.dirty = false;
        this.damageTickTimer = 0;
        this.packetTimer = 0;
    }

    @Override
    public void tickUpdate(Player player, Level level, boolean isStart)
    {
        if (getTickTimer() == -1)
            return;

        if (isStart)
        {
            packetTimer++;
            return;
        }

        if (oldPos == null)
            oldPos = player.position();

        this.addTickTimer(1);
        if (getTickTimer() >= 10)
        {
            this.setTickTimer(0);

            if (player.hasEffect(MobEffectRegistry.HYDRATION_FILL))
            {
                if (getHydrationLevel() < MAX_HYDRATION)
                {
                    addHydrationLevel(1);
                }
                return;
            }

            // if player has moved at least 1 block, trigger the thirst exhaust, allowing afk player not dying from thirst
            if (oldPos.distanceTo(player.position()) > 1)
            {
                float thirstExhausted;
                if (player.isSprinting() && this.wasSprinting)
                    thirstExhausted = (float) Config.Baked.sprintingHydrationExhaustion;
                else if (player.isSprinting() || this.wasSprinting)
                    thirstExhausted = (float) ((Config.Baked.sprintingHydrationExhaustion + Config.Baked.baseHydrationExhaustion) / 2.0d);
                else
                    thirstExhausted = (float) Config.Baked.baseHydrationExhaustion;

                this.addThirstExhaustion(thirstExhausted);
                this.oldPos = player.position();
                this.wasSprinting = player.isSprinting();
            }
        }

        // Process exhaustion, similar to hunger system. At 4 exhaustion, remove 1 thirst level or thirst saturation
        if (this.getThirstExhaustion() > 4)
        {
            // Exhausted, do a thirst tick
            this.addThirstExhaustion(-4.0f);

            if (this.getSaturationLevel() > 0.0f)
            {
                // Exhaust from saturation
                this.addSaturationLevel(-1.0f);
            } else if (DifficultyUtil.isModDangerous())
            {
                // Exhaust from hydration
                this.addHydrationLevel(-1);
            }
        }

        // Hurt ticking
        if (this.getHydrationLevel() <= 0)
        {
            this.addThirstDamageTickTimer(1);

            // Hurt player every 4s, similar as hunger hurting
            if (this.getThirstDamageTickTimer() > 80)
            {
                this.setThirstDamageTickTimer(0);

                if (DifficultyUtil.isModDangerous() &&
                        DifficultyUtil.healthAboveDifficulty(player) &&
                        !player.isSpectator() && !player.isCreative() &&
                        Config.Baked.dangerousDehydration)
                {
                    applyDangerousEffect(player);
                }
            }
        } else
        {
            // Reset the timer if not dying of thirst
            this.setThirstDamageTickTimer(0);
            this.setThirstDamageCounter(0);
        }
    }

    private void applyDangerousEffect(Player player)
    {
        // Apply dehydration damages
        this.addThirstDamageCounter(1);
        float thirstDamageToApply = (float) (this.getThirstDamageCounter() * Config.Baked.dehydrationDamageScaling);
        ModDamageTypes.dehydration(player, thirstDamageToApply);
    }

    @Override
    public boolean isDirty()
    {
        return this.thirst != this.oldHydration ||
                this.saturation != this.oldSaturation ||
                this.exhaustion != this.oldExhaustion ||
                this.dirty;
    }

    @Override
    public void setClean()
    {
        this.oldHydration = this.thirst;
        this.oldSaturation = this.saturation;
        this.oldExhaustion = this.exhaustion;
        this.dirty = false;
    }

    @Override
    public void setDirty()
    {
        this.dirty = true;
    }

    @Override
    public float getThirstExhaustion()
    {
        return exhaustion;
    }

    public void setThirstExhaustion(float exhaustion)
    {
        setExhaustion(exhaustion);
    }

    @Override
    public int getHydrationLevel()
    {
        return thirst;
    }

    @Override
    public void setHydrationLevel(int thirst)
    {
        this.thirst = Mth.clamp(thirst, 0, MAX_HYDRATION);

    }

    @Override
    public float getSaturationLevel()
    {
        return saturation;
    }

    @Override
    public int getTickTimer()
    {
        return tickTimer;
    }

    @Override
    public void setTickTimer(int ticktimer)
    {
        this.tickTimer = ticktimer;
    }

    public void setThirstTickTimer(int ticktimer)
    {
        setTickTimer(ticktimer);
    }

    @Override
    public int getThirstDamageTickTimer()
    {
        return damageTickTimer;
    }

    @Override
    public void setThirstDamageTickTimer(int damageTickTimer)
    {
        this.damageTickTimer = damageTickTimer;
    }

    @Override
    public int getThirstDamageCounter()
    {
        return damageCounter;
    }

    @Override
    public void setThirstDamageCounter(int damageCounter)
    {
        this.damageCounter = damageCounter;
    }

    @Override
    public void setExhaustion(float exhaustion)
    {
        this.exhaustion = Math.max(exhaustion, 0.0f);

        if (!Float.isFinite(this.exhaustion))
            this.exhaustion = 0.0f;
    }

    @Override
    public void setSaturation(float saturation)
    {
        this.saturation = Mth.clamp(saturation, 0.0f, MAX_SATURATION);

        if (!Float.isFinite(this.saturation))
            this.saturation = 0.0f;
    }

    public void setThirstSaturation(float saturation)
    {
        setSaturation(saturation);
    }

    @Override
    public void addThirstExhaustion(float exhaustion)
    {
        this.setExhaustion(this.getThirstExhaustion() + exhaustion);
    }

    @Override
    public void addHydrationLevel(int thirst)
    {
        this.setHydrationLevel(this.getHydrationLevel() + thirst);
    }

    @Override
    public void addSaturationLevel(float saturation)
    {
        // Never allow thirst saturation lower than 0
        this.setSaturation(Math.max(Math.round((this.getSaturationLevel() + saturation) * 100.0f) / 100.0f, 0.0f));
    }

    @Override
    public void addTickTimer(int ticktimer)
    {
        this.setTickTimer(this.getTickTimer() + ticktimer);
    }

    public void addThirstTickTimer(int ticktimer)
    {
        addTickTimer(ticktimer);
    }

    @Override
    public void addThirstDamageTickTimer(int damageTickTimer)
    {
        this.setThirstDamageTickTimer(this.getThirstDamageTickTimer() + damageTickTimer);
    }

    @Override
    public void addThirstDamageCounter(int damageCounter)
    {
        this.setThirstDamageCounter(this.getThirstDamageCounter() + damageCounter);
    }

    @Override
    public boolean isHydrationLevelAtMax()
    {
        return this.getHydrationLevel() >= MAX_HYDRATION;
    }

    @Override
    public boolean hasShownBlurWarning()
    {
        return this.hasShownBlurWarning;
    }

    @Override
    public void setShownBlurWarning(boolean shown)
    {
        this.hasShownBlurWarning = shown;
    }

    @Override
    public int getPacketTimer()
    {
        return packetTimer;
    }

    public CompoundTag writeNBT()
    {
        CompoundTag compound = new CompoundTag();
        compound.putFloat("exhaustion", this.getThirstExhaustion());
        compound.putInt("hydrationLevel", this.getHydrationLevel());
        compound.putFloat("saturation", this.getSaturationLevel());
        compound.putInt("tickTimer", this.getTickTimer());
        compound.putInt("thirstDamageTickTimer", this.getThirstDamageTickTimer());
        compound.putInt("thirstDamageCounter", this.getThirstDamageCounter());
        compound.putBoolean("hasShownBlurWarning", this.hasShownBlurWarning());
        return compound;
    }

    public void readNBT(CompoundTag nbt)
    {
        this.init();
        if (nbt.contains("exhaustion"))
            this.setExhaustion(nbt.getFloat("exhaustion"));
        if (nbt.contains("hydrationLevel"))
            this.setHydrationLevel(nbt.getInt("hydrationLevel"));
        if (nbt.contains("saturation"))
            this.setSaturation(nbt.getFloat("saturation"));
        if (nbt.contains("tickTimer"))
            this.setTickTimer(nbt.getInt("tickTimer"));
        if (nbt.contains("thirstDamageTickTimer"))
            this.setThirstDamageTickTimer(nbt.getInt("thirstDamageTickTimer"));
        if (nbt.contains("thirstDamageCounter"))
            this.setThirstDamageCounter(nbt.getInt("thirstDamageCounter"));
        if (nbt.contains("hasShownBlurWarning"))
            this.setShownBlurWarning(nbt.getBoolean("hasShownBlurWarning"));
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
