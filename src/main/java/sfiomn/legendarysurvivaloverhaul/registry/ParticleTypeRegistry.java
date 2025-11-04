package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;


public class ParticleTypeRegistry
{
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SUN_FERN_BLOSSOM = PARTICLE_TYPES.register("sun_fern_blossom", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ICE_FERN_BLOSSOM = PARTICLE_TYPES.register("ice_fern_blossom", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> COLD_BREATH = PARTICLE_TYPES.register("cold_breath", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus)
    {
        PARTICLE_TYPES.register(eventBus);
    }
}
