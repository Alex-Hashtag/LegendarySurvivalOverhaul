package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class JsonMobEffect
{
    public static final Codec<JsonMobEffect> CODEC = RecordCodecBuilder.create(
            (inst) -> inst.group(
                    Codec.STRING.fieldOf("effect").forGetter(m -> m.name),
                    Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(m -> m.chance),
                    Codec.INT.fieldOf("duration").forGetter(m -> m.duration),
                    Codec.INT.optionalFieldOf("amplifier", 0).forGetter(m -> m.amplifier)
            ).apply(inst, JsonMobEffect::new));

    public static final Codec<List<JsonMobEffect>> LIST_CODEC = CODEC.listOf();

    public float chance;
    public String name;
    public int duration;
    public int amplifier;

    public JsonMobEffect(String name, float chance, int duration, int amplifier)
    {
        this.name = name;
        this.chance = chance < 0 ? 0 : chance > 1 ? 1 : chance;
        this.duration = Math.max(duration, 0);
        this.amplifier = Math.max(amplifier, 0);
    }
}
