package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyDamageHealingConsumableListener;

import java.util.HashMap;
import java.util.Map;

public record SyncBodyDamageHealingConsumablesPacket(
        Map<ResourceLocation, JsonHealingConsumable> healingConsumables
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_body_damage_healing_consumables");

    // Reader (old decode)
    public SyncBodyDamageHealingConsumablesPacket(FriendlyByteBuf buf) {
        this(readMap(buf));
    }

    private static Map<ResourceLocation, JsonHealingConsumable> readMap(FriendlyByteBuf buf) {
        int size = buf.readInt();
        Map<ResourceLocation, JsonHealingConsumable> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buf.readResourceLocation();
            CompoundTag tag = buf.readNbt();
            if (tag != null) {
                JsonHealingConsumable.CODEC.parse(NbtOps.INSTANCE, tag)
                        .result().ifPresent(v -> map.put(key, v));
            }
        }
        return Map.copyOf(map);
    }

    // Writer (old encode)
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(healingConsumables.size());
        for (var e : healingConsumables.entrySet()) {
            buf.writeResourceLocation(e.getKey());
            var r = JsonHealingConsumable.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
            r.result().ifPresent(j -> buf.writeNbt((CompoundTag) j));
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    // Handler (replaces old handle(...Supplier<NetworkEvent.Context>))
    public static void handle(SyncBodyDamageHealingConsumablesPacket pkt, PlayPayloadContext ctx) {
        // If this payload is server->client only, you’ll register client handler only.
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        BodyDamageHealingConsumableListener.acceptServerHealingConsumables(pkt.healingConsumables())
                )
        );
    }

    // Convenience senders (replace NetworkHandler.INSTANCE.send(...))
    public static void sendToServer(Map<ResourceLocation, JsonHealingConsumable> data) {
        PacketDistributor.SERVER.noArg().send(new SyncBodyDamageHealingConsumablesPacket(data));
    }

    // Example: server->one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonHealingConsumable> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncBodyDamageHealingConsumablesPacket(data));
    }
}
