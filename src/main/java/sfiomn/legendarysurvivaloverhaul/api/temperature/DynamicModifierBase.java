package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;

//  Dynamic modifier is meant to be processed after all base modifiers
public class DynamicModifierBase
{

    public DynamicModifierBase()
    {
    }

    public float applyDynamicPlayerInfluence(Player player, float currentTemperature, float currentResistance)
    {
        return 0.0f;
    }

    public float applyDynamicWorldInfluence(Player player, Level world, BlockPos pos, float currentTemperature, float currentResistance)
    {
        return 0.0f;
    }

    public float getEffectiveResistance(float currentTemperature, float currentResistance, JsonTemperatureResistance jsonTemperatureResistance)
    {
        float diffToAverage = currentTemperature - TemperatureEnum.NORMAL.getValue();

        if (diffToAverage == 0)
        {
            return 0.0f;
        }

        boolean isHot = diffToAverage > 0;
        float diff = Math.abs(diffToAverage);
        float adjustedResistance = isHot ? currentResistance : -currentResistance;
        double maxResistance = jsonTemperatureResistance.thermalResistance +
                (isHot ? jsonTemperatureResistance.heatResistance : jsonTemperatureResistance.coldResistance);

        float effectiveResistance = (float) Mth.clamp(maxResistance, adjustedResistance, diff + adjustedResistance);
        return isHot ? -effectiveResistance : effectiveResistance;
    }
}
