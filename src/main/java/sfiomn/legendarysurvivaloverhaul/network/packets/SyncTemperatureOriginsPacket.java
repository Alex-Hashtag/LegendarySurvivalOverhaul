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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureOriginListener;

import java.util.HashMap;
import java.util.Map;

public record SyncTemperatureOriginsPacket(
        Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_origins");

    // Buffer constructor (was decode)
    public SyncTemperatureOriginsPacket(FriendlyByteBuf buf) {
        this(readMap(buf));
    }

    private static Map<ResourceLocation, JsonTemperatureResistance> readMap(FriendlyByteBuf buf) {
        int size = buf.readInt();
        Map<ResourceLocation, JsonTemperatureResistance> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buf.readResourceLocation();
            CompoundTag tag = buf.readNbt();
            if (tag != null) {
                JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag)
                        .result().ifPresent(v -> map.put(key, v));
            }
        }
        return Map.copyOf(map);
    }

    // Writer (was encode)
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(temperatureOrigins.size());
        for (var e : temperatureOrigins.entrySet()) {
            buf.writeResourceLocation(e.getKey());
            JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue())
                    .result().ifPresent(j -> buf.writeNbt((CompoundTag) j));
        }
    }

    @Override
    public ResourceLocation id() { return ID; }

    // Handler
    public static void handle(SyncTemperatureOriginsPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        TemperatureOriginListener.acceptServerTemperatureOrigins(pkt.temperatureOrigins())
                )
        );
    }

    /* ---------- Convenience send helpers ---------- */

    // Client -> Server
    public static void sendToServer(Map<ResourceLocation, JsonTemperatureResistance> data) {
        PacketDistributor.SERVER.noArg().send(new SyncTemperatureOriginsPacket(data));
    }

    // Server -> one player
    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player,
                                    Map<ResourceLocation, JsonTemperatureResistance> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncTemperatureOriginsPacket(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, JsonTemperatureResistance> data) {
        PacketDistributor.ALL.noArg().send(new SyncTemperatureOriginsPacket(data));
    }
}
