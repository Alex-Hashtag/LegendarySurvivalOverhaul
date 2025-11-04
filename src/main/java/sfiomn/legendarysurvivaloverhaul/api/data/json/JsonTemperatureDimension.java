package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JsonTemperatureDimension
{
    public static final Codec<JsonTemperatureDimension> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.FLOAT.fieldOf("temperature").forGetter(d -> d.temperature),
            Codec.INT.optionalFieldOf("sea_level_height", 64).forGetter(d -> d.seaLevelHeight),
            Codec.BOOL.optionalFieldOf("has_altitude", true).forGetter(d -> d.hasAltitude)
    ).apply(inst, JsonTemperatureDimension::new));

    public float temperature;
    public int seaLevelHeight;
    public boolean hasAltitude;

    public JsonTemperatureDimension(float temperature, int seaLevelHeight, boolean hasAltitude)
    {
        this.temperature = temperature;
        this.seaLevelHeight = seaLevelHeight;
        this.hasAltitude = hasAltitude;
    }
}
