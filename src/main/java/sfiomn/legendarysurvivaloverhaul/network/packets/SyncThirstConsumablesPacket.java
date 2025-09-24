package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstConsumableListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SyncThirstConsumablesPacket(
        Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_thirst_consumables");

    // Buffer ctor (old decode)
    public SyncThirstConsumablesPacket(FriendlyByteBuf buf) {
        this(readMap(buf));
    }

    private static Map<ResourceLocation, List<JsonThirstConsumable>> readMap(FriendlyByteBuf buf) {
        int size = buf.readInt();
        Map<ResourceLocation, List<JsonThirstConsumable>> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buf.readResourceLocation();
            int count = buf.readInt();
            List<JsonThirstConsumable> list = new ArrayList<>(count);
            for (int j = 0; j < count; j++) {
                CompoundTag tag = buf.readNbt();
                if (tag != null) {
                    JsonThirstConsumable.CODEC.parse(NbtOps.INSTANCE, tag)
                            .result().ifPresent(list::add);
                }
            }
            map.put(key, List.copyOf(list));
        }
        return Map.copyOf(map);
    }

    // Writer (old encode)
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(thirstConsumables.size());
        for (var e : thirstConsumables.entrySet()) {
            buf.writeResourceLocation(e.getKey());
            List<JsonThirstConsumable> list = e.getValue();
            buf.writeInt(list.size());
            JsonThirstConsumable.LIST_CODEC.encodeStart(NbtOps.INSTANCE, list)
                    .result()
                    .ifPresent(nbt -> ((ListTag) nbt).forEach(el -> buf.writeNbt((CompoundTag) el)));
        }
    }

    @Override
    public ResourceLocation id() { return ID; }

    // Handler (runs on main thread via workHandler)
    public static void handle(SyncThirstConsumablesPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        ThirstConsumableListener.acceptServerThirstConsumables(pkt.thirstConsumables())
                )
        );
    }

    /* ---------- Convenience send helpers ---------- */

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, List<JsonThirstConsumable>> data) {
        PacketDistributor.SERVER.noArg().send(new SyncThirstConsumablesPacket(data));
    }

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, List<JsonThirstConsumable>> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncThirstConsumablesPacket(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, List<JsonThirstConsumable>> data) {
        PacketDistributor.ALL.noArg().send(new SyncThirstConsumablesPacket(data));
    }
}
