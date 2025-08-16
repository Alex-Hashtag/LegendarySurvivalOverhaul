package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JsonHealingConsumable {
    public static final Codec<JsonHealingConsumable> CODEC = RecordCodecBuilder.<JsonHealingConsumable>create((inst) -> inst.group(
            Codec.INT.fieldOf("healing_charges").forGetter(d -> d.healingCharges),
            Codec.FLOAT.fieldOf("healing_value").forGetter(d -> d.healingValue),
            Codec.INT.fieldOf("healing_time").forGetter(d -> d.healingTime),
            Codec.FLOAT.optionalFieldOf("player_healing_value", 0.0f).forGetter(d -> d.playerHealingValue)
    ).apply(inst, JsonHealingConsumable::new));

    public int healingCharges;
    public float healingValue;
    public int healingTime;
    public float playerHealingValue;

    public JsonHealingConsumable(int healingCharges, float healingValue, int healingTime, float playerHealingValue) {
        this.healingCharges = healingCharges;
        this.healingValue = healingValue;
        this.healingTime = healingTime;
        this.playerHealingValue = playerHealingValue;
    }
}
