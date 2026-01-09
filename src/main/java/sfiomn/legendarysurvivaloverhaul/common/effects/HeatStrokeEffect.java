package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.DifficultyUtil;

public class HeatStrokeEffect extends IncurableMobEffect
{
    public HeatStrokeEffect()
    {
        super(MobEffectCategory.HARMFUL, 16756041);
    }

    public static boolean playerIsImmuneToHeat(Player player)
    {
        return player.hasEffect(MobEffectRegistry.HEAT_IMMUNITY) || player.hasEffect(MobEffectRegistry.TEMPERATURE_IMMUNITY);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier)
    {
        if (entity instanceof Player player && !entity.hasEffect(MobEffectRegistry.HEAT_IMMUNITY))
        {
            // For infinite duration effects, check if enough ticks have passed
            int interval = 50 >> amplifier;
            if (interval > 0 && entity.tickCount % interval != 0)
                return true;
            
            if (DifficultyUtil.isModDangerous() && DifficultyUtil.healthAboveDifficulty(player) && !player.isSleeping())
            {
                ModDamageTypes.hyperthermia(player, 1.0f);
            }
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
