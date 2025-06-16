package sfiomn.legendarysurvivaloverhaul.common.temperature.attribute;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;

public class ItemModifier extends AttributeModifierBase
{
	public ItemModifier() {}

	@Override
	public JsonTemperatureResistance getItemAttributes(ItemStack stack)
	{
		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
		JsonTemperatureResistance config = TemperatureDataManager.getItem(itemRegistryName);
		return config == null ? new JsonTemperatureResistance() : config;
	}
}
