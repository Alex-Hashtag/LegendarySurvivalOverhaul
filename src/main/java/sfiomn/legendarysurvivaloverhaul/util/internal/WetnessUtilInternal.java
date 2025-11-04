package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.wetness.IWetnessUtil;
import sfiomn.legendarysurvivaloverhaul.common.attachments.wetness.WetnessAttachment;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public class WetnessUtilInternal implements IWetnessUtil
{
    @Override
    public void addWetness(Player player, int wetness)
    {
        if (!isWetnessActive(player))
            return;

        WetnessAttachment cap = AttachmentUtil.getWetnessAttachment(player);
        cap.addWetness(wetness);
    }

    @Override
    public void deactivateWetness(Player player)
    {
        if (!Config.Baked.wetnessEnabled)
            return;

        WetnessAttachment cap = AttachmentUtil.getWetnessAttachment(player);
        cap.setWetnessTickTimer(-1);
        cap.setDirty();
    }

    @Override
    public void activateWetness(Player player)
    {
        if (!Config.Baked.wetnessEnabled)
            return;

        WetnessAttachment cap = AttachmentUtil.getWetnessAttachment(player);
        if (cap.getWetnessTickTimer() == -1)
        {
            cap.setWetnessTickTimer(0);
            cap.setDirty();
        }
    }

    @Override
    public boolean isWetnessActive(Player player)
    {
        if (!Config.Baked.wetnessEnabled)
            return false;

        return AttachmentUtil.getWetnessAttachment(player).getWetnessTickTimer() != -1;
    }
}
