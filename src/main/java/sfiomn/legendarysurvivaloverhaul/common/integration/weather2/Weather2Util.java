package sfiomn.legendarysurvivaloverhaul.common.integration.weather2;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;


public class Weather2Util {

    public static float isUnderParasol(Player player, Level level, BlockPos pos) {
        if (LegendarySurvivalOverhaul.weather2Loaded) {
        }
        return 0.0f;
    }

    public static boolean isUnderWeather(Player player) {
        return true;
    }
}
