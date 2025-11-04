package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IHealingConsumableData;

public class HealingConsumableData implements IHealingConsumableData
{
    private int healingCharges;
    private float healingValue;
    private int healingTime;
    private int recoveryDuration;
    private int recoveryAmplifier;

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
    public IHealingConsumableData healingValue(float healingValue)
    {
        this.healingValue = healingValue;
        return this;
    }

    @Override
    public IHealingConsumableData duration(int durationInTick)
    {
        this.healingTime = durationInTick;
        return this;
    }

    @Override
    public IHealingConsumableData recoveryEffectDuration(int durationInTick)
    {
        this.recoveryDuration = durationInTick;
        return this;
    }

    @Override
    public IHealingConsumableData recoveryEffectAmplifier(int amplifier)
    {
        this.recoveryAmplifier = amplifier;
        return this;
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("healing_charges", this.healingCharges);
        json.addProperty("healing_value", this.healingValue);
        json.addProperty("healing_time", this.healingTime);
        json.addProperty("recovery_effect_duration", this.recoveryDuration);
        json.addProperty("recovery_effect_amplifier", this.recoveryAmplifier);
        return json;
    }
}
