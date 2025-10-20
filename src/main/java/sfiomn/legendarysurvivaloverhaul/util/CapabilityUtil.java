package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.common.attachments.ModAttachments;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.registry.DataComponentRegistry;

/**
 * Helper functions for quickly getting player capabilities.
 * @author Icey
 */
public final class CapabilityUtil
{
    private CapabilityUtil() {}
	
	/**
	 * Gets the temperature capability of the given player.
	 * @param player Player
	 * @return The temperature capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
    public static TemperatureCapability getTempCapability(Player player)
    {
        TemperatureCapability cap = player.getData(ModAttachments.TEMPERATURE.get());
        return cap != null ? cap : new TemperatureCapability();
    }

	/**
	 * Gets the temperature item capability of the given itemstack.
	 * @param itemStack ItemStack
	 * @return The temperature item capability of the given itemstack if it exists, or a new dummy capability if it doesn't.
	 */
    public static TemperatureItemCapability getTempItemCapability(ItemStack itemStack)
    {
        // Use data component instead of attachment (1.21+ uses data components for ItemStacks)
        return new TemperatureItemCapability(itemStack);
    }

	/**
	 * Gets the health capability of the given player.
	 * @param player Player
	 * @return The health capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
    public static HealthCapability getHealthCapability(Player player)
    {
        HealthCapability cap = player.getData(ModAttachments.HEALTH.get());
        return cap != null ? cap : new HealthCapability();
    }

	/**
	 * Gets the wetness capability of the given player.
	 * @param player Player
	 * @return The wetness capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
    public static WetnessCapability getWetnessCapability(Player player)
    {
        WetnessCapability cap = player.getData(ModAttachments.WETNESS.get());
        return cap != null ? cap : new WetnessCapability();
    }

	/**
	 * Gets the thirst capability of the given player.
	 * @param player Player
	 * @return The thirst capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
    public static ThirstCapability getThirstCapability(Player player)
    {
        ThirstCapability cap = player.getData(ModAttachments.THIRST.get());
        return cap != null ? cap : new ThirstCapability();
    }

	/**
	 * Gets the Food capability of the given player.
	 * @param player Player
	 * @return The food capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
    public static FoodCapability getFoodCapability(Player player)
    {
        FoodCapability cap = player.getData(ModAttachments.FOOD.get());
        return cap != null ? cap : new FoodCapability();
    }

	/**
	 * Gets the Body Damage capability of the given player.
	 * @param player Player
	 * @return The body damage capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
    public static BodyDamageCapability getBodyDamageCapability(Player player)
    {
        BodyDamageCapability cap = player.getData(ModAttachments.BODY_DAMAGE.get());
        return cap != null ? cap : new BodyDamageCapability();
    }
}
