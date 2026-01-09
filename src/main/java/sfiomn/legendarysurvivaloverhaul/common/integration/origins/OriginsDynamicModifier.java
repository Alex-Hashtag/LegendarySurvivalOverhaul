package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;

public class OriginsDynamicModifier extends DynamicModifierBase
{
    public OriginsDynamicModifier()
    {
    }

    @Override
    public float applyDynamicPlayerInfluence(Player player, float currentTemperature, float currentResistance)
    {

        if (!LegendarySurvivalOverhaul.originsLoaded)
            return 0.0f;

        float effectiveResistance = 0.0f;
        float diffToAverage = currentTemperature - TemperatureEnum.NORMAL.getValue();

        // NeoForge: getAttachment now returns the instance or null (LazyOptional removed)
//        IOriginContainer origins = player.getAttachment(OriginsAPI.ORIGIN_CONTAINER);
//        if (origins != null) {
//            for (ResourceKey<Origin> origin : origins.getOrigins().values()) {
//                JsonTemperatureResistance config = TemperatureDataManager.getOrigin(origin.location());
//                if (config != null) {
//                    double maxResistance = config.thermalResistance;

//                    if (diffToAverage > 0) {
//                        maxResistance += config.heatResistance;
//                        effectiveResistance = (float) Mth.clamp(maxResistance, currentResistance, diffToAverage + currentResistance);
//                        effectiveResistance = -effectiveResistance;
//                    } else if (diffToAverage < 0) {
//                        maxResistance += config.coldResistance;
//                        diffToAverage = -diffToAverage;
//                        currentResistance = -currentResistance;
//                        effectiveResistance = (float) Mth.clamp(maxResistance, currentResistance, diffToAverage + currentResistance);
//                    }
//                }
//            }
//        }

        return effectiveResistance;
    }
}
