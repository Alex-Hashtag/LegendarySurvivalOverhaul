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
import sfiomn.legendarysurvivaloverhaul.common.attachments.wetness.WetnessAttachment;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public record UpdateWetnessPayload(
        CompoundTag compound
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "update_wetness");
    public static final Type<UpdateWetnessPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateWetnessPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.COMPOUND_TAG,
                    UpdateWetnessPayload::compound,
                    UpdateWetnessPayload::new
            );

    // Handler (client side apply)
    public static void handle(UpdateWetnessPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player != null)
            {
                WetnessAttachment wetness = AttachmentUtil.getWetnessAttachment(player);
                wetness.readNBT(pkt.compound());
            }
        });
    }

    // Convenience senders
    public static void sendToServer(CompoundTag compound)
    {
        PacketDistributor.sendToServer(new UpdateWetnessPayload(compound));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, CompoundTag compound)
    {
        PacketDistributor.sendToPlayer(player, new UpdateWetnessPayload(compound));
    }

    public static void sendToAll(CompoundTag compound)
    {
        PacketDistributor.sendToAllPlayers(new UpdateWetnessPayload(compound));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
