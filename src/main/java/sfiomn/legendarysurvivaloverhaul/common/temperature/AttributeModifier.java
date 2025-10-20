package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

public class AttributeModifier extends ModifierBase
{
    public AttributeModifier()
    {
        super();
    }

    @Override
    public float getPlayerInfluence(Player player)
    {
        return (float) (player.getAttributeValue(AttributeRegistry.HEATING_TEMPERATURE) + player.getAttributeValue(AttributeRegistry.COOLING_TEMPERATURE));
    }
}
