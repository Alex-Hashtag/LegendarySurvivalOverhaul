package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class DamageSourceUtil
{

    private DamageSourceUtil()
    {
    }

    private static Registry<DamageType> damageTypes(Level level)
    {
        return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> damageType)
    {
        return new DamageSource(damageTypes(level).getHolderOrThrow(damageType));
    }
}
