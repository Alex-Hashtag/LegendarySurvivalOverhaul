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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstBlockListener;

import java.util.List;
import java.util.Map;

public record SyncThirstBlocksPayload(
        Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_thirst_blocks");

    public static final Type<SyncThirstBlocksPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncThirstBlocksPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            java.util.HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonThirstBlock.LIST_CODEC)
                    ),
                    SyncThirstBlocksPayload::thirstBlocks,
                    SyncThirstBlocksPayload::new
            );

    // Handler (client-only)
    public static void handle(SyncThirstBlocksPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> ThirstBlockListener.acceptServerThirstBlocks(pkt.thirstBlocks()));
    }

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, List<JsonThirstBlock>> data)
    {
        PacketDistributor.sendToServer(new SyncThirstBlocksPayload(data));
    }

    /* ---------- Convenience send helpers ---------- */

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, List<JsonThirstBlock>> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncThirstBlocksPayload(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, List<JsonThirstBlock>> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncThirstBlocksPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
