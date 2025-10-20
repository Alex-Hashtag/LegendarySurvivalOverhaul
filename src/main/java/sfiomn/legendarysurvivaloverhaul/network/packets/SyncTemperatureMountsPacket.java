package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureMountListener;

import java.util.HashMap;
import java.util.Map;

public record SyncTemperatureMountsPacket(
        Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_mounts");

    public static final Type<SyncTemperatureMountsPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureMountsPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureResistance.CODEC)
                    ),
                    SyncTemperatureMountsPacket::temperatureMounts,
                    SyncTemperatureMountsPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    // Handler (client-only)
    public static void handle(SyncTemperatureMountsPacket pkt, IPayloadContext ctx) {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureMountListener.acceptServerTemperatureMounts(pkt.temperatureMounts()));
    }

    /* ---------- Convenience send helpers ---------- */

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, JsonTemperatureResistance> data) {
        PacketDistributor.sendToServer(new SyncTemperatureMountsPacket(data));
    }

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonTemperatureResistance> data) {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureMountsPacket(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, JsonTemperatureResistance> data) {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureMountsPacket(data));
    }
}
