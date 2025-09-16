package sfiomn.legendarysurvivaloverhaul.common.events;


import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModBusEvents {

    @SubscribeEvent
    public static void onEntityAttributesChange(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEATING_TEMPERATURE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEATING_TEMPERATURE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.COOLING_TEMPERATURE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.COOLING_TEMPERATURE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEAT_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEAT_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.COLD_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.COLD_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.THERMAL_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.THERMAL_RESISTANCE.get()
            );
        }

        if (!event.has(EntityType.PLAYER, AttributeRegistry.BODY_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.BODY_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEAD_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEAD_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.CHEST_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.CHEST_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.RIGHT_ARM_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.RIGHT_ARM_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.LEFT_ARM_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.LEFT_ARM_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.LEGS_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.LEGS_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.FEET_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.FEET_RESISTANCE.get()
            );
        }

        if (!event.has(EntityType.PLAYER, AttributeRegistry.BROKEN_HEART.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.BROKEN_HEART.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.PERMANENT_HEART.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.PERMANENT_HEART.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.BROKEN_HEART_RESILIENCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.BROKEN_HEART_RESILIENCE.get()
            );
        }
    }
}
