package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;

public class BodyHealingItem extends Item
{

    public BodyHealingItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity)
    {
        return 20;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        JsonHealingConsumable healingConsumable = BodyDamageDataManager.getHealingItem(BuiltInRegistries.ITEM.getKey(this));

        // Only allow the use if the item would heal something
        if (healingConsumable != null && BodyDamageUtil.canUseHealingConsumable(player, healingConsumable))
        {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
    {
        if (entity instanceof Player player)
            BodyDamageUtil.applyConsumableHealing(player, stack, false);

        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}
