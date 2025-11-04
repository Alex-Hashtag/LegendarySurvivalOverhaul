package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.*;

import java.util.List;

public class TemperatureDataManager
{

    public static ITemperatureBlockManager internalBlock;
    public static ITemperatureConsumableManager internalConsumable;
    public static ITemperatureConsumableBlockManager internalConsumableBlock;
    public static ITemperatureItemManager internalItem;
    public static ITemperatureBiomeManager internalBiome;
    public static ITemperatureFuelItemManager internalFuelItem;
    public static ITemperatureDimensionManager internalDimension;
    public static ITemperatureMountManager internalMount;
    public static ITemperatureOriginManager internalOrigin;

    /**
     * Retrieves the list of Temperature Information related to the provided block registry name
     *
     * @param blockRegistryName resource location of the block
     * @return list of Temperature Information
     */
    public static List<JsonTemperatureBlock> getBlock(ResourceLocation blockRegistryName)
    {
        return internalBlock.get(blockRegistryName);
    }

    /**
     * Retrieves the list of Temporary Temperature Effect related to the provided consumable item registry name
     *
     * @param itemRegistryName resource location of the consumable item
     * @return list of Temporary Temperature Effect
     */
    public static List<JsonTemperatureConsumable> getConsumable(ResourceLocation itemRegistryName)
    {
        return internalConsumable.get(itemRegistryName);
    }

    /**
     * Retrieves the list of Temporary Temperature Effect related to the provided consumable block registry name
     *
     * @param itemRegistryName resource location of the consumable block
     * @return list of Temporary Temperature Effect
     */
    public static List<JsonTemperatureConsumableBlock> getConsumableBlock(ResourceLocation itemRegistryName)
    {
        return internalConsumableBlock.get(itemRegistryName);
    }

    /**
     * Retrieves the Temperature Resistances related to the provided item registry name
     *
     * @param itemRegistryName resource location of the item
     * @return Temperature Resistances
     */
    @Nullable
    public static JsonTemperatureResistance getItem(ResourceLocation itemRegistryName)
    {
        return internalItem.get(itemRegistryName);
    }

    /**
     * Retrieves the Biome Temperature that overrides the default Minecraft Biome Temperature for the provided biome registry name
     *
     * @param biomeRegistryName resource location of the biome
     * @return Biome Temperature overriding Minecraft Biome Temperature
     */
    @Nullable
    public static JsonTemperatureBiomeOverride getBiome(ResourceLocation biomeRegistryName)
    {
        return internalBiome.get(biomeRegistryName);
    }

    /**
     * Retrieves the fuel values used in the cooler / heater for the provided item registry name
     *
     * @param itemRegistryName resource location of the biome
     * @return Fuel values
     */
    @Nullable
    public static JsonTemperatureFuelItem getFuelItem(ResourceLocation itemRegistryName)
    {
        return internalFuelItem.get(itemRegistryName);
    }

    /**
     * Retrieves the temperature value of the provided dimension registry name
     *
     * @param dimensionRegistryName resource location of the dimension
     * @return Dimension Temperature
     */
    @Nullable
    public static JsonTemperatureDimension getDimension(ResourceLocation dimensionRegistryName)
    {
        return internalDimension.get(dimensionRegistryName);
    }

    /**
     * Retrieves the temperature value of the provided entity type registry name
     *
     * @param mountRegistryName resource location of the entity type
     * @return Entity Temperature
     */
    @Nullable
    public static JsonTemperatureResistance getMount(ResourceLocation mountRegistryName)
    {
        return internalMount.get(mountRegistryName);
    }

    /**
     * Retrieves the temperature value of the provided origin registry name
     *
     * @param originRegistryName resource location of the origin
     * @return Origin Temperature
     */
    @Nullable
    public static JsonTemperatureResistance getOrigin(ResourceLocation originRegistryName)
    {
        return internalOrigin.get(originRegistryName);
    }
}
