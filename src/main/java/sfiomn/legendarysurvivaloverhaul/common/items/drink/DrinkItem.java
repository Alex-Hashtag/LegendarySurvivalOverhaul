package sfiomn.legendarysurvivaloverhaul.common.items.drink;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public class DrinkItem extends Item
{

    public DrinkItem(Item.Properties properties)
    {
        super(properties);
    }

    public void runSecondaryEffect(Player player, ItemStack stack)
    {
        //Can be overridden to run a special task
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack)
    {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, LivingEntity entity)
    {
        return 16;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (!Config.Baked.thirstEnabled)
        {
            // Don't restrict drinking if thirst is disabled
            player.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
        }

        IThirstAttachment capability = AttachmentUtil.getThirstAttachment(player);
        if (!capability.isHydrationLevelAtMax())
        {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity entity)
    {
        if (level.isClientSide || !(entity instanceof Player player))
            return stack;

        runSecondaryEffect(player, stack);

        stack.shrink(1);

        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}
