package sfiomn.legendarysurvivaloverhaul.common.integration.vampirism;

import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import static de.teamlapen.vampirism.api.VReference.VAMPIRE_FACTION;

public class VampirismEvents {

    @SubscribeEvent
    public static void onFactionChanged(PlayerFactionEvent.FactionLevelChanged event) {
        Player player = event.getPlayer().asEntity();
        if (LegendarySurvivalOverhaul.vampirismLoaded && event.getCurrentFaction() == VAMPIRE_FACTION && !Config.Baked.thirstEnabledIfVampire)
            ThirstUtil.deactivateThirst(player);
        else
            ThirstUtil.activateThirst(player);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (LegendarySurvivalOverhaul.vampirismLoaded && !Config.Baked.thirstEnabledIfVampire && VampirismUtil.isVampire(event.getEntity())) {
            ThirstUtil.deactivateThirst(event.getEntity());
        } else {
            ThirstUtil.activateThirst(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!ThirstUtil.isThirstActive(event.getEntity()))
            ThirstUtil.activateThirst(event.getEntity());
    }
}
