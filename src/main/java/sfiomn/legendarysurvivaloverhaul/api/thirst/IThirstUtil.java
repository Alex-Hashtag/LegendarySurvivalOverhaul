package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonMobEffect;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;

import java.util.List;

public interface IThirstUtil
{
    void takeDrink(Player player, ItemStack itemStack);

    void takeDrink(Player player, int thirst, float saturation, List<JsonMobEffect> effects);

    void takeDrink(Player player, int thirst, float saturation);

    void addExhaustion(Player player, float exhaustion);

    JsonThirstBlock getFluidThirstLookedAt(Player player, double finalDistance);

    JsonThirstBlock getBlockThirstLookedAt(Player player, double finalDistance);

    void setHydrationEnumTag(final ItemStack stack, HydrationEnum hydrationEnum);

    HydrationEnum getHydrationEnumTag(final ItemStack stack);

    void removeHydrationEnumTag(final ItemStack stack);

    void setCapacityTag(final ItemStack stack, int capacity);

    int getCapacityTag(final ItemStack stack);

    void removeCapacityTag(final ItemStack stack);

    void deactivateThirst(Player player);

    void activateThirst(Player player);

    boolean isThirstActive(Player player);
}
