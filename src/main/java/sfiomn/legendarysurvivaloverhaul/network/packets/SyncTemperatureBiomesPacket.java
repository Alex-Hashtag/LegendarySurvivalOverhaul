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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBiomeOverride;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBiomeListener;

import java.util.HashMap;
import java.util.Map;

public record SyncTemperatureBiomesPacket(
        Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_biomes");

    public SyncTemperatureBiomesPacket(FriendlyByteBuf buffer) {
        this(readMap(buffer));
    }

    private static Map<ResourceLocation, JsonTemperatureBiomeOverride> readMap(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Map<ResourceLocation, JsonTemperatureBiomeOverride> out = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation key = buffer.readResourceLocation();
            CompoundTag tag = buffer.readNbt();
            if (tag != null) {
                JsonTemperatureBiomeOverride.CODEC.parse(NbtOps.INSTANCE, tag)
                        .result().ifPresent(v -> out.put(key, v));
            }
        }
        return Map.copyOf(out);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(temperatureBiomes.size());
        for (Map.Entry<ResourceLocation, JsonTemperatureBiomeOverride> e : temperatureBiomes.entrySet()) {
            buffer.writeResourceLocation(e.getKey());
            JsonTemperatureBiomeOverride.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue())
                    .result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
        }
    }

    @Override
    public ResourceLocation id() { return ID; }

    public static void handle(SyncTemperatureBiomesPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        TemperatureBiomeListener.acceptServerTemperatureBiomes(pkt.temperatureBiomes())
                )
        );
    }

    public static void sendToServer(Map<ResourceLocation, JsonTemperatureBiomeOverride> data) {
        PacketDistributor.SERVER.noArg().send(new SyncTemperatureBiomesPacket(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureBiomeOverride> data) {
        PacketDistributor.PLAYER.with(player).send(new SyncTemperatureBiomesPacket(data));
    }

    // Server -> all players
    public static void sendToAll(Map<ResourceLocation, JsonTemperatureBiomeOverride> data) {
        PacketDistributor.ALL.noArg().send(new SyncTemperatureBiomesPacket(data));
    }
}
