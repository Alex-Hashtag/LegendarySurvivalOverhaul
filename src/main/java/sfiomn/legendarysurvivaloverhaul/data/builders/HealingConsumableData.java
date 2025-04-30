package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IHealingConsumableData;

public class HealingConsumableData implements IHealingConsumableData {
    private int healingCharge;
    private float healingValue;
    private int healingTime;

    public HealingConsumableData() {
    }

    @Override
    public IHealingConsumableData healingCharge(int healingCharge) {
        this.healingCharge = healingCharge;
        return this;
    }

    @Override
    public IHealingConsumableData healingValue(float healingValue) {
        this.healingValue = healingValue;
        return this;
    }

    @Override
    public IHealingConsumableData duration(int durationInTick) {
        this.healingTime = durationInTick;
        return this;
    }

    @Override
    public JsonObject build() {
        JsonObject json = new JsonObject();
        json.addProperty("healing_charge", this.healingCharge);
        json.addProperty("healing_value", this.healingValue);
        json.addProperty("healing_time", this.healingTime);
        return json;
    }
}
