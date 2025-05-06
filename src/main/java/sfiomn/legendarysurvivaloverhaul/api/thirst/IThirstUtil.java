package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonMobEffect;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;

import java.util.List;

public interface IThirstUtil
{
	public void takeDrink(Player player, int thirst, float saturation, List<JsonMobEffect> effects);

	public void takeDrink(Player player, int thirst, float saturation);

	public void addExhaustion(Player player, float exhaustion);

	public JsonThirstBlock getJsonBlockThirstLookedAt(Player player, double finalDistance);

	public void setThirstEnumTag(final ItemStack stack, HydrationEnum hydrationEnum);

	public HydrationEnum getHydrationEnumTag(final ItemStack stack);

	public void removeHydrationEnumTag(final ItemStack stack);

	public void setCapacityTag(final ItemStack stack, int capacity);

	public int getCapacityTag(final ItemStack stack);

	public void removeCapacityTag(final ItemStack stack);

	public void deactivateThirst(Player player);

	public void activateThirst(Player player);

	public boolean isThirstActive(Player player);
}
