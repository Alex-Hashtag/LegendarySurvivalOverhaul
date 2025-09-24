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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureFuelItem;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureFuelItemListener;

import java.util.HashMap;
import java.util.Map;

public record SyncTemperatureFuelItemsPacket(
        Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_fuel_items");

    public SyncTemperatureFuelItemsPacket(FriendlyByteBuf buffer) {
        this(readMap(buffer));
    }

    private static Map<ResourceLocation, JsonTemperatureFuelItem> readMap(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Map<ResourceLocation, JsonTemperatureFuelItem> out = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buffer.readResourceLocation();
            CompoundTag tag = buffer.readNbt();
            if (tag != null) {
                JsonTemperatureFuelItem.CODEC.parse(NbtOps.INSTANCE, tag)
                        .result().ifPresent(v -> out.put(key, v));
            }
        }
        return Map.copyOf(out);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(temperatureFuelItems.size());
        for (Map.Entry<ResourceLocation, JsonTemperatureFuelItem> e : temperatureFuelItems.entrySet()) {
            buffer.writeResourceLocation(e.getKey());
            JsonTemperatureFuelItem.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue())
                    .result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
        }
    }

    @Override
    public ResourceLocation id() { return ID; }

    public static void handle(SyncTemperatureFuelItemsPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        TemperatureFuelItemListener.acceptServerTemperatureFuelItems(pkt.temperatureFuelItems())
                )
        );
    }

    public static void sendToServer(Map<ResourceLocation, JsonTemperatureFuelItem> data) {
        PacketDistributor.SERVER.noArg().send(new SyncTemperatureFuelItemsPacket(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureFuelItem> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncTemperatureFuelItemsPacket(data));
    }

    public static void sendToAll(Map<ResourceLocation, JsonTemperatureFuelItem> data) {
        PacketDistributor.ALL.noArg().send(new SyncTemperatureFuelItemsPacket(data));
    }
}
