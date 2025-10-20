package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
// removed deprecated import: ClientPacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartResistanceItemListener;

import java.util.HashMap;
import java.util.Map;

public record SyncBodyPartResistanceItemsPacket(
        Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_body_part_resistance_items");
    public static final Type<SyncBodyPartResistanceItemsPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBodyPartResistanceItemsPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonBodyPartResistance.CODEC)
                    ),
                    SyncBodyPartResistanceItemsPacket::bodyPartResistanceItems,
                    SyncBodyPartResistanceItemsPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    // Handler (client-only)
    public static void handle(SyncBodyPartResistanceItemsPacket pkt, IPayloadContext ctx) {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> BodyPartResistanceItemListener.acceptServerBodyPartResistanceItems(pkt.bodyPartResistanceItems()));
    }

    /* -------- Convenience send helpers -------- */

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, JsonBodyPartResistance> data) {
        PacketDistributor.sendToServer(new SyncBodyPartResistanceItemsPacket(data));
    }

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonBodyPartResistance> data) {
        PacketDistributor.sendToPlayer(player, new SyncBodyPartResistanceItemsPacket(data));
    }

    // Server -> everyone
    public static void sendToAll(Map<ResourceLocation, JsonBodyPartResistance> data) {
        PacketDistributor.sendToAllPlayers(new SyncBodyPartResistanceItemsPacket(data));
    }
}
