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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumableBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableBlockListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SyncTemperatureConsumableBlocksPacket(
        Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumableBlocks
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_consumable_blocks");

    public SyncTemperatureConsumableBlocksPacket(FriendlyByteBuf buffer) {
        this(readMap(buffer));
    }

    private static Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> readMap(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> out = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buffer.readResourceLocation();
            int jtcSize = buffer.readInt();
            List<JsonTemperatureConsumableBlock> list = new ArrayList<>();
            for (int j = 0; j < jtcSize; j++) {
                CompoundTag tag = buffer.readNbt();
                if (tag != null) {
                    JsonTemperatureConsumableBlock.CODEC.parse(NbtOps.INSTANCE, tag).result().ifPresent(list::add);
                }
            }
            out.put(key, list);
        }
        return Map.copyOf(out);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(temperatureConsumableBlocks.size());
        for (Map.Entry<ResourceLocation, List<JsonTemperatureConsumableBlock>> e : temperatureConsumableBlocks.entrySet()) {
            buffer.writeResourceLocation(e.getKey());
            buffer.writeInt(e.getValue().size());
            JsonTemperatureConsumableBlock.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue())
                    .result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
        }
    }

    @Override
    public ResourceLocation id() { return ID; }

    public static void handle(SyncTemperatureConsumableBlocksPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        TemperatureConsumableBlockListener.acceptServerTemperatureConsumableBlocks(pkt.temperatureConsumableBlocks())
                )
        );
    }

    public static void sendToServer(Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> data) {
        PacketDistributor.SERVER.noArg().send(new SyncTemperatureConsumableBlocksPacket(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncTemperatureConsumableBlocksPacket(data));
    }

    public static void sendToAll(Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> data) {
        PacketDistributor.ALL.noArg().send(new SyncTemperatureConsumableBlocksPacket(data));
    }
}
