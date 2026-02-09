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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
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
import sfiomn.legendarysurvivaloverhaul.common.enchantments.ModEnchantments;
import sfiomn.legendarysurvivaloverhaul.common.integration.crayfish.CrayfishFurnitureUtil;
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

    public int getMaxCapacity(ItemStack stack)
    {
        return getMaxCapacity();
    }

    public int getMaxCapacity(ItemStack stack, Level level)
    {
        int baseCapacity = getMaxCapacity();
        if (level == null) return baseCapacity;
        int reservoirLevel = stack.getEnchantmentLevel(level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(ModEnchantments.RESERVOIR));
        return baseCapacity + reservoirLevel;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, LivingEntity entity)
    {
        return canDrink(stack) ? 40 : 0;
    }

    public boolean canFill(ItemStack stack, Level level)
    {
        // Prevent filling if canteen contains other than normal water
        return Config.Baked.allowOverridePurifiedWater ?
                ThirstUtil.getCapacityTag(stack) < getMaxCapacity(stack, level) :
                ThirstUtil.getCapacityTag(stack) < getMaxCapacity(stack, level) && ThirstUtil.getHydrationEnumTag(stack) != HydrationEnum.PURIFIED;
    }

    public void fill(ItemStack stack, Level level)
    {
        ThirstUtil.setCapacityTag(stack, Math.min(getMaxCapacity(stack, level), ThirstUtil.getCapacityTag(stack) + 1));
        // Check if canteen has Purity enchantment
        boolean hasPurity = level != null && stack.getEnchantmentLevel(level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(ModEnchantments.PURITY)) > 0;
        ThirstUtil.setHydrationEnumTag(stack, hasPurity ? HydrationEnum.PURIFIED : HydrationEnum.NORMAL);
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
        ItemStack canteen = useOnContext.getItemInHand();
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos clickedPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(clickedPos);

        if (player == null) {
            return InteractionResult.PASS;
        }

        int currentCapacity = ThirstUtil.getCapacityTag(canteen);
        int maxCapacity = getMaxCapacity(canteen);
        boolean isFullCanteen = currentCapacity >= maxCapacity;

        // Priority 1: If canteen is 100% full, always empty into cauldron first
        if (isFullCanteen && canDrink(canteen)) {
            // Empty into empty cauldron
            if (blockState.is(Blocks.CAULDRON)) {
                if (!level.isClientSide) {
                    shrinkCapacity(canteen);
                    level.setBlockAndUpdate(clickedPos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1));
                    level.playSound(null, clickedPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, clickedPos);
                }
                player.swing(InteractionHand.MAIN_HAND, true);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            
            // Empty into partial water cauldron
            if (blockState.is(Blocks.WATER_CAULDRON)) {
                int currentLevel = blockState.getValue(LayeredCauldronBlock.LEVEL);
                if (currentLevel < 3) {
                    if (!level.isClientSide) {
                        shrinkCapacity(canteen);
                        level.setBlockAndUpdate(clickedPos, blockState.setValue(LayeredCauldronBlock.LEVEL, currentLevel + 1));
                        level.playSound(null, clickedPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent(null, GameEvent.FLUID_PLACE, clickedPos);
                    }
                    player.swing(InteractionHand.MAIN_HAND, true);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        // Priority 2: Try Crayfish furniture for filling (always check first)
        if (LegendarySurvivalOverhaul.crayfishFurnitureLoaded) {
            InteractionResult result = CrayfishFurnitureUtil.tryFillCanteenFromSinkOrBasin(level, clickedPos, player, canteen);
            if (result.consumesAction()) {
                player.swing(InteractionHand.MAIN_HAND, true);
                return result;
            }
        }

        // Priority 3: Handle vanilla water cauldron - fill canteen from it
        if (blockState.is(Blocks.WATER_CAULDRON) && canFill(canteen, level)) {
            if (!level.isClientSide) {
                this.fill(canteen, level);
                LayeredCauldronBlock.lowerFillLevel(blockState, level, clickedPos);
                level.playSound(null, clickedPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, clickedPos);
            }
            player.swing(InteractionHand.MAIN_HAND, true);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Priority 4: Empty partial canteen into empty cauldron
        if (blockState.is(Blocks.CAULDRON) && canDrink(canteen)) {
            if (!level.isClientSide) {
                shrinkCapacity(canteen);
                level.setBlockAndUpdate(clickedPos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1));
                level.playSound(null, clickedPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, clickedPos);
            }
            player.swing(InteractionHand.MAIN_HAND, true);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        // Priority 5: Empty partial canteen into partial water cauldron
        if (blockState.is(Blocks.WATER_CAULDRON) && canDrink(canteen)) {
            int currentLevel = blockState.getValue(LayeredCauldronBlock.LEVEL);
            if (currentLevel < 3) {
                if (!level.isClientSide) {
                    shrinkCapacity(canteen);
                    level.setBlockAndUpdate(clickedPos, blockState.setValue(LayeredCauldronBlock.LEVEL, currentLevel + 1));
                    level.playSound(null, clickedPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, clickedPos);
                }
                player.swing(InteractionHand.MAIN_HAND, true);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // Handle other water sources
        boolean isWater = isWater(level, clickedPos);
        if (canFill(canteen, level) && isWater) {
            player.swing(InteractionHand.MAIN_HAND, true);

            // Play fill sound
            level.playSound(player, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
            this.fill(canteen, level);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand)
    {
        HitResult positionLookedAt = player.pick(Math.max(3.0, player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) / 2), 0.0F, true);

        ItemStack canteen = player.getItemInHand(hand);

        // Check if looking at a water block
        boolean isWater = false;
        BlockPos blockPos = null;
        if (positionLookedAt.getType() == HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult) positionLookedAt).getBlockPos();
            isWater = isWater(level, blockPos);
        }

        // If it's a water source block, fill the canteen
        if (canFill(canteen, level) && isWater) {
            player.swing(InteractionHand.MAIN_HAND);
            player.playSound(SoundEvents.BOTTLE_FILL, 1.0f, 1.0f);
            this.fill(canteen, level);
            return InteractionResultHolder.consume(canteen);
        }

        // Only pass to useOn() for blocks that it actually handles:
        // - Cauldrons (both empty and water-filled)
        // - Modded blocks (sinks, basins, etc.)
        // This prevents blocking drinking when looking at regular blocks
        if (positionLookedAt.getType() == HitResult.Type.BLOCK && blockPos != null) {
            BlockState blockState = level.getBlockState(blockPos);
            // Check if it's a block that useOn() can handle
            boolean isHandledByUseOn = blockState.is(Blocks.CAULDRON) || 
                                       blockState.is(Blocks.WATER_CAULDRON) ||
                                       (LegendarySurvivalOverhaul.crayfishFurnitureLoaded && 
                                        CrayfishFurnitureUtil.isSinkOrBasin(blockState));
            
            if (isHandledByUseOn) {
                return InteractionResultHolder.pass(canteen);
            }
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
        
        // Apply Refreshing enchantment bonus
        int refreshingLevel = stack.getEnchantmentLevel(level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(ModEnchantments.REFRESHING));
        if (refreshingLevel > 0)
        {
            // Each level gives X additional thirst half bars and (X - 1) additional saturation
            int bonusHydration = refreshingLevel;
            float bonusSaturation = Math.max(0, refreshingLevel - 1);
            ThirstUtil.takeDrink(player, bonusHydration, bonusSaturation);
        }

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
        // Use base capacity for display since we don't have access to Level here
        float max = getMaxCapacity();
        if (max == 0.0f)
            return 0;

        // Clamp to maximum of 13 to prevent visual overflow
        return Math.min(13, Math.round(ThirstUtil.getCapacityTag(stack) / max * 13));
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack)
    {
        // Use base capacity for display since we don't have access to Level here
        float f = Math.max(0.0F, ThirstUtil.getCapacityTag(stack) / (float) this.getMaxCapacity());
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
    
    @Override
    public boolean isEnchantable(@NotNull ItemStack stack)
    {
        return true;
    }
    
    @Override
    public int getEnchantmentValue()
    {
        return 10;
    }
    
    /**
     * Called when Purity enchantment is applied via anvil to immediately purify existing water
     */
    public static void onPurityApplied(ItemStack stack)
    {
        if (ThirstUtil.getCapacityTag(stack) > 0 && ThirstUtil.getHydrationEnumTag(stack) == HydrationEnum.NORMAL)
            ThirstUtil.setHydrationEnumTag(stack, HydrationEnum.PURIFIED);
    }
}
