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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBiomeOverride;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBiomeListener;

import java.util.Map;

public record SyncTemperatureBiomesPayload(
        Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_biomes");
    public static final Type<SyncTemperatureBiomesPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureBiomesPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            java.util.HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureBiomeOverride.CODEC)
                    ),
                    SyncTemperatureBiomesPayload::temperatureBiomes,
                    SyncTemperatureBiomesPayload::new
            );

    public static void handle(SyncTemperatureBiomesPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureBiomeListener.acceptServerTemperatureBiomes(pkt.temperatureBiomes()));
    }

    public static void sendToServer(Map<ResourceLocation, JsonTemperatureBiomeOverride> data)
    {
        PacketDistributor.sendToServer(new SyncTemperatureBiomesPayload(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureBiomeOverride> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureBiomesPayload(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, JsonTemperatureBiomeOverride> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureBiomesPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
