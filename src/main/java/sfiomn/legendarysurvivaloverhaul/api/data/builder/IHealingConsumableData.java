package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;

public interface IHealingConsumableData {

    IHealingConsumableData healingCharge(int healingCharge);

    IHealingConsumableData healingValue(float healingValue);

    IHealingConsumableData duration(int durationInTick);

    JsonObject build();
}
