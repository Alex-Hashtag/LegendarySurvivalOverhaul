package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbConditions;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbPenalty;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.event.LimbConditionsChangedEvent;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

/**
 * Gameplay effects of the {@link LimbPenalty limb penalties}. No potion effect is involved: every hook starts with a
 * bit test on the player's cached limb conditions, and leaves right away while the penalty isn't active.
 * <p>
 * Penalties depending on the player input (no sneaking, no swimming) are in the client hooks, like the vanilla sprint rules.
 */
@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class LimbPenaltyHooks
{
    private static final ResourceLocation SLOW_SPRINT_MODIFIER = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "limb_slow_sprint");

    /**
     * @return the player's limb conditions, {@link LimbConditions#NONE} if localized body damage is disabled or doesn't apply
     */
    private static int conditions(Player player)
    {
        if (!Config.Baked.localizedBodyDamageEnabled || player.isCreative() || player.isSpectator())
            return LimbConditions.NONE;

        return AttachmentUtil.getBodyDamageAttachment(player).getLimbConditions();
    }

    @SubscribeEvent
    public static void onLimbConditionsChanged(LimbConditionsChangedEvent event)
    {
        if (event.isClientSide())
            return;

        if (event.lost(LimbPenalty.SLOW_SPRINT))
            setSlowSprint(event.getPlayer(), false);
    }

    @SubscribeEvent
    public static void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event)
    {
        // Penalties don't apply in creative and spectator
        setSlowSprint(event.getEntity(), false);
    }

    /**
     * Fractured legs: sprinting is slower. <br>
     * There is no event when a player starts or stops sprinting, so the sprint state is compared each tick,
     * but only for players having the penalty.
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event)
    {
        Player player = event.getEntity();
        if (player.level().isClientSide || !LimbConditions.has(conditions(player), LimbPenalty.SLOW_SPRINT))
            return;

        setSlowSprint(player, player.isSprinting());
    }

    private static void setSlowSprint(Player player, boolean slow)
    {
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null || movementSpeed.hasModifier(SLOW_SPRINT_MODIFIER) == slow)
            return;

        if (slow)
            movementSpeed.addTransientModifier(new AttributeModifier(SLOW_SPRINT_MODIFIER, -Config.Baked.slowSprintSpeedPenalty, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        else
            movementSpeed.removeModifier(SLOW_SPRINT_MODIFIER);
    }

    /**
     * Fractured / broken feet: more fall damage. <br>
     * Fractured / broken torso: more damage from everything.
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onIncomingDamage(LivingIncomingDamageEvent event)
    {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide)
            return;

        int conditions = conditions(player);
        if (!LimbConditions.hasAnyPenalty(conditions))
            return;

        double multiplier = 1.0;
        if (event.getSource().is(DamageTypeTags.IS_FALL))
        {
            if (LimbConditions.has(conditions, LimbPenalty.VERY_HARD_FALLING))
                multiplier *= Config.Baked.veryHardFallingDamageMultiplier;
            else if (LimbConditions.has(conditions, LimbPenalty.HARD_FALLING))
                multiplier *= Config.Baked.hardFallingDamageMultiplier;
        }

        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))
        {
            if (LimbConditions.has(conditions, LimbPenalty.VERY_FRAGILE_TORSO))
                multiplier *= Config.Baked.veryFragileTorsoDamageMultiplier;
            else if (LimbConditions.has(conditions, LimbPenalty.FRAGILE_TORSO))
                multiplier *= Config.Baked.fragileTorsoDamageMultiplier;
        }

        if (multiplier != 1.0)
            event.setAmount((float) (event.getAmount() * multiplier));
    }

    /**
     * Fractured arms: eating and drinking is slower. <br>
     * Broken arms: using healing items is slower.
     */
    @SubscribeEvent
    public static void onStartUsingItem(LivingEntityUseItemEvent.Start event)
    {
        if (!(event.getEntity() instanceof Player player))
            return;

        int conditions = conditions(player);
        if (!LimbConditions.hasAnyPenalty(conditions))
            return;

        double speedMultiplier = 1.0;
        UseAnim useAnimation = event.getItem().getUseAnimation();
        if (event.getItem().getItem() instanceof BodyHealingItem)
        {
            if (LimbConditions.has(conditions, LimbPenalty.SLOW_BANDAGING))
                speedMultiplier = Config.Baked.slowBandagingMultiplier;
        } else if (useAnimation == UseAnim.EAT || useAnimation == UseAnim.DRINK)
        {
            if (LimbConditions.has(conditions, LimbPenalty.SLOW_EATING))
                speedMultiplier = Config.Baked.slowEatingMultiplier;
        }

        if (speedMultiplier < 1.0 && speedMultiplier > 0)
            event.setDuration((int) Math.ceil(event.getDuration() / speedMultiplier));
    }

    /**
     * Fractured arms: bows and crossbows are drawn slower. <br>
     * Their power comes from how long they have been used, so some use ticks are frozen instead of changing the use duration.
     */
    @SubscribeEvent
    public static void onUsingItemTick(LivingEntityUseItemEvent.Tick event)
    {
        if (!(event.getEntity() instanceof Player player) || !(event.getItem().getItem() instanceof ProjectileWeaponItem))
            return;

        if (!LimbConditions.has(conditions(player), LimbPenalty.SLOW_BOW_DRAW))
            return;

        // The draw only progresses on the ticks where (tick * multiplier) reaches a new integer
        double multiplier = Config.Baked.slowBowDrawMultiplier;
        int tick = player.tickCount;
        if ((int) (tick * multiplier) == (int) ((tick - 1) * multiplier))
            event.setDuration(event.getDuration() + 1);
    }

    /**
     * Broken arms: projectiles thrown or shot by the player are slower, so they go less far and arrows hit weaker.
     */
    @SubscribeEvent
    public static void onProjectileSpawn(EntityJoinLevelEvent event)
    {
        if (event.getLevel().isClientSide() || event.loadedFromDisk() || !(event.getEntity() instanceof Projectile projectile)
                || !(projectile.getOwner() instanceof Player player))
            return;

        if (LimbConditions.has(conditions(player), LimbPenalty.WEAK_PROJECTILES))
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(Config.Baked.weakProjectilesVelocityMultiplier));
    }

    /**
     * Broken torso: air runs out faster underwater.
     */
    @SubscribeEvent
    public static void onBreathe(LivingBreatheEvent event)
    {
        // Fired every tick for every living entity: leave as soon as possible
        if (event.canBreathe() || !(event.getEntity() instanceof Player player))
            return;

        if (LimbConditions.has(conditions(player), LimbPenalty.SHORTNESS_OF_BREATH))
            event.setConsumeAirAmount((int) Math.round(event.getConsumeAirAmount() * Config.Baked.shortnessOfBreathAirLossMultiplier));
    }
}
