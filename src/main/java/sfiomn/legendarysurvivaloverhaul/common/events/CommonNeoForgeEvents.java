package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.neoforged.api.distmarker.Dist;
//import net.neoforged.neoforge.common.ForgeMod;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
//import net.neoforged.neoforge.eventbus.api.Event;
import net.neoforged.bus.api.EventPriority;
//import net.neoforged.neoforge.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.attachments.health.HealthAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.thirst.ThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.medsandherbs.MedsAndHerbsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries.SupplementariesUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.common.listeners.*;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;
import sfiomn.legendarysurvivaloverhaul.util.ItemUtil;
import sfiomn.legendarysurvivaloverhaul.util.PlayerModelUtil;

import java.util.*;

import static sfiomn.legendarysurvivaloverhaul.util.internal.BodyDamageUtilInternal.*;
import static sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal.*;
import static sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal.equipmentSlotTemperatureUuid;


@EventBusSubscriber(
        modid = LegendarySurvivalOverhaul.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME
)
public class CommonNeoForgeEvents
{
    // Track players who just blocked with a shield to prevent limb damage
    private static final Set<Player> recentlyBlockedPlayers = ConcurrentHashMap.newKeySet();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();

        if (event.getHand() != event.getEntity().getUsedItemHand())
            return;

        ItemStack usedItemStack = event.getItemStack();
        if (LegendarySurvivalOverhaul.supplementariesLoaded) {
            ItemStack itemStackInBasket = SupplementariesUtil.getSelectedItemInLunchBasket(event.getItemStack());
            if (itemStackInBasket != ItemStack.EMPTY)
                usedItemStack = itemStackInBasket;
        }
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(usedItemStack.getItem());

