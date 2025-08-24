package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class Medkit extends BodyHealingItem {
    public Medkit(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return Config.Baked.medkitUseTime;
    }
}
