package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;

public class EntityModifier extends ModifierBase {
    public EntityModifier()
    {
        super();
    }

    @Override
    public float getPlayerInfluence(Player player)
    {
        if (player.getVehicle() != null) {
            return processEntityJson(player.getVehicle());
        }
        return 0.0f;
    }

    private float processEntityJson(Entity entity)
    {
        ResourceLocation entityRegistryName = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        JsonTemperature jsonTemperature = TemperatureDataManager.getMount(entityRegistryName);
        if (jsonTemperature != null)
        {
            return jsonTemperature.temperature;
        }

        return 0.0f;
    }
}
