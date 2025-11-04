package sfiomn.legendarysurvivaloverhaul.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.util.DamageSourceUtil;

import static net.minecraft.world.damagesource.DamageEffects.*;

public class ModDamageTypes
{
    public static final ResourceKey<DamageType> HYPOTHERMIA = registerKey("hypothermia");
    public static final ResourceKey<DamageType> HYPERTHERMIA = registerKey("hyperthermia");
    public static final ResourceKey<DamageType> DEHYDRATION = registerKey("dehydration");


    public static ResourceKey<DamageType> registerKey(String name)
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, name));
    }

    public static void hyperthermia(Entity entity, float amount)
    {
        entity.hurt(DamageSourceUtil.getDamageSource(entity.level(), HYPERTHERMIA), amount);
    }

    public static void hypothermia(Entity entity, float amount)
    {
        entity.hurt(DamageSourceUtil.getDamageSource(entity.level(), HYPOTHERMIA), amount);
    }

    public static void dehydration(Entity entity, float amount)
    {
        entity.hurt(DamageSourceUtil.getDamageSource(entity.level(), DEHYDRATION), amount);
    }

    public static void bootstrap(BootstrapContext<DamageType> context)
    {
        context.register(HYPOTHERMIA, new DamageType(LegendarySurvivalOverhaul.MOD_ID + ".hypothermia", DamageScaling.NEVER, 0.1f, FREEZING));
        context.register(HYPERTHERMIA, new DamageType(LegendarySurvivalOverhaul.MOD_ID + ".hyperthermia", DamageScaling.NEVER, 0.1f, BURNING));
        context.register(DEHYDRATION, new DamageType(LegendarySurvivalOverhaul.MOD_ID + ".dehydration", DamageScaling.NEVER, 0.1f, POKING));
    }
}
