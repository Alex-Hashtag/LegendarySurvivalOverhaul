package sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import static sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons.EclipticSeasonsUtil.*;


public class EclipticSeasonsModifier extends ModifierBase
{
    public EclipticSeasonsModifier()
    {
        super();
    }

    @Override
    public float getWorldInfluence(Player player, Level level, BlockPos pos)
    {
        if (!LegendarySurvivalOverhaul.eclipticSeasonsLoaded || !Config.Baked.eclipticSeasonsEnabled)
            return 0.0f;

        try
        {
            // In theory, this should only ever run if Ecliptic Seasons is installed
            // However, just to be safe, we put this inside of a try/catch to make
            // sure something weird hasn't happened with the API
            return getUncaughtWorldInfluence(level, pos);
        } catch (Exception e)
        {
            // If an error somehow occurs, disable compatibility
            LegendarySurvivalOverhaul.LOGGER.error("An error has occurred with Ecliptic Seasons compatibility, disabling modifier", e);
            LegendarySurvivalOverhaul.eclipticSeasonsLoaded = false;

            return 0.0f;
        }
    }

    public float getUncaughtWorldInfluence(Level level, BlockPos pos)
    {
        if (!hasDimensionSeason(level))
            return 0.0f;

        if (EclipticSeasonsCompat.isSolarTermNone(level))
            return 0.0f;

        int timeInSubSeason = (int) (EclipticSeasonsCompat.getTimeInTerm(level) * 24000 + level.getLevelData().getDayTime() % 24000);
        int subSeasonDuration = EclipticSeasonsUtil.getDaysInSolarTerm(level) * 24000;
        int ordinal = EclipticSeasonsCompat.getSolarTermOrdinal(level);
        float value = getBlendedSeasonModifier(getSeasonModifier(ordinal - 1), getSeasonModifier(ordinal), getSeasonModifier(ordinal + 1), timeInSubSeason, subSeasonDuration);
        double targetUndergroundTemperature = 0;

        return applyUndergroundEffect(value, level, pos, (float) targetUndergroundTemperature);
    }
}
