package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

public class WeatherModifier extends ModifierBase
{
    public WeatherModifier()
    {
        super();
    }

    @Override
    public float getWorldInfluence(Player player, Level level, BlockPos pos)
    {
        Biome.Precipitation precipitation = Biome.Precipitation.NONE;
        boolean isUndercover = WorldUtil.isPlayerOrPosUndercover(level, player, pos.above());

        if (!isUndercover)
            precipitation = WorldUtil.getPrecipitationAt(level, player, pos.above());

        // Don't apply shade nor weather if there are no precipitation and player not undercover
        // Shade effect depends on Time, no shade effect at sunrise and sunset, max effect at noon
        if (precipitation == Biome.Precipitation.NONE &&
                !isUndercover)
        {
            return 0.0f;
        }

        float weatherTemperature = 0.0f;
        long time = level.getLevelData().getDayTime();

        // Apply shade effect either if it's raining or block above player or player under parasol
        if (Config.Baked.shadeTimeModifier != 0 && time <= 12000)
        {
            if ((TemperatureModifierRegistry.BIOME.get().getWorldInfluence(player, level, pos) +
                    TemperatureModifierRegistry.SERENE_SEASONS.get().getWorldInfluence(player, level, pos) +
                    TemperatureModifierRegistry.ECLIPTIC_SEASONS.get().getWorldInfluence(player, level, pos)) >= Config.Baked.shadeTimeModifierThreshold)
            {
                // PI / 12000 = 0.00026179938
                float shadeTemperature = (float) Config.Baked.shadeTimeModifier * (float) Math.sin(time * 0.00026179938);
                weatherTemperature += shadeTemperature;
            }
        }

        if (precipitation == Biome.Precipitation.RAIN)
            weatherTemperature += (float) Config.Baked.rainTemperatureModifier;
        else if (precipitation == Biome.Precipitation.SNOW)
            weatherTemperature += (float) Config.Baked.snowTemperatureModifier;

        return weatherTemperature;
    }
}
