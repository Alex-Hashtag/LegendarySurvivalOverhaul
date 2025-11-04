package sfiomn.legendarysurvivaloverhaul.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.network.payloads.*;

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

        reg.playBidirectional(UpdateTemperaturesPayload.TYPE, UpdateTemperaturesPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateTemperaturesPayload::handle, UpdateTemperaturesPayload::handle));

        reg.playBidirectional(UpdateWetnessPayload.TYPE, UpdateWetnessPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateWetnessPayload::handle, UpdateWetnessPayload::handle));

        reg.playBidirectional(UpdateThirstPayload.TYPE, UpdateThirstPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateThirstPayload::handle, UpdateThirstPayload::handle));

        reg.playBidirectional(UpdateHeartsPayload.TYPE, UpdateHeartsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateHeartsPayload::handle, UpdateHeartsPayload::handle));

        reg.playBidirectional(UpdateBodyDamagePayload.TYPE, UpdateBodyDamagePayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UpdateBodyDamagePayload::handle, UpdateBodyDamagePayload::handle));

        reg.playBidirectional(DrinkBlockFluidMessage.TYPE, DrinkBlockFluidMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(DrinkBlockFluidMessage::handle, DrinkBlockFluidMessage::handle));

        reg.playBidirectional(BodyPartHealingTimeMessage.TYPE, BodyPartHealingTimeMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(BodyPartHealingTimeMessage::handle, BodyPartHealingTimeMessage::handle));

        reg.playBidirectional(SyncTemperatureConsumablesPayload.TYPE, SyncTemperatureConsumablesPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureConsumablesPayload::handle, SyncTemperatureConsumablesPayload::handle));
        reg.playBidirectional(SyncTemperatureConsumableBlocksPayload.TYPE, SyncTemperatureConsumableBlocksPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureConsumableBlocksPayload::handle, SyncTemperatureConsumableBlocksPayload::handle));
        reg.playBidirectional(SyncTemperatureBlocksPayload.TYPE, SyncTemperatureBlocksPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureBlocksPayload::handle, SyncTemperatureBlocksPayload::handle));
        reg.playBidirectional(SyncTemperatureItemsPayload.TYPE, SyncTemperatureItemsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureItemsPayload::handle, SyncTemperatureItemsPayload::handle));
        reg.playBidirectional(SyncTemperatureBiomesPayload.TYPE, SyncTemperatureBiomesPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureBiomesPayload::handle, SyncTemperatureBiomesPayload::handle));
        reg.playBidirectional(SyncTemperatureFuelItemsPayload.TYPE, SyncTemperatureFuelItemsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureFuelItemsPayload::handle, SyncTemperatureFuelItemsPayload::handle));
        reg.playBidirectional(SyncTemperatureMountsPayload.TYPE, SyncTemperatureMountsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureMountsPayload::handle, SyncTemperatureMountsPayload::handle));
        reg.playBidirectional(SyncTemperatureDimensionsPayload.TYPE, SyncTemperatureDimensionsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureDimensionsPayload::handle, SyncTemperatureDimensionsPayload::handle));
        reg.playBidirectional(SyncTemperatureOriginsPayload.TYPE, SyncTemperatureOriginsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncTemperatureOriginsPayload::handle, SyncTemperatureOriginsPayload::handle));

        reg.playBidirectional(SyncThirstBlocksPayload.TYPE, SyncThirstBlocksPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncThirstBlocksPayload::handle, SyncThirstBlocksPayload::handle));
        reg.playBidirectional(SyncThirstConsumablesPaload.TYPE, SyncThirstConsumablesPaload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncThirstConsumablesPaload::handle, SyncThirstConsumablesPaload::handle));

        reg.playBidirectional(SyncBodyDamageHealingConsumablesPayload.TYPE, SyncBodyDamageHealingConsumablesPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncBodyDamageHealingConsumablesPayload::handle, SyncBodyDamageHealingConsumablesPayload::handle));
        reg.playBidirectional(SyncBodyPartsDamageSourcesPayload.TYPE, SyncBodyPartsDamageSourcesPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncBodyPartsDamageSourcesPayload::handle, SyncBodyPartsDamageSourcesPayload::handle));
        reg.playBidirectional(SyncBodyPartResistanceItemsPayload.TYPE, SyncBodyPartResistanceItemsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(SyncBodyPartResistanceItemsPayload::handle, SyncBodyPartResistanceItemsPayload::handle));
    }
}
