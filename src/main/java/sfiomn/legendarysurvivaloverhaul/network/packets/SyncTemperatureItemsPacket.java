package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureItemListener;

import java.util.HashMap;
import java.util.Map;

public record SyncTemperatureItemsPacket(
        Map<ResourceLocation, JsonTemperatureResistance> temperatureItems
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_items");

    public SyncTemperatureItemsPacket(FriendlyByteBuf buffer) {
        this(readMap(buffer));
    }

    private static Map<ResourceLocation, JsonTemperatureResistance> readMap(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Map<ResourceLocation, JsonTemperatureResistance> out = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buffer.readResourceLocation();
            CompoundTag tag = buffer.readNbt();
            if (tag != null) {
                JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag)
                        .result().ifPresent(v -> out.put(key, v));
            }
        }
        return Map.copyOf(out);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(temperatureItems.size());
        for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : temperatureItems.entrySet()) {
            buffer.writeResourceLocation(e.getKey());
            JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue())
                    .result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
        }
    }

    @Override
    public ResourceLocation id() { return ID; }

    public static void handle(SyncTemperatureItemsPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        TemperatureItemListener.acceptServerTemperatureItems(pkt.temperatureItems())
                )
        );
    }

    public static void sendToServer(Map<ResourceLocation, JsonTemperatureResistance> data) {
        PacketDistributor.SERVER.noArg().send(new SyncTemperatureItemsPacket(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureResistance> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncTemperatureItemsPacket(data));
    }

    public static void sendToAll(Map<ResourceLocation, JsonTemperatureResistance> data) {
        PacketDistributor.ALL.noArg().send(new SyncTemperatureItemsPacket(data));
    }
}
