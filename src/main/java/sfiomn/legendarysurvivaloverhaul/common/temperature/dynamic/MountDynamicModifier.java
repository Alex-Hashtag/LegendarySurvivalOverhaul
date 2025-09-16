package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.registries.Registries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

import java.util.Objects;

public class MountDynamicModifier extends DynamicModifierBase {

    public MountDynamicModifier()
    {
        super();
    }

    @Override
    public float applyDynamicPlayerInfluence(Player player, float currentTemperature, float currentResistance) {
        if (player.getVehicle() == null) return 0.0f;

        return getEffectiveResistance(currentTemperature, currentResistance, processMountJson(player.getVehicle()));
    }

    private JsonTemperatureResistance processMountJson(Entity entity)
    {
        ResourceLocation entityRegistryName = Registries.ENTITY_TYPES.getKey(entity.getType());
        JsonTemperatureResistance jsonTemperatureResistance = TemperatureDataManager.getMount(entityRegistryName);
        return Objects.requireNonNullElseGet(jsonTemperatureResistance, JsonTemperatureResistance::new);
    }
}
