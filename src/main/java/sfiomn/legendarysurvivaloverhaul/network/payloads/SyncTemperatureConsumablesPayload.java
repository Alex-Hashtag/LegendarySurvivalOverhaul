package sfiomn.legendarysurvivaloverhaul.network.payloads;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;

import java.util.List;
import java.util.Map;

public record SyncTemperatureConsumablesPayload(
        Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_consumables");
    public static final Type<SyncTemperatureConsumablesPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureConsumablesPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            java.util.HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureConsumable.LIST_CODEC)
                    ),
                    SyncTemperatureConsumablesPayload::temperatureConsumables,
                    SyncTemperatureConsumablesPayload::new
            );

    public static void handle(SyncTemperatureConsumablesPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureConsumableListener.acceptServerTemperatureConsumables(pkt.temperatureConsumables()));
    }

    public static void sendToServer(Map<ResourceLocation, List<JsonTemperatureConsumable>> data)
    {
        PacketDistributor.sendToServer(new SyncTemperatureConsumablesPayload(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureConsumable>> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureConsumablesPayload(data));
    }

    public static void sendToAll(Map<ResourceLocation, List<JsonTemperatureConsumable>> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureConsumablesPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
