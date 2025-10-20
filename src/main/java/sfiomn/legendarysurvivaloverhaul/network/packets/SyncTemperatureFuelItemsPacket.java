package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
// removed deprecated import: ClientPacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureFuelItem;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureFuelItemListener;

import java.util.HashMap;
import java.util.Map;

public record SyncTemperatureFuelItemsPacket(
        Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_fuel_items");

    public static final Type<SyncTemperatureFuelItemsPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureFuelItemsPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureFuelItem.CODEC)
                    ),
                    SyncTemperatureFuelItemsPacket::temperatureFuelItems,
                    SyncTemperatureFuelItemsPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SyncTemperatureFuelItemsPacket pkt, IPayloadContext ctx) {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureFuelItemListener.acceptServerTemperatureFuelItems(pkt.temperatureFuelItems()));
    }

    public static void sendToServer(Map<ResourceLocation, JsonTemperatureFuelItem> data) {
        PacketDistributor.sendToServer(new SyncTemperatureFuelItemsPacket(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureFuelItem> data) {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureFuelItemsPacket(data));
    }

    public static void sendToAll(Map<ResourceLocation, JsonTemperatureFuelItem> data) {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureFuelItemsPacket(data));
    }
}
