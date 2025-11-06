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
import sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage.BodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public record UpdateBodyDamagePayload(
        CompoundTag compound
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "update_body_damage");
    public static final Type<UpdateBodyDamagePayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateBodyDamagePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.COMPOUND_TAG,
                    UpdateBodyDamagePayload::compound,
                    UpdateBodyDamagePayload::new
            );

    public static void handle(UpdateBodyDamagePayload pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player != null)
            {
                BodyDamageAttachment bodyDamageAttachment = AttachmentUtil.getBodyDamageAttachment(player);
                bodyDamageAttachment.readNBT(pkt.compound());
            }
        });
    }

    public static void sendToServer(CompoundTag compound)
    {
        PacketDistributor.sendToServer(new UpdateBodyDamagePayload(compound));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, CompoundTag compound)
    {
        PacketDistributor.sendToPlayer(player, new UpdateBodyDamagePayload(compound));
    }

    public static void sendToAll(CompoundTag compound)
    {
        PacketDistributor.sendToAllPlayers(new UpdateBodyDamagePayload(compound));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
