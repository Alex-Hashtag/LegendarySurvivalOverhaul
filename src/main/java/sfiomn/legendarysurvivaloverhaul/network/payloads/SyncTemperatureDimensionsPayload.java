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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureDimension;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureDimensionListener;

import java.util.Map;

public record SyncTemperatureDimensionsPayload(
        Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_dimensions");
    public static final Type<SyncTemperatureDimensionsPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureDimensionsPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            java.util.HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureDimension.CODEC)
                    ),
                    SyncTemperatureDimensionsPayload::temperatureDimensions,
                    SyncTemperatureDimensionsPayload::new
            );

    // Handler (client-only)
    public static void handle(SyncTemperatureDimensionsPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureDimensionListener.acceptServerTemperatureDimensions(pkt.temperatureDimensions()));
    }

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, JsonTemperatureDimension> data)
    {
        PacketDistributor.sendToServer(new SyncTemperatureDimensionsPayload(data));
    }

    /* ---------- Convenience send helpers ---------- */

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonTemperatureDimension> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureDimensionsPayload(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, JsonTemperatureDimension> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureDimensionsPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
