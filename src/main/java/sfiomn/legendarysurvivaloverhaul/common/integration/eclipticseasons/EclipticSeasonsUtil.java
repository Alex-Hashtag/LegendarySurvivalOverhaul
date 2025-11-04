package sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.List;

public class EclipticSeasonsUtil
{
    public static double averageSeasonTemperature;

    public static void initAverageTemperatures()
    {
        averageSeasonTemperature = 0;
        List<List<? extends Double>> seasonsValues = List.of(
                Config.Baked.esAutumnModifier,
                Config.Baked.esSpringModifier,
                Config.Baked.esWinterModifier,
                Config.Baked.esSummerModifier);

        for (List<? extends Double> seasonValues : seasonsValues)
        {
            for (Double seasonValue : seasonValues)
                averageSeasonTemperature += seasonValue;
        }
        averageSeasonTemperature /= (
                Config.Baked.esAutumnModifier.size() +
                        Config.Baked.esSpringModifier.size() +
                        Config.Baked.esWinterModifier.size() +
                        Config.Baked.esSummerModifier.size());
    }

    public static double getSeasonModifier(int index)
    {
        index = ((index + 24) % 24);
        List<? extends Double> listConfigValue = switch (index / 6)
        {
            case 0 -> Config.Baked.esSpringModifier;
            case 1 -> Config.Baked.esSummerModifier;
            case 2 -> Config.Baked.esAutumnModifier;
            case 3 -> Config.Baked.esWinterModifier;
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
        return listConfigValue.size() < 6 ? 0 : listConfigValue.get(index % 6);
    }

    public static float getBlendedSeasonModifier(double previousSeasonModifier, double currentSeasonModifier, double nextSeasonModifier, int time, int subSeasonDuration)
    {
        if (time < subSeasonDuration / 2)
            return calculateSinusoidalBetweenSeasons(previousSeasonModifier, currentSeasonModifier, time + (subSeasonDuration / 2), subSeasonDuration);

        return calculateSinusoidalBetweenSeasons(currentSeasonModifier, nextSeasonModifier, time - (subSeasonDuration / 2), subSeasonDuration);
    }

    public static float calculateSinusoidalBetweenSeasons(double previousSeasonModifier, double nextSeasonModifier, int time, int subSeasonDuration)
    {
        double tempDiff = nextSeasonModifier - previousSeasonModifier;
        // PI / 2 = 1.5707963267948966
        double seasonModifier = (Math.sin(((time * Math.PI) / subSeasonDuration) - 1.5707963267948966) + 1) * (tempDiff / 2) + previousSeasonModifier;
        return MathUtil.round((float) seasonModifier, 2);
    }

    public static boolean hasDimensionSeason(Level level)
    {
        return LegendarySurvivalOverhaul.eclipticSeasonsLoaded && EclipticSeasonsCompat.isSeasonEnabled(level);
    }

    public static long getSunRiseTime(Level level)
    {
        if (!LegendarySurvivalOverhaul.eclipticSeasonsLoaded || !hasDimensionSeason(level))
            return 6000L;

        return (30000L - (EclipticSeasonsCompat.getSolarTermDayTime(level) / 2L)) % 24000;
    }

    public static Biome.Precipitation getPrecipitation(Level level, BlockPos pos)
    {
        if (!LegendarySurvivalOverhaul.eclipticSeasonsLoaded)
            return Biome.Precipitation.NONE;

        return EclipticSeasonsCompat.getCurrentPrecipitationAt(level, pos);
    }

    public static int getDayDuration(Level level)
    {
        if (!LegendarySurvivalOverhaul.eclipticSeasonsLoaded)
            return 12000;

        return EclipticSeasonsCompat.getSolarTermDayTime(level);
    }

    public static int getDaysInSolarTerm(Level level)
    {
        if (!LegendarySurvivalOverhaul.eclipticSeasonsLoaded)
            return 0;

        return EclipticSeasonsCompat.getLastingDaysOfEachTerm(level);
    }

    public static double getDayInSeasonCycle(Level level)
    {
        if (!LegendarySurvivalOverhaul.eclipticSeasonsLoaded)
            return 0;

        //  current day in the full season cycle / the total of days to make a full season cycle
        return EclipticSeasonsCompat.getSolarDays(level) / (24.0f * getDaysInSolarTerm(level));
    }

    public static Component seasonTooltip(Level level)
    {
        if (!LegendarySurvivalOverhaul.eclipticSeasonsLoaded)
            return Component.empty();
        if (EclipticSeasonsCompat.isSolarTermNone(level) || !hasDimensionSeason(level))
            return Component.translatable("message.legendarysurvivaloverhaul.eclipticseasons.no_season_dimension");

        return Component.translatable("message.legendarysurvivaloverhaul.eclipticseasons.season_info",
                EclipticSeasonsCompat.getSolarTermTranslation(level),
                EclipticSeasonsCompat.getSeasonTranslation(level),
                EclipticSeasonsCompat.getTimeInTerm(level),
                getDaysInSolarTerm(level));
    }
}
