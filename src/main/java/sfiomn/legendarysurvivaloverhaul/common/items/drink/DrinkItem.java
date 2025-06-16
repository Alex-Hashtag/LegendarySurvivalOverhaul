package sfiomn.legendarysurvivaloverhaul.common.items.drink;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstCapability;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class DrinkItem extends Item {

    public DrinkItem(Item.Properties properties) {
        super(properties);
    }

    public void runSecondaryEffect(Player player, ItemStack stack)
    {
        //Can be overridden to run a special task
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 16;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(!Config.Baked.thirstEnabled)
        {
            // Don't restrict drinking if thirst is disabled
            player.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
        }

        IThirstCapability capability = CapabilityUtil.getThirstCapability(player);
        if(!capability.isHydrationLevelAtMax())
        {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
    {
        if(level.isClientSide || !(entity instanceof Player player))
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
