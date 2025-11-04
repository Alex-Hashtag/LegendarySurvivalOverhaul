package sfiomn.legendarysurvivaloverhaul.network.payloads;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumableBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableBlockListener;

import java.util.List;
import java.util.Map;

public record SyncTemperatureConsumableBlocksPayload(
        Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumableBlocks
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_consumable_blocks");
    public static final Type<SyncTemperatureConsumableBlocksPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureConsumableBlocksPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            java.util.HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureConsumableBlock.LIST_CODEC)
                    ),
                    SyncTemperatureConsumableBlocksPayload::temperatureConsumableBlocks,
                    SyncTemperatureConsumableBlocksPayload::new
            );

    public static void handle(SyncTemperatureConsumableBlocksPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureConsumableBlockListener.acceptServerTemperatureConsumableBlocks(pkt.temperatureConsumableBlocks()));
    }

    public static void sendToServer(Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> data)
    {
        PacketDistributor.sendToServer(new SyncTemperatureConsumableBlocksPayload(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureConsumableBlocksPayload(data));
    }

    public static void sendToAll(Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureConsumableBlocksPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
