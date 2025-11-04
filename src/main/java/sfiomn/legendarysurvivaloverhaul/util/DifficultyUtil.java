package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public final class DifficultyUtil
{
    // Utility class for dealing with damaging players

    private DifficultyUtil()
    {
    }

    public static boolean isModDangerous()
    {
        // can the mod do damage?
        return Config.Baked.difficultyMode != EnumUtil.DifficultyMode.PEACEFUL;
    }

    public static boolean healthAboveDifficulty(Player player)
    {
        EnumUtil.DifficultyMode difficulty = Config.Baked.difficultyMode;
        return difficulty == EnumUtil.DifficultyMode.HARD ||
                (difficulty == EnumUtil.DifficultyMode.NORMAL && player.getHealth() > 2f) ||
                ((difficulty == EnumUtil.DifficultyMode.EASY || difficulty == EnumUtil.DifficultyMode.PEACEFUL) && player.getHealth() > 10f);
    }
}
