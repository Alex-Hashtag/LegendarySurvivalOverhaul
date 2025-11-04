package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;

import java.util.List;

public class ThirstDataManager
{

    public static IThirstBlockManager internalBlock;
    public static IThirstConsumableManager internalConsumable;

    /**
     * Retrieves the list of Thirst Information related to the provided block registry name
     *
     * @param blockRegistryName resource location of the block
     * @return list of Thirst Information
     */
    @Nullable
    public static List<JsonThirstBlock> getBlock(ResourceLocation blockRegistryName)
    {
        return internalBlock.get(blockRegistryName);
    }

    /**
     * Retrieves the Thirst Information matching the provided block state
     *
     * @param block block state
     * @return Thirst Information
     */
    @Nullable
    public static JsonThirstBlock getBlock(BlockState block)
    {
        return internalBlock.get(block);
    }

    /**
     * Retrieves the Thirst Information matching the provided fluid state
     *
     * @param fluid fluid state
     * @return Thirst Information
     */
    @Nullable
    public static JsonThirstBlock getBlock(FluidState fluid)
    {
        return internalBlock.get(fluid);
    }

    /**
     * Retrieves the list of Thirst Information related to the provided item registry name
     *
     * @param itemRegistryName resource location of the item
     * @return list of Thirst Information
     */
    @Nullable
    public static List<JsonThirstConsumable> getConsumable(ResourceLocation itemRegistryName)
    {
        return internalConsumable.get(itemRegistryName);
    }

    /**
     * Retrieves the JsonConsumableThirst for the given item stack, based on its nbt data
     *
     * @param itemStack item stack
     * @return Thirst Information
     */
    @Nullable
    public static JsonThirstConsumable getConsumable(ItemStack itemStack)
    {
        return internalConsumable.get(itemStack);
    }
}
