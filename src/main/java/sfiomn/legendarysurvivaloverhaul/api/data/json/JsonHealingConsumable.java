package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JsonHealingConsumable
{
    public static final Codec<JsonHealingConsumable> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.INT.optionalFieldOf("healing_charges", 0).forGetter(d -> d.healingCharges),
            Codec.INT.optionalFieldOf("limb_healing", 1).forGetter(d -> d.limbHealing),
            Codec.INT.optionalFieldOf("healing_time", 0).forGetter(d -> d.healingTime),
            Codec.BOOL.optionalFieldOf("heals_broken_limbs", false).forGetter(d -> d.healsBrokenLimbs),
            Codec.INT.optionalFieldOf("limb_shields", 0).forGetter(d -> d.limbShields),
            Codec.INT.optionalFieldOf("regeneration_duration", 0).forGetter(d -> d.regenerationDuration),
            Codec.INT.optionalFieldOf("regeneration_amplifier", 0).forGetter(d -> d.regenerationAmplifier)
    ).apply(inst, JsonHealingConsumable::new));

    /**
     * Number of limbs the player chooses to heal. 0 means every limb is healed at once.
     */
    public int healingCharges;
    /**
     * Limb hearts restored on each healed limb.
     */
    public int limbHealing;
    /**
     * Time in ticks taken to heal each chosen limb.
     */
    public int healingTime;
    /**
     * Whether a broken limb can be healed.
     */
    public boolean healsBrokenLimbs;
    /**
     * Number of limb shields given, to random limbs weighted by their damage.
     */
    public int limbShields;
    public int regenerationDuration;
    public int regenerationAmplifier;

    public JsonHealingConsumable(int healingCharges, int limbHealing, int healingTime, boolean healsBrokenLimbs, int limbShields, int regenerationDuration, int regenerationAmplifier)
    {
        this.healingCharges = healingCharges;
        this.limbHealing = limbHealing;
        this.healingTime = healingTime;
        this.healsBrokenLimbs = healsBrokenLimbs;
        this.limbShields = limbShields;
        this.regenerationDuration = regenerationDuration;
        this.regenerationAmplifier = regenerationAmplifier;
    }

    /**
     * Whether the player has to choose the limbs to heal.
     */
    public boolean isLimbSelection()
    {
        return this.healingCharges > 0 && this.limbHealing > 0;
    }
}
