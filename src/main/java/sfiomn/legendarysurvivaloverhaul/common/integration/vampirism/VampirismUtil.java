package sfiomn.legendarysurvivaloverhaul.common.integration.vampirism;


import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import static de.teamlapen.vampirism.api.VReference.VAMPIRE_FACTION;

public class VampirismUtil
{
    public static boolean isVampire(Player player)
    {
        if (LegendarySurvivalOverhaul.vampirismLoaded && player != null)
        {
            IFactionPlayerHandler factionHandler = VampirismAPI.getFactionPlayerHandler(player).orElse(null);
            if (factionHandler != null)
            {
                return factionHandler.isInFaction(VAMPIRE_FACTION);
            }
        }

        return false;
    }
}
