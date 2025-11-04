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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureOriginListener;

import java.util.HashMap;
import java.util.Map;

public record SyncTemperatureOriginsPayload(
        Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_origins");

    public static final Type<SyncTemperatureOriginsPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureOriginsPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureResistance.CODEC)
                    ),
                    SyncTemperatureOriginsPayload::temperatureOrigins,
                    SyncTemperatureOriginsPayload::new
            );

    // Handler (client-only)
    public static void handle(SyncTemperatureOriginsPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureOriginListener.acceptServerTemperatureOrigins(pkt.temperatureOrigins()));
    }

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, JsonTemperatureResistance> data)
    {
        PacketDistributor.sendToServer(new SyncTemperatureOriginsPayload(data));
    }

    /* ---------- Convenience send helpers ---------- */

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonTemperatureResistance> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureOriginsPayload(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, JsonTemperatureResistance> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureOriginsPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
