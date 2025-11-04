package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JsonTemperatureBiomeOverride
{
    public static final Codec<JsonTemperatureBiomeOverride> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.FLOAT.fieldOf("temperature").forGetter(d -> d.temperature),
            Codec.BOOL.fieldOf("is_dry").forGetter(d -> d.isDry)
    ).apply(inst, JsonTemperatureBiomeOverride::new));

    public float temperature;
    public boolean isDry;

    public JsonTemperatureBiomeOverride(float temperature, boolean isDry)
    {
        this.temperature = temperature;
        this.isDry = isDry;
    }
}
