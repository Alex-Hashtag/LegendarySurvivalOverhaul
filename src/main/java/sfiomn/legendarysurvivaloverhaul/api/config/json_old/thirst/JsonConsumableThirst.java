package sfiomn.legendarysurvivaloverhaul.api.config.json_old.thirst;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json_old.JsonPropertyValue;

import java.util.*;

public class JsonConsumableThirst
{
    @SerializedName("hydration")
    public int hydration;
    @SerializedName("saturation")
    public float saturation;
    @SerializedName("effects")
    public List<JsonEffectParameter> effects;
    @SerializedName("nbt")
    public Map<String, String> nbt;

    public JsonConsumableThirst(int hydration, float saturation, JsonEffectParameter[] effects, JsonPropertyValue... nbt)
    {
        this.hydration = hydration;
        this.saturation = saturation;

        this.effects = new ArrayList<>();
        this.effects.addAll(Arrays.asList(effects));

        this.nbt = new HashMap<>();
        for (JsonPropertyValue prop : nbt)
        {
            this.nbt.put(prop.name, prop.value);
        }
    }

    public JsonPropertyValue[] getNbtArray()
    {
        List<JsonPropertyValue> jpvList = new ArrayList<>();
        for (Map.Entry<String, String> entry : this.nbt.entrySet())
        {
            jpvList.add(new JsonPropertyValue(entry.getKey(), entry.getValue()));
        }
        return jpvList.toArray(new JsonPropertyValue[0]);
    }

    public boolean matchesNbt(ItemStack itemStack)
    {
        boolean hasCustom = itemStack.has(DataComponents.CUSTOM_DATA);
        if (hasCustom == nbt.isEmpty())
            return false;
        CustomData custom = itemStack.get(DataComponents.CUSTOM_DATA);
        CompoundTag itemStackTag = custom != null ? custom.copyTag() : null;

        if (itemStackTag == null && nbt.isEmpty())
            return true;

        assert itemStackTag != null;

        for (Map.Entry<String, String> nbtEntry : this.nbt.entrySet())
        {
            String key = nbtEntry.getKey();
            String expected = nbtEntry.getValue();
            if (!itemStackTag.contains(key))
                return false;

            byte tagType = itemStackTag.getTagType(key);
            if (tagType == 8)
            {
                if (!itemStackTag.getString(key).equals(expected))
                    return false;
            } else if (tagType == 1 || tagType == 2 || tagType == 3 || tagType == 4 || tagType == 5 || tagType == 6)
            {
                if (Double.compare(itemStackTag.getDouble(key), Double.parseDouble(expected)) != 0)
                    return false;
            } else
            {
                LegendarySurvivalOverhaul.LOGGER.error("Error while matching nbt for {} : Tag type {} not taken into account.\nIt can either be a String (tag type 8) or a numeric (tag type in [1-6])", itemStack.getDescriptionId(), tagType);
                return false;
            }
        }

        return true;
    }

    public boolean isDefault()
    {
        return this.nbt.isEmpty();
    }

    public boolean matchesNbt(JsonPropertyValue... nbt)
    {
        if (nbt.length != this.nbt.size())
        {
            return false;
        }

        for (JsonPropertyValue prop : nbt)
        {
            if (!this.nbt.containsKey(prop.name))
            {
                return false;
            } else if (!prop.value.equals(this.nbt.get(prop.name)))
            {
                return false;
            }
        }

        return true;
    }
}