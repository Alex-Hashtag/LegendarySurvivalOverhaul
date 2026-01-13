package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumableBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.attachments.temperature.TemperatureAttachment;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry.DYNAMIC_MODIFIERS;
import static sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry.MODIFIERS;

/**
 * Internal utility class for temperature-related calculations.
 */
public class TemperatureUtilInternal implements ITemperatureUtil
{
    public static final String COAT_TAG = "Coat";

    public static final AttributeBuilder HEATING_TEMPERATURE = new AttributeBuilder(AttributeRegistry.HEATING_TEMPERATURE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "heating_temperature"));
    public static final AttributeBuilder COOLING_TEMPERATURE = new AttributeBuilder(AttributeRegistry.COOLING_TEMPERATURE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cooling_temperature"));
    public static final AttributeBuilder HEAT_RESISTANCE = new AttributeBuilder(AttributeRegistry.HEAT_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "heat_resistance"));
    public static final AttributeBuilder COLD_RESISTANCE = new AttributeBuilder(AttributeRegistry.COLD_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cold_resistance"));
    public static final AttributeBuilder THERMAL_RESISTANCE = new AttributeBuilder(AttributeRegistry.THERMAL_RESISTANCE, ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "thermal_resistance"));

    public static final Map<EquipmentSlot, UUID> equipmentSlotTemperatureUuid = new HashMap<>();

    static
    {
        equipmentSlotTemperatureUuid.put(EquipmentSlot.HEAD, UUID.fromString("06e30f27-2340-4bdb-9a91-a657f1e2880f"));
        equipmentSlotTemperatureUuid.put(EquipmentSlot.CHEST, UUID.fromString("1e7ef99e-2fe7-4edc-95b1-27fa056eae6d"));
        equipmentSlotTemperatureUuid.put(EquipmentSlot.LEGS, UUID.fromString("f46c0aff-7381-4f99-890e-75eb3781af21"));
        equipmentSlotTemperatureUuid.put(EquipmentSlot.FEET, UUID.fromString("34f98220-a7d9-4cc1-8930-b3dc4115ad07"));
        equipmentSlotTemperatureUuid.put(EquipmentSlot.MAINHAND, UUID.fromString("7b1e1c2c-746c-4631-8037-f76c82529909"));
        equipmentSlotTemperatureUuid.put(EquipmentSlot.OFFHAND, UUID.fromString("389caa2f-2c18-49da-b521-b53cc5713e14"));
    }

    @Override
    public float getPlayerTargetTemperature(Player player)
    {
        float sum = 0.0f;
        Level world = player.getCommandSenderWorld();
        BlockPos pos = WorldUtil.getSidedBlockPos(world, player);

        for (var holder : MODIFIERS.getEntries())
        {
            ModifierBase modifier = holder.get();
            float worldInfluence = modifier.getWorldInfluence(player, world, pos);
            float playerInfluence = modifier.getPlayerInfluence(player);
            if (player.getMainHandItem().is(Items.DEBUG_STICK))
            {
                LegendarySurvivalOverhaul.LOGGER.info("{} : world influence={}, player influence={}", holder.getId(), worldInfluence, playerInfluence);
            }

            sum += worldInfluence + playerInfluence;
        }

        float dynamicModification = 0.0f;
        for (var holder : DYNAMIC_MODIFIERS.getEntries())
        {
            DynamicModifierBase dynamicModifier = holder.get();
            float worldInfluence = dynamicModifier.applyDynamicWorldInfluence(player, world, pos, sum, dynamicModification);
            float playerInfluence = dynamicModifier.applyDynamicPlayerInfluence(player, sum, dynamicModification);
            if (player.getMainHandItem().is(Items.DEBUG_STICK))
            {
                LegendarySurvivalOverhaul.LOGGER.info("{} : dynamic world influence={}, dynamic player influence={}", holder.getId(), worldInfluence, playerInfluence);
            }

            dynamicModification += worldInfluence + playerInfluence;
        }
        sum += dynamicModification;
        return MathUtil.round(sum, 1);
    }

    @Override
    public float getWorldTemperature(Level world, BlockPos pos)
    {
        float sum = 0.0f;

        for (var holder : MODIFIERS.getEntries())
        {
            ModifierBase modifier = holder.get();
            // LegendarySurvivalOverhaul.LOGGER.debug("tmp influence : " + modifier.getRegistryName() + ", " + modifier.getWorldInfluence(world, pos));
            sum += modifier.getWorldInfluence(null, world, pos);
        }

        float dynamicModification = 0.0f;
        for (var holder : DYNAMIC_MODIFIERS.getEntries())
        {
            DynamicModifierBase dynamicModifier = holder.get();
            // LegendarySurvivalOverhaul.LOGGER.debug("tmp influence : " + dynamicModifier.getRegistryName() + ", " + dynamicModifier.applyDynamicWorldInfluence(world, pos, sum));
            dynamicModification += dynamicModifier.applyDynamicWorldInfluence(null, world, pos, sum, dynamicModification);
        }
        sum += dynamicModification;

        return MathUtil.round(sum, 1);
    }

    @Override
    public TemperatureEnum getTemperatureEnum(float temperature)
    {
        return TemperatureEnum.get(temperature);
    }

    @Override
    public boolean hasImmunity(Player player, TemperatureImmunityEnum immunity)
    {
        if (!Config.Baked.temperatureEnabled)
            return false;

        TemperatureAttachment cap = AttachmentUtil.getTempAttachment(player);
        return cap.getTemperatureImmunities().contains(immunity.id);
    }

    @Override
    public void addImmunity(Player player, TemperatureImmunityEnum immunity)
    {
        if (!Config.Baked.temperatureEnabled)
            return;

        TemperatureAttachment cap = AttachmentUtil.getTempAttachment(player);
        cap.addTemperatureImmunityId(immunity.id);
    }

    @Override
    public void removeImmunity(Player player, TemperatureImmunityEnum immunity)
    {
        if (!Config.Baked.temperatureEnabled)
            return;

        TemperatureAttachment cap = AttachmentUtil.getTempAttachment(player);
        cap.removeTemperatureImmunityId(immunity.id);
    }

    @Override
    public void applyConsumableTemperature(Player player, ResourceLocation itemRegistryName)
    {
        if (Config.Baked.temperatureEnabled)
        {
            List<JsonTemperatureConsumable> jsonConsumableTemperatures = TemperatureDataManager.getConsumable(itemRegistryName);

            if (jsonConsumableTemperatures != null)
            {
                for (JsonTemperatureConsumable jtc : jsonConsumableTemperatures)
                {
                    if (jtc.getEffect() != null)
                    {
                        player.addEffect(new MobEffectInstance(jtc.getEffectHolder(), jtc.duration, (Math.abs(jtc.temperatureLevel) - 1), false, false, true));
                        if (jtc.getOppositeEffectHolder() != null)
                            player.removeEffect(jtc.getOppositeEffectHolder());
                    }
                }
            }
        }
    }

    @Override
    public void applyConsumableBlockTemperature(Player player, BlockState blockState)
    {
        if (Config.Baked.temperatureEnabled)
        {
            ResourceLocation blockRegistryName = BuiltInRegistries.BLOCK.getKey(blockState.getBlock());
            List<JsonTemperatureConsumableBlock> jsonConsumableBlockTemperatures = TemperatureDataManager.getConsumableBlock(blockRegistryName);

            if (jsonConsumableBlockTemperatures != null)
            {
                for (JsonTemperatureConsumableBlock jtcb : jsonConsumableBlockTemperatures)
                {
                    if (jtcb.getEffect() != null && jtcb.matchesState(blockState))
                    {
                        player.addEffect(new MobEffectInstance(jtcb.getEffectHolder(), jtcb.duration, (Math.abs(jtcb.temperatureLevel) - 1), false, false, true));
                        if (jtcb.getOppositeEffectHolder() != null)
                            player.removeEffect(jtcb.getOppositeEffectHolder());
                    }
                }
            }
        }
    }

    @Override
    public void addTemperatureModifier(Player player, double temperature, UUID uuid)
    {
        HEATING_TEMPERATURE.addModifier(player, uuid, Math.max(temperature, 0));
        COOLING_TEMPERATURE.addModifier(player, uuid, Math.min(temperature, 0));
    }

    @Override
    public void addHeatResistanceModifier(Player player, double resistance, UUID uuid)
    {
        HEAT_RESISTANCE.addModifier(player, uuid, resistance);
    }

    @Override
    public void addColdResistanceModifier(Player player, double resistance, UUID uuid)
    {
        COLD_RESISTANCE.addModifier(player, uuid, resistance);
    }

    public void addThermalResistanceModifier(Player player, double resistance, UUID uuid)
    {
        THERMAL_RESISTANCE.addModifier(player, uuid, resistance);
    }

    @Override
    public void setArmorCoatTag(ItemStack stack, String coatId)
    {
        CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag compound = custom != null ? custom.copyTag() : new CompoundTag();
        compound.putString(COAT_TAG, coatId);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
    }


    @Override
    public float clampTemperature(float temperature)
    {
        return Mth.clamp(temperature, TemperatureEnum.getMin(), TemperatureEnum.getMax());
    }

    @Override
    public String getArmorCoatTag(ItemStack stack)
    {
        CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
        if (custom != null)
        {
            final CompoundTag compound = custom.copyTag();
            // TODO: remove this temporary transfer to new coat tag name
            if (compound.contains("ArmorPadding"))
            {
                compound.putString(COAT_TAG, compound.getString("ArmorPadding"));
                compound.remove("ArmorPadding");
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
            }
            if (compound.contains(COAT_TAG))
            {
                return compound.getString(COAT_TAG);
            }
        }
        return "";
    }

    @Override
    public void removeArmorCoatTag(ItemStack stack)
    {
        CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
        if (custom != null)
        {
            final CompoundTag compound = custom.copyTag();
            if (compound.contains(COAT_TAG))
            {
                compound.remove(COAT_TAG);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
            }
        }
    }
}
