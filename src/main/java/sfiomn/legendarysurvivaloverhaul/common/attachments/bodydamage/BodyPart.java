package sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;

public class BodyPart
{
    private final float healthMultiplier;
    private final BodyPartEnum bodyPartEnum;
    // Unsaved Data
    private float oldMaxHealth;
    private float oldDamage;
    // Saved Data
    private float damage;
    private float maxHealth;
    private int remainingHealingTicks;
    private float healingPerTicks;

    public BodyPart(BodyPartEnum bodyPart, float healthMultiplier)
    {
        this.bodyPartEnum = bodyPart;
        this.healthMultiplier = healthMultiplier;
        // Don't set maxHealth here - it will be set by init() based on STATIC/DYNAMIC mode
        this.maxHealth = 0;
        this.damage = 0;
        this.remainingHealingTicks = 0;
        this.healingPerTicks = 0;
        // Set oldMaxHealth to 0 so first update won't apply damage adjustment
        this.oldMaxHealth = 0;
        this.oldDamage = 0;
    }

    public boolean isDirty()
    {
        return this.oldDamage != this.damage || this.oldMaxHealth != this.maxHealth;
    }

    public void setClean()
    {
        this.oldDamage = this.damage;
        this.oldMaxHealth = this.maxHealth;
    }

    public void heal(float value)
    {
        this.setDamage(this.damage - value);
    }

    public void hurt(float value)
    {
        this.setDamage(this.damage + value);
    }

    public boolean isMaxHealth()
    {
        return this.damage == 0;
    }

    public float getHealthMultiplier()
    {
        return this.healthMultiplier;
    }

    public BodyPartEnum getBodyPartEnum()
    {
        return this.bodyPartEnum;
    }

    public float getDamage()
    {
        return this.damage;
    }

    public void setDamage(float value)
    {
        this.damage = Mth.clamp(value, 0, this.maxHealth);
    }

    public float getMaxHealth()
    {
        return this.maxHealth;
    }

    public void setMaxHealth(float value)
    {
        this.maxHealth = value;
        this.damage = Math.min(this.maxHealth, this.damage);
    }

    public int getRemainingHealingTicks()
    {
        return this.remainingHealingTicks;
    }

    public void reduceRemainingHealingTicks(int healingTick)
    {
        this.remainingHealingTicks -= Math.min(healingTick, this.remainingHealingTicks);
        if (this.remainingHealingTicks == 0)
        {
            this.healingPerTicks = 0;
        }
    }

    public float getHealingPerTicks()
    {
        return this.healingPerTicks;
    }

    public void setHealing(int healingTick, float healingValuePerTick)
    {
        this.remainingHealingTicks = healingTick;
        this.healingPerTicks = healingValuePerTick;
    }

    public CompoundTag writeNbt(CompoundTag nbt)
    {
        nbt.putFloat(this.bodyPartEnum.name() + "_damage", this.damage);
        nbt.putFloat(this.bodyPartEnum.name() + "_maxHealth", this.maxHealth);
        nbt.putFloat(this.bodyPartEnum.name() + "_healingPerTicks", this.healingPerTicks);
        nbt.putInt(this.bodyPartEnum.name() + "_remainingHealingTicks", this.remainingHealingTicks);


        return nbt;
    }

    public void readNBT(CompoundTag compound)
    {
        float oldMaxHealth = this.maxHealth;
        float oldDamage = this.damage;

        // Only override maxHealth if it's saved in NBT (backward compatibility with existing saves)
        if (compound.contains(this.bodyPartEnum.name() + "_maxHealth"))
        {
            float savedMaxHealth = compound.getFloat(this.bodyPartEnum.name() + "_maxHealth");
            // Only set maxHealth if it's valid (> 0)
            // If it's 0, keep current value (will be set properly on next tick in DYNAMIC mode)
            if (savedMaxHealth > 0.0f)
            {
                this.setMaxHealth(savedMaxHealth);
            }
        }
        if (compound.contains(this.bodyPartEnum.name() + "_damage"))
        {
            this.setDamage(compound.getFloat(this.bodyPartEnum.name() + "_damage"));
        }
        if (compound.contains(this.bodyPartEnum.name() + "_remainingHealingTicks"))
        {
            this.remainingHealingTicks = compound.getInt(this.bodyPartEnum.name() + "_remainingHealingTicks");
        }
        if (compound.contains(this.bodyPartEnum.name() + "_healingPerTicks"))
        {
            this.healingPerTicks = compound.getFloat(this.bodyPartEnum.name() + "_healingPerTicks");
        }

    }
}
