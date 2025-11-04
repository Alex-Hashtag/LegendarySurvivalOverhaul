package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;

public interface IHealingConsumableData
{

    IHealingConsumableData healingCharges(int healingCharges);

    IHealingConsumableData healingValue(float healingValue);

    IHealingConsumableData duration(int durationInTick);

    IHealingConsumableData recoveryEffectDuration(int durationInTick);

    IHealingConsumableData recoveryEffectAmplifier(int amplifier);

    JsonObject build();
}
