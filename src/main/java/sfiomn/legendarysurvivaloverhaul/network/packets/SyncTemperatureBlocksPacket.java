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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBlockListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SyncTemperatureBlocksPacket(
        Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_blocks");

    public SyncTemperatureBlocksPacket(FriendlyByteBuf buffer) {
        this(readMap(buffer));
    }

    private static Map<ResourceLocation, List<JsonTemperatureBlock>> readMap(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Map<ResourceLocation, List<JsonTemperatureBlock>> out = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buffer.readResourceLocation();
            int jtbSize = buffer.readInt();
            List<JsonTemperatureBlock> list = new ArrayList<>();
            for (int j = 0; j < jtbSize; j++) {
                CompoundTag tag = buffer.readNbt();
                if (tag != null) {
                    JsonTemperatureBlock.CODEC.parse(NbtOps.INSTANCE, tag).result().ifPresent(list::add);
                }
            }
            out.put(key, list);
        }
        return Map.copyOf(out);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(temperatureBlocks.size());
        for (Map.Entry<ResourceLocation, List<JsonTemperatureBlock>> e : temperatureBlocks.entrySet()) {
            buffer.writeResourceLocation(e.getKey());
            buffer.writeInt(e.getValue().size());
            JsonTemperatureBlock.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue())
                    .result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
        }
    }

    @Override
    public ResourceLocation id() { return ID; }

    public static void handle(SyncTemperatureBlocksPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        TemperatureBlockListener.acceptServerTemperatureBlocks(pkt.temperatureBlocks())
                )
        );
    }

    public static void sendToServer(Map<ResourceLocation, List<JsonTemperatureBlock>> data) {
        PacketDistributor.SERVER.noArg().send(new SyncTemperatureBlocksPacket(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureBlock>> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncTemperatureBlocksPacket(data));
    }

    public static void sendToAll(Map<ResourceLocation, List<JsonTemperatureBlock>> data) {
        PacketDistributor.ALL.noArg().send(new SyncTemperatureBlocksPacket(data));
    }
}
