package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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

public record UpdateBodyDamagePacket(
        CompoundTag compound
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "update_body_damage");
    public static final Type<UpdateBodyDamagePacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateBodyDamagePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.COMPOUND_TAG,
                    UpdateBodyDamagePacket::compound,
                    UpdateBodyDamagePacket::new
            );

    public static void handle(UpdateBodyDamagePacket pkt, IPayloadContext ctx)
    {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null)
            {
                BodyDamageAttachment bodyDamageAttachment = AttachmentUtil.getBodyDamageAttachment(player);
                bodyDamageAttachment.readNBT(pkt.compound());
            }
        });
    }

    public static void sendToServer(CompoundTag compound)
    {
        PacketDistributor.sendToServer(new UpdateBodyDamagePacket(compound));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, CompoundTag compound)
    {
        PacketDistributor.sendToPlayer(player, new UpdateBodyDamagePacket(compound));
    }

    public static void sendToAll(CompoundTag compound)
    {
        PacketDistributor.sendToAllPlayers(new UpdateBodyDamagePacket(compound));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
