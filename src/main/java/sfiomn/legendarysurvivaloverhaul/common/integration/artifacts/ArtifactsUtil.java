package sfiomn.legendarysurvivaloverhaul.common.integration.artifacts;

import artifacts.item.UmbrellaItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import javax.annotation.Nonnull;

public class ArtifactsUtil
{

    public static boolean isHoldingUmbrella(Player player)
    {
        if (LegendarySurvivalOverhaul.artifactsLoaded && player != null)
        {
            //  Check player is holding umbrella but not using it
            if (player.isHolding(itemStack -> itemStack.getItem() instanceof UmbrellaItem))
                return !(player.getUseItem().getItem() instanceof UmbrellaItem);
        }
        return false;
    }

    public static boolean canProvideShade(@Nonnull ResourceLocation itemRegistryName)
    {
        return LegendarySurvivalOverhaul.artifactsLoaded && (itemRegistryName.toString().equals("artifacts:umbrella"));
    }
}
