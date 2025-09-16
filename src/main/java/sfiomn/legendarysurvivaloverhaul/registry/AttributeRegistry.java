package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;


public class AttributeRegistry
{
    public static DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<Attribute, ? extends Attribute> HEATING_TEMPERATURE = ATTRIBUTES.register("heating_temperature", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heating_temperature", 0.0f, -10000f, 10000f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> COOLING_TEMPERATURE = ATTRIBUTES.register("cooling_temperature", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cooling_temperature", 0.0f, -10000f, 10000f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> HEAT_RESISTANCE = ATTRIBUTES.register("heat_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heat_resistance", 0.0f, 0.0f, 10000.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> COLD_RESISTANCE = ATTRIBUTES.register("cold_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cold_resistance", 0.0f, 0.0f, 10000.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> THERMAL_RESISTANCE = ATTRIBUTES.register("thermal_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".thermal_resistance", 0.0f, 0.0f, 10000.0f).setSyncable(true));

    public static final DeferredHolder<Attribute, ? extends Attribute> BODY_RESISTANCE = ATTRIBUTES.register("body_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".body_resistance", 0.0f, -200.0f, 100.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> HEAD_RESISTANCE = ATTRIBUTES.register("head_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".head_resistance", 0.0f, -200.0f, 100.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> CHEST_RESISTANCE = ATTRIBUTES.register("chest_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".chest_resistance", 0.0f, -200.0f, 100.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> RIGHT_ARM_RESISTANCE = ATTRIBUTES.register("right_arm_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".right_arm_resistance", 0.0f, -200.0f, 100.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> LEFT_ARM_RESISTANCE = ATTRIBUTES.register("left_arm_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".left_arm_resistance", 0.0f, -200.0f, 100.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> LEGS_RESISTANCE = ATTRIBUTES.register("legs_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".legs_resistance", 0.0f, -200.0f, 100.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> FEET_RESISTANCE = ATTRIBUTES.register("feet_resistance", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".feet_resistance", 0.0f, -200.0f, 100.0f).setSyncable(true));

    public static final DeferredHolder<Attribute, ? extends Attribute> BROKEN_HEART = ATTRIBUTES.register("broken_heart", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".broken_heart", 0.0f, 0.0f, 10000.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> PERMANENT_HEART = ATTRIBUTES.register("permanent_heart", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".permanent_heart", 1.0f, 1.0f, 10000.0f).setSyncable(true));
    public static final DeferredHolder<Attribute, ? extends Attribute> BROKEN_HEART_RESILIENCE = ATTRIBUTES.register("broken_heart_resilience", () -> new RangedAttribute("attribute." + LegendarySurvivalOverhaul.MOD_ID + ".broken_heart_resilience", 1.0f, 0.0f, 10000.0f).setSyncable(true));

    public static void register(IEventBus eventBus)
    {
        ATTRIBUTES.register(eventBus);
    }
}
