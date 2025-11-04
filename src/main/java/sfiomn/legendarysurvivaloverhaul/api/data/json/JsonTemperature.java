package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JsonTemperature
{
    public static final Codec<JsonTemperature> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.FLOAT.fieldOf("temperature").forGetter(d -> d.temperature)
    ).apply(inst, JsonTemperature::new));

    public float temperature;

    public JsonTemperature(float temperature)
    {
        this.temperature = temperature;
    }
}
