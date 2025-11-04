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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureItemListener;

import java.util.Map;

public record SyncTemperatureItemsPayload(
        Map<ResourceLocation, JsonTemperatureResistance> temperatureItems
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_items");
    public static final Type<SyncTemperatureItemsPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemperatureItemsPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            java.util.HashMap::new,
                            ResourceLocation.STREAM_CODEC,
                            ByteBufCodecs.fromCodecTrusted(JsonTemperatureResistance.CODEC)
                    ),
                    SyncTemperatureItemsPayload::temperatureItems,
                    SyncTemperatureItemsPayload::new
            );

    public static void handle(SyncTemperatureItemsPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> TemperatureItemListener.acceptServerTemperatureItems(pkt.temperatureItems()));
    }

    public static void sendToServer(Map<ResourceLocation, JsonTemperatureResistance> data)
    {
        PacketDistributor.sendToServer(new SyncTemperatureItemsPayload(data));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureResistance> data)
    {
        PacketDistributor.sendToPlayer(player, new SyncTemperatureItemsPayload(data));
    }

    public static void sendToAll(Map<ResourceLocation, JsonTemperatureResistance> data)
    {
        PacketDistributor.sendToAllPlayers(new SyncTemperatureItemsPayload(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
