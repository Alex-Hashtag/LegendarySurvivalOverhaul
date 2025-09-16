package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.ForgeMod;
import net.neoforged.neoforge.network.NetworkDirection;
import net.neoforged.neoforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.function.Supplier;

public class DrinkBlockFluidMessage
{
    // CLIENT to SERVER side message

    public DrinkBlockFluidMessage()
    {
    }

    public static void encode(DrinkBlockFluidMessage message, FriendlyByteBuf buffer)
    {
    }

    public static DrinkBlockFluidMessage decode(FriendlyByteBuf buffer)
    {
        return new DrinkBlockFluidMessage();
    }

    public static void handle(DrinkBlockFluidMessage message, Supplier<NetworkEvent.Context> supplier)
    {
        final NetworkEvent.Context context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayer player = context.getSender();
            if (player != null) {
                context.enqueueWork(() -> DrinkWaterOnServer(player));
            }
        }
        supplier.get().setPacketHandled(true);
    }

    public static void DrinkWaterOnServer(ServerPlayer player) {
        JsonThirstBlock jsonFluidThirst = ThirstUtil.getFluidThirstLookedAt(player, player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) / 2);

        if (jsonFluidThirst == null)
            return;

        ThirstUtil.takeDrink(player, jsonFluidThirst.hydration, jsonFluidThirst.saturation, jsonFluidThirst.effects);
    }

    public static void sendToServer() {
        DrinkBlockFluidMessage messageDrinkToServer = new DrinkBlockFluidMessage();
        NetworkHandler.INSTANCE.sendToServer(messageDrinkToServer);
    }
}
