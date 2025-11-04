package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonThirstBlock
{
    public static final Codec<JsonThirstBlock> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.INT.fieldOf("hydration").forGetter(c -> c.hydration),
            Codec.FLOAT.fieldOf("saturation").forGetter(c -> c.saturation),
            JsonMobEffect.LIST_CODEC.optionalFieldOf("effects", new ArrayList<>()).forGetter(c -> c.effects),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", new HashMap<>()).forGetter(c -> c.properties)
    ).apply(inst, JsonThirstBlock::new));

    public static final Codec<List<JsonThirstBlock>> LIST_CODEC = CODEC.listOf();

    public int hydration;
    public float saturation;
    public List<JsonMobEffect> effects;
    public Map<String, String> properties;

    public JsonThirstBlock(int hydration, float saturation, List<JsonMobEffect> effects, Map<String, String> properties)
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


    public boolean matchesState(BlockState blockState)
    {
        for (Property<?> property : blockState.getProperties())
        {
            String name = property.getName();

            if (properties.containsKey(name))
            {
                String stateValue = blockState.getValue(property).toString();

                if (!properties.get(name).equalsIgnoreCase(stateValue))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean matchesState(FluidState fluidState)
    {
        for (Property<?> property : fluidState.getProperties())
        {
            String name = property.getName();

            if (properties.containsKey(name))
            {
                String stateValue = fluidState.getValue(property).toString();

                if (!properties.get(name).equalsIgnoreCase(stateValue))
                {
                    return false;
                }
            }
        }

        return true;
    }
}
