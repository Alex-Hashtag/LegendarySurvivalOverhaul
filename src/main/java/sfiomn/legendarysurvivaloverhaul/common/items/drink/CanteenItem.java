package sfiomn.legendarysurvivaloverhaul.common.items.drink;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonMobEffect;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.wetness.WetnessUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.crayfish.CrayfishFurnitureUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EnchantmentRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class CanteenItem extends DrinkItem {

    public CanteenItem(Item.Properties properties){
        super(properties.stacksTo(1));
    }

    public int getMaxCapacity() {
        return Config.Baked.canteenCapacity;
    }

    public int getMaxCapacity(ItemStack stack) {
        int baseCapacity = getMaxCapacity();
        int reservoirLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.RESERVOIR.get(), stack);
        return baseCapacity + reservoirLevel;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return canDrink(stack) ? 40 : 0;
    }

    public static boolean canDrink(ItemStack stack){
        return ThirstUtil.getCapacityTag(stack) > 0 && ThirstUtil.getHydrationEnumTag(stack) != null;
    }

    public boolean canFill(ItemStack stack) {
        // Prevent filling if canteen contains other than normal water
        return Config.Baked.allowOverridePurifiedWater ?
                ThirstUtil.getCapacityTag(stack) < getMaxCapacity(stack) :
                ThirstUtil.getCapacityTag(stack) < getMaxCapacity(stack) && ThirstUtil.getHydrationEnumTag(stack) != HydrationEnum.PURIFIED;
    }

    public void fill(ItemStack stack) {
        ThirstUtil.setCapacityTag(stack,  Math.min(getMaxCapacity(stack), ThirstUtil.getCapacityTag(stack) + 1));
        // Check if canteen has Purity enchantment
        boolean hasPurity = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PURITY.get(), stack) > 0;
        ThirstUtil.setHydrationEnumTag(stack, hasPurity ? HydrationEnum.PURIFIED : HydrationEnum.NORMAL);
    }

    public boolean isWater(Level level, BlockPos blockPos) {

        FluidState fluidState = level.getFluidState(blockPos);
        JsonThirstBlock thirstInfo = ThirstDataManager.getBlock(fluidState);
        if (thirstInfo == null)
            thirstInfo = ThirstDataManager.getBlock(level.getBlockState(blockPos));

        if (thirstInfo != null && thirstInfo.hydration == 3 && thirstInfo.saturation == 0 && !thirstInfo.effects.isEmpty()) {
            for (JsonMobEffect jsonMobEffect : thirstInfo.effects) {
                if (jsonMobEffect.name.equalsIgnoreCase(MobEffectRegistry.THIRST.getId().toString()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
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
        if (blockState.is(Blocks.WATER_CAULDRON) && canFill(canteen)) {
            if (!level.isClientSide) {
                this.fill(canteen);
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
        if (canFill(canteen) && isWater) {
            player.swing(InteractionHand.MAIN_HAND, true);

            if (player instanceof ServerPlayer serverPlayer) {
                ForgeRegistries.SOUND_EVENTS.getHolder(SoundEvents.BOTTLE_FILL).ifPresent(soundHolder -> serverPlayer.connection.send(
                        new ClientboundSoundPacket(
                                soundHolder, SoundSource.PLAYERS, serverPlayer.getX(),
                                serverPlayer.getY(), serverPlayer.getZ(), 1.0F, 1.0F, player.level().getRandom().nextLong())));
            }
            this.fill(canteen);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        HitResult positionLookedAt = player.pick(player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) / 2, 0.0F, true);

        ItemStack canteen = player.getItemInHand(hand);

        // If looking at a block, let useOn() handle it first
        // This allows useOn() to handle cauldrons, sinks, etc.
        if (positionLookedAt.getType() == HitResult.Type.BLOCK) {
            // Don't consume here - let useOn() handle block interactions
            return InteractionResultHolder.pass(canteen);
        }

        // Only handle water sources when not clicking on a specific block
        boolean isWater = false;
        if (positionLookedAt.getType() == HitResult.Type.BLOCK) {
            isWater = isWater(level, ((BlockHitResult) positionLookedAt).getBlockPos());
        }

        if (canFill(canteen) && isWater) {
            player.swing(InteractionHand.MAIN_HAND);
            player.playSound(SoundEvents.BOTTLE_FILL, 1.0f, 1.0f);
            this.fill(canteen);
            return InteractionResultHolder.consume(canteen);
        }

        if (player.isCrouching() && player.getViewXRot(1.0f) < -60.0f && canDrink(canteen) && Config.Baked.selfWateringCanteenEnabled) {
            player.playSound(SoundRegistry.SELF_WATERING.get(), 1.0f, 1.0f);
            if (player.isOnFire())
                player.setSecondsOnFire(0);
            if (Config.Baked.selfWateringCanteenWetnessIncrease > 0)
                WetnessUtil.addWetness(player, Config.Baked.selfWateringCanteenWetnessIncrease);
            player.swing(InteractionHand.MAIN_HAND);
            shrinkCapacity(canteen);
            return InteractionResultHolder.consume(canteen);
        }

        if (canDrink(canteen) && !CapabilityUtil.getThirstCapability(player).isHydrationLevelAtMax()) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(canteen);
        }
        return InteractionResultHolder.fail(canteen);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity entity) {
        if(level.isClientSide || !(entity instanceof Player player))
            return stack;

        runSecondaryEffect(player, stack);
        
        // Apply Refreshing enchantment bonus
        int refreshingLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.REFRESHING.get(), stack);
        if (refreshingLevel > 0) {
            // Each level gives X additional thirst half bars and (X - 1) additional saturation
            int bonusHydration = refreshingLevel;
            float bonusSaturation = Math.max(0, refreshingLevel - 1);
            ThirstUtil.takeDrink(player, bonusHydration, bonusSaturation);
        }

        shrinkCapacity(stack);

        return stack;
    }

    public static void shrinkCapacity(ItemStack stack) {
        int newCapacity = ThirstUtil.getCapacityTag(stack) - 1;
        ThirstUtil.setCapacityTag(stack, newCapacity);
        if (newCapacity == 0)
            ThirstUtil.removeHydrationEnumTag(stack);

    }

    @Override
    public @NotNull String getDescriptionId(ItemStack stack) {
        if(ThirstUtil.getCapacityTag(stack) == 0)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen_empty";

        if (ThirstUtil.getHydrationEnumTag(stack) == HydrationEnum.PURIFIED)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen_purified";
        else
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen";
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return ThirstUtil.getCapacityTag(stack) > 0;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack)
    {
        float max = getMaxCapacity(stack);
        if(max == 0.0f)
            return 0;

        return Math.round(ThirstUtil.getCapacityTag(stack) / max * 13);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        float f = Math.max(0.0F, ThirstUtil.getCapacityTag(stack) / (float)this.getMaxCapacity(stack));
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
    
    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
    }
    
    @Override
    public int getEnchantmentValue() {
        return 10;
    }
    
    /**
     * Called when Purity enchantment is applied to immediately purify existing water
     */
    public static void onPurityApplied(ItemStack stack) {
        if (ThirstUtil.getCapacityTag(stack) > 0 && ThirstUtil.getHydrationEnumTag(stack) == HydrationEnum.NORMAL) {
            ThirstUtil.setHydrationEnumTag(stack, HydrationEnum.PURIFIED);
        }
    }
}
