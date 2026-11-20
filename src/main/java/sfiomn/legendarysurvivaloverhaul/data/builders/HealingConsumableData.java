package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IHealingConsumableData;

public class HealingConsumableData implements IHealingConsumableData
{
    private int healingCharges;
    private int limbHealing = 1;
    private int healingTime;
    private boolean healsBrokenLimbs;
    private int limbShields;
    private int regenerationDuration;
    private int regenerationAmplifier;

    public HealingConsumableData()
    {
    }

    @Override
    public IHealingConsumableData healingCharges(int healingCharges)
    {
        this.healingCharges = healingCharges;
        return this;
    }

    @Override
    public IHealingConsumableData limbHealing(int limbHealing)
    {
        this.limbHealing = limbHealing;
        return this;
    }

    @Override
    public IHealingConsumableData healingTime(int healingTimeInTick)
    {
        this.healingTime = healingTimeInTick;
        return this;
    }

    @Override
    public IHealingConsumableData healsBrokenLimbs()
    {
        this.healsBrokenLimbs = true;
        return this;
    }

    @Override
    public IHealingConsumableData limbShields(int limbShields)
    {
        this.limbShields = limbShields;
        return this;
    }

    @Override
    public IHealingConsumableData regeneration(int durationInTick, int amplifier)
    {
        this.regenerationDuration = durationInTick;
        this.regenerationAmplifier = amplifier;
        return this;
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("healing_charges", this.healingCharges);
        if (this.healingTime > 0)
            json.addProperty("healing_time", this.healingTime);
        json.addProperty("limb_healing", this.limbHealing);
        json.addProperty("heals_broken_limbs", this.healsBrokenLimbs);
        if (this.limbShields > 0)
            json.addProperty("limb_shields", this.limbShields);
        if (this.regenerationDuration > 0)
        {
            json.addProperty("regeneration_duration", this.regenerationDuration);
            json.addProperty("regeneration_amplifier", this.regenerationAmplifier);
        }
        return json;
    }
}
