package sfiomn.legendarysurvivaloverhaul.common.temperature.attribute;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;

public class ItemAttributeModifier extends AttributeModifierBase
{
    public ItemAttributeModifier()
    {
    }

    @Override
    public JsonTemperatureResistance getItemAttributes(ItemStack stack)
    {
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(stack.getItem());
        JsonTemperatureResistance config = TemperatureDataManager.getItem(itemRegistryName);
        return config == null ? new JsonTemperatureResistance() : config;
    }
}
