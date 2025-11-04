package sfiomn.legendarysurvivaloverhaul.config.json_old;

import com.google.common.collect.Maps;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.JsonPropertyValue;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.bodydamage.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.bodydamage.JsonConsumableHeal;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.temperature.*;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.thirst.JsonEffectParameter;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonConfig
{
    public static Map<String, JsonTemperature> dimensionTemperatures = Maps.newHashMap();
    public static Map<String, JsonTemperature> defaultDimensionTemperatures = Maps.newHashMap();
    public static Map<String, JsonBiomeIdentity> biomeOverrides = Maps.newHashMap();
    public static Map<String, JsonBiomeIdentity> defaultBiomeOverrides = Maps.newHashMap();
    public static Map<String, JsonTemperatureResistance> itemTemperatures = Maps.newHashMap();
    public static Map<String, JsonTemperatureResistance> defaultItemTemperatures = Maps.newHashMap();
    public static Map<String, List<JsonBlockFluidTemperature>> blockFluidTemperatures = Maps.newHashMap();
    public static Map<String, List<JsonBlockFluidTemperature>> defaultBlockFluidTemperatures = Maps.newHashMap();
    public static Map<String, JsonTemperature> entityTemperatures = Maps.newHashMap();
    public static Map<String, JsonTemperature> defaultEntityTemperatures = Maps.newHashMap();
    public static Map<String, JsonFuelItem> fuelItems = Maps.newHashMap();
    public static Map<String, JsonFuelItem> defaultFuelItems = Maps.newHashMap();
    public static Map<String, List<JsonBlockFluidThirst>> blockFluidThirst = Maps.newHashMap();
    public static Map<String, List<JsonBlockFluidThirst>> defaultBlockFluidThirst = Maps.newHashMap();
    public static Map<String, List<JsonConsumableTemperature>> consumableTemperature = Maps.newHashMap();
    public static Map<String, List<JsonConsumableTemperature>> defaultConsumableTemperature = Maps.newHashMap();
    public static Map<String, List<JsonConsumableThirst>> consumableThirst = Maps.newHashMap();
    public static Map<String, List<JsonConsumableThirst>> defaultConsumableThirst = Maps.newHashMap();
    public static Map<String, JsonConsumableHeal> consumableHeal = Maps.newHashMap();
    public static Map<String, JsonConsumableHeal> defaultConsumableHeal = Maps.newHashMap();
    public static Map<String, JsonBodyPartsDamageSource> damageSourceBodyParts = Maps.newHashMap();
    public static Map<String, JsonBodyPartsDamageSource> defaultDamageSourceBodyParts = Maps.newHashMap();


    public static void registerDefaultDimensionTemperature(String registryName, float temperature)
    {
        if (!defaultDimensionTemperatures.containsKey(registryName))
            defaultDimensionTemperatures.put(registryName, new JsonTemperature(temperature));
    }

    public static void registerDimensionTemperature(String registryName, float temperature)
    {
        if (!dimensionTemperatures.containsKey(registryName))
            dimensionTemperatures.put(registryName, new JsonTemperature(temperature));
    }

    public static void registerDefaultBiomeOverride(String registryName, float temperature)
    {
        registerDefaultBiomeOverride(registryName, temperature, false);
    }

    public static void registerDefaultBiomeOverride(String registryName, float temperature, boolean isDry)
    {
        if (!defaultBiomeOverrides.containsKey(registryName))
            defaultBiomeOverrides.put(registryName, new JsonBiomeIdentity(temperature, isDry));
    }

    public static void registerBiomeOverride(String registryName, float temperature)
    {
        registerBiomeOverride(registryName, temperature, false);
    }

    public static void registerBiomeOverride(String registryName, float temperature, boolean isDry)
    {
        if (!biomeOverrides.containsKey(registryName))
            biomeOverrides.put(registryName, new JsonBiomeIdentity(temperature, isDry));
    }

    public static void registerDefaultBlockFluidTemperature(String registryName, float temperature, JsonPropertyValue... properties)
    {
        if (!defaultBlockFluidTemperatures.containsKey(registryName))
            defaultBlockFluidTemperatures.put(registryName, new ArrayList<>());

        final List<JsonBlockFluidTemperature> alreadyDefinedConfig = defaultBlockFluidTemperatures.get(registryName);

        JsonBlockFluidTemperature newBlockFluidTemp = new JsonBlockFluidTemperature(temperature, properties);

        if (properties.length > 0)
        {
            //  If the same set of nbt already exists, replace it
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonBlockFluidTemperature jpt = alreadyDefinedConfig.get(i);
                if (jpt.matchesProperties(properties))
                {
                    alreadyDefinedConfig.set(i, newBlockFluidTemp);
                    return;
                }
            }
        } else
        {
            // Reject config if you want to add empty properties in a block config that has properties
            for (JsonBlockFluidTemperature jpt : alreadyDefinedConfig)
            {
                if (!jpt.properties.isEmpty())
                {
                    return;
                }
            }
            // Replace empty config if it already exists
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonBlockFluidTemperature jpt = alreadyDefinedConfig.get(i);
                if (jpt.properties.isEmpty())
                {
                    alreadyDefinedConfig.set(i, newBlockFluidTemp);
                    return;
                }
            }
        }
        alreadyDefinedConfig.add(newBlockFluidTemp);
    }

    public static void registerBlockFluidTemperature(String registryName, float temperature, JsonPropertyValue... properties)
    {
        if (!blockFluidTemperatures.containsKey(registryName))
            blockFluidTemperatures.put(registryName, new ArrayList<>());

        final List<JsonBlockFluidTemperature> alreadyDefinedConfig = blockFluidTemperatures.get(registryName);

        JsonBlockFluidTemperature newBlockFluidTemp = new JsonBlockFluidTemperature(temperature, properties);

        if (properties.length > 0)
        {
            //  If the same set of nbt already exists, replace it
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonBlockFluidTemperature jpt = alreadyDefinedConfig.get(i);
                if (jpt.matchesProperties(properties))
                {
                    alreadyDefinedConfig.set(i, newBlockFluidTemp);
                    return;
                }
            }
        } else
        {
            // Reject config if you want to add empty properties in a block config that has properties
            for (JsonBlockFluidTemperature jpt : alreadyDefinedConfig)
            {
                if (!jpt.properties.isEmpty())
                {
                    return;
                }
            }
            // Replace empty config if it already exists
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonBlockFluidTemperature jpt = alreadyDefinedConfig.get(i);
                if (jpt.properties.isEmpty())
                {
                    alreadyDefinedConfig.set(i, newBlockFluidTemp);
                    return;
                }
            }
        }
        alreadyDefinedConfig.add(newBlockFluidTemp);
    }

    public static void registerDefaultFuelItems(String registryName, ThermalTypeEnum thermalType, int fuelValue)
    {
        if (!defaultFuelItems.containsKey(registryName))
            defaultFuelItems.put(registryName, new JsonFuelItem(thermalType, fuelValue));
    }

    public static void registerFuelItems(String registryName, ThermalTypeEnum thermalType, int fuelValue)
    {
        if (!fuelItems.containsKey(registryName))
            fuelItems.put(registryName, new JsonFuelItem(thermalType, fuelValue));
    }

    public static void registerDefaultItemTemperature(String registryName, float temperature)
    {
        if (!defaultItemTemperatures.containsKey(registryName))
            defaultItemTemperatures.put(registryName, new JsonTemperatureResistance(temperature));
    }

    public static void registerDefaultItemTemperature(String registryName, float temperature, float heatResistance, float coldResistance, float thermalResistance)
    {
        if (!defaultItemTemperatures.containsKey(registryName))
            defaultItemTemperatures.put(registryName, new JsonTemperatureResistance(temperature, heatResistance, coldResistance, thermalResistance));
    }

    public static void registerItemTemperature(String registryName, float temperature)
    {
        if (!itemTemperatures.containsKey(registryName))
            itemTemperatures.put(registryName, new JsonTemperatureResistance(temperature));
    }

    public static void registerItemTemperature(String registryName, float temperature, float heatResistance, float coldResistance, float thermalResistance)
    {
        if (!itemTemperatures.containsKey(registryName))
            itemTemperatures.put(registryName, new JsonTemperatureResistance(temperature, heatResistance, coldResistance, thermalResistance));
    }

    public static void registerDefaultEntityTemperature(String registryName, float temperature)
    {
        if (!defaultEntityTemperatures.containsKey(registryName))
            defaultEntityTemperatures.put(registryName, new JsonTemperature(temperature));
    }

    public static void registerEntityTemperature(String registryName, float temperature)
    {
        if (!entityTemperatures.containsKey(registryName))
            entityTemperatures.put(registryName, new JsonTemperature(temperature));
    }

    public static void registerDefaultConsumableTemperature(TemporaryModifierGroupEnum group, String registryName, int temperatureLevel, int duration)
    {
        if (!defaultConsumableTemperature.containsKey(registryName))
        {
            defaultConsumableTemperature.put(registryName, new ArrayList<>());
        }

        final List<JsonConsumableTemperature> currentList = defaultConsumableTemperature.get(registryName);

        JsonConsumableTemperature jsonConsumableTemperature = new JsonConsumableTemperature(group, temperatureLevel, duration);

        if (temperatureLevel == 0)
        {
            LegendarySurvivalOverhaul.LOGGER.debug("Error with consumable " + registryName + " : temperature can't be 0");
            return;
        }

        for (int i = 0; i < currentList.size(); i++)
        {
            JsonConsumableTemperature jct = currentList.get(i);
            if (Objects.equals(jct.group, jsonConsumableTemperature.group))
            {
                currentList.set(i, jsonConsumableTemperature);
                return;
            }
        }

        currentList.add(jsonConsumableTemperature);
    }

    public static void registerConsumableTemperature(TemporaryModifierGroupEnum group, String registryName, int temperatureLevel, int duration)
    {
        if (!consumableTemperature.containsKey(registryName))
        {
            consumableTemperature.put(registryName, new ArrayList<>());
        }

        final List<JsonConsumableTemperature> currentList = consumableTemperature.get(registryName);

        JsonConsumableTemperature jsonConsumableTemperature = new JsonConsumableTemperature(group, temperatureLevel, duration);

        if (temperatureLevel == 0)
        {
            LegendarySurvivalOverhaul.LOGGER.debug("Error with consumable " + registryName + " : temperature can't be 0");
            return;
        }

        for (int i = 0; i < currentList.size(); i++)
        {
            JsonConsumableTemperature jct = currentList.get(i);
            if (Objects.equals(jct.group, jsonConsumableTemperature.group))
            {
                currentList.set(i, jsonConsumableTemperature);
                return;
            }
        }

        currentList.add(jsonConsumableTemperature);
    }

    public static void registerDefaultBlockFluidThirst(String registryName, int hydration, float saturation)
    {
        registerDefaultBlockFluidThirst(registryName, hydration, saturation, new JsonPropertyValue[]{});
    }

    public static void registerDefaultBlockFluidThirst(String registryName, int hydration, float saturation, JsonPropertyValue... properties)
    {
        registerDefaultBlockFluidThirst(registryName, hydration, saturation, new JsonEffectParameter[]{}, properties);
    }

    public static void registerDefaultBlockFluidThirst(String registryName, int hydration, float saturation, JsonEffectParameter[] effects, JsonPropertyValue... properties)
    {
        if (!defaultBlockFluidThirst.containsKey(registryName))
            defaultBlockFluidThirst.put(registryName, new ArrayList<>());

        final List<JsonBlockFluidThirst> alreadyDefinedConfig = defaultBlockFluidThirst.get(registryName);

        JsonBlockFluidThirst newBlockFluidThirst = new JsonBlockFluidThirst(hydration, saturation, effects, properties);

        if (properties.length > 0)
        {
            //  If the same set of nbt already exists, replace it
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonBlockFluidThirst jpt = alreadyDefinedConfig.get(i);
                if (jpt.matchesProperties(properties))
                {
                    alreadyDefinedConfig.set(i, newBlockFluidThirst);
                    return;
                }
            }
        } else
        {
            // Replace empty config if it already exists
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonBlockFluidThirst jpt = alreadyDefinedConfig.get(i);
                if (jpt.properties.isEmpty())
                {
                    alreadyDefinedConfig.set(i, newBlockFluidThirst);
                    return;
                }
            }
        }
        alreadyDefinedConfig.add(newBlockFluidThirst);
    }

    public static void registerBlockFluidThirst(String registryName, int hydration, float saturation)
    {
        registerDefaultBlockFluidThirst(registryName, hydration, saturation, new JsonPropertyValue[]{});
    }

    public static void registerBlockFluidThirst(String registryName, int hydration, float saturation, JsonPropertyValue... properties)
    {
        registerDefaultBlockFluidThirst(registryName, hydration, saturation, new JsonEffectParameter[]{}, properties);
    }

    public static void registerBlockFluidThirst(String registryName, int hydration, float saturation, JsonEffectParameter[] effects, JsonPropertyValue... properties)
    {
        if (!blockFluidThirst.containsKey(registryName))
            blockFluidThirst.put(registryName, new ArrayList<>());

        final List<JsonBlockFluidThirst> alreadyDefinedConfig = blockFluidThirst.get(registryName);

        JsonBlockFluidThirst newBlockFluidThirst = new JsonBlockFluidThirst(hydration, saturation, effects, properties);

        if (properties.length > 0)
        {
            //  If the same set of nbt already exists, replace it
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonBlockFluidThirst jpt = alreadyDefinedConfig.get(i);
                if (jpt.matchesProperties(properties))
                {
                    alreadyDefinedConfig.set(i, newBlockFluidThirst);
                    return;
                }
            }
        } else
        {
            // Replace empty config if it already exists
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonBlockFluidThirst jpt = alreadyDefinedConfig.get(i);
                if (jpt.properties.isEmpty())
                {
                    alreadyDefinedConfig.set(i, newBlockFluidThirst);
                    return;
                }
            }
        }
        alreadyDefinedConfig.add(newBlockFluidThirst);
    }

    public static void registerDefaultConsumableThirst(String registryName, int hydration, float saturation)
    {
        registerDefaultConsumableThirst(registryName, hydration, saturation, new JsonPropertyValue[]{});
    }

    public static void registerDefaultConsumableThirst(String registryName, int hydration, float saturation, JsonPropertyValue... nbt)
    {
        registerDefaultConsumableThirst(registryName, hydration, saturation, new JsonEffectParameter[]{}, nbt);
    }

    public static void registerDefaultConsumableThirst(String registryName, int hydration, float saturation, JsonEffectParameter[] effects, JsonPropertyValue... nbt)
    {
        if (!defaultConsumableThirst.containsKey(registryName))
        {
            defaultConsumableThirst.put(registryName, new ArrayList<>());
        }

        final List<JsonConsumableThirst> alreadyDefinedConfig = defaultConsumableThirst.get(registryName);

        JsonConsumableThirst newThirstConfig = new JsonConsumableThirst(hydration, saturation, effects, nbt);

        if (nbt.length > 0)
        {
            //  If the same set of nbt already exists, replace it
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonConsumableThirst jct = alreadyDefinedConfig.get(i);
                if (jct.matchesNbt(nbt))
                {
                    alreadyDefinedConfig.set(i, newThirstConfig);
                    return;
                }
            }
        } else
        {
            //  If an empty nbt already exists, replace it
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonConsumableThirst jct = alreadyDefinedConfig.get(i);
                if (jct.nbt.isEmpty())
                {
                    alreadyDefinedConfig.set(i, newThirstConfig);
                    return;
                }
            }
        }
        alreadyDefinedConfig.add(newThirstConfig);
    }

    public static void registerConsumableThirst(String registryName, int hydration, float saturation)
    {
        registerDefaultConsumableThirst(registryName, hydration, saturation, new JsonPropertyValue[]{});
    }

    public static void registerConsumableThirst(String registryName, int hydration, float saturation, JsonPropertyValue... nbt)
    {
        registerDefaultConsumableThirst(registryName, hydration, saturation, new JsonEffectParameter[]{}, nbt);
    }

    public static void registerConsumableThirst(String registryName, int hydration, float saturation, JsonEffectParameter[] effects, JsonPropertyValue... nbt)
    {
        if (!consumableThirst.containsKey(registryName))
        {
            consumableThirst.put(registryName, new ArrayList<>());
        }

        final List<JsonConsumableThirst> alreadyDefinedConfig = consumableThirst.get(registryName);

        JsonConsumableThirst newThirstConfig = new JsonConsumableThirst(hydration, saturation, effects, nbt);

        if (nbt.length > 0)
        {
            //  If the same set of nbt already exists, replace it
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonConsumableThirst jct = alreadyDefinedConfig.get(i);
                if (jct.matchesNbt(nbt))
                {
                    alreadyDefinedConfig.set(i, newThirstConfig);
                    return;
                }
            }
        } else
        {
            //  If an empty nbt already exists, replace it
            for (int i = 0; i < alreadyDefinedConfig.size(); i++)
            {
                JsonConsumableThirst jct = alreadyDefinedConfig.get(i);
                if (jct.nbt.isEmpty())
                {
                    alreadyDefinedConfig.set(i, newThirstConfig);
                    return;
                }
            }
        }
        alreadyDefinedConfig.add(newThirstConfig);
    }

    public static void registerDefaultConsumableHeal(String registryName, int healingCharges, float healingValue, int healingTime)
    {
        if (!defaultConsumableHeal.containsKey(registryName))
            if (healingCharges < 0)
                LegendarySurvivalOverhaul.LOGGER.debug("Error with consumable " + registryName + " : healing charges can't be negative");
            else
                defaultConsumableHeal.put(registryName, new JsonConsumableHeal(healingCharges, healingValue, healingTime));
    }

    public static void registerConsumableHeal(String registryName, int healingCharges, float healingValue, int healingTime)
    {
        if (!consumableHeal.containsKey(registryName))
            if (healingCharges < 0)
                LegendarySurvivalOverhaul.LOGGER.debug("Error with consumable " + registryName + " : healing charges can't be negative");
            else
                consumableHeal.put(registryName, new JsonConsumableHeal(healingCharges, healingValue, healingTime));
    }

    public static void registerDefaultDamageSourceBodyParts(String damageSource, DamageDistributionEnum damageDistribution, List<BodyPartEnum> bodyParts)
    {
        if (!defaultDamageSourceBodyParts.containsKey(damageSource))
            defaultDamageSourceBodyParts.put(damageSource, new JsonBodyPartsDamageSource(damageDistribution, bodyParts));
    }

    public static void registerDamageSourceBodyParts(String damageSource, DamageDistributionEnum damageDistribution, List<BodyPartEnum> bodyParts)
    {
        if (!damageSourceBodyParts.containsKey(damageSource))
            damageSourceBodyParts.put(damageSource, new JsonBodyPartsDamageSource(damageDistribution, bodyParts));
    }
}
