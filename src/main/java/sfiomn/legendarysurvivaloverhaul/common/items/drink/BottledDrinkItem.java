package sfiomn.legendarysurvivaloverhaul.common.items.drink;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public abstract class BottledDrinkItem extends DrinkItem
{

    public BottledDrinkItem(Properties properties)
    {
        super(properties.stacksTo(64));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, LivingEntity entity)
    {
        return 32;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity entity)
    {
        if (level.isClientSide || !(entity instanceof Player player))
            return stack;

        stack = super.finishUsingItem(stack, level, player);

        if (Config.Baked.glassBottleLootAfterDrink)
        {
            ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);

            if (stack.isEmpty())
            {
                return glassBottle;
            } else
            {
                int slot = player.getInventory().findSlotMatchingUnusedItem(glassBottle);
                if (slot == -1)
                    slot = player.getInventory().getFreeSlot();
                if (slot > -1)
                    player.getInventory().add(slot, glassBottle);
                else
                    player.drop(glassBottle, false);
            }
        }
        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}
