package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.DifficultyUtil;

public class FrostbiteEffect extends IncurableMobEffect
{
	public FrostbiteEffect()
	{
		super(MobEffectCategory.HARMFUL, 9164281);
	}
	
	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier)
	{
		if(entity instanceof Player player && !entity.hasEffect(MobEffectRegistry.COLD_IMMUNITY.get()))
		{
            if (DifficultyUtil.isModDangerous() && DifficultyUtil.healthAboveDifficulty(player) && !player.isSleeping())
			{
				ModDamageTypes.hypothermia(player, 1.0f);
			}
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		int time = 50 >> amplifier;
		return time == 0 || duration % time == 0;
	}

	public static boolean playerIsImmuneToFrost(Player player)
	{
		return player.hasEffect(MobEffectRegistry.COLD_IMMUNITY.get()) || player.hasEffect(MobEffectRegistry.TEMPERATURE_IMMUNITY.get());
	}
}
