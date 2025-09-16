package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class OriginsEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            if (event.player.tickCount % 20 == 0) {
                OriginsUtil.assignOriginsFeatures(event.player);
            }
        }
    }
}
