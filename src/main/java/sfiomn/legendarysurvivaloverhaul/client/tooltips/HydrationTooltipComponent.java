package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class HydrationTooltipComponent implements TooltipComponent {
    public final int hydration;
    public final float saturation;

    public HydrationTooltipComponent(int hydration, float saturation) {
        this.hydration = hydration;
        this.saturation = saturation;
    }
}

