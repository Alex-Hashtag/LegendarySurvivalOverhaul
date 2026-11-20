package sfiomn.legendarysurvivaloverhaul.network.payloads;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;

/**
 * Sent by the body health screen when the player chooses a limb to heal with the healing item they just used.
 */
public record HealBodyPartPayload(
        int bodyPartIndex
) implements CustomPacketPayload
{
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "heal_body_part");
    public static final Type<HealBodyPartPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, HealBodyPartPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    HealBodyPartPayload::bodyPartIndex,
                    HealBodyPartPayload::new
            );

    public static void handle(HealBodyPartPayload pkt, IPayloadContext ctx)
    {
        ctx.enqueueWork(() -> {
            BodyPartEnum[] bodyParts = BodyPartEnum.values();
            if (ctx.player() instanceof ServerPlayer serverPlayer && pkt.bodyPartIndex() >= 0 && pkt.bodyPartIndex() < bodyParts.length)
                BodyDamageUtil.healBodyPartWithHealingSession(serverPlayer, bodyParts[pkt.bodyPartIndex()]);
        });
    }

    public static void sendToServer(BodyPartEnum bodyPart)
    {
        PacketDistributor.sendToServer(new HealBodyPartPayload(bodyPart.ordinal()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
