package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class ThirstEffect extends MobEffect
{
	public ThirstEffect()
	{
		super(MobEffectCategory.HARMFUL, 10870382);
	}
	
	@Override
	public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier)
	{
		if(entity instanceof Player)
		{
			ThirstCapability thirstCapability = CapabilityUtil.getThirstCapability((Player) entity);

			// By default, twice strength of Hunger effect (0.005F)
			thirstCapability.addThirstExhaustion((float) (Config.Baked.thirstEffectModifier * amplifier + 1));
		}
		return true;
	}

	@Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int time = 50 >> amplifier;
        return time == 0 || duration % time == 0;
    }


}
