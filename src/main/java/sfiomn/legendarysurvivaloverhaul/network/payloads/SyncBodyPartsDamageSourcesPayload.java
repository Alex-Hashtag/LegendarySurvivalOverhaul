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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartsDamageSourceListener;

import java.util.HashMap;
import java.util.Map;

public record SyncBodyPartsDamageSourcesPayload(
        Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_body_parts_damage_sources");

    public static final Type<SyncBodyPartsDamageSourcesPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBodyPartsDamageSourcesPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonBodyPartsDamageSource.CODEC)
                    ),
                    SyncBodyPartsDamageSourcesPayload::damageSources,
                    SyncBodyPartsDamageSourcesPayload::new
            );

    // Handler (client-only)
    public static void handle(SyncBodyPartsDamageSourcesPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> BodyPartsDamageSourceListener.acceptServerDamageSources(pkt.damageSources()));
    }

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, JsonBodyPartsDamageSource> data)
    {
        PacketDistributor.sendToServer(new SyncBodyPartsDamageSourcesPayload(data));
    }

    /* -------- Convenience send helpers -------- */

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonBodyPartsDamageSource> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncBodyPartsDamageSourcesPayload(data));
    }

    // Server -> all
    public static void sendToAll(Map<ResourceLocation, JsonBodyPartsDamageSource> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncBodyPartsDamageSourcesPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
