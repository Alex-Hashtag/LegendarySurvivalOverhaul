package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class OriginsEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity().tickCount % 20 == 0) {
                OriginsUtil.assignOriginsFeatures(event.getEntity());
            }
        }
    }
}
