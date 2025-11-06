package sfiomn.legendarysurvivaloverhaul.network.payloads;

import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.attachments.temperature.TemperatureAttachment;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public record UpdateTemperaturesPayload(
        CompoundTag compound
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "update_temperatures");
    public static final Type<UpdateTemperaturesPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTemperaturesPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.COMPOUND_TAG,
                    UpdateTemperaturesPayload::compound,
                    UpdateTemperaturesPayload::new
            );

    // Handler (replaces old handle(...Supplier<NetworkEvent.Context>))
    public static void handle(UpdateTemperaturesPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player != null)
            {
                TemperatureAttachment temperature = AttachmentUtil.getTempAttachment(player);
                temperature.readNBT(pkt.compound());
            }
        });
    }

    // Convenience senders
    public static void sendToServer(CompoundTag compound)
    {
        PacketDistributor.sendToServer(new UpdateTemperaturesPayload(compound));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, CompoundTag compound)
    {
        PacketDistributor.sendToPlayer(player, new UpdateTemperaturesPayload(compound));
    }

    public static void sendToAll(CompoundTag compound)
    {
        PacketDistributor.sendToAllPlayers(new UpdateTemperaturesPayload(compound));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
