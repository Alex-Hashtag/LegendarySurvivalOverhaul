package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonThirstConsumable
{
    public static final Codec<JsonThirstConsumable> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.INT.fieldOf("hydration").forGetter(c -> c.hydration),
            Codec.FLOAT.fieldOf("saturation").forGetter(c -> c.saturation),
            JsonMobEffect.LIST_CODEC.optionalFieldOf("effects", new ArrayList<>()).forGetter(c -> c.effects),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", new HashMap<>()).forGetter(c -> c.properties)
    ).apply(inst, JsonThirstConsumable::new));

    public static final Codec<List<JsonThirstConsumable>> LIST_CODEC = CODEC.listOf();

    public int hydration;
    public float saturation;
    public List<JsonMobEffect> effects;
    public Map<String, String> properties;

    public JsonThirstConsumable(int hydration, float saturation, List<JsonMobEffect> effects, Map<String, String> properties)
    {

        this.hydration = hydration;
        this.saturation = saturation;

        this.effects = new ArrayList<>();
        this.effects.addAll(effects);

        this.properties = new HashMap<>();
        this.properties.putAll(properties);
    }

    public boolean isDefault()
    {
        return this.properties.isEmpty();
    }

    //  1.21 moved the potion type out of item NBT and into the POTION_CONTENTS data component.
    //  Re-expose it under the legacy "Potion" key so the thirst data files keep matching.
    private static CompoundTag buildLookupTag(ItemStack itemStack)
    {
        CustomData custom = itemStack.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag = custom != null ? custom.copyTag() : new CompoundTag();

        if (!tag.contains("Potion"))
        {
            PotionContents potionContents = itemStack.get(DataComponents.POTION_CONTENTS);
            if (potionContents != null)
            {
                tag.putString("Potion", potionContents.potion()
                        .flatMap(Holder::unwrapKey)
                        .map(key -> key.location().toString())
                        .orElse("minecraft:empty"));
            }
        }

        return tag;
    }

    public boolean matchesNbt(ItemStack itemStack)
    {
        CompoundTag itemStackTag = buildLookupTag(itemStack);
        boolean hasData = !itemStackTag.isEmpty();

        //  An entry with no properties only matches items carrying no data, and vice versa.
        if (hasData == properties.isEmpty())
            return false;

        if (!hasData)
            return true;

        for (Map.Entry<String, String> nbtEntry : properties.entrySet())
        {
            if (!itemStackTag.contains(nbtEntry.getKey()))
                return false;

            byte tagType = itemStackTag.getTagType(nbtEntry.getKey());
            //  String type
            if (tagType == 8)
            {
                if (!itemStackTag.getString(nbtEntry.getKey()).equals(nbtEntry.getValue()))
                    return false;

                //  Numerical type
            } else if (tagType == 1 || tagType == 2 || tagType == 3 || tagType == 4 || tagType == 5 || tagType == 6)
            {
                if (itemStackTag.getDouble(nbtEntry.getKey()) != Double.parseDouble(nbtEntry.getValue()))
                    return false;
            } else
                LegendarySurvivalOverhaul.LOGGER.error("Error while matching nbt for {} : Tag type {} not taken into account.\n" +
                        "It can either be a String (tag type 8) or a numeric (tag type in [1-6])", itemStack.getDescriptionId(), tagType);

        }

        return true;
    }
}