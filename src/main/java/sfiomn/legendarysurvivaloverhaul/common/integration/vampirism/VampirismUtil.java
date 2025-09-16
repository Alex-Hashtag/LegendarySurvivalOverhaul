package sfiomn.legendarysurvivaloverhaul.common.integration.vampirism;


import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import static de.teamlapen.vampirism.api.VReference.VAMPIRE_FACTION;

public class VampirismUtil {
    public static boolean isVampire(Player player) {
        if (LegendarySurvivalOverhaul.vampirismLoaded && player != null) {
            LazyOptional<IFactionPlayerHandler> factionHandlerOptional = VampirismAPI.getFactionPlayerHandler(player);
            if (factionHandlerOptional.isPresent() && factionHandlerOptional.resolve().isPresent()) {
                return factionHandlerOptional.resolve().get().isInFaction(VAMPIRE_FACTION);
            }
        }

        return false;
    }
}
