package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.json.*;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.medsandherbs.MedsAndHerbsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries.SupplementariesUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.DrinkItem;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.common.listeners.*;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.ItemUtil;
import sfiomn.legendarysurvivaloverhaul.util.PlayerModelUtil;

import java.util.*;

import static sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry.PAINKILLER;
import static sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry.PAINKILLER_ADDICTION;
import static sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry.ITEM_ATTRIBUTE_MODIFIERS_REGISTRY;
import static sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal.*;


@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    /*
    @SubscribeEvent
    public static void onFoodTick(LivingEntityUseItemEvent.Tick event) {
        LegendarySurvivalOverhaul.LOGGER.debug("use tick for " + event.getItem() + ", tick : " + event.getDuration());
        if (event.getDuration() > 1)
            return;

        ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(event.getItem().getItem());

        if (Config.Baked.localizedBodyDamageEnabled && !(event.getItem().getItem() instanceof BodyHealingItem)) {
            JsonConsumableHeal jsonConsumableHeal = null;
            if (itemRegistryName != null)
                jsonConsumableHeal = JsonConfig.consumableHeal.get(itemRegistryName.toString());

            if (jsonConsumableHeal != null) {
                if (jsonConsumableHeal.healingCharges > 0) {
                    LegendarySurvivalOverhaul.LOGGER.debug("cancel tick");
                }
            }
        }
    }*/

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        LivingEntity entity = event.getEntity();

        if (!(entity instanceof Player player) || event.getHand() != event.getEntity().getUsedItemHand())
            return;

        ItemStack usedItemStack = event.getItemStack();
        ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(usedItemStack.getItem());

        if (Config.Baked.localizedBodyDamageEnabled && LegendarySurvivalOverhaul.medsandherbsLoaded
                && itemRegistryName != null && itemRegistryName.getNamespace().equals("meds_and_herbs")) {
            if(itemRegistryName.equals(new ResourceLocation("meds_and_herbs", "syringe_morphine"))) {
                if (!MedsAndHerbsUtil.triggerMorphineBehavior(player)) {
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.CONSUME);
                }
            }
            BodyDamageUtil.applyConsumableHealing(player, itemRegistryName);
        }
    }

    @SubscribeEvent
    public static void onFinishUseItem(LivingEntityUseItemEvent.Finish event)
    {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player))
            return;

        ItemStack usedItemStack = event.getItem();
        if (LegendarySurvivalOverhaul.supplementariesLoaded) {
            ItemStack itemStackInBasket = SupplementariesUtil.getSelectedItemInLunchBasket(event.getItem());
            if (itemStackInBasket != ItemStack.EMPTY)
                usedItemStack = itemStackInBasket;
        }

        ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(usedItemStack.getItem());

        if (!entity.level().isClientSide) {
            TemperatureUtil.applyConsumableTemperature(player, itemRegistryName);
        }

        if (!entity.level().isClientSide) {
            ThirstUtil.takeDrink(player, usedItemStack);
        }

        if (Config.Baked.localizedBodyDamageEnabled && !(usedItemStack.getItem() instanceof BodyHealingItem)) {
            BodyDamageUtil.applyConsumableHealing(player, itemRegistryName);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (shouldApplyThirst(event.getEntity())) {
            // Only run on main hand (otherwise it runs twice)
            if(event.getHand() == InteractionHand.MAIN_HAND && event.getEntity().getMainHandItem().isEmpty())
            {
                Player player = event.getEntity();

                ThirstCapability thirstCapability = CapabilityUtil.getThirstCapability(player);
                if (!thirstCapability.isHydrationLevelAtMax()) {

                    boolean hasMenu = event.getLevel().getBlockEntity(event.getHitVec().getBlockPos()) instanceof MenuProvider;
                    if (hasMenu)
                        return;

                    JsonThirstBlock jsonBlockThirst = ThirstUtil.getBlockThirstLookedAt(player, player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) / 2);
                    JsonThirstBlock jsonFluidThirst = ThirstUtil.getFluidThirstLookedAt(player, player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) / 2);

                    //  If we can drink on a block, cancel its use except if crouching
                    if (jsonBlockThirst != null && (jsonBlockThirst.hydration != 0 || jsonBlockThirst.saturation != 0) && !event.getEntity().isCrouching()) {
                        if (event.getLevel().isClientSide)
                            playerDrinkEffect(event.getEntity());
                        else {
                            ThirstUtil.takeDrink(event.getEntity(), jsonBlockThirst.hydration, jsonBlockThirst.saturation, jsonBlockThirst.effects);
                        }
                        event.setCanceled(true);
                        event.setCancellationResult(InteractionResult.CONSUME);
                        return;
                    } else if (jsonFluidThirst != null && (jsonFluidThirst.hydration != 0 || jsonFluidThirst.saturation != 0)) {
                        if (event.getLevel().isClientSide)
                            playerDrinkEffect(event.getEntity());
                        else {
                            ThirstUtil.takeDrink(event.getEntity(), jsonFluidThirst.hydration, jsonFluidThirst.saturation, jsonFluidThirst.effects);
                        }
                        return;
                    }
                }
            }
        }

        if (Config.Baked.temperatureEnabled) {

            // Only run on main hand (otherwise it runs twice)
            if (event.getHand() == InteractionHand.MAIN_HAND && !event.getLevel().isClientSide())
            {
                Player player = event.getEntity();
                BlockState usedBlock = event.getLevel().getBlockState(event.getHitVec().getBlockPos());
                ResourceLocation blockRegistryName = ForgeRegistries.BLOCKS.getKey(usedBlock.getBlock());
                if (player != null && blockRegistryName != null) {
                    TemperatureUtil.applyConsumableTemperature(player, blockRegistryName);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttributeModifier(ItemAttributeModifierEvent event) {
        if (!Config.Baked.temperatureEnabled)
            return;

        if (FMLEnvironment.dist == Dist.CLIENT)
            if(Minecraft.getInstance().level == null) return;

        if (ItemUtil.canBeEquippedInSlot(event.getItemStack(), event.getSlotType())) {
            JsonTemperatureResistance config = new JsonTemperatureResistance();
            for (AttributeModifierBase attributeModifier : ITEM_ATTRIBUTE_MODIFIERS_REGISTRY.get().getValues()) {
                config.add(attributeModifier.getItemAttributes(event.getItemStack()));
            }

            UUID modifierUuid = equipmentSlotUuid.get(event.getSlotType());

            if (config.temperature != 0) {
                HEATING_TEMPERATURE.addModifier(event, modifierUuid, Math.max(config.temperature, 0));
                COOLING_TEMPERATURE.addModifier(event, modifierUuid, Math.min(config.temperature, 0));
            }

            if (config.heatResistance != 0)
                HEAT_RESISTANCE.addModifier(event, modifierUuid, config.heatResistance);

            if (config.coldResistance != 0)
                COLD_RESISTANCE.addModifier(event, modifierUuid, config.coldResistance);

            if (config.thermalResistance != 0)
                THERMAL_RESISTANCE.addModifier(event, modifierUuid, config.thermalResistance);
        }
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player && shouldApplyThirst((Player) entity) && !entity.level().isClientSide) {
            ThirstUtil.addExhaustion((Player) entity, (float) Config.Baked.onJumpThirstExhaustion);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (shouldApplyThirst(player) && !player.level().isClientSide && event.getState().getDestroySpeed(event.getLevel(), event.getPos()) > 0.0f) {
            ThirstUtil.addExhaustion(player, (float) Config.Baked.onBlockBreakThirstExhaustion);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (shouldApplyThirst(player) && !player.level().isClientSide) {
            Entity monster = event.getTarget();
            if(monster.isAttackable()) {
                ThirstUtil.addExhaustion(player, (float) Config.Baked.onAttackThirstExhaustion);
                player.causeFoodExhaustion((float) Config.Baked.onAttackFoodExhaustion);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurt(LivingHurtEvent event) {
        if (!event.getSource().is(DamageTypes.FALL) &&
            !event.getSource().is(DamageTypes.STARVE) &&
            !event.getSource().is(DamageTypes.FREEZE) &&
            !event.getSource().is(DamageTypes.DROWN) &&
            !event.getSource().is(ModDamageTypes.DEHYDRATION) &&
            !event.getSource().is(ModDamageTypes.HYPOTHERMIA) &&
            !event.getSource().is(ModDamageTypes.HYPERTHERMIA) && event.getEntity().hasEffect(MobEffectRegistry.VULNERABILITY.get())) {

            event.setAmount(event.getAmount() * (1 + 0.2f * Objects.requireNonNull(event.getEntity().getEffect(MobEffectRegistry.VULNERABILITY.get())).getAmplifier() + 1));

        } else if (event.getSource().is(DamageTypes.FALL) && event.getEntity().hasEffect(MobEffectRegistry.HARD_FALLING.get())) {

            event.setAmount(event.getAmount() * (1 + 0.2f * Objects.requireNonNull(event.getEntity().getEffect(MobEffectRegistry.HARD_FALLING.get())).getAmplifier() + 1));
            event.getEntity().level().playSound(null, event.getEntity(), SoundRegistry.HARD_FALLING_HURT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurtDamage(LivingDamageEvent event) {
        Player player;
        if (event.getEntity() instanceof Player)
            player = (Player) event.getEntity();
        else return;

        if (player.level().isClientSide)
            return;

        if (shouldApplyHealthOverhaul(player))
            event.setAmount(HealthUtil.hurtPlayer(player, event.getAmount()));

        if (shouldApplyLocalizedBodyDamage(player)) {
            float bodyPartDamageValue = event.getAmount() * (float) Config.Baked.bodyDamageMultiplier;
            DamageSource source = event.getSource();

            JsonBodyPartsDamageSource damageSourceBodyParts = BodyDamageDataManager.getBodyParts(source.getMsgId());
            List<BodyPartEnum> hitBodyParts = new ArrayList<>();
            if (damageSourceBodyParts != null) {

                //  If there are pre-defined body parts for this damage source, use it
                if (damageSourceBodyParts.damageDistribution != DamageDistributionEnum.NONE)
                    hitBodyParts.addAll(damageSourceBodyParts.getBodyParts(player));

            } else {
                if (source.is(DamageTypeTags.IS_PROJECTILE) && source.getDirectEntity() != null) {
                    hitBodyParts.addAll(PlayerModelUtil.getPreciseEntityImpact(source.getDirectEntity(), player));

                } else if (source.getDirectEntity() != null) {
                    List<BodyPartEnum> possibleHitParts = PlayerModelUtil.getEntityImpact(source.getDirectEntity(), player);
                    if (!possibleHitParts.isEmpty()) {
                        hitBodyParts.addAll(DamageDistributionEnum.ONE_OF.getBodyParts(player, possibleHitParts));
                    }
                }

                //  Default random body part assignation
                if (hitBodyParts.isEmpty()) {
                    hitBodyParts.addAll(DamageDistributionEnum.ONE_OF.getBodyParts(player, Arrays.asList(BodyPartEnum.values())));
                }
            }

            if (!hitBodyParts.isEmpty())
                BodyDamageUtil.balancedHurtBodyParts(player, hitBodyParts, bodyPartDamageValue);

            if (source.is(DamageTypeTags.IS_PROJECTILE)
                    && hitBodyParts.contains(BodyPartEnum.HEAD)
                    && Config.Baked.headCriticalShotMultiplier > 1
                    && player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                event.setAmount(event.getAmount() * (float) Config.Baked.headCriticalShotMultiplier);
                player.level().playLocalSound(player.blockPosition(), SoundRegistry.HEADSHOT.get(), SoundSource.HOSTILE, 1.0F, 1.0F, false);
            }
        }
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        for (Player player : event.getLevel().players()) {
            if (player.isSleepingLongEnough()) {
                if (Config.Baked.localizedBodyDamageEnabled && Config.Baked.bodyHealthRatioRecoveredFromSleep > 0) {
                    for (BodyPartEnum bodyPart : BodyPartEnum.values()) {
                        double healthRecovered = BodyDamageUtil.getMaxHealth(player, bodyPart) * Config.Baked.bodyHealthRatioRecoveredFromSleep;
                        BodyDamageUtil.healBodyPart(player, bodyPart, (float) healthRecovered);
                    }
                    CapabilityUtil.getBodyDamageCapability(player).updateBrokenHearts();
                    BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
                }

                if (Config.Baked.healthRatioRecoveredFromSleep > 0) {
                    HealthUtil.updatePlayerMaxHealthAttribute(player);
                    double healthRecovered = player.getMaxHealth() * Config.Baked.healthRatioRecoveredFromSleep;
                    player.heal((float) healthRecovered);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEffect(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player && !event.getEntity().level().isClientSide) {
            if (Config.Baked.healthOverhaulEnabled &&
                    event.getEffectInstance().getEffect() == MobEffects.ABSORPTION &&
                    Config.Baked.absorptionEffectOverride) {

                HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);
                healthCapability.addShieldHealth(2);

                event.setResult(Event.Result.DENY);
            }
            if (event.getEffectInstance().getEffect() == MobEffectRegistry.THIRST.get() &&
                    CuriosUtil.isCurioItemEquipped(player, ItemRegistry.WATER_PURIFIER.get())) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.isEndConquered())
            return;

        if (Config.Baked.temperatureImmunityOnDeathEnabled && Config.Baked.temperatureEnabled) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.TEMPERATURE_IMMUNITY.get(), Config.Baked.temperatureImmunityOnDeathTime, 0, false, false, true));
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (Config.Baked.temperatureImmunityOnFirstSpawnEnabled && Config.Baked.temperatureEnabled && !event.getEntity().getPersistentData().getBoolean("tempImmuneOnSpawn")) {
            event.getEntity().getPersistentData().putBoolean("tempImmuneOnSpawn", true);
            event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.TEMPERATURE_IMMUNITY.get(), Config.Baked.temperatureImmunityOnFirstSpawnTime, 0, false, false, true));
        }

        if (Config.Baked.healthOverhaulEnabled) {
            HealthUtil.initializeHealthAttributes(event.getEntity());
        }

        HealthUtil.updatePlayerMaxHealthAttribute(event.getEntity());
        BodyDamageUtil.updatePlayerBrokenHeartAttribute(event.getEntity());
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide()) {
            LevelData levelData = event.getLevel().getLevelData();
            if (levelData instanceof PrimaryLevelData primaryLevelData) {
                primaryLevelData.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).set(Config.Baked.naturalRegenerationEnabled, event.getLevel().getServer());
            }
        }
    }

    @SubscribeEvent
    public static void onDataPackSyncEvent(OnDatapackSyncEvent event) {
        final ServerPlayer player = event.getPlayer();
        final PacketDistributor.PacketTarget target = player == null ? PacketDistributor.ALL.noArg() : PacketDistributor.PLAYER.with(() -> player);

        ThirstBlockListener.sendDataToClient(target);
        ThirstConsumableListener.sendDataToClient(target);

        TemperatureBiomeListener.sendDataToClient(target);
        TemperatureBlockListener.sendDataToClient(target);
        TemperatureConsumableListener.sendDataToClient(target);
        TemperatureDimensionListener.sendDataToClient(target);
        TemperatureFuelItemListener.sendDataToClient(target);
        TemperatureItemListener.sendDataToClient(target);
        TemperatureMountListener.sendDataToClient(target);
        TemperatureOriginListener.sendDataToClient(target);

        BodyDamageHealingConsumableListener.sendDataToClient(target);
        BodyPartsDamageSourceListener.sendDataToClient(target);
    }

    private static boolean shouldApplyThirst(Player player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player);
    }

    private static boolean shouldApplyLocalizedBodyDamage(Player player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.localizedBodyDamageEnabled;
    }

    private static boolean shouldApplyHealthOverhaul(Player player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.healthOverhaulEnabled;
    }

    public static void playerDrinkEffect(Player player)
    {
        //Play sound and swing arm
        player.swing(InteractionHand.MAIN_HAND);
        player.playSound(SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
    }
}
