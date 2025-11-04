package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class PlasterItem extends BodyHealingItem
{
    public PlasterItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity)
    {
        return Config.Baked.plasterUseTime;
    }
}
