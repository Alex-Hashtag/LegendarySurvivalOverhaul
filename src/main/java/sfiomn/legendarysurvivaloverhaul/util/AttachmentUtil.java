package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.common.attachments.ModAttachments;
import sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage.BodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.food.FoodAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.health.HealthAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.temperature.TemperatureAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.temperature.TemperatureItemAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.thirst.ThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.wetness.WetnessAttachment;

/**
 * Helper functions for quickly getting player capabilities.
 *
 * @author Icey
 */
public final class AttachmentUtil
{
    private AttachmentUtil()
    {
    }

    /**
     * Gets the temperature capability of the given player.
     *
     * @param player Player
     * @return The temperature capability of the given player if it exists, or a new dummy capability if it doesn't.
     */
    public static TemperatureAttachment getTempAttachment(Player player)
    {
        TemperatureAttachment cap = player.getData(ModAttachments.TEMPERATURE.get());
        return cap != null ? cap : new TemperatureAttachment();
    }

    /**
     * Gets the temperature item capability of the given itemstack.
     *
     * @param itemStack ItemStack
     * @return The temperature item capability of the given itemstack if it exists, or a new dummy capability if it doesn't.
     */
    public static TemperatureItemAttachment getTempItemAttachment(ItemStack itemStack)
    {
        // Use data component instead of attachment (1.21+ uses data components for ItemStacks)
        return new TemperatureItemAttachment(itemStack);
    }

    /**
     * Gets the health capability of the given player.
     *
     * @param player Player
     * @return The health capability of the given player if it exists, or a new dummy capability if it doesn't.
     */
    public static HealthAttachment getHealthAttachment(Player player)
    {
        HealthAttachment cap = player.getData(ModAttachments.HEALTH.get());
        return cap != null ? cap : new HealthAttachment();
    }

    /**
     * Gets the wetness capability of the given player.
     *
     * @param player Player
     * @return The wetness capability of the given player if it exists, or a new dummy capability if it doesn't.
     */
    public static WetnessAttachment getWetnessAttachment(Player player)
    {
        WetnessAttachment cap = player.getData(ModAttachments.WETNESS.get());
        return cap != null ? cap : new WetnessAttachment();
    }

    /**
     * Gets the thirst capability of the given player.
     *
     * @param player Player
     * @return The thirst capability of the given player if it exists, or a new dummy capability if it doesn't.
     */
    public static ThirstAttachment getThirstAttachment(Player player)
    {
        ThirstAttachment cap = player.getData(ModAttachments.THIRST.get());
        return cap != null ? cap : new ThirstAttachment();
    }

    /**
     * Gets the Food capability of the given player.
     *
     * @param player Player
     * @return The food capability of the given player if it exists, or a new dummy capability if it doesn't.
     */
    public static FoodAttachment getFoodAttachment(Player player)
    {
        FoodAttachment cap = player.getData(ModAttachments.FOOD.get());
        return cap != null ? cap : new FoodAttachment();
    }

    /**
     * Gets the Body Damage capability of the given player.
     *
     * @param player Player
     * @return The body damage capability of the given player if it exists, or a new dummy capability if it doesn't.
     */
    public static BodyDamageAttachment getBodyDamageAttachment(Player player)
    {
        BodyDamageAttachment cap = player.getData(ModAttachments.BODY_DAMAGE.get());
        return cap != null ? cap : new BodyDamageAttachment();
    }
}
