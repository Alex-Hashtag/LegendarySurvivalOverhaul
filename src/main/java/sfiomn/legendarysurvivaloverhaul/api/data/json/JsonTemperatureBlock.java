package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTemperatureBlock
{
    public static final Codec<JsonTemperatureBlock> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.FLOAT.fieldOf("temperature").forGetter(c -> c.temperature),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", new HashMap<>()).forGetter(c -> c.properties)
    ).apply(inst, JsonTemperatureBlock::new));

    public static final Codec<List<JsonTemperatureBlock>> LIST_CODEC = CODEC.listOf();

    public float temperature;
    public Map<String, String> properties;

    public JsonTemperatureBlock(float temperature, Map<String, String> properties)
    {

        this.temperature = temperature;

        this.properties = new HashMap<>();
        this.properties.putAll(properties);
    }

    public boolean isDefault()
    {
        return this.properties.isEmpty();
    }

    public boolean matchesBlockEntity(BlockEntity blockEntity)
    {
        CompoundTag blockEntityTag = blockEntity.saveWithFullMetadata(blockEntity.getLevel().registryAccess());
        for (Map.Entry<String, String> property : properties.entrySet())
        {
            String name = property.getKey();

            if (blockEntityTag.contains(name))
            {
                String stateValue = String.valueOf(blockEntityTag.get(name));

                if (!property.getValue().equalsIgnoreCase(stateValue))
                {
                    return false;
                }
            } else
            {
                return false;
            }
        }

        return true;
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
