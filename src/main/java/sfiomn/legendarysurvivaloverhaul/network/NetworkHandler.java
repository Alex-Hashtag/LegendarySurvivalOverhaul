package sfiomn.legendarysurvivaloverhaul.network;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.network.packets.*;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    private static <T> void registerMessage(Class<T> type, SimpleChannel.MessageBuilder<T> builder) {
        builder.add();
    }

    public static void register() {
        INSTANCE.messageBuilder(UpdateTemperaturesPacket.class, nextId++)
                .encoder(UpdateTemperaturesPacket::encode)
                .decoder(UpdateTemperaturesPacket::decode)
                .consumerMainThread(UpdateTemperaturesPacket::handle)
                .add();

        INSTANCE.messageBuilder(UpdateWetnessPacket.class, nextId++)
                .encoder(UpdateWetnessPacket::encode)
                .decoder(UpdateWetnessPacket::decode)
                .consumerMainThread(UpdateWetnessPacket::handle)
                .add();

        INSTANCE.messageBuilder(UpdateThirstPacket.class, nextId++)
                .encoder(UpdateThirstPacket::encode)
                .decoder(UpdateThirstPacket::decode)
                .consumerMainThread(UpdateThirstPacket::handle)
                .add();

        INSTANCE.messageBuilder(UpdateHeartsPacket.class, nextId++)
                .encoder(UpdateHeartsPacket::encode)
                .decoder(UpdateHeartsPacket::decode)
                .consumerMainThread(UpdateHeartsPacket::handle)
                .add();

        INSTANCE.messageBuilder(UpdateBodyDamagePacket.class, nextId++)
                .encoder(UpdateBodyDamagePacket::encode)
                .decoder(UpdateBodyDamagePacket::decode)
                .consumerMainThread(UpdateBodyDamagePacket::handle)
                .add();

        INSTANCE.messageBuilder(DrinkBlockFluidMessage.class, nextId++)
                .encoder(DrinkBlockFluidMessage::encode)
                .decoder(DrinkBlockFluidMessage::decode)
                .consumerMainThread(DrinkBlockFluidMessage::handle)
                .add();

        INSTANCE.messageBuilder(BodyPartHealingTimeMessage.class, nextId++)
                .encoder(BodyPartHealingTimeMessage::encode)
                .decoder(BodyPartHealingTimeMessage::decode)
                .consumerMainThread(BodyPartHealingTimeMessage::handle)
                .add();

        INSTANCE.messageBuilder(SyncTemperatureConsumablesPacket.class, nextId++)
                .encoder(SyncTemperatureConsumablesPacket::encode)
                .decoder(SyncTemperatureConsumablesPacket::decode)
                .consumerMainThread(SyncTemperatureConsumablesPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncTemperatureConsumableBlocksPacket.class, nextId++)
                .encoder(SyncTemperatureConsumableBlocksPacket::encode)
                .decoder(SyncTemperatureConsumableBlocksPacket::decode)
                .consumerMainThread(SyncTemperatureConsumableBlocksPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncTemperatureBlocksPacket.class, nextId++)
                .encoder(SyncTemperatureBlocksPacket::encode)
                .decoder(SyncTemperatureBlocksPacket::decode)
                .consumerMainThread(SyncTemperatureBlocksPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncTemperatureItemsPacket.class, nextId++)
                .encoder(SyncTemperatureItemsPacket::encode)
                .decoder(SyncTemperatureItemsPacket::decode)
                .consumerMainThread(SyncTemperatureItemsPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncTemperatureBiomesPacket.class, nextId++)
                .encoder(SyncTemperatureBiomesPacket::encode)
                .decoder(SyncTemperatureBiomesPacket::decode)
                .consumerMainThread(SyncTemperatureBiomesPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncTemperatureFuelItemsPacket.class, nextId++)
                .encoder(SyncTemperatureFuelItemsPacket::encode)
                .decoder(SyncTemperatureFuelItemsPacket::decode)
                .consumerMainThread(SyncTemperatureFuelItemsPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncTemperatureMountsPacket.class, nextId++)
                .encoder(SyncTemperatureMountsPacket::encode)
                .decoder(SyncTemperatureMountsPacket::decode)
                .consumerMainThread(SyncTemperatureMountsPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncTemperatureDimensionsPacket.class, nextId++)
                .encoder(SyncTemperatureDimensionsPacket::encode)
                .decoder(SyncTemperatureDimensionsPacket::decode)
                .consumerMainThread(SyncTemperatureDimensionsPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncTemperatureOriginsPacket.class, nextId++)
                .encoder(SyncTemperatureOriginsPacket::encode)
                .decoder(SyncTemperatureOriginsPacket::decode)
                .consumerMainThread(SyncTemperatureOriginsPacket::handle)
                .add();

        INSTANCE.messageBuilder(SyncThirstBlocksPacket.class, nextId++)
                .encoder(SyncThirstBlocksPacket::encode)
                .decoder(SyncThirstBlocksPacket::decode)
                .consumerMainThread(SyncThirstBlocksPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncThirstConsumablesPacket.class, nextId++)
                .encoder(SyncThirstConsumablesPacket::encode)
                .decoder(SyncThirstConsumablesPacket::decode)
                .consumerMainThread(SyncThirstConsumablesPacket::handle)
                .add();

        INSTANCE.messageBuilder(SyncBodyDamageHealingConsumablesPacket.class, nextId++)
                .encoder(SyncBodyDamageHealingConsumablesPacket::encode)
                .decoder(SyncBodyDamageHealingConsumablesPacket::decode)
                .consumerMainThread(SyncBodyDamageHealingConsumablesPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncBodyPartsDamageSourcesPacket.class, nextId++)
                .encoder(SyncBodyPartsDamageSourcesPacket::encode)
                .decoder(SyncBodyPartsDamageSourcesPacket::decode)
                .consumerMainThread(SyncBodyPartsDamageSourcesPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncBodyPartResistanceItemsPacket.class, nextId++)
                .encoder(SyncBodyPartResistanceItemsPacket::encode)
                .decoder(SyncBodyPartResistanceItemsPacket::decode)
                .consumerMainThread(SyncBodyPartResistanceItemsPacket::handle)
                .add();
    }
}
