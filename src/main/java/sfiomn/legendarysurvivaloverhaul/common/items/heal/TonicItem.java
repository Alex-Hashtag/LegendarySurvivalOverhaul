package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class TonicItem extends BodyHealingItem
{
    public TonicItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity)
    {
        return Config.Baked.tonicUseTime;
    }
}
