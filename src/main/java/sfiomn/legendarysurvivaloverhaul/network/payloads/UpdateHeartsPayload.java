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
import sfiomn.legendarysurvivaloverhaul.common.attachments.health.HealthAttachment;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public record UpdateHeartsPayload(
        CompoundTag compound
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "update_hearts");
    public static final Type<UpdateHeartsPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateHeartsPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.COMPOUND_TAG,
                    UpdateHeartsPayload::compound,
                    UpdateHeartsPayload::new
            );

    public static void handle(UpdateHeartsPayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player != null)
            {
                HealthAttachment healthAttachment = AttachmentUtil.getHealthAttachment(player);
                healthAttachment.readNBT(pkt.compound());
            }
        });
    }

    public static void sendToServer(CompoundTag compound)
    {
        PacketDistributor.sendToServer(new UpdateHeartsPayload(compound));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, CompoundTag compound)
    {
        PacketDistributor.sendToPlayer(player, new UpdateHeartsPayload(compound));
    }

    public static void sendToAll(CompoundTag compound)
    {
        PacketDistributor.sendToAllPlayers(new UpdateHeartsPayload(compound));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
