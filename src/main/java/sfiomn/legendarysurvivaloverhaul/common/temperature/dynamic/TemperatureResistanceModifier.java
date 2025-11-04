package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

public class TemperatureResistanceModifier extends DynamicModifierBase
{

    public TemperatureResistanceModifier()
    {
        super();
    }

    @Override
    public float applyDynamicPlayerInfluence(Player player, float currentTemperature, float currentResistance)
    {
        JsonTemperatureResistance jsonTemperatureResistance = new JsonTemperatureResistance();

        if (player.getAttributes().hasAttribute(AttributeRegistry.THERMAL_RESISTANCE))
            jsonTemperatureResistance.thermalResistance = (float) player.getAttributeValue(AttributeRegistry.THERMAL_RESISTANCE);

        if (player.getAttributes().hasAttribute(AttributeRegistry.HEAT_RESISTANCE))
            jsonTemperatureResistance.heatResistance = (float) player.getAttributeValue(AttributeRegistry.HEAT_RESISTANCE);

        if (player.getAttributes().hasAttribute(AttributeRegistry.COLD_RESISTANCE))
            jsonTemperatureResistance.coldResistance = (float) player.getAttributeValue(AttributeRegistry.COLD_RESISTANCE);

        return getEffectiveResistance(currentTemperature, currentResistance, jsonTemperatureResistance);
    }
}
