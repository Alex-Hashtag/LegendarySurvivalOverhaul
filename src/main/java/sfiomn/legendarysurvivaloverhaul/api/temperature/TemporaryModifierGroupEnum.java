package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

public enum TemporaryModifierGroupEnum
{
    FOOD(MobEffectRegistry.HOT_FOOD, MobEffectRegistry.COLD_FOOD),
    DRINK(MobEffectRegistry.HOT_DRINk, MobEffectRegistry.COLD_DRINK);

    public final DeferredHolder<MobEffect, MobEffect> hotEffect;
    public final DeferredHolder<MobEffect, MobEffect> coldEffect;

    TemporaryModifierGroupEnum(DeferredHolder<MobEffect, MobEffect> hotEffect, DeferredHolder<MobEffect, MobEffect> coldEffect)
    {
        this.hotEffect = hotEffect;
        this.coldEffect = coldEffect;
    }

    public static TemporaryModifierGroupEnum get(String name)
    {
        for (TemporaryModifierGroupEnum t : values())
            if (t.name().equalsIgnoreCase(name)) return t;
        throw new IllegalArgumentException();
    }
}
