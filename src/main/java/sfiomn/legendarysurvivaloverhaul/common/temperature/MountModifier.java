package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;

public class MountModifier extends ModifierBase
{
    public MountModifier()
    {
        super();
    }

    @Override
    public float getPlayerInfluence(Player player)
    {
        if (player.getVehicle() != null)
        {
            return processMountJson(player.getVehicle());
        }
        return 0.0f;
    }

    private float processMountJson(Entity entity)
    {
        ResourceLocation entityRegistryName = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        JsonTemperatureResistance jsonTemperature = TemperatureDataManager.getMount(entityRegistryName);
        if (jsonTemperature != null)
        {
            return jsonTemperature.temperature;
        }

        return 0.0f;
    }
}
