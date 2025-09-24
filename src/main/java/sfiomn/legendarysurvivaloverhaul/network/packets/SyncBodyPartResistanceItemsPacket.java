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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartResistanceItemListener;

import java.util.HashMap;
import java.util.Map;

public record SyncBodyPartResistanceItemsPacket(
        Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_body_part_resistance_items");

    // Buffer ctor (was decode)
    public SyncBodyPartResistanceItemsPacket(FriendlyByteBuf buf) {
        this(readMap(buf));
    }

    private static Map<ResourceLocation, JsonBodyPartResistance> readMap(FriendlyByteBuf buf) {
        int size = buf.readInt();
        Map<ResourceLocation, JsonBodyPartResistance> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buf.readResourceLocation();
            CompoundTag tag = buf.readNbt();
            if (tag != null) {
                JsonBodyPartResistance.CODEC.parse(NbtOps.INSTANCE, tag)
                        .result().ifPresent(v -> map.put(key, v));
            }
        }
        return Map.copyOf(map);
    }

    // Writer (was encode)
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(bodyPartResistanceItems.size());
        for (var e : bodyPartResistanceItems.entrySet()) {
            buf.writeResourceLocation(e.getKey());
            JsonBodyPartResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue())
                    .result().ifPresent(j -> buf.writeNbt((CompoundTag) j));
        }
    }

    @Override
    public ResourceLocation id() { return ID; }

    // Handler (replaces Supplier<NetworkEvent.Context>)
    public static void handle(SyncBodyPartResistanceItemsPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        BodyPartResistanceItemListener.acceptServerBodyPartResistanceItems(pkt.bodyPartResistanceItems())
                )
        );
    }

    /* -------- Convenience send helpers -------- */

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, JsonBodyPartResistance> data) {
        PacketDistributor.SERVER.noArg().send(new SyncBodyPartResistanceItemsPacket(data));
    }

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonBodyPartResistance> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncBodyPartResistanceItemsPacket(data));
    }

    // Server -> everyone
    public static void sendToAll(Map<ResourceLocation, JsonBodyPartResistance> data) {
        PacketDistributor.ALL.noArg().send(new SyncBodyPartResistanceItemsPacket(data));
    }
}
