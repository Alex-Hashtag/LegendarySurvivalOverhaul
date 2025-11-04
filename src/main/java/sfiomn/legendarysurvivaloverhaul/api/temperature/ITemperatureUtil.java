package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public interface ITemperatureUtil
{
    float getPlayerTargetTemperature(Player player);

    float getWorldTemperature(Level world, BlockPos pos);

    float clampTemperature(float temperature);

    void applyConsumableTemperature(Player player, ResourceLocation itemRegistryName);

    void applyConsumableBlockTemperature(Player player, BlockState blockState);

    TemperatureEnum getTemperatureEnum(float temperature);

    boolean hasImmunity(Player player, TemperatureImmunityEnum immunity);

    void addImmunity(Player player, TemperatureImmunityEnum immunity);

    void removeImmunity(Player player, TemperatureImmunityEnum immunity);

    void addTemperatureModifier(Player player, double temperature, UUID uuid);

    void addHeatResistanceModifier(Player player, double temperature, UUID uuid);

    void addColdResistanceModifier(Player player, double temperature, UUID uuid);

    void addThermalResistanceModifier(Player player, double temperature, UUID uuid);

    void setArmorCoatTag(final ItemStack stack, String temperatureType);

    String getArmorCoatTag(final ItemStack stack);

    void removeArmorCoatTag(final ItemStack stack);
}
