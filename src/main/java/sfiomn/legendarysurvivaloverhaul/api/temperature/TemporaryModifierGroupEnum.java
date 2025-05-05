package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

public enum TemporaryModifierGroupEnum
{
	FOOD(MobEffectRegistry.HOT_FOOD, MobEffectRegistry.COLD_FOOD),
	DRINK(MobEffectRegistry.HOT_DRINk, MobEffectRegistry.COLD_DRINK);

	public final RegistryObject<MobEffect> hotEffect;
	public final RegistryObject<MobEffect> coldEffect;
	
	TemporaryModifierGroupEnum(RegistryObject<MobEffect> hotEffect, RegistryObject<MobEffect> coldEffect)
	{
		this.hotEffect = hotEffect;
		this.coldEffect = coldEffect;
	}

	public static TemporaryModifierGroupEnum get(String name) {
		for(TemporaryModifierGroupEnum t : values())
			if(t.name().equalsIgnoreCase(name)) return t;
		throw new IllegalArgumentException();
	}
}
