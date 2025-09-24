package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public record UpdateWetnessPacket(
        CompoundTag compound
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "update_wetness");

    // Reader (old decode)
    public UpdateWetnessPacket(FriendlyByteBuf buf) {
        this(buf.readNbt());
    }

    // Writer (old encode)
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(compound);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    // Handler (client side apply)
    public static void handle(UpdateWetnessPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player != null) {
                        WetnessCapability wetness = CapabilityUtil.getWetnessCapability(player);
                        wetness.readNBT(pkt.compound());
                    }
                })
        );
    }

    // Convenience senders
    public static void sendToServer(CompoundTag compound) {
        PacketDistributor.SERVER.noArg().send(new UpdateWetnessPacket(compound));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, CompoundTag compound) {
        PacketDistributor.PLAYER.with(player).send(new UpdateWetnessPacket(compound));
    }

    public static void sendToAll(CompoundTag compound) {
        PacketDistributor.ALL.noArg().send(new UpdateWetnessPacket(compound));
    }
}
