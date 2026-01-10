package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.common.integration.crayfish.CrayfishFurnitureUtil;

@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CanteenInteractionHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack itemStack = player.getItemInHand(hand);
        Level level = event.getLevel();

        // Check if player is holding a canteen
        if (!(itemStack.getItem() instanceof CanteenItem canteenItem)) {
            return;
        }

        // Try to interact with Crayfish furniture
        if (LegendarySurvivalOverhaul.crayfishFurnitureLoaded) {
            // First try to fill canteen from sink/basin
            InteractionResult fillResult = CrayfishFurnitureUtil.tryFillCanteenFromSinkOrBasin(
                level, 
                event.getPos(), 
                player, 
                itemStack
            );

            if (fillResult.consumesAction()) {
                event.setCanceled(true);
                event.setCancellationResult(fillResult);
                player.swing(hand, true);
                return;
            }

            // If filling didn't work, try to empty canteen into sink/basin
            InteractionResult emptyResult = CrayfishFurnitureUtil.tryEmptyCanteenIntoSinkOrBasin(
                level,
                event.getPos(),
                player,
                itemStack
            );

            if (emptyResult.consumesAction()) {
                event.setCanceled(true);
                event.setCancellationResult(emptyResult);
                player.swing(hand, true);
                return;
            }
        }

        // Let the normal canteen useOn() handle other blocks (cauldrons, etc.)
        // by not canceling the event
    }
}
