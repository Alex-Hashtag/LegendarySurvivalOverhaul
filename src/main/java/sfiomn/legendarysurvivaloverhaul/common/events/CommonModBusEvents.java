package sfiomn.legendarysurvivaloverhaul.common.events;


import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModBusEvents
{

    @SubscribeEvent
    public static void onEntityAttributesChange(EntityAttributeModificationEvent event)
    {
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEATING_TEMPERATURE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEATING_TEMPERATURE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.COOLING_TEMPERATURE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.COOLING_TEMPERATURE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEAT_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEAT_RESISTANCE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.COLD_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.COLD_RESISTANCE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.THERMAL_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.THERMAL_RESISTANCE.getDelegate()
            );
        }

        if (!event.has(EntityType.PLAYER, AttributeRegistry.BODY_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.BODY_RESISTANCE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEAD_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEAD_RESISTANCE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.CHEST_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.CHEST_RESISTANCE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.RIGHT_ARM_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.RIGHT_ARM_RESISTANCE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.LEFT_ARM_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.LEFT_ARM_RESISTANCE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.LEGS_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.LEGS_RESISTANCE.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.FEET_RESISTANCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.FEET_RESISTANCE.getDelegate()
            );
        }

        if (!event.has(EntityType.PLAYER, AttributeRegistry.BROKEN_HEART.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.BROKEN_HEART.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.PERMANENT_HEART.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.PERMANENT_HEART.getDelegate()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.BROKEN_HEART_RESILIENCE.getDelegate()))
        {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.BROKEN_HEART_RESILIENCE.getDelegate()
            );
        }
    }
}
