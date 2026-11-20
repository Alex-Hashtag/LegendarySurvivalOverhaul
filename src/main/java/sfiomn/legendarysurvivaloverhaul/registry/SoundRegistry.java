package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;


public class SoundRegistry
{
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> HEAT_STROKE_EARLY = registerSoundEvent("heat_stroke_early");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> HEAT_STROKE = registerSoundEvent("heat_stroke");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> PANTING = registerSoundEvent("panting");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> FROSTBITE_EARLY = registerSoundEvent("frostbite_early");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> FROSTBITE = registerSoundEvent("frostbite");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> SHIVERING = registerSoundEvent("shivering");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> SEWING_TABLE = registerSoundEvent("sewing_table");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> COOLER_BLOCK = registerSoundEvent("cooler_block");

    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> SELF_WATERING = registerSoundEvent("self_watering");

    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> HEADSHOT = registerSoundEvent("headshot");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> HEAL_BODY_PART = registerSoundEvent("heal_body_part");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> HARD_FALLING_HURT = registerSoundEvent("hard_falling_hurt");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> HEADACHE_HEARTBEAT = registerSoundEvent("headache_heartbeat");

    // Limb penalties. PLACEHOLDERS: sounds.json points them to existing sounds until the real ones are made
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> LIMB_EAR_RINGING = registerSoundEvent("limb_ear_ringing");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> LIMB_HEARTBEAT_FAST = registerSoundEvent("limb_heartbeat_fast");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> LIMB_HEARTBEAT = registerSoundEvent("limb_heartbeat");
    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> LIMB_SHORT_BREATH = registerSoundEvent("limb_short_breath");

    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> HEART_CONTAINER = registerSoundEvent("heart_container");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name)
    {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, name)
        ));
    }

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENTS.register(eventBus);
    }
}
