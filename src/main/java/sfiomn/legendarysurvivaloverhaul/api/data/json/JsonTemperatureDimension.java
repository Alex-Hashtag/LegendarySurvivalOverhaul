package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JsonTemperatureDimension {
    public static final Codec<JsonTemperatureDimension> CODEC = RecordCodecBuilder.<JsonTemperatureDimension>create((inst) -> inst.group(
            Codec.FLOAT.fieldOf("temperature").forGetter(d -> d.temperature),
            Codec.INT.fieldOf("sea_level_height").forGetter(d -> d.seaLevelHeight),
            Codec.BOOL.fieldOf("has_altitude").forGetter(d -> d.hasAltitude)
    ).apply(inst, JsonTemperatureDimension::new));

    public float temperature;
    public int seaLevelHeight;
    public boolean hasAltitude;

    public JsonTemperatureDimension(float temperature, int seaLevelHeight, boolean hasAltitude) {
        this.temperature = temperature;
        this.seaLevelHeight = seaLevelHeight;
        this.hasAltitude = hasAltitude;
    }
}
