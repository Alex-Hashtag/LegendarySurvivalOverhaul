package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

public class IncurableMobEffect extends MobEffect
{
    protected IncurableMobEffect(MobEffectCategory category, int color)
    {
        super(category, color);
    }

    // NeoForge 1.21: override the extension hook; use NeoForge EffectCure entries (1.21); leave empty to make effect incurable
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance)
    {
        // No cures
    }
}
