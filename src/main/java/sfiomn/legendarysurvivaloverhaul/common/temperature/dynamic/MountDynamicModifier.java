package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;

import java.util.Objects;

public class MountDynamicModifier extends DynamicModifierBase
{

    public MountDynamicModifier()
    {
        super();
    }

    @Override
    public float applyDynamicPlayerInfluence(Player player, float currentTemperature, float currentResistance)
    {
        if (player.getVehicle() == null) return 0.0f;

        return getEffectiveResistance(currentTemperature, currentResistance, processMountJson(player.getVehicle()));
    }

    private JsonTemperatureResistance processMountJson(Entity entity)
    {
        ResourceLocation entityRegistryName = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        JsonTemperatureResistance jsonTemperatureResistance = TemperatureDataManager.getMount(entityRegistryName);
        return Objects.requireNonNullElseGet(jsonTemperatureResistance, JsonTemperatureResistance::new);
    }
}
