package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.beachparty.BeachpartyUtil;
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
		// Apply weather / shade effect when it's hot if the player is either "hidden from sky" or time is raining
		// Shade effect depends on Time, no shade effect at sunrise and sunset, max effect at noon
		if (!level.isRaining() && level.canSeeSky(pos.above()) && !BeachpartyUtil.isUnderParasol(player, level, pos)) {
			return 0.0f;
		}

		Biome biome = level.getBiome(pos).get();
		float weatherTemperature = 0.0f;
		long time = level.getLevelData().getDayTime();

		if(Config.Baked.shadeTimeModifier != 0 && time <= 12000)
		{
			if ((TemperatureModifierRegistry.BIOME.get().getWorldInfluence(player, level, pos) +
					TemperatureModifierRegistry.SERENE_SEASONS.get().getWorldInfluence(player, level, pos)) >= Config.Baked.shadeTimeModifierThreshold) {
				float shadeTemperature = (float) Config.Baked.shadeTimeModifier * (float) Math.sin((time * Math.PI) / 12000.0f);
				weatherTemperature += shadeTemperature;
			}
		}

		if(WorldUtil.isRainingOrSnowingAt(level, pos.above())) {

			if (biome.getPrecipitationAt(pos.above()) == Biome.Precipitation.SNOW) {
				weatherTemperature += (float) Config.Baked.snowTemperatureModifier;
			} else if (biome.getPrecipitationAt(pos.above()) == Biome.Precipitation.RAIN) {
				weatherTemperature += (float) Config.Baked.rainTemperatureModifier;
			}
		}
		return weatherTemperature;
	}
}
