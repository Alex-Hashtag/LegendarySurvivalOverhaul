package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;

public class WearableCurioItem extends Item
{

    public WearableCurioItem(Properties p_41383_)
    {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {

        ItemStack itemstack = player.getItemInHand(hand);

        boolean isWorn = CuriosUtil.equipCurio(player, itemstack, hand);
        if (isWorn)
            level.playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC.value(), player.getSoundSource(), 1.0f, 1.0f);

        return isWorn ? InteractionResultHolder.success(itemstack) : InteractionResultHolder.fail(itemstack);
    }
}
