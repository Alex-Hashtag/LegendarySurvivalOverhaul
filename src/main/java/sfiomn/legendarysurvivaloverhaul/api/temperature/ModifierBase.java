package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBiomeOverride;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

/**
 * Abstract class representing temperature modifiers.
 *
 * @author Icey
 */
public abstract class ModifierBase
{
    private static final float COLDEST_BIOME_TEMP = -0.5f;
    private static final float HOTTEST_BIOME_TEMP = 2.0f;
    /**
     * Global World Modifiers
     *
     * Altitude
     * Biome
     * Default
     * Season // requires serene seasons
     * Snow
     * Time
     * Humidity
     * --
     * Proximity World Modifiers
     *
     * Blocks
     * Tile Entities
     * Player Huddling
     * --
     * Unique Player Modifiers
     *
     * Armor
     * Sprinting
     * Temporary
     */

    /**
     * Default temperature of the world.
     */

    public ModifierBase()
    {
    }

    /**
     * Returns temperature from factors based directly on the player, such as what items they
     * are holding, if they are sprinting, on fire, or what temporary modifiers they have.
     * <p>
     * Although it is also possible to get data from the world by calling player$getWorld and
     * player$getPosition, it's not recommended as this will not affect thermometer temperatures
     * or other items/blocks that depend on world influences
     */
    public float getPlayerInfluence(Player player)
    {
        return 0.0f;
    }

    /*
     * Returns temperature based on environmental factors, such as the biome at the given position,
     * proximity to hot/cold blocks, altitude, time, weather, etc.
     * The provided player can be used to check temperature immunities for example.
     * Careful that the provided player is null to calculate the world temperature influence from the temperature util
     */
    public float getWorldInfluence(@Nullable Player player, Level world, BlockPos pos)
    {
        return 0.0f;
    }


    protected float getNormalizedTempForBiome(Level level, Biome biome)
    {
        // Minecraft's temperatures is defined from -0.7 to 2.0, plains are at 0.8
        // Get the biome temperature, clamp it between -0.5 and 2.0 in case of extreme biomes from other mods,
        // and then normalize it from 0 to 1
        // Plains returned temperature 0.44, savanna 0.7, Ice plain 0.26

        ResourceLocation name = level.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);
        JsonTemperatureBiomeOverride biomeInfo = TemperatureDataManager.getBiome(name);
        if (name != null && biomeInfo != null)
        {
            return clampNormalizeTemperature(biomeInfo.temperature);
        }

        return clampNormalizeTemperature(biome.getBaseTemperature());
    }

    protected float applyUndergroundEffect(float temperature, Level level, BlockPos pos, float undergroundTemperature)
    {
        // If we're in a dimension that has a ceiling,
        // then just return the default value.
        if (level.dimensionType().hasCeiling())
        {
            return temperature;
        }

        return Mth.lerp(WorldUtil.getUndergroundEffectAtPos(level, pos), temperature, undergroundTemperature);
    }

    protected float getHumidityForBiome(Level level, Biome biome)
    {
        // Get the biome's humidity
        // Dry biomes have humidity below 0.2

        ResourceLocation name = level.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getKey(biome);

        JsonTemperatureBiomeOverride biomeInfo = TemperatureDataManager.getBiome(name);
        if (name != null && biomeInfo != null)
        {
            return biomeInfo.isDry ? 0.1f : 0.5f;
        }

        return biome.getModifiedClimateSettings().downfall();
    }

    // Clamp and normalize the temperature
    protected float clampNormalizeTemperature(float temp)
    {
        return ((Mth.clamp(temp, COLDEST_BIOME_TEMP, HOTTEST_BIOME_TEMP)) - COLDEST_BIOME_TEMP) / (HOTTEST_BIOME_TEMP - COLDEST_BIOME_TEMP);
    }

    //  Assume input is between 0 and 1
    //  Convert range to -1 and 1 instead
    protected float normalizeToPositiveNegative(float value)
    {
        return (value * 2.0f) - 1.0f;
    }
}
