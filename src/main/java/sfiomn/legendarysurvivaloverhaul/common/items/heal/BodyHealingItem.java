package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

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

        if (!Config.Baked.localizedBodyDamageEnabled)
        {
            // Don't restrict use item if localized Body Damage is disabled
            player.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
        }

        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(this);
        JsonHealingConsumable jsonConsumableHeal = BodyDamageDataManager.getHealingItem(registryName);

        if (jsonConsumableHeal != null)
        {
            IBodyDamageAttachment capability = AttachmentUtil.getBodyDamageAttachment(player);
            // 0 healing charge = heal all body parts => only allow healing if one wounded limb is not yet healing
            if (jsonConsumableHeal.healingCharges == 0)
            {
                for (BodyPartEnum bodyPart : BodyPartEnum.values())
                {
                    if (capability.getBodyPartDamage(bodyPart) > 0 || player.getHealth() < player.getMaxHealth())
                    {
                        player.startUsingItem(hand);
                        return InteractionResultHolder.success(stack);
                    }
                }
            } else if (jsonConsumableHeal.healingCharges > 0)
            {
                player.startUsingItem(hand);
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
    {
        if (!(entity instanceof Player player))
            return stack;

        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(this);
        JsonHealingConsumable jsonConsumableHeal = BodyDamageDataManager.getHealingItem(registryName);

        if (jsonConsumableHeal == null)
            return stack;

        if (!Config.Baked.localizedBodyDamageEnabled)
        {
            if (jsonConsumableHeal.recoveryEffectDuration > 0)
                player.addEffect(new MobEffectInstance(
                        MobEffectRegistry.RECOVERY,
                        jsonConsumableHeal.recoveryEffectDuration,
                        jsonConsumableHeal.recoveryEffectAmplifier, false, true, true));
            stack.shrink(1);
            return stack;
        }

        BodyDamageUtil.applyConsumableHealing(player, stack, false);

        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}
