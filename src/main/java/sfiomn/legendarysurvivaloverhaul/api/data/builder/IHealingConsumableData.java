package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;

public interface IHealingConsumableData
{
    /**
     * Number of limbs the player chooses to heal. 0 (default) means every limb is healed at once.
     */
    IHealingConsumableData healingCharges(int healingCharges);

    /**
     * Limb hearts restored on each healed limb. Default 1.
     */
    IHealingConsumableData limbHealing(int limbHealing);

    /**
     * Time in ticks taken to heal each chosen limb. Default 0.
     */
    IHealingConsumableData healingTime(int healingTimeInTick);

    /**
     * Allows healing broken limbs.
     */
    IHealingConsumableData healsBrokenLimbs();

    /**
     * Number of limb shields given, to random limbs weighted by their damage.
     */
    IHealingConsumableData limbShields(int limbShields);

    /**
     * Vanilla regeneration effect given when the item is used.
     */
    IHealingConsumableData regeneration(int durationInTick, int amplifier);

    JsonObject build();
}
