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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyDamageHealingConsumableListener;

import java.util.HashMap;
import java.util.Map;

public record SyncBodyDamageHealingConsumablesPacket(
        Map<ResourceLocation, JsonHealingConsumable> healingConsumables
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_body_damage_healing_consumables");
    public static final Type<SyncBodyDamageHealingConsumablesPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBodyDamageHealingConsumablesPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonHealingConsumable.CODEC)
                    ),
                    SyncBodyDamageHealingConsumablesPacket::healingConsumables,
                    SyncBodyDamageHealingConsumablesPacket::new
            );

    // Handler (client-side; guard to avoid running on server)
    public static void handle(SyncBodyDamageHealingConsumablesPacket pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> BodyDamageHealingConsumableListener.acceptServerHealingConsumables(pkt.healingConsumables()));
    }

    // Convenience senders (replace NetworkHandler.INSTANCE.send(...))
    public static void sendToServer(Map<ResourceLocation, JsonHealingConsumable> data)
    {
        PacketDistributor.sendToServer(new SyncBodyDamageHealingConsumablesPacket(data));
    }

    // Example: server->one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonHealingConsumable> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncBodyDamageHealingConsumablesPacket(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
