package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.registries.DeferredHolder;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonTemperatureConsumableBlock
{
    public static final Codec<JsonTemperatureConsumableBlock> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.STRING.fieldOf("group").forGetter(c -> c.group.name()),
            Codec.INT.fieldOf("temperature_level").forGetter(c -> c.temperatureLevel),
            Codec.INT.fieldOf("duration").forGetter(c -> c.duration),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", new HashMap<>()).forGetter(c -> c.properties)
    ).apply(inst, JsonTemperatureConsumableBlock::new));

    public static final Codec<List<JsonTemperatureConsumableBlock>> LIST_CODEC = CODEC.listOf();

    public TemporaryModifierGroupEnum group;
    public int temperatureLevel;
    public int duration;
    public Map<String, String> properties;
    private DeferredHolder<MobEffect, ? extends MobEffect> effect;
    private DeferredHolder<MobEffect, ? extends MobEffect> oppositeEffect;

    public JsonTemperatureConsumableBlock(String group, int temperatureLevel, int duration, Map<String, String> properties)
    {

        this.temperatureLevel = temperatureLevel;
        this.duration = duration;
        this.group = TemporaryModifierGroupEnum.get(group);

        this.properties = new HashMap<>();
        this.properties.putAll(properties);

        this.effect = null;
        this.oppositeEffect = null;
        if (temperatureLevel > 0)
        {
            this.effect = this.group.hotEffect;
            this.oppositeEffect = this.group.coldEffect;
        } else if (temperatureLevel < 0)
        {
            this.effect = this.group.coldEffect;
            this.oppositeEffect = this.group.hotEffect;
        }
    }

    public MobEffect getEffect()
    {
        return this.effect.get();
    }

    public MobEffect getOppositeEffect()
    {
        return this.oppositeEffect.get();
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

    public DeferredHolder<MobEffect, ? extends MobEffect> getEffectHolder()
    {
        return this.effect;
    }

    public DeferredHolder<MobEffect, ? extends MobEffect> getOppositeEffectHolder()
    {
        return this.oppositeEffect;
    }
}
