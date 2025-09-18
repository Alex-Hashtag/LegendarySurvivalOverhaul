package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureDimension;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons.EclipticSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import static sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons.EclipticSeasonsUtil.hasDimensionSeason;

public class TimeModifier extends ModifierBase
{
	
	public TimeModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(Player player, Level level, BlockPos pos)
	{
		JsonTemperatureDimension jsonTemperatureDimension = TemperatureDataManager.getDimension(level.dimension().location());
		int timeCycleTicks = jsonTemperatureDimension != null ? jsonTemperatureDimension.temperatureTimeCycleTicks : 0;

		if (timeCycleTicks == 0 || TerraFirmaCraftUtil.shouldUseTerraFirmaCraftTemp())
		{
			return 0.0f;
		}
		
		long time = level.getLevelData().getDayTime();

		// 2 * PI / 24000 = 0.00026179938
		double timeAngle = time * 2 * Math.PI / timeCycleTicks;

		// Adjust timeAngle based on Ecliptic custom day time length
		if (hasDimensionSeason(level)) {
			long sunRiseTime = EclipticSeasonsUtil.getSunRiseTime(level);
			int dayDuration = EclipticSeasonsUtil.getDayDuration(level);
			long sunSetTime = sunRiseTime + dayDuration;

			// If during day time -> use sinusoidal starting at sunrise and reaching 1 at noon (= day duration / 2)
			if (time > sunRiseTime && time < sunSetTime % timeCycleTicks) {
				timeAngle = (Math.PI / dayDuration) * ((time - sunRiseTime) % timeCycleTicks);
			} else {
				timeAngle = -(Math.PI / (timeCycleTicks - dayDuration)) * ((time - sunSetTime) % timeCycleTicks);
			}
		}

		// Add + - timeModifier temperature value based on time of the day
		float timeTemperature = (float) Math.sin(timeAngle) * (float) Config.Baked.timeModifier;

		// Biome Multiplier will increase the diff between noon and midnight based on extremity of biome temp
		float biomeMultiplier = 1.0f + (Math.abs(normalizeToPositiveNegative(getNormalizedTempForBiome(level, level.getBiome(pos).get()))) * ((float)Config.Baked.biomeTimeMultiplier - 1.0f));
		timeTemperature *= biomeMultiplier;

		// LegendarySurvivalOverhaul.LOGGER.debug("Time temp influence : " + timeTemperature);
		// float tempInfl = applyUndergroundEffect(timeTemperature, world, pos);
		// LegendarySurvivalOverhaul.LOGGER.debug("Time temp influence after underground : " + tempInfl);

		return applyUndergroundEffect(timeTemperature, level, pos, 0);
	}
}