package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstConsumableListener;

import java.util.List;
import java.util.Map;

public record SyncThirstConsumablesPacket(
        Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_thirst_consumables");

    public static final Type<SyncThirstConsumablesPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncThirstConsumablesPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            java.util.HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonThirstConsumable.LIST_CODEC)
                    ),
                    SyncThirstConsumablesPacket::thirstConsumables,
                    SyncThirstConsumablesPacket::new
            );

    // Handler (client-only)
    public static void handle(SyncThirstConsumablesPacket pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> ThirstConsumableListener.acceptServerThirstConsumables(pkt.thirstConsumables()));
    }

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, List<JsonThirstConsumable>> data)
    {
        PacketDistributor.sendToServer(new SyncThirstConsumablesPacket(data));
    }

    /* ---------- Convenience send helpers ---------- */

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, List<JsonThirstConsumable>> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncThirstConsumablesPacket(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, List<JsonThirstConsumable>> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncThirstConsumablesPacket(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
