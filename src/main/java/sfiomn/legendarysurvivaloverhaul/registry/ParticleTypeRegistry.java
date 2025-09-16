package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;


public class ParticleTypeRegistry
{
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<SimpleParticleType, ? extends SimpleParticleType> SUN_FERN_BLOSSOM = PARTICLE_TYPES.register("sun_fern_blossom", () -> new SimpleParticleType(true));
    public static final DeferredHolder<SimpleParticleType, ? extends SimpleParticleType> ICE_FERN_BLOSSOM = PARTICLE_TYPES.register("ice_fern_blossom", () -> new SimpleParticleType(true));
    public static final DeferredHolder<SimpleParticleType, ? extends SimpleParticleType> COLD_BREATH = PARTICLE_TYPES.register("cold_breath", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus)
    {
        PARTICLE_TYPES.register(eventBus);
    }
}
