package sfiomn.legendarysurvivaloverhaul.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.client.screens.BodyHealthScreen;

public class ClientHooks
{
    /**
     * Open the body health screen to choose the limbs healed by the healing item just used.
     */
    public static void openBodyHealthScreen(Player player, InteractionHand hand, JsonHealingConsumable healingConsumable)
    {
        if (Minecraft.getInstance().screen == null)
            Minecraft.getInstance().setScreen(new BodyHealthScreen(player, hand, healingConsumable));
    }

    /**
     * Open the body health screen only to look at the limbs.
     */
    public static void openBodyHealthScreen(Player player)
    {
        Minecraft.getInstance().setScreen(new BodyHealthScreen(player, null, null));
    }
}
