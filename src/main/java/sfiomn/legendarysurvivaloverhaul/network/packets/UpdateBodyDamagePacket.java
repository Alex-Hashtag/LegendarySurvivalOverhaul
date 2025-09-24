package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public record UpdateBodyDamagePacket(
        CompoundTag compound
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "update_body_damage");

    public UpdateBodyDamagePacket(FriendlyByteBuf buf) {
        this(buf.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(compound);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(UpdateBodyDamagePacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player != null) {
                        BodyDamageCapability bodyDamageCapability = CapabilityUtil.getBodyDamageCapability(player);
                        bodyDamageCapability.readNBT(pkt.compound());
                    }
                })
        );
    }

    public static void sendToServer(CompoundTag compound) {
        PacketDistributor.SERVER.noArg().send(new UpdateBodyDamagePacket(compound));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, CompoundTag compound) {
        PacketDistributor.PLAYER.with(player).send(new UpdateBodyDamagePacket(compound));
    }

    public static void sendToAll(CompoundTag compound) {
        PacketDistributor.ALL.noArg().send(new UpdateBodyDamagePacket(compound));
    }
}
