package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
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
        JsonThirstBlock jsonBlockFluidThirst = ThirstUtil.getJsonBlockThirstLookedAt(player, player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) / 2);

        if (jsonBlockFluidThirst == null)
            return;

        ThirstUtil.takeDrink(player, jsonBlockFluidThirst.hydration, jsonBlockFluidThirst.saturation, jsonBlockFluidThirst.effects);
    }

    public static void sendToServer() {
        DrinkBlockFluidMessage messageDrinkToServer = new DrinkBlockFluidMessage();
        NetworkHandler.INSTANCE.sendToServer(messageDrinkToServer);
    }
}
