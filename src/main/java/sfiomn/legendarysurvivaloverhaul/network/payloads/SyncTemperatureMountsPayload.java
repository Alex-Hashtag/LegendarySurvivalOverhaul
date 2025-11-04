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
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureMountListener;

import java.util.HashMap;
import java.util.Map;

public record SyncTemperatureMountsPayload(
        Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_mounts");

    public static final Type<SyncTemperatureMountsPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureMountsPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureResistance.CODEC)
                    ),
                    SyncTemperatureMountsPayload::temperatureMounts,
                    SyncTemperatureMountsPayload::new
            );

    // Handler (client-only)
    public static void handle(SyncTemperatureMountsPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureMountListener.acceptServerTemperatureMounts(pkt.temperatureMounts()));
    }

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, JsonTemperatureResistance> data)
    {
        PacketDistributor.sendToServer(new SyncTemperatureMountsPayload(data));
    }

    /* ---------- Convenience send helpers ---------- */

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonTemperatureResistance> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureMountsPayload(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, JsonTemperatureResistance> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureMountsPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
