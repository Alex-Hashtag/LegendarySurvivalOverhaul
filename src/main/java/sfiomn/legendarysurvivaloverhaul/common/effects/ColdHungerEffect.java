package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class ColdHungerEffect extends IncurableMobEffect {

    public ColdHungerEffect()
    {
        super(MobEffectCategory.HARMFUL, 10870382);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier)
    {
        if(entity instanceof Player)
        {
            ((Player)entity).causeFoodExhaustion((float) (Config.Baked.coldHungerEffectModifier * (amplifier + 1)));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int time = 50 >> amplifier;
        return time == 0 || duration % time == 0;
    }
}
