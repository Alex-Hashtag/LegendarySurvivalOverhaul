package sfiomn.legendarysurvivaloverhaul.common.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.TickEvent.Phase;
import net.neoforged.neoforge.event.TickEvent.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessProvider;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.packets.*;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilities
{
	public static final ResourceLocation TEMPERATURE_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature");
	public static final ResourceLocation WETNESS_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "wetness");
	public static final ResourceLocation THIRST_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirst");
	public static final ResourceLocation HEALTH_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "health");
	public static final ResourceLocation FOOD_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "food");
	public static final ResourceLocation BODY_DAMAGE_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "body_damage");
	
	@SubscribeEvent
	public static void attachCapabilityPlayer(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof LivingEntity)
		{
			if (event.getObject() instanceof Player player)
			{
				event.addCapability(TEMPERATURE_RES, new TemperatureProvider());
				event.addCapability(WETNESS_RES, new WetnessProvider());
				event.addCapability(THIRST_RES, new ThirstProvider());
				event.addCapability(HEALTH_RES, new HealthProvider());
				event.addCapability(FOOD_RES, new FoodProvider());
				event.addCapability(BODY_DAMAGE_RES, new BodyDamageProvider());
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		if (event.side.isClient())
		{
			// Client Side
			Player player = event.player;

			if (shouldSkipTick(player)) return;

			if (Config.Baked.temperatureEnabled) {
				TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);

				tempCap.tickClient(player, event.phase);
			}
		}
		else
		{
			// Server Side
			Player player = event.player;
			Level level = player.level();

			if (shouldSkipTick(player)) return;

			if (!Config.Baked.vanillaFreezeEnabled) {
				if (player.getTicksFrozen() > 0)
					player.setTicksFrozen(0);
			}

			if (Config.Baked.temperatureEnabled) {
				TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);
				
				tempCap.tickUpdate(player, level, event.phase);
				
				if(event.phase == Phase.START && (tempCap.isDirty() || tempCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					tempCap.setClean();
					sendTemperatureUpdate(player);
				}
			}
			
			if (Config.Baked.wetnessEnabled) {
				WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);
				
				wetCap.tickUpdate(player, level, event.phase);
				
				/**
				 * Because of the way wetness is ticked, if it's dirty, it's probably going to be dirty next tick,
				 * and if it's clean, it's probably going to be clean the next tick
				 * Thus, we don't want to clean up the wetness capability every single tick
				 * just because the player is standing out in the rain
				 * since it's not good for performance
				 */
				if (event.phase == Phase.START && (wetCap.getPacketTimer() % Config.Baked.routinePacketSync == 0 || wetCap.isDirty()))
				{
					wetCap.setClean();
					sendWetnessUpdate(player);
				}
			}

			if (Config.Baked.thirstEnabled) {
				ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);

				thirstCap.tickUpdate(player, level, event.phase);

				if (event.phase == Phase.START && (thirstCap.isDirty() || thirstCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					thirstCap.setClean();
					sendThirstUpdate(player);
				}
			}

			if (Config.Baked.baseFoodExhaustion > 0) {
				FoodCapability foodCapability = CapabilityUtil.getFoodCapability(player);

				foodCapability.tickUpdate(player, level, event.phase);
			}

			if (Config.Baked.localizedBodyDamageEnabled) {
				BodyDamageCapability bodyDamageCapability = CapabilityUtil.getBodyDamageCapability(player);

				bodyDamageCapability.tickUpdate(player, level, event.phase);

				if(event.phase == Phase.START && (bodyDamageCapability.isDirty() || bodyDamageCapability.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					bodyDamageCapability.setClean();
					sendBodyDamageUpdate(player);
				}
			}

			if (Config.Baked.healthOverhaulEnabled) {
				HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);

				if(event.phase == Phase.START && healthCapability.isDirty())
				{
					healthCapability.setClean();
					sendHealthUpdate(player);
				}
			}
		}
	}

	@SubscribeEvent
	public static void deathHandler(PlayerEvent.Clone event)
	{
		Player orig = event.getOriginal();
		Player player = event.getEntity();

		if (event.isWasDeath())
		{
			if (Config.Baked.localizedBodyDamageEnabled) {
				sendBodyDamageUpdate(player);
				BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
			}

			if (Config.Baked.temperatureEnabled)
				player.getPersistentData().putBoolean("tempImmuneOnSpawn", orig.getPersistentData().getBoolean("tempImmuneOnSpawn"));

			if (Config.Baked.healthOverhaulEnabled)
			{
				orig.reviveCaps();
				HealthCapability oldCap = CapabilityUtil.getHealthCapability(orig);
				orig.invalidateCaps();

				HealthCapability newCap = CapabilityUtil.getHealthCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				HealthUtil.initializeHealthAttributes(player);

				if (Config.Baked.heartsLostOnDeath > 0)
					HealthUtil.loseHearth(player, Config.Baked.heartsLostOnDeath);
				else
					HealthUtil.updatePlayerMaxHealthAttribute(player);

				player.setHealth(player.getMaxHealth());
				sendHealthUpdate(player);
			}
		}
		else
		{
			if (Config.Baked.temperatureEnabled)
			{
				orig.reviveCaps();
				TemperatureCapability oldCap = CapabilityUtil.getTempCapability(orig);
				orig.invalidateCaps();

				TemperatureCapability newCap = CapabilityUtil.getTempCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				sendTemperatureUpdate(player);
			}

			if (Config.Baked.wetnessEnabled)
			{
				orig.reviveCaps();
				WetnessCapability oldCap = CapabilityUtil.getWetnessCapability(orig);
				orig.invalidateCaps();

				WetnessCapability newCap = CapabilityUtil.getWetnessCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				sendWetnessUpdate(player);
			}

			if (Config.Baked.thirstEnabled)
			{
				orig.reviveCaps();
				ThirstCapability oldCap = CapabilityUtil.getThirstCapability(orig);
				orig.invalidateCaps();

				ThirstCapability newCap = CapabilityUtil.getThirstCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				sendThirstUpdate(player);
			}
			
			if (Config.Baked.healthOverhaulEnabled)
			{
				orig.reviveCaps();
				HealthCapability oldCap = CapabilityUtil.getHealthCapability(orig);
				orig.invalidateCaps();

				HealthCapability newCap = CapabilityUtil.getHealthCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				HealthUtil.initializeHealthAttributes(player);
				HealthUtil.updatePlayerMaxHealthAttribute(player);
				player.setHealth(player.getMaxHealth());
				sendHealthUpdate(player);
			}

			if (Config.Baked.localizedBodyDamageEnabled)
			{
				orig.reviveCaps();
				BodyDamageCapability oldCap = CapabilityUtil.getBodyDamageCapability(orig);
				orig.invalidateCaps();

				BodyDamageCapability newCap = CapabilityUtil.getBodyDamageCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
				sendBodyDamageUpdate(player);
			}
		}
	}

	private static void sendTemperatureUpdate(Player player)
	{
		if (!player.level().isClientSide())
		{
			UpdateTemperaturesPacket.sendTo(
					PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					CapabilityUtil.getTempCapability(player).writeNBT());
		}
	}

	private static void sendWetnessUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateWetnessPacket.sendTo(
					PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					CapabilityUtil.getWetnessCapability(player).writeNBT());
		}
	}

	private static void sendThirstUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateThirstPacket.sendTo(
					PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					CapabilityUtil.getThirstCapability(player).writeNBT());
		}
	}

	private static void sendBodyDamageUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateBodyDamagePacket.sendTo(
					PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					CapabilityUtil.getBodyDamageCapability(player).writeNBT());
		}
	}

	private static void sendHealthUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateHeartsPacket.sendTo(
					PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					CapabilityUtil.getHealthCapability(player).writeNBT());
		}
	}

	@SubscribeEvent
	public static void syncCapsOnDimensionChange(PlayerChangedDimensionEvent event)
	{
		Player player = event.getEntity();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.wetnessEnabled)
			sendWetnessUpdate(player);
		if (Config.Baked.thirstEnabled)
			sendThirstUpdate(player);
		if (Config.Baked.healthOverhaulEnabled)
			sendHealthUpdate(player);
		if (Config.Baked.localizedBodyDamageEnabled)
			sendBodyDamageUpdate(player);
	}

	@SubscribeEvent
	public static void syncCapsOnLogin(PlayerLoggedInEvent event)
	{
		Player player = event.getEntity();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.wetnessEnabled)
			sendWetnessUpdate(player);
		if (Config.Baked.thirstEnabled)
			sendThirstUpdate(player);
		if (Config.Baked.healthOverhaulEnabled)
			sendHealthUpdate(player);
		if (Config.Baked.localizedBodyDamageEnabled)
			sendBodyDamageUpdate(player);
	}
	
	protected static boolean shouldSkipTick(Player player)
	{
		return player.isCreative() || player.isSpectator();
	}
}