        if (LegendarySurvivalOverhaul.medsandherbsLoaded
                && itemRegistryName != null && itemRegistryName.getNamespace().equals("meds_and_herbs")) {
            if(itemRegistryName.equals(ResourceLocation.fromNamespaceAndPath("meds_and_herbs", "syringe_morphine"))) {
                if (!MedsAndHerbsUtil.triggerMorphineBehavior(player)) {
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.CONSUME);
                }
            }
            BodyDamageUtil.applyConsumableHealing(player, usedItemStack, true);
        }
    }

    public static void onUseItem(LivingEntityUseItemEvent.Start event) {
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

        if (!entity.level().isClientSide) {
            ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(usedItemStack.getItem());
            TemperatureUtil.applyConsumableTemperature(player, itemRegistryName);
        }

        if (!entity.level().isClientSide) {
            ThirstUtil.takeDrink(player, usedItemStack);
        }

        if (!(usedItemStack.getItem() instanceof BodyHealingItem)) {
            BodyDamageUtil.applyConsumableHealing(player, usedItemStack, true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (shouldApplyThirst(event.getEntity())) {
            // Only run on main hand (otherwise it runs twice)
            if(event.getHand() == InteractionHand.MAIN_HAND && event.getEntity().getMainHandItem().isEmpty())
            {
                Player player = event.getEntity();

                ThirstAttachment thirstAttachment = AttachmentUtil.getThirstAttachment(player);
                if (!thirstAttachment.isHydrationLevelAtMax()) {

                    boolean hasMenu = event.getLevel().getBlockEntity(event.getHitVec().getBlockPos()) instanceof MenuProvider;
                    if (hasMenu)
                        return;

                    JsonThirstBlock jsonBlockThirst = ThirstUtil.getBlockThirstLookedAt(player, (float)(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) / 2));
                    JsonThirstBlock jsonFluidThirst = ThirstUtil.getFluidThirstLookedAt(player, (float)(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) / 2));

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
                if (player != null) {
                    TemperatureUtil.applyConsumableBlockTemperature(player, usedBlock);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttributeModifier(ItemAttributeModifierEvent event) {
        if (!Config.Baked.temperatureEnabled && !Config.Baked.localizedBodyDamageEnabled)
            return;

        if (FMLEnvironment.dist == Dist.CLIENT)
            if(Minecraft.getInstance().level == null) return;

        if (ItemUtil.canBeEquippedInSlot(event.getItemStack(), ItemUtil.getEquippableSlot(event.getItemStack()))) {

            if (Config.Baked.temperatureEnabled) {
                JsonTemperatureResistance config = new JsonTemperatureResistance();
                for (AttributeModifierBase attributeModifier : new AttributeModifierBase[] {
                        TemperatureModifierRegistry.ITEM_ATTRIBUTE.get(),
                        TemperatureModifierRegistry.COAT_ATTRIBUTE.get()
                }) {
                    config.add(attributeModifier.getItemAttributes(event.getItemStack()));
                }

                UUID modifierUuid = equipmentSlotTemperatureUuid.get(ItemUtil.getEquippableSlot(event.getItemStack()));

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

            if ((Config.Baked.localizedBodyDamageEnabled)) {
                ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem());
                JsonBodyPartResistance config = BodyDamageDataManager.getBodyResistanceItem(itemRegistryName);

                if (itemRegistryName == null || config == null)
                    return;

                UUID modifierUuid = equipmentSlotBodyResistanceUuid.get(ItemUtil.getEquippableSlot(event.getItemStack()));

                if (config.bodyResistance != 0)
                    BODY_RESISTANCE.addModifier(event, modifierUuid, config.bodyResistance);

                if (config.headResistance != 0)
                    HEAD_RESISTANCE.addModifier(event, modifierUuid, config.headResistance);

                if (config.chestResistance != 0)
                    CHEST_RESISTANCE.addModifier(event, modifierUuid, config.chestResistance);

                if (config.rightArmResistance != 0)
                    RIGHT_ARM_RESISTANCE.addModifier(event, modifierUuid, config.rightArmResistance);

                if (config.leftArmResistance != 0)
                    LEFT_ARM_RESISTANCE.addModifier(event, modifierUuid, config.leftArmResistance);

                if (config.legsResistance != 0)
                    LEGS_RESISTANCE.addModifier(event, modifierUuid, config.legsResistance);

                if (config.feetResistance != 0)
                    FEET_RESISTANCE.addModifier(event, modifierUuid, config.feetResistance);
            }
        }
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player && shouldApplyThirst((Player) entity) && !entity.level().isClientSide) {
            ThirstUtil.addExhaustion((Player) entity, (float) Config.Baked.onJumpHydrationExhaustion);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (shouldApplyThirst(player) && !player.level().isClientSide && event.getState().getDestroySpeed(event.getLevel(), event.getPos()) > 0.0f) {
            ThirstUtil.addExhaustion(player, (float) Config.Baked.onBlockBreakHydrationExhaustion);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (shouldApplyThirst(player) && !player.level().isClientSide) {
            Entity monster = event.getTarget();
            if(monster.isAttackable()) {
                ThirstUtil.addExhaustion(player, (float) Config.Baked.onAttackHydrationExhaustion);
                player.causeFoodExhaustion((float) Config.Baked.onAttackFoodExhaustion);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurt(LivingIncomingDamageEvent event) {
        if (!event.getSource().is(DamageTypes.FALL) &&
            !event.getSource().is(DamageTypes.STARVE) &&
            !event.getSource().is(DamageTypes.FREEZE) &&
            !event.getSource().is(DamageTypes.DROWN) &&
            !event.getSource().is(ModDamageTypes.DEHYDRATION) &&
            !event.getSource().is(ModDamageTypes.HYPOTHERMIA) &&
            !event.getSource().is(ModDamageTypes.HYPERTHERMIA) && event.getEntity().hasEffect(MobEffectRegistry.VULNERABILITY)) {

            event.setAmount(event.getAmount() * (1 + 0.2f * Objects.requireNonNull(event.getEntity().getEffect(MobEffectRegistry.VULNERABILITY)).getAmplifier() + 1));

        } else if (event.getSource().is(DamageTypes.FALL) && event.getEntity().hasEffect(MobEffectRegistry.HARD_FALLING)) {

            event.setAmount(event.getAmount() * (1 + 0.2f * Objects.requireNonNull(event.getEntity().getEffect(MobEffectRegistry.HARD_FALLING)).getAmplifier() + 1));
            event.getEntity().level().playSound(null, event.getEntity(), SoundRegistry.HARD_FALLING_HURT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        if (event.getBlocked() && event.getEntity() instanceof Player player) {
            recentlyBlockedPlayers.add(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurtDamage(LivingIncomingDamageEvent event) {
        Player player;
        if (event.getEntity() instanceof Player)
            player = (Player) event.getEntity();
        else return;

        if (player.level().isClientSide)
            return;

        // Skip if damage was blocked by shield
        if (recentlyBlockedPlayers.remove(player)) {
            return;
        }

        // Skip if damage was fully negated
        if (event.getAmount() <= 0)
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

            if (!hitBodyParts.isEmpty()) {
                BodyDamageUtil.balancedHurtBodyParts(player, hitBodyParts, bodyPartDamageValue);
                // Update broken hearts after dealing damage
                AttachmentUtil.getBodyDamageAttachment(player).updateBrokenHearts(player);
            }

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
                    // Update broken hearts after healing from sleep
                    AttachmentUtil.getBodyDamageAttachment(player).updateBrokenHearts(player);
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

                HealthAttachment healthAttachment = AttachmentUtil.getHealthAttachment(player);
                healthAttachment.addShieldHealth(2);
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
            if (event.getEffectInstance().getEffect() == MobEffectRegistry.THIRST.get() &&
                    CuriosUtil.isCurioItemEquipped(player, ItemRegistry.WATER_PURIFIER.get())) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.isEndConquered())
            return;

        Player player = event.getEntity();

        if (Config.Baked.temperatureImmunityOnDeathEnabled && Config.Baked.temperatureEnabled) {
            player.addEffect(new MobEffectInstance(MobEffectRegistry.TEMPERATURE_IMMUNITY, Config.Baked.temperatureImmunityOnDeathTime, 0, false, false, true));
        }

        // Reset temperature to NORMAL on respawn
        if (Config.Baked.temperatureEnabled) {
            var tempCap = AttachmentUtil.getTempAttachment(player);
            tempCap.setTemperatureLevel(sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum.NORMAL.getValue());
            tempCap.setTargetTemperatureLevel(sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum.NORMAL.getValue());
            
            if (player instanceof ServerPlayer serverPlayer) {
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateTemperaturesPayload.sendToPlayer(
                    serverPlayer, tempCap.writeNBT()
                );
            }
        }

        // Reset all limb health to full on respawn
        if (Config.Baked.localizedBodyDamageEnabled) {
            var bodyCap = AttachmentUtil.getBodyDamageAttachment(player);
            for (BodyPartEnum part : BodyPartEnum.values()) {
                bodyCap.setBodyPartDamage(part, 0);
            }
            bodyCap.updateBrokenHearts(player);
            bodyCap.setManualDirty();

            if (player instanceof ServerPlayer serverPlayer) {
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateBodyDamagePayload.sendToPlayer(
                    serverPlayer, bodyCap.writeNBT()
                );
            }
        }

        // Reset thirst to full on respawn
        if (Config.Baked.thirstEnabled && shouldApplyThirst(player)) {
            var thirstCap = AttachmentUtil.getThirstAttachment(player);
            thirstCap.init();
            thirstCap.setDirty();

            if (player instanceof ServerPlayer serverPlayer) {
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateThirstPayload.sendToPlayer(
                    serverPlayer, thirstCap.writeNBT()
                );
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (Config.Baked.temperatureImmunityOnFirstSpawnEnabled && Config.Baked.temperatureEnabled && !event.getEntity().getPersistentData().getBoolean("tempImmuneOnSpawn")) {
            event.getEntity().getPersistentData().putBoolean("tempImmuneOnSpawn", true);
            event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.TEMPERATURE_IMMUNITY, Config.Baked.temperatureImmunityOnFirstSpawnTime, 0, false, false, true));
        }

        if (Config.Baked.healthOverhaulEnabled) {
            HealthUtil.initializeHealthAttributes(event.getEntity());
        }

        HealthUtil.updatePlayerMaxHealthAttribute(event.getEntity());
        BodyDamageUtil.updatePlayerBrokenHeartAttribute(event.getEntity());

        // Sync all capabilities to client on login
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            if (Config.Baked.temperatureEnabled) {
                var tempCap = AttachmentUtil.getTempAttachment(serverPlayer);
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateTemperaturesPayload.sendToPlayer(
                    serverPlayer, tempCap.writeNBT()
                );
            }
            if (Config.Baked.thirstEnabled) {
                var thirstCap = AttachmentUtil.getThirstAttachment(serverPlayer);
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateThirstPayload.sendToPlayer(
                    serverPlayer, thirstCap.writeNBT()
                );
            }
            if (Config.Baked.wetnessEnabled) {
                var wetnessCap = AttachmentUtil.getWetnessAttachment(serverPlayer);
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateWetnessPayload.sendToPlayer(
                    serverPlayer, wetnessCap.writeNBT()
                );
            }
            if (Config.Baked.localizedBodyDamageEnabled) {
                var bodyCap = AttachmentUtil.getBodyDamageAttachment(serverPlayer);
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateBodyDamagePayload.sendToPlayer(
                    serverPlayer, bodyCap.writeNBT()
                );
            }
            if (Config.Baked.healthOverhaulEnabled) {
                var healthCap = AttachmentUtil.getHealthAttachment(serverPlayer);
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateHeartsPayload.sendToPlayer(
                    serverPlayer, healthCap.writeNBT()
                );
            }
        }
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
        
        ThirstBlockListener.sendDataToClient(player);
        ThirstConsumableListener.sendDataToClient(player);

        TemperatureBiomeListener.sendDataToClient(player);
        TemperatureBlockListener.sendDataToClient(player);
        TemperatureConsumableListener.sendDataToClient(player);
        TemperatureDimensionListener.sendDataToClient(player);
        TemperatureFuelItemListener.sendDataToClient(player);
        TemperatureItemListener.sendDataToClient(player);
        TemperatureMountListener.sendDataToClient(player);
        TemperatureOriginListener.sendDataToClient(player);

        BodyDamageHealingConsumableListener.sendDataToClient(player);
        BodyPartsDamageSourceListener.sendDataToClient(player);
        BodyPartResistanceItemListener.sendDataToClient(player);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || !(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer))
            return;

        Level level = player.level();
        boolean isStart = player.tickCount < 20;

        // Update Temperature
        if (Config.Baked.temperatureEnabled && !player.isCreative() && !player.isSpectator()) {
            var tempCap = AttachmentUtil.getTempAttachment(player);
            tempCap.tickUpdate(player, level, isStart);
            if (tempCap.isDirty()) {
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateTemperaturesPayload.sendToPlayer(
                    serverPlayer, tempCap.writeNBT()
                );
                tempCap.setClean();
            }
        }

        // Update Thirst
        if (Config.Baked.thirstEnabled && shouldApplyThirst(player)) {
            var thirstCap = AttachmentUtil.getThirstAttachment(player);
            thirstCap.tickUpdate(player, level, isStart);
            if (thirstCap.isDirty()) {
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateThirstPayload.sendToPlayer(
                    serverPlayer, thirstCap.writeNBT()
                );
                thirstCap.setClean();
            }
        }

        // Update Wetness
        if (Config.Baked.wetnessEnabled) {
            var wetnessCap = AttachmentUtil.getWetnessAttachment(player);
            wetnessCap.tickUpdate(player, level, isStart);
            if (wetnessCap.isDirty()) {
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateWetnessPayload.sendToPlayer(
                    serverPlayer, wetnessCap.writeNBT()
                );
                wetnessCap.setClean();
            }
        }

        // Update Food (temperature effects on hunger)
        if (Config.Baked.temperatureEnabled) {
            AttachmentUtil.getFoodAttachment(player).tickUpdate(player, level, isStart);
        }

        // Update Body Damage
        if (Config.Baked.localizedBodyDamageEnabled) {
            var bodyCap = AttachmentUtil.getBodyDamageAttachment(player);
            bodyCap.tickUpdate(player, level, isStart);
            if (bodyCap.isDirty()) {
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateBodyDamagePayload.sendToPlayer(
                    serverPlayer, bodyCap.writeNBT()
                );
                bodyCap.setClean();
            }
        }

        // Update Health
        if (Config.Baked.healthOverhaulEnabled) {
            var healthCap = AttachmentUtil.getHealthAttachment(player);
            if (healthCap.isDirty()) {
                sfiomn.legendarysurvivaloverhaul.network.payloads.UpdateHeartsPayload.sendToPlayer(
                    serverPlayer, healthCap.writeNBT()
                );
                healthCap.setClean();
            }
        }
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
