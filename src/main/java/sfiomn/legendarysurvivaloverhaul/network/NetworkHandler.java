package sfiomn.legendarysurvivaloverhaul.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.network.packets.*;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

    public NetworkHandler() {
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar reg = event.registrar(LegendarySurvivalOverhaul.MOD_ID).versioned("1");

        reg.play(UpdateTemperaturesPacket.ID, UpdateTemperaturesPacket::new,
                h -> h.client(UpdateTemperaturesPacket::handle)
                        .server(UpdateTemperaturesPacket::handle));

        reg.play(UpdateWetnessPacket.ID, UpdateWetnessPacket::new,
                h -> h.client(UpdateWetnessPacket::handle)
                        .server(UpdateWetnessPacket::handle));

        reg.play(UpdateThirstPacket.ID, UpdateThirstPacket::new,
                h -> h.client(UpdateThirstPacket::handle)
                        .server(UpdateThirstPacket::handle));

        reg.play(UpdateHeartsPacket.ID, UpdateHeartsPacket::new,
                h -> h.client(UpdateHeartsPacket::handle)
                        .server(UpdateHeartsPacket::handle));

        reg.play(UpdateBodyDamagePacket.ID, UpdateBodyDamagePacket::new,
                h -> h.client(UpdateBodyDamagePacket::handle)
                        .server(UpdateBodyDamagePacket::handle));

        reg.play(DrinkBlockFluidMessage.ID, DrinkBlockFluidMessage::new,
                h -> h.client(DrinkBlockFluidMessage::handle)
                        .server(DrinkBlockFluidMessage::handle));

        reg.play(BodyPartHealingTimeMessage.ID, BodyPartHealingTimeMessage::new,
                h -> h.client(BodyPartHealingTimeMessage::handle)
                        .server(BodyPartHealingTimeMessage::handle));

        reg.play(SyncTemperatureConsumablesPacket.ID, SyncTemperatureConsumablesPacket::new,
                h -> h.client(SyncTemperatureConsumablesPacket::handle)
                        .server(SyncTemperatureConsumablesPacket::handle));
        reg.play(SyncTemperatureConsumableBlocksPacket.ID, SyncTemperatureConsumableBlocksPacket::new,
                h -> h.client(SyncTemperatureConsumableBlocksPacket::handle)
                        .server(SyncTemperatureConsumableBlocksPacket::handle));
        reg.play(SyncTemperatureBlocksPacket.ID, SyncTemperatureBlocksPacket::new,
                h -> h.client(SyncTemperatureBlocksPacket::handle)
                        .server(SyncTemperatureBlocksPacket::handle));
        reg.play(SyncTemperatureItemsPacket.ID, SyncTemperatureItemsPacket::new,
                h -> h.client(SyncTemperatureItemsPacket::handle)
                        .server(SyncTemperatureItemsPacket::handle));
        reg.play(SyncTemperatureBiomesPacket.ID, SyncTemperatureBiomesPacket::new,
                h -> h.client(SyncTemperatureBiomesPacket::handle)
                        .server(SyncTemperatureBiomesPacket::handle));
        reg.play(SyncTemperatureFuelItemsPacket.ID, SyncTemperatureFuelItemsPacket::new,
                h -> h.client(SyncTemperatureFuelItemsPacket::handle)
                        .server(SyncTemperatureFuelItemsPacket::handle));
        reg.play(SyncTemperatureMountsPacket.ID, SyncTemperatureMountsPacket::new,
                h -> h.client(SyncTemperatureMountsPacket::handle)
                        .server(SyncTemperatureMountsPacket::handle));
        reg.play(SyncTemperatureDimensionsPacket.ID, SyncTemperatureDimensionsPacket::new,
                h -> h.client(SyncTemperatureDimensionsPacket::handle)
                        .server(SyncTemperatureDimensionsPacket::handle));
        reg.play(SyncTemperatureOriginsPacket.ID, SyncTemperatureOriginsPacket::new,
                h -> h.client(SyncTemperatureOriginsPacket::handle)
                        .server(SyncTemperatureOriginsPacket::handle));

        reg.play(SyncThirstBlocksPacket.ID, SyncThirstBlocksPacket::new,
                h -> h.client(SyncThirstBlocksPacket::handle)
                        .server(SyncThirstBlocksPacket::handle));
        reg.play(SyncThirstConsumablesPacket.ID, SyncThirstConsumablesPacket::new,
                h -> h.client(SyncThirstConsumablesPacket::handle)
                        .server(SyncThirstConsumablesPacket::handle));

        reg.play(SyncBodyDamageHealingConsumablesPacket.ID, SyncBodyDamageHealingConsumablesPacket::new,
                h -> h.client(SyncBodyDamageHealingConsumablesPacket::handle)
                        .server(SyncBodyDamageHealingConsumablesPacket::handle));
        reg.play(SyncBodyPartsDamageSourcesPacket.ID, SyncBodyPartsDamageSourcesPacket::new,
                h -> h.client(SyncBodyPartsDamageSourcesPacket::handle)
                        .server(SyncBodyPartsDamageSourcesPacket::handle));
        reg.play(SyncBodyPartResistanceItemsPacket.ID, SyncBodyPartResistanceItemsPacket::new,
                h -> h.client(SyncBodyPartResistanceItemsPacket::handle)
                        .server(SyncBodyPartResistanceItemsPacket::handle));
    }
}
