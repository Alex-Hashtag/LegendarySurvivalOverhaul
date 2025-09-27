package sfiomn.legendarysurvivaloverhaul.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.client.screens.BodyHealthScreen;

public class ClientHooks {
    public static void openBodyHealthScreen(Player player, ResourceLocation itemRegistryName, InteractionHand hand, boolean alreadyConsumed, int healingCharges, float healingValue, int healingTime) {
        Minecraft.getInstance().setScreen(new BodyHealthScreen(player, itemRegistryName, hand, alreadyConsumed, healingCharges, healingValue, healingTime));
    }

    public static void openBodyHealthScreen(Player player) {
        openBodyHealthScreen(player, null, null, false, 0, 0, 0);
    }
}
