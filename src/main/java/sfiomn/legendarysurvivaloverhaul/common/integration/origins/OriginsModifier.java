package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;


public class OriginsModifier extends ModifierBase {
    public OriginsModifier() {}

    @Override
    public float getPlayerInfluence(Player player) {

        if (!LegendarySurvivalOverhaul.originsLoaded)
            return 0.0f;

        LazyOptional<IOriginContainer> optionalOrigin = player.getCapability(OriginsAPI.ORIGIN_CONTAINER);

        float temp = 0.0f;

        if (optionalOrigin.isPresent() && optionalOrigin.resolve().isPresent()) {
            IOriginContainer origins = optionalOrigin.resolve().get();
            for (ResourceKey<Origin> origin : origins.getOrigins().values()) {
                JsonTemperatureResistance config = TemperatureDataManager.getOrigin(origin.location());
                temp += config != null ? config.temperature : 0;
            }
        }

        return temp;
    }
}
