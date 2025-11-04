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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBlockListener;

import java.util.List;
import java.util.Map;

public record SyncTemperatureBlocksPayload(
        Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_blocks");
    public static final Type<SyncTemperatureBlocksPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureBlocksPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            java.util.HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureBlock.LIST_CODEC)
                    ),
                    SyncTemperatureBlocksPayload::temperatureBlocks,
                    SyncTemperatureBlocksPayload::new
            );

    public static void handle(SyncTemperatureBlocksPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureBlockListener.acceptServerTemperatureBlocks(pkt.temperatureBlocks()));
    }

    public static void sendToServer(Map<ResourceLocation, List<JsonTemperatureBlock>> data)
    {
        PacketDistributor.sendToServer(new SyncTemperatureBlocksPayload(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureBlock>> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureBlocksPayload(data));
    }

    public static void sendToAll(Map<ResourceLocation, List<JsonTemperatureBlock>> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureBlocksPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
