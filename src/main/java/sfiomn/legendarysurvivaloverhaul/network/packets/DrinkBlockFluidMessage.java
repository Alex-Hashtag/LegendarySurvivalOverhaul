package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import net.neoforged.neoforge.common.NeoForgeMod;

public record DrinkBlockFluidMessage() implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "drink_block_fluid");

    public DrinkBlockFluidMessage(FriendlyByteBuf buf) {
        this();
    }

    @Override
    public void write(FriendlyByteBuf buf) { }

    @Override
    public ResourceLocation id() { return ID; }

    public static void handle(DrinkBlockFluidMessage pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> ctx.player().ifPresent(DrinkBlockFluidMessage::DrinkWaterOnServer));
    }

    public static void DrinkWaterOnServer(Player player) {
        JsonThirstBlock jsonFluidThirst = ThirstUtil.getFluidThirstLookedAt(player, player.getAttributeValue(NeoNeoForgeMod.BLOCK_REACH.get) / 2);

        if (jsonFluidThirst == null)
            return;

        ThirstUtil.takeDrink(player, jsonFluidThirst.hydration, jsonFluidThirst.saturation, jsonFluidThirst.effects);
    }

    public static void sendToServer() {
        PacketDistributor.SERVER.noArg().send(new DrinkBlockFluidMessage());
    }
}
