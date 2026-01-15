package sfiomn.legendarysurvivaloverhaul.network.payloads;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;

public record DrinkBlockFluidMessage() implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "drink_block_fluid");
    public static final Type<DrinkBlockFluidMessage> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, DrinkBlockFluidMessage> STREAM_CODEC =
            StreamCodec.unit(new DrinkBlockFluidMessage());

    public static void handle(DrinkBlockFluidMessage pkt, IPayloadContext ctx)
    {
        ctx.enqueueWork(() -> {
            if (ctx.player() != null)
            {
                DrinkWaterOnServer(ctx.player());
            }
        });
    }

    public static void DrinkWaterOnServer(Player player)
    {
        JsonThirstBlock jsonFluidThirst = ThirstUtil.getFluidThirstLookedAt(player, (float) Math.max(3.0, player.blockInteractionRange() / 2));

        if (jsonFluidThirst == null)
            return;

        ThirstUtil.takeDrink(player, jsonFluidThirst.hydration, jsonFluidThirst.saturation, jsonFluidThirst.effects);
    }

    public static void sendToServer()
    {
        PacketDistributor.sendToServer(new DrinkBlockFluidMessage());
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
