package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

public class TemperatureResistanceModifier extends DynamicModifierBase {

    public TemperatureResistanceModifier()
    {
        super();
    }

    @Override
    public float applyDynamicPlayerInfluence(Player player, float currentTemperature, float currentResistance) {
        JsonTemperatureResistance jsonTemperatureResistance = new JsonTemperatureResistance();

        if (player.getAttributes().hasAttribute(AttributeRegistry.THERMAL_RESISTANCE.get()))
            jsonTemperatureResistance.thermalResistance = (float) player.getAttributeValue(AttributeRegistry.THERMAL_RESISTANCE.get());

        if (player.getAttributes().hasAttribute(AttributeRegistry.HEAT_RESISTANCE.get()))
            jsonTemperatureResistance.heatResistance = (float) player.getAttributeValue(AttributeRegistry.HEAT_RESISTANCE.get());

        if (player.getAttributes().hasAttribute(AttributeRegistry.COLD_RESISTANCE.get()))
            jsonTemperatureResistance.coldResistance = (float) player.getAttributeValue(AttributeRegistry.COLD_RESISTANCE.get());

        return getEffectiveResistance(currentTemperature, currentResistance, jsonTemperatureResistance);
    }
}
