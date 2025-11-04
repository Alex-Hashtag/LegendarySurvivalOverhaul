package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JsonHealingConsumable
{
    public static final Codec<JsonHealingConsumable> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.INT.fieldOf("healing_charges").forGetter(d -> d.healingCharges),
            Codec.FLOAT.fieldOf("healing_value").forGetter(d -> d.healingValue),
            Codec.INT.fieldOf("healing_time").forGetter(d -> d.healingTime),
            Codec.INT.optionalFieldOf("recovery_effect_duration", 0).forGetter(d -> d.recoveryEffectDuration),
            Codec.INT.optionalFieldOf("recovery_effect_amplifier", 0).forGetter(d -> d.recoveryEffectAmplifier)
    ).apply(inst, JsonHealingConsumable::new));

    public int healingCharges;
    public float healingValue;
    public int healingTime;
    public int recoveryEffectDuration;
    public int recoveryEffectAmplifier;

    public JsonHealingConsumable(int healingCharges, float healingValue, int healingTime, int recoveryEffectDuration, int recoveryEffectAmplifier)
    {
        this.healingCharges = healingCharges;
        this.healingValue = healingValue;
        this.healingTime = healingTime;
        this.recoveryEffectDuration = recoveryEffectDuration;
        this.recoveryEffectAmplifier = recoveryEffectAmplifier;
    }
}
