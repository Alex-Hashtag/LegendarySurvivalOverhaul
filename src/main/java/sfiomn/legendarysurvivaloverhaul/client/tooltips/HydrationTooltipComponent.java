package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record HydrationTooltipComponent(int hydration, float saturation) implements TooltipComponent
{
}

