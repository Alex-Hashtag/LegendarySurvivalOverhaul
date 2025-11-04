package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class RecoveryEffect extends MobEffect
{
    public RecoveryEffect()
    {
        super(MobEffectCategory.BENEFICIAL, 1166574);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier)
    {
        entity.heal((float) Config.Baked.recoveryEffectHealingValue);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier)
    {
        // amplifier 0 => every 50 ticks, amplifier 1 => every 25 ticks, amplifier 2 => every 12 ticks
        int i = 50 >> amplifier;
        if (i > 0)
        {
            return duration % i == 0;
        } else
        {
            return true;
        }
    }
}
