package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.attachments.wetness.WetnessAttachment;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;


public class WetModifier extends ModifierBase
{
    public WetModifier()
    {
        super();
    }

    @Override
    public float getPlayerInfluence(Player player)
    {
        if (Config.Baked.wetnessEnabled)
        {
            WetnessAttachment wetCap = AttachmentUtil.getWetnessAttachment(player);
            if (wetCap.getWetness() == 0)
            {
                // LegendarySurvivalOverhaul.LOGGER.debug("Wet player temp influence : " + 0.0f);
                return 0.0f;
            } else
            {
                // LegendarySurvivalOverhaul.LOGGER.debug("Wet player temp influence : " + (float) (Config.Baked.wetMultiplier * MathUtil.invLerp(0, WetnessAttachment.WETNESS_LIMIT, wetCap.getWetness())));
                return (float) (Config.Baked.wetMultiplier * MathUtil.invLerp(0, WetnessAttachment.WETNESS_LIMIT, wetCap.getWetness()));
            }
        }
        return 0.0f;
    }
}
