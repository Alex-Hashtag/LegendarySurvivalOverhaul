package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonMobEffect;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;

import java.util.List;

public class ThirstUtil
{
    public static IThirstUtil internal;

    /**
     * Player takes a drink from the provided item stack
     *
     * @param player    Player drinking the item
     * @param itemStack Item stack providing hydration values
     */
    public static void takeDrink(Player player, ItemStack itemStack)
    {
        internal.takeDrink(player, itemStack);
    }

    /**
     * Player takes a drink with the specified values and a list of possible effects
     *
     * @param player     Player drinking the item
     * @param hydration  Hydration added to the player
     * @param saturation Saturation added to the player
     * @param effects    List of effects applied to the player
     */
    public static void takeDrink(Player player, int hydration, float saturation, List<JsonMobEffect> effects)
    {
        internal.takeDrink(player, hydration, saturation, effects);
    }

    /**
     * Player takes a drink with the specified values and no chance to trigger side effects
     *
     * @param player     Player drinking the item
     * @param hydration  Hydration added to the player
     * @param saturation Saturation added to the player
     */
    public static void takeDrink(Player player, int hydration, float saturation)
    {
        internal.takeDrink(player, hydration, saturation);
    }

    /**
     * Add thirst exhaustion responsible for the thirst depletion
     *
     * @param player     Player exhausting hydration
     * @param exhaustion Hydration exhaustion value added to the player
     */
    public static void addExhaustion(Player player, float exhaustion)
    {
        internal.addExhaustion(player, exhaustion);
    }

    /**
     * Get fluid json config that the player is looking at, with the given maximum distance
     *
     * @param player        Player trying to drink for a fluid
     * @param finalDistance Player reach distance for drinking
     */
    public static JsonThirstBlock getFluidThirstLookedAt(Player player, double finalDistance)
    {
        return internal.getFluidThirstLookedAt(player, finalDistance);
    }

    /**
     * Get block json config that the player is looking at, with the given maximum distance
     *
     * @param player        Player trying to drink for a block
     * @param finalDistance Player reach distance for drinking
     */
    public static JsonThirstBlock getBlockThirstLookedAt(Player player, double finalDistance)
    {
        return internal.getBlockThirstLookedAt(player, finalDistance);
    }

    /**
     * Sets the thirst enum name tag on the stack
     *
     * @param stack         Item stack drink
     * @param hydrationEnum Thirst Enum
     */
    public static void setHydrationEnumTag(final ItemStack stack, HydrationEnum hydrationEnum)
    {
        internal.setHydrationEnumTag(stack, hydrationEnum);
    }

    /**
     * Gets the hydration enum from the thirst enum tag on the stack
     *
     * @param stack Item stack drink
     * @return null if tag is missing
     */
    @Nullable
    public static HydrationEnum getHydrationEnumTag(final ItemStack stack)
    {
        return internal.getHydrationEnumTag(stack);
    }

    /**
     * Removes the hydration enum tag on the stack if it exists
     *
     * @param stack Item stack drink
     */
    public static void removeHydrationEnumTag(final ItemStack stack)
    {
        internal.removeHydrationEnumTag(stack);
    }

    /**
     * Sets the thirst capacity tag on the stack, so the number of drink doses
     *
     * @param stack    Item stack drink
     * @param capacity number of doses drink
     */
    public static void setCapacityTag(final ItemStack stack, int capacity)
    {
        internal.setCapacityTag(stack, capacity);
    }

    /**
     * Gets the capacity tag on the stack
     *
     * @param stack Item stack drink
     * @return 0 if tag is missing
     */
    public static int getCapacityTag(final ItemStack stack)
    {
        return internal.getCapacityTag(stack);
    }

    /**
     * Removes the capacity tag on the stack if it exists
     *
     * @param stack Item stack drink
     */
    public static void removeCapacityTag(final ItemStack stack)
    {
        internal.removeCapacityTag(stack);
    }

    /**
     * Deactivate the thirst system for the given player
     *
     * @param player The player to which deactivate the thirst system
     */
    public static void deactivateThirst(Player player)
    {
        internal.deactivateThirst(player);
    }

    /**
     * Activate the thirst system for the given player
     *
     * @param player The player to which activate the thirst system
     */
    public static void activateThirst(Player player)
    {
        internal.activateThirst(player);
    }

    /**
     * Check if the thirst system is active for the given player
     *
     * @param player The player thirst system to be checked
     */
    public static boolean isThirstActive(Player player)
    {
        return internal.isThirstActive(player);
    }
}
