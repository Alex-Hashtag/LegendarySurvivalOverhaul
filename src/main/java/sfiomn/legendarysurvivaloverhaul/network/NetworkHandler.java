package sfiomn.legendarysurvivaloverhaul.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.network.packets.*;

public class NetworkHandler
{
	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);
	
	public static void register()
	{
		int id = -1;
		
		INSTANCE.registerMessage(id++, UpdateTemperaturesPacket.class, UpdateTemperaturesPacket::encode, UpdateTemperaturesPacket::decode, UpdateTemperaturesPacket::handle);
		INSTANCE.registerMessage(id++, UpdateWetnessPacket.class, UpdateWetnessPacket::encode, UpdateWetnessPacket::decode, UpdateWetnessPacket::handle);
		INSTANCE.registerMessage(id++, UpdateThirstPacket.class, UpdateThirstPacket::encode, UpdateThirstPacket::decode, UpdateThirstPacket::handle);
		INSTANCE.registerMessage(id++, UpdateHeartsPacket.class, UpdateHeartsPacket::encode, UpdateHeartsPacket::decode, UpdateHeartsPacket::handle);
		INSTANCE.registerMessage(id++, UpdateBodyDamagePacket.class, UpdateBodyDamagePacket::encode, UpdateBodyDamagePacket::decode, UpdateBodyDamagePacket::handle);
		INSTANCE.registerMessage(id++, DrinkBlockFluidMessage.class, DrinkBlockFluidMessage::encode, DrinkBlockFluidMessage::decode, DrinkBlockFluidMessage::handle);
		INSTANCE.registerMessage(id++, BodyPartHealingTimeMessage.class, BodyPartHealingTimeMessage::encode, BodyPartHealingTimeMessage::decode, BodyPartHealingTimeMessage::handle);

		INSTANCE.registerMessage(id++, SyncTemperatureConsumablesPacket.class, SyncTemperatureConsumablesPacket::encode, SyncTemperatureConsumablesPacket::decode, SyncTemperatureConsumablesPacket::handle);
		INSTANCE.registerMessage(id++, SyncTemperatureConsumableBlocksPacket.class, SyncTemperatureConsumableBlocksPacket::encode, SyncTemperatureConsumableBlocksPacket::decode, SyncTemperatureConsumableBlocksPacket::handle);
		INSTANCE.registerMessage(id++, SyncTemperatureBlocksPacket.class, SyncTemperatureBlocksPacket::encode, SyncTemperatureBlocksPacket::decode, SyncTemperatureBlocksPacket::handle);
		INSTANCE.registerMessage(id++, SyncTemperatureItemsPacket.class, SyncTemperatureItemsPacket::encode, SyncTemperatureItemsPacket::decode, SyncTemperatureItemsPacket::handle);
		INSTANCE.registerMessage(id++, SyncTemperatureBiomesPacket.class, SyncTemperatureBiomesPacket::encode, SyncTemperatureBiomesPacket::decode, SyncTemperatureBiomesPacket::handle);
		INSTANCE.registerMessage(id++, SyncTemperatureFuelItemsPacket.class, SyncTemperatureFuelItemsPacket::encode, SyncTemperatureFuelItemsPacket::decode, SyncTemperatureFuelItemsPacket::handle);
		INSTANCE.registerMessage(id++, SyncTemperatureMountsPacket.class, SyncTemperatureMountsPacket::encode, SyncTemperatureMountsPacket::decode, SyncTemperatureMountsPacket::handle);
		INSTANCE.registerMessage(id++, SyncTemperatureDimensionsPacket.class, SyncTemperatureDimensionsPacket::encode, SyncTemperatureDimensionsPacket::decode, SyncTemperatureDimensionsPacket::handle);
		INSTANCE.registerMessage(id++, SyncTemperatureOriginsPacket.class, SyncTemperatureOriginsPacket::encode, SyncTemperatureOriginsPacket::decode, SyncTemperatureOriginsPacket::handle);

		INSTANCE.registerMessage(id++, SyncThirstBlocksPacket.class, SyncThirstBlocksPacket::encode, SyncThirstBlocksPacket::decode, SyncThirstBlocksPacket::handle);
		INSTANCE.registerMessage(id++, SyncThirstConsumablesPacket.class, SyncThirstConsumablesPacket::encode, SyncThirstConsumablesPacket::decode, SyncThirstConsumablesPacket::handle);

		INSTANCE.registerMessage(id++, SyncBodyDamageHealingConsumablesPacket.class, SyncBodyDamageHealingConsumablesPacket::encode, SyncBodyDamageHealingConsumablesPacket::decode, SyncBodyDamageHealingConsumablesPacket::handle);
		INSTANCE.registerMessage(id++, SyncBodyPartsDamageSourcesPacket.class, SyncBodyPartsDamageSourcesPacket::encode, SyncBodyPartsDamageSourcesPacket::decode, SyncBodyPartsDamageSourcesPacket::handle);
		INSTANCE.registerMessage(id++, SyncBodyPartResistanceItemsPacket.class, SyncBodyPartResistanceItemsPacket::encode, SyncBodyPartResistanceItemsPacket::decode, SyncBodyPartResistanceItemsPacket::handle);
	}
}