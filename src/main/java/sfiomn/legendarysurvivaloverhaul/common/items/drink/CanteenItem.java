package sfiomn.legendarysurvivaloverhaul.common.items.drink;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonMobEffect;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.wetness.WetnessUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public class CanteenItem extends DrinkItem
{

    public CanteenItem(Item.Properties properties)
    {
        super(properties.stacksTo(1));
    }

    public static boolean canDrink(ItemStack stack)
    {
        return ThirstUtil.getCapacityTag(stack) > 0 && ThirstUtil.getHydrationEnumTag(stack) != null;
    }

    public static void shrinkCapacity(ItemStack stack)
    {
        int newCapacity = ThirstUtil.getCapacityTag(stack) - 1;
        ThirstUtil.setCapacityTag(stack, newCapacity);
        if (newCapacity == 0)
            ThirstUtil.removeHydrationEnumTag(stack);

    }

    public int getMaxCapacity()
    {
        return Config.Baked.canteenCapacity;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, LivingEntity entity)
    {
        return canDrink(stack) ? 40 : 0;
    }

    public boolean canFill(ItemStack stack)
    {
        // Prevent filling if canteen contains other than normal water
        return Config.Baked.allowOverridePurifiedWater ?
                ThirstUtil.getCapacityTag(stack) < getMaxCapacity() :
                ThirstUtil.getCapacityTag(stack) < getMaxCapacity() && ThirstUtil.getHydrationEnumTag(stack) != HydrationEnum.PURIFIED;
    }

    public void fill(ItemStack stack)
    {
        ThirstUtil.setCapacityTag(stack, Math.min(getMaxCapacity(), ThirstUtil.getCapacityTag(stack) + 1));
        ThirstUtil.setHydrationEnumTag(stack, HydrationEnum.NORMAL);
    }

    public boolean isWater(Level level, BlockPos blockPos)
    {

        FluidState fluidState = level.getFluidState(blockPos);
        JsonThirstBlock thirstInfo = ThirstDataManager.getBlock(fluidState);
        if (thirstInfo == null)
            thirstInfo = ThirstDataManager.getBlock(level.getBlockState(blockPos));

        if (thirstInfo != null && thirstInfo.hydration == 3 && thirstInfo.saturation == 0 && !thirstInfo.effects.isEmpty())
        {
            for (JsonMobEffect jsonMobEffect : thirstInfo.effects)
            {
                if (jsonMobEffect.name.equalsIgnoreCase(MobEffectRegistry.THIRST.getId().toString()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext)
    {
        boolean isWater = isWater(useOnContext.getLevel(), useOnContext.getClickedPos());
        ItemStack canteen = useOnContext.getItemInHand();
        Player player = useOnContext.getPlayer();
        if (canFill(canteen) && isWater && player != null)
        {
            player.swing(InteractionHand.MAIN_HAND, true);

            // Play fill sound
            useOnContext.getLevel().playSound(player, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
            this.fill(canteen);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand)
    {
        HitResult positionLookedAt = player.pick(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) / 2, 0.0F, true);

        boolean isWater = false;
        if (positionLookedAt.getType() == HitResult.Type.BLOCK)
        {
            isWater = isWater(level, ((BlockHitResult) positionLookedAt).getBlockPos());
        }

        ItemStack canteen = player.getItemInHand(hand);

        if (canFill(canteen) && isWater)
        {
            player.swing(InteractionHand.MAIN_HAND);
            player.playSound(SoundEvents.BOTTLE_FILL, 1.0f, 1.0f);
            this.fill(canteen);
            return InteractionResultHolder.consume(canteen);
        }

        if (player.isCrouching() && player.getViewXRot(1.0f) < -60.0f && canDrink(canteen) && Config.Baked.selfWateringCanteenEnabled)
        {
            player.playSound(SoundRegistry.SELF_WATERING.get(), 1.0f, 1.0f);
            if (player.isOnFire())
                player.clearFire();
            if (Config.Baked.selfWateringCanteenWetnessIncrease > 0)
                WetnessUtil.addWetness(player, Config.Baked.selfWateringCanteenWetnessIncrease);
            player.swing(InteractionHand.MAIN_HAND);
            shrinkCapacity(canteen);
            return InteractionResultHolder.consume(canteen);
        }

        if (canDrink(canteen) && !AttachmentUtil.getThirstAttachment(player).isHydrationLevelAtMax())
        {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(canteen);
        }
        return InteractionResultHolder.fail(canteen);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity entity)
    {
        if (level.isClientSide || !(entity instanceof Player player))
            return stack;

        runSecondaryEffect(player, stack);

        shrinkCapacity(stack);

        return stack;
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack stack)
    {
        if (ThirstUtil.getCapacityTag(stack) == 0)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen_empty";

        if (ThirstUtil.getHydrationEnumTag(stack) == HydrationEnum.PURIFIED)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen_purified";
        else
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen";
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack)
    {
        return ThirstUtil.getCapacityTag(stack) > 0;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack)
    {
        float max = getMaxCapacity();
        if (max == 0.0f)
            return 0;

        return Math.round(ThirstUtil.getCapacityTag(stack) / max * 13);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack)
    {
        float f = Math.max(0.0F, ThirstUtil.getCapacityTag(stack) / (float) this.getMaxCapacity());
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
}
