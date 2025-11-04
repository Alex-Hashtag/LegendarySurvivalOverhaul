package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;


public class OriginsModifier extends ModifierBase
{
    public OriginsModifier()
    {
    }

    @Override
    public float getPlayerInfluence(Player player)
    {

        if (!LegendarySurvivalOverhaul.originsLoaded)
            return 0.0f;

//        LazyOptional<IOriginContainer> optionalOrigin = player.getAttachment(OriginsAPI.ORIGIN_CONTAINER);
//
        float temp = 0.0f;
//
//        if (optionalOrigin.isPresent() && optionalOrigin.resolve().isPresent()) {
//            IOriginContainer origins = optionalOrigin.resolve().get();
//            for (ResourceKey<Origin> origin : origins.getOrigins().values()) {
//                JsonTemperatureResistance config = TemperatureDataManager.getOrigin(origin.location());
//                temp += config != null ? config.temperature : 0;
//            }
//        }

        return temp;
    }
}
