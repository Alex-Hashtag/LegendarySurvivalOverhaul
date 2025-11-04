package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.effects.*;


@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class MobEffectRegistry
{

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, LegendarySurvivalOverhaul.MOD_ID);
    public static final DeferredRegister<Potion> TEMPERATURE_POTIONS = DeferredRegister.create(Registries.POTION, LegendarySurvivalOverhaul.MOD_ID);
    public static final DeferredRegister<Potion> THIRST_POTIONS = DeferredRegister.create(Registries.POTION, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<MobEffect, ThirstEffect> THIRST = EFFECTS.register("thirst", ThirstEffect::new);
    public static final DeferredHolder<Potion, Potion> THIRST_POTION = THIRST_POTIONS.register("thirst", () -> new Potion("thirst", new MobEffectInstance(THIRST, 3600, 0, false, true, true)));
    public static final DeferredHolder<Potion, Potion> THIRST_POTION_LONG = THIRST_POTIONS.register("thirst_long", () -> new Potion("thirst_long", new MobEffectInstance(THIRST, 9600, 0, false, true, true)));
    public static final DeferredHolder<MobEffect, HydrationFillEffect> HYDRATION_FILL = EFFECTS.register("hydration_fill", HydrationFillEffect::new);
    public static final DeferredHolder<Potion, Potion> HYDRATION_FILL_POTION = THIRST_POTIONS.register("hydration_fill", () -> new Potion("hydration_fill", new MobEffectInstance(HYDRATION_FILL, 3600, 0, false, true, true)));
    public static final DeferredHolder<Potion, Potion> HYDRATION_FILL_POTION_LONG = THIRST_POTIONS.register("hydration_fill_long", () -> new Potion("hydration_fill_long", new MobEffectInstance(HYDRATION_FILL, 9600, 0, false, true, true)));

    public static final DeferredHolder<MobEffect, MobEffect> HOT_FOOD = EFFECTS.register("hot_food", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 16714764, 1).addAttributeModifier(AttributeRegistry.HEATING_TEMPERATURE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "hot_food_modifier"), 1.0, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> HOT_DRINk = EFFECTS.register("hot_drink", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 16714764, 1).addAttributeModifier(AttributeRegistry.HEATING_TEMPERATURE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "hot_drink_modifier"), 1.0, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> COLD_FOOD = EFFECTS.register("cold_food", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 1166574, -1).addAttributeModifier(AttributeRegistry.COOLING_TEMPERATURE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cold_food_modifier"), -1.0, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> COLD_DRINK = EFFECTS.register("cold_drink", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 1166574, -1).addAttributeModifier(AttributeRegistry.COOLING_TEMPERATURE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cold_drink_modifier"), -1.0, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> HEAT_RESISTANCE = EFFECTS.register("heat_resistance", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 16420407, 1).addAttributeModifier(AttributeRegistry.HEAT_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "heat_resistance_modifier"), 1.0, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<Potion, ? extends Potion> HEAT_RESISTANCE_POTION = TEMPERATURE_POTIONS.register("heat_resistance", () -> new Potion("heat_resistance", new MobEffectInstance(HEAT_RESISTANCE, 1800, 1, false, true, true)));
    public static final DeferredHolder<Potion, ? extends Potion> HEAT_RESISTANCE_POTION_LONG = TEMPERATURE_POTIONS.register("heat_resistance_long", () -> new Potion("heat_resistance_long", new MobEffectInstance(HEAT_RESISTANCE, 2400, 1, false, true, true)));
    public static final DeferredHolder<MobEffect, MobEffect> COLD_RESISTANCE = EFFECTS.register("cold_resistance", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 6466303, 1).addAttributeModifier(AttributeRegistry.COLD_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cold_resistance_modifier"), 1.0, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<Potion, ? extends Potion> COLD_RESISTANCE_POTION = TEMPERATURE_POTIONS.register("cold_resistance", () -> new Potion("cold_resistance", new MobEffectInstance(COLD_RESISTANCE, 1800, 1, false, true, true)));
    public static final DeferredHolder<Potion, ? extends Potion> COLD_RESISTANCE_POTION_LONG = TEMPERATURE_POTIONS.register("cold_resistance_long", () -> new Potion("cold_resistance_long", new MobEffectInstance(COLD_RESISTANCE, 2400, 1, false, true, true)));
    public static final DeferredHolder<MobEffect, FrostbiteEffect> FROSTBITE = EFFECTS.register("frostbite", FrostbiteEffect::new);
    public static final DeferredHolder<MobEffect, ColdHungerEffect> COLD_HUNGER = EFFECTS.register("cold_hunger", ColdHungerEffect::new);
    public static final DeferredHolder<MobEffect, HeatStrokeEffect> HEAT_STROKE = EFFECTS.register("heat_stroke", HeatStrokeEffect::new);
    public static final DeferredHolder<MobEffect, HeatThirstEffect> HEAT_THIRST = EFFECTS.register("heat_thirst", HeatThirstEffect::new);
    public static final DeferredHolder<MobEffect, ColdImmunityEffect> COLD_IMMUNITY = EFFECTS.register("cold_immunity", ColdImmunityEffect::new);
    public static final DeferredHolder<Potion, ? extends Potion> COLD_IMMUNITY_POTION = TEMPERATURE_POTIONS.register("cold_immunity", () -> new Potion("cold_immunity", new MobEffectInstance(COLD_IMMUNITY, 1800, 0, false, true, true)));
    public static final DeferredHolder<Potion, ? extends Potion> COLD_IMMUNITY_POTION_LONG = TEMPERATURE_POTIONS.register("cold_immunity_long", () -> new Potion("cold_immunity_long", new MobEffectInstance(COLD_IMMUNITY, 2400, 0, false, true, true)));
    public static final DeferredHolder<MobEffect, HeatImmunityEffect> HEAT_IMMUNITY = EFFECTS.register("heat_immunity", HeatImmunityEffect::new);
    public static final DeferredHolder<Potion, Potion> HEAT_IMMUNITY_POTION = TEMPERATURE_POTIONS.register("heat_immunity", () -> new Potion("heat_immunity", new MobEffectInstance(HEAT_IMMUNITY, 1800, 0, false, true, true)));
    public static final DeferredHolder<Potion, ? extends Potion> HEAT_IMMUNITY_POTION_LONG = TEMPERATURE_POTIONS.register("heat_immunity_long", () -> new Potion("heat_immunity_long", new MobEffectInstance(HEAT_IMMUNITY, 2400, 0, false, true, true)));
    public static final DeferredHolder<MobEffect, TemperatureImmunityEffect> TEMPERATURE_IMMUNITY = EFFECTS.register("temperature_immunity", TemperatureImmunityEffect::new);
    public static final DeferredHolder<Potion, ? extends Potion> TEMPERATURE_IMMUNITY_POTION = TEMPERATURE_POTIONS.register("temperature_immunity", () -> new Potion("temperature_immunity", new MobEffectInstance(TEMPERATURE_IMMUNITY, 1800, 0, false, true, true)));
    public static final DeferredHolder<Potion, ? extends Potion> TEMPERATURE_IMMUNITY_POTION_LONG = TEMPERATURE_POTIONS.register("temperature_immunity_long", () -> new Potion("temperature_immunity_long", new MobEffectInstance(TEMPERATURE_IMMUNITY, 2400, 0, false, true, true)));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> PAINKILLER = EFFECTS.register("painkiller", PainKillerEffect::new);
    public static final DeferredHolder<MobEffect, ? extends MobEffect> PAINKILLER_ADDICTION = EFFECTS.register("painkiller_addiction", PainkillerAddictionEffect::new);

    public static final DeferredHolder<MobEffect, ? extends MobEffect> HARD_FALLING = EFFECTS.register("hard_falling", HardFallingEffect::new);
    public static final DeferredHolder<MobEffect, ? extends MobEffect> VULNERABILITY = EFFECTS.register("vulnerability", VulnerabilityEffect::new);
    public static final DeferredHolder<MobEffect, ? extends MobEffect> HEADACHE = EFFECTS.register("headache", HeadacheEffect::new);
    public static final DeferredHolder<MobEffect, ? extends MobEffect> RECOVERY = EFFECTS.register("recovery", RecoveryEffect::new);

    @SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event)
    {
        var builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, ItemRegistry.SUN_FERN.get(), HEAT_RESISTANCE_POTION);
        builder.addMix(HEAT_RESISTANCE_POTION, Items.REDSTONE, HEAT_RESISTANCE_POTION_LONG);

        builder.addMix(Potions.AWKWARD, ItemRegistry.ICE_FERN.get(), COLD_RESISTANCE_POTION);
        builder.addMix(COLD_RESISTANCE_POTION, Items.REDSTONE, COLD_RESISTANCE_POTION_LONG);

        builder.addMix(Potions.AWKWARD, ItemRegistry.SUN_FERN_GOLD.get(), HEAT_IMMUNITY_POTION);
        builder.addMix(HEAT_IMMUNITY_POTION, Items.REDSTONE, HEAT_IMMUNITY_POTION_LONG);

        builder.addMix(Potions.AWKWARD, ItemRegistry.ICE_FERN_GOLD.get(), COLD_IMMUNITY_POTION);
        builder.addMix(COLD_IMMUNITY_POTION, Items.REDSTONE, COLD_IMMUNITY_POTION_LONG);

        builder.addMix(HEAT_IMMUNITY_POTION, ItemRegistry.ICE_FERN_GOLD.get(), TEMPERATURE_IMMUNITY_POTION);
        builder.addMix(HEAT_IMMUNITY_POTION_LONG, ItemRegistry.ICE_FERN_GOLD.get(), TEMPERATURE_IMMUNITY_POTION_LONG);

        builder.addMix(COLD_IMMUNITY_POTION, ItemRegistry.SUN_FERN_GOLD.get(), TEMPERATURE_IMMUNITY_POTION);
        builder.addMix(COLD_IMMUNITY_POTION_LONG, ItemRegistry.SUN_FERN_GOLD.get(), TEMPERATURE_IMMUNITY_POTION_LONG);
        builder.addMix(TEMPERATURE_IMMUNITY_POTION, Items.REDSTONE, TEMPERATURE_IMMUNITY_POTION_LONG);
    }

    public static void register(IEventBus eventBus)
    {
        EFFECTS.register(eventBus);
        TEMPERATURE_POTIONS.register(eventBus);
        THIRST_POTIONS.register(eventBus);
    }
}
