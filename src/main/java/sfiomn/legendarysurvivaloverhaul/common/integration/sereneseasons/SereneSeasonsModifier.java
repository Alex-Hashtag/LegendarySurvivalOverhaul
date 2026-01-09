package sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.getSeasonModifier;


public class SereneSeasonsModifier extends ModifierBase
{
    public SereneSeasonsModifier()
    {
        super();
    }

    @Override
    public float getWorldInfluence(Player player, Level level, BlockPos pos)
    {
        if (!LegendarySurvivalOverhaul.sereneSeasonsLoaded)
            return 0.0f;

        if (!Config.Baked.sereneSeasonsEnabled)
            return 0.0f;

        try
        {
            // In theory, this should only ever run if Serene Seasons is installed
            // However, just to be safe, we put this inside of a try/catch to make
            // sure something weird hasn't happened with the API
            return getUncaughtWorldInfluence(level, pos);
        } catch (Exception e)
        {
            // If an error somehow occurs, disable compatibility
            LegendarySurvivalOverhaul.LOGGER.error("An error has occurred with Serene Seasons compatibility, disabling modifier", e);
            LegendarySurvivalOverhaul.sereneSeasonsLoaded = false;

            return 0.0f;
        }
    }

    public float getUncaughtWorldInfluence(Level level, BlockPos pos)
    {
        ISeasonState seasonState = SeasonHelper.getSeasonState(level);

        if (seasonState == null || !SereneSeasonsUtil.hasSeasons(level))
            return 0.0f;

        Vec3i[] posOffsets;
        if (Config.Baked.ssTropicalSeasonsEnabled)
            posOffsets = new Vec3i[]{
                    new Vec3i(0, 0, 0),
                    new Vec3i(10, 0, 0),
                    new Vec3i(-10, 0, 0),
                    new Vec3i(0, 0, 10),
                    new Vec3i(0, 0, -10)
            };
        else
            posOffsets = new Vec3i[]{new Vec3i(0, 0, 0)};

        float value = 0.0f;
        int validSpot = posOffsets.length;
        double targetUndergroundTemperature = 0;

        for (Vec3i offset : posOffsets)
        {
            SereneSeasonsUtil.SeasonType seasonType = SereneSeasonsUtil.getSeasonType(level.getBiome(pos.offset(offset)));

            if (seasonType == SereneSeasonsUtil.SeasonType.NO_SEASON)
            {
                validSpot -= 1;
                continue;
            }
            if (seasonType != SereneSeasonsUtil.SeasonType.TROPICAL_SEASON)
            {
                int timeInSubSeason = seasonState.getSeasonCycleTicks() % seasonState.getSubSeasonDuration();
                targetUndergroundTemperature = SereneSeasonsUtil.averageSeasonTemperature;
                switch (seasonState.getSubSeason())
                {
                    case EARLY_SPRING:
                        value += getSeasonModifier(Config.Baked.ssLateWinterModifier, Config.Baked.ssEarlySpringModifier, Config.Baked.ssMidSpringModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case MID_SPRING:
                        value += getSeasonModifier(Config.Baked.ssEarlySpringModifier, Config.Baked.ssMidSpringModifier, Config.Baked.ssLateSpringModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case LATE_SPRING:
                        value += getSeasonModifier(Config.Baked.ssMidSpringModifier, Config.Baked.ssLateSpringModifier, Config.Baked.ssEarlySummerModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case EARLY_SUMMER:
                        value += getSeasonModifier(Config.Baked.ssLateSpringModifier, Config.Baked.ssEarlySummerModifier, Config.Baked.ssMidSummerModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case MID_SUMMER:
                        value += getSeasonModifier(Config.Baked.ssEarlySummerModifier, Config.Baked.ssMidSummerModifier, Config.Baked.ssLateSummerModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case LATE_SUMMER:
                        value += getSeasonModifier(Config.Baked.ssMidSummerModifier, Config.Baked.ssLateSummerModifier, Config.Baked.ssEarlyAutumnModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case EARLY_AUTUMN:
                        value += getSeasonModifier(Config.Baked.ssLateSummerModifier, Config.Baked.ssEarlyAutumnModifier, Config.Baked.ssMidAutumnModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case MID_AUTUMN:
                        value += getSeasonModifier(Config.Baked.ssEarlyAutumnModifier, Config.Baked.ssMidAutumnModifier, Config.Baked.ssLateAutumnModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case LATE_AUTUMN:
                        value += getSeasonModifier(Config.Baked.ssMidAutumnModifier, Config.Baked.ssLateAutumnModifier, Config.Baked.ssEarlyWinterModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case EARLY_WINTER:
                        value += getSeasonModifier(Config.Baked.ssLateAutumnModifier, Config.Baked.ssEarlyWinterModifier, Config.Baked.ssMidWinterModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case MID_WINTER:
                        value += getSeasonModifier(Config.Baked.ssEarlyWinterModifier, Config.Baked.ssMidWinterModifier, Config.Baked.ssLateWinterModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                    case LATE_WINTER:
                        value += getSeasonModifier(Config.Baked.ssMidWinterModifier, Config.Baked.ssLateWinterModifier, Config.Baked.ssEarlySpringModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
                        break;
                }
            } else
            {
                int timeInSubSeason = (seasonState.getSeasonCycleTicks() + seasonState.getSubSeasonDuration()) % (seasonState.getSubSeasonDuration() * 2);
                targetUndergroundTemperature = SereneSeasonsUtil.averageTropicalSeasonTemperature;
                switch (seasonState.getTropicalSeason())
                {
                    case EARLY_DRY:
                        value += getSeasonModifier(Config.Baked.ssLateWetSeasonModifier, Config.Baked.ssEarlyDrySeasonModifier, Config.Baked.ssMidDrySeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
                        break;
                    case MID_DRY:
                        value += getSeasonModifier(Config.Baked.ssEarlyDrySeasonModifier, Config.Baked.ssMidDrySeasonModifier, Config.Baked.ssLateDrySeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
                        break;
                    case LATE_DRY:
                        value += getSeasonModifier(Config.Baked.ssMidDrySeasonModifier, Config.Baked.ssLateDrySeasonModifier, Config.Baked.ssEarlyWetSeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
                        break;
                    case EARLY_WET:
                        value += getSeasonModifier(Config.Baked.ssLateDrySeasonModifier, Config.Baked.ssEarlyWetSeasonModifier, Config.Baked.ssMidWetSeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
                        break;
                    case MID_WET:
                        value += getSeasonModifier(Config.Baked.ssEarlyWetSeasonModifier, Config.Baked.ssMidWetSeasonModifier, Config.Baked.ssLateWetSeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
                        break;
                    case LATE_WET:
                        value += getSeasonModifier(Config.Baked.ssMidWetSeasonModifier, Config.Baked.ssLateWetSeasonModifier, Config.Baked.ssEarlyDrySeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
                        break;
                }
            }
        }

        value = validSpot == 0 ? 0 : value / validSpot;

        return applyUndergroundEffect(value, level, pos, (float) targetUndergroundTemperature);
    }
}
