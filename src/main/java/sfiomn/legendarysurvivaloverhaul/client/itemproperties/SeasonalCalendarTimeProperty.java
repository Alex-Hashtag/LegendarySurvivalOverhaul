package sfiomn.legendarysurvivaloverhaul.client.itemproperties;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons.EclipticSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;


public class SeasonalCalendarTimeProperty implements ClampedItemPropertyFunction
{
    float olderD0 = 0;

    @OnlyIn(Dist.CLIENT)
    @Override
    public float unclampedCall(@NotNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity entity, int i)
    {
        Level level = clientLevel;
        Entity holder = (entity != null ? entity : itemStack.getFrame());

        if (level == null && holder != null)
        {
            level = holder.level();
        }

        if (level == null)
        {
            return 0;
        } else
        {
            try
            {
                double d0 = 0;

                if (LegendarySurvivalOverhaul.sereneSeasonsLoaded)
                {
                    d0 = SereneSeasonsUtil.getTimeInSeasonCycle(level);
                } else if (LegendarySurvivalOverhaul.eclipticSeasonsLoaded)
                    d0 = EclipticSeasonsUtil.getDayInSeasonCycle(level);

                return Mth.positiveModulo((float) d0, 1.0F);
            } catch (NullPointerException e)
            {
                return 0;
            }

        }
    }
}
