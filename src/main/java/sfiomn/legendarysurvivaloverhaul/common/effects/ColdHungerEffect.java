package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class ColdHungerEffect extends IncurableMobEffect
{

    public ColdHungerEffect()
    {
        super(MobEffectCategory.HARMFUL, 10870382);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier)
    {
        if (entity instanceof Player)
        {
            // For infinite duration effects, check if enough ticks have passed
            int interval = 50 >> amplifier;
            if (interval > 0 && entity.tickCount % interval != 0)
                return true;
            
            ((Player) entity).causeFoodExhaustion((float) (Config.Baked.coldHungerEffectModifier * (amplifier + 1)));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier)
    {
        // For infinite duration (-1), always return true and let applyEffectTick handle timing
        if (duration < 0)
            return true;
        
        int time = 50 >> amplifier;
        if (time <= 0)
            return true;
        
        return duration % time == 0;
    }
}
