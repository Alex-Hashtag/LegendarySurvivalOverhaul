package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SyncTemperatureConsumablesPacket(
        Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_consumables");

    public SyncTemperatureConsumablesPacket(FriendlyByteBuf buffer) {
        this(readMap(buffer));
    }

    private static Map<ResourceLocation, List<JsonTemperatureConsumable>> readMap(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Map<ResourceLocation, List<JsonTemperatureConsumable>> out = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buffer.readResourceLocation();
            int jtcSize = buffer.readInt();
            List<JsonTemperatureConsumable> jtcList = new ArrayList<>();
            for (int j = 0; j < jtcSize; j++) {
                CompoundTag tag = buffer.readNbt();
                if (tag != null) {
                    JsonTemperatureConsumable.CODEC.parse(NbtOps.INSTANCE, tag).result().ifPresent(jtcList::add);
                }
            }
            out.put(key, jtcList);
        }
        return Map.copyOf(out);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(temperatureConsumables.size());
        for (Map.Entry<ResourceLocation, List<JsonTemperatureConsumable>> e : temperatureConsumables.entrySet()) {
            buffer.writeResourceLocation(e.getKey());
            buffer.writeInt(e.getValue().size());
            JsonTemperatureConsumable.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue())
                    .result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
        }
    }

    @Override
    public ResourceLocation id() { return ID; }

    public static void handle(SyncTemperatureConsumablesPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        TemperatureConsumableListener.acceptServerTemperatureConsumables(pkt.temperatureConsumables())
                )
        );
    }

    public static void sendToServer(Map<ResourceLocation, List<JsonTemperatureConsumable>> data) {
        PacketDistributor.SERVER.noArg().send(new SyncTemperatureConsumablesPacket(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureConsumable>> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncTemperatureConsumablesPacket(data));
    }

    public static void sendToAll(Map<ResourceLocation, List<JsonTemperatureConsumable>> data) {
        PacketDistributor.ALL.noArg().send(new SyncTemperatureConsumablesPacket(data));
    }
}
