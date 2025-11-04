package sfiomn.legendarysurvivaloverhaul.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.network.packets.*;

@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler
{

    public NetworkHandler()
    {
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar reg = event.registrar(LegendarySurvivalOverhaul.MOD_ID).versioned("1");

        reg.playBidirectional(UpdateTemperaturesPacket.TYPE, UpdateTemperaturesPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateTemperaturesPacket::handle, UpdateTemperaturesPacket::handle));

        reg.playBidirectional(UpdateWetnessPacket.TYPE, UpdateWetnessPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateWetnessPacket::handle, UpdateWetnessPacket::handle));

        reg.playBidirectional(UpdateThirstPacket.TYPE, UpdateThirstPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateThirstPacket::handle, UpdateThirstPacket::handle));

        reg.playBidirectional(UpdateHeartsPacket.TYPE, UpdateHeartsPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateHeartsPacket::handle, UpdateHeartsPacket::handle));

        reg.playBidirectional(UpdateBodyDamagePacket.TYPE, UpdateBodyDamagePacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateBodyDamagePacket::handle, UpdateBodyDamagePacket::handle));

        reg.playBidirectional(DrinkBlockFluidMessage.TYPE, DrinkBlockFluidMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(DrinkBlockFluidMessage::handle, DrinkBlockFluidMessage::handle));

        reg.playBidirectional(BodyPartHealingTimeMessage.TYPE, BodyPartHealingTimeMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(BodyPartHealingTimeMessage::handle, BodyPartHealingTimeMessage::handle));

        reg.playBidirectional(SyncTemperatureConsumablesPacket.TYPE, SyncTemperatureConsumablesPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureConsumablesPacket::handle, SyncTemperatureConsumablesPacket::handle));
        reg.playBidirectional(SyncTemperatureConsumableBlocksPacket.TYPE, SyncTemperatureConsumableBlocksPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureConsumableBlocksPacket::handle, SyncTemperatureConsumableBlocksPacket::handle));
        reg.playBidirectional(SyncTemperatureBlocksPacket.TYPE, SyncTemperatureBlocksPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureBlocksPacket::handle, SyncTemperatureBlocksPacket::handle));
        reg.playBidirectional(SyncTemperatureItemsPacket.TYPE, SyncTemperatureItemsPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureItemsPacket::handle, SyncTemperatureItemsPacket::handle));
        reg.playBidirectional(SyncTemperatureBiomesPacket.TYPE, SyncTemperatureBiomesPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureBiomesPacket::handle, SyncTemperatureBiomesPacket::handle));
        reg.playBidirectional(SyncTemperatureFuelItemsPacket.TYPE, SyncTemperatureFuelItemsPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureFuelItemsPacket::handle, SyncTemperatureFuelItemsPacket::handle));
        reg.playBidirectional(SyncTemperatureMountsPacket.TYPE, SyncTemperatureMountsPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureMountsPacket::handle, SyncTemperatureMountsPacket::handle));
        reg.playBidirectional(SyncTemperatureDimensionsPacket.TYPE, SyncTemperatureDimensionsPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureDimensionsPacket::handle, SyncTemperatureDimensionsPacket::handle));
        reg.playBidirectional(SyncTemperatureOriginsPacket.TYPE, SyncTemperatureOriginsPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureOriginsPacket::handle, SyncTemperatureOriginsPacket::handle));

        reg.playBidirectional(SyncThirstBlocksPacket.TYPE, SyncThirstBlocksPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncThirstBlocksPacket::handle, SyncThirstBlocksPacket::handle));
        reg.playBidirectional(SyncThirstConsumablesPacket.TYPE, SyncThirstConsumablesPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncThirstConsumablesPacket::handle, SyncThirstConsumablesPacket::handle));

        reg.playBidirectional(SyncBodyDamageHealingConsumablesPacket.TYPE, SyncBodyDamageHealingConsumablesPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncBodyDamageHealingConsumablesPacket::handle, SyncBodyDamageHealingConsumablesPacket::handle));
        reg.playBidirectional(SyncBodyPartsDamageSourcesPacket.TYPE, SyncBodyPartsDamageSourcesPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncBodyPartsDamageSourcesPacket::handle, SyncBodyPartsDamageSourcesPacket::handle));
        reg.playBidirectional(SyncBodyPartResistanceItemsPacket.TYPE, SyncBodyPartResistanceItemsPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncBodyPartResistanceItemsPacket::handle, SyncBodyPartResistanceItemsPacket::handle));
    }
}
