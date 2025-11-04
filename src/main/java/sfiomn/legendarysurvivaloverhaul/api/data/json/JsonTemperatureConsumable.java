package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.List;

public class JsonTemperatureConsumable
{
    public static final Codec<JsonTemperatureConsumable> CODEC =
            RecordCodecBuilder.create(inst -> inst.group(
                    Codec.STRING.fieldOf("group").forGetter(c -> c.group.name()),
                    Codec.INT.fieldOf("temperature_level").forGetter(c -> c.temperatureLevel),
                    Codec.INT.fieldOf("duration").forGetter(c -> c.duration)
            ).apply(inst, JsonTemperatureConsumable::new));

    public static final Codec<List<JsonTemperatureConsumable>> LIST_CODEC = CODEC.listOf();

    public TemporaryModifierGroupEnum group;
    public int temperatureLevel;
    public int duration;
    private DeferredHolder<MobEffect, MobEffect> effect;
    private DeferredHolder<MobEffect, MobEffect> oppositeEffect;

    public JsonTemperatureConsumable(String group, int temperatureLevel, int duration)
    {
        this.temperatureLevel = temperatureLevel;
        this.duration = duration;
        this.group = TemporaryModifierGroupEnum.get(group);
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

    public DeferredHolder<MobEffect, MobEffect> getEffectHolder()
    {
        return this.effect;
    }

    public DeferredHolder<MobEffect, MobEffect> getOppositeEffectHolder()
    {
        return this.oppositeEffect;
    }
}
