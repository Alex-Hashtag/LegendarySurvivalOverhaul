package sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons;

import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.List;

public class EclipticSeasonsUtil {
    public static double averageSeasonTemperature;

    public static Component seasonTooltip(Level level) {
        if (EclipticUtil.getNowSolarTerm(level) == SolarTerm.NONE || !MapChecker.isValidDimension(level))
            return Component.translatable("message.legendarysurvivaloverhaul.eclipticseasons.no_season_dimension");

        SolarTerm solarTerm = EclipticUtil.getNowSolarTerm(level);
        Season season = EclipticUtil.getNowSolarTerm(level).getSeason();

        return Component.translatable("message.legendarysurvivaloverhaul.eclipticseasons.season_info",
                solarTerm.getTranslation(),
                season.getTranslation(),
                EclipticUtil.getTimeInSolarTerm(level),
                getDaysInSolarTerm());
    }

    public static long getSunRiseTime(Level level) {
        SolarTerm solarTerm = EclipticUtil.getNowSolarTerm(level);

        return (30000L - (solarTerm.getDayTime() / 2L)) % 24000;
    }

    public static Biome.Precipitation getPrecipitation(Level level, BlockPos pos) {
        return WeatherManager.getRainOrSnow(level, MapChecker.getSurfaceBiome(level, pos).value(), pos);
    }

    public static int getDayDuration(Level level) {
        return EclipticUtil.getNowSolarTerm(level).getDayTime();
    }

    public static int getDaysInSolarTerm() {
        return CommonConfig.Season.lastingDaysOfEachTerm.get();
    }

    public static double getTimeInSeasonCycle(Level level) {
        int seasonCycleTicks = EclipticUtil.getNowSolarDay(level);
        return (double) seasonCycleTicks / (24.0f * getDaysInSolarTerm());
    }

    public static double getSeasonModifier(int index) {
        index = ((index + 24) % 24);
        List<? extends Double> listConfigValue = switch (index / 6) {
            case 0 -> Config.Baked.esSpringModifier;
            case 1 -> Config.Baked.esSummerModifier;
            case 2 -> Config.Baked.esAutumnModifier;
            case 3 -> Config.Baked.esWinterModifier;
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
        return listConfigValue.size() < 6 ? 0 : listConfigValue.get(index % 6);
    }

    public static float getBlendedSeasonModifier(double previousSeasonModifier, double currentSeasonModifier, double nextSeasonModifier, int time, int subSeasonDuration) {
        return time < subSeasonDuration / 2 ?
                calculateSinusoidalBetweenSeasons(previousSeasonModifier, currentSeasonModifier, time + (subSeasonDuration / 2), subSeasonDuration):
                calculateSinusoidalBetweenSeasons(currentSeasonModifier, nextSeasonModifier, time - (subSeasonDuration / 2), subSeasonDuration);
    }

    public static float calculateSinusoidalBetweenSeasons(double previousSeasonModifier, double nextSeasonModifier, int time, int subSeasonDuration) {
        double tempDiff = nextSeasonModifier - previousSeasonModifier;
        // PI / 2 = 1.5707963267948966
        double seasonModifier = (Math.sin(((time * Math.PI) / subSeasonDuration) - 1.5707963267948966) + 1) * (tempDiff / 2) + previousSeasonModifier;
        return MathUtil.round((float) seasonModifier, 2);
    }

    public static void initAverageTemperatures() {
        averageSeasonTemperature = 0;
        List<List<? extends Double>> seasonsValues = List.of(
                Config.Baked.esAutumnModifier,
                Config.Baked.esSpringModifier,
                Config.Baked.esWinterModifier,
                Config.Baked.esSummerModifier);

        for (List<? extends Double> seasonValues : seasonsValues) {
            for (Double seasonValue: seasonValues)
                averageSeasonTemperature += seasonValue;
        }
        averageSeasonTemperature /= (
                Config.Baked.esAutumnModifier.size() +
                Config.Baked.esSpringModifier.size() +
                Config.Baked.esWinterModifier.size() +
                Config.Baked.esSummerModifier.size());
    }
}
