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
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public record UpdateHeartsPacket(
        CompoundTag compound
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "update_hearts");

    public UpdateHeartsPacket(FriendlyByteBuf buf) {
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

    public static void handle(UpdateHeartsPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player != null) {
                        HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);
                        healthCapability.readNBT(pkt.compound());
                    }
                })
        );
    }

    public static void sendToServer(CompoundTag compound) {
        PacketDistributor.SERVER.noArg().send(new UpdateHeartsPacket(compound));
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, CompoundTag compound) {
        PacketDistributor.PLAYER.with(player).send(new UpdateHeartsPacket(compound));
    }

    public static void sendToAll(CompoundTag compound) {
        PacketDistributor.ALL.noArg().send(new UpdateHeartsPacket(compound));
    }
}
