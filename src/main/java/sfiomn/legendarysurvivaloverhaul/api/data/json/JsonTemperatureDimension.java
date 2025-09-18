package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JsonTemperatureDimension {
    public static final Codec<JsonTemperatureDimension> CODEC = RecordCodecBuilder.<JsonTemperatureDimension>create((inst) -> inst.group(
            Codec.FLOAT.fieldOf("temperature").forGetter(d -> d.temperature),
            Codec.INT.optionalFieldOf("sea_level_height", 64).forGetter(d -> d.seaLevelHeight),
            Codec.BOOL.optionalFieldOf("has_altitude", true).forGetter(d -> d.hasAltitude),
            Codec.INT.optionalFieldOf("temperatureTimeCycleTicks", 0).forGetter(d -> d.temperatureTimeCycleTicks)
    ).apply(inst, JsonTemperatureDimension::new));

    public float temperature;
    public int seaLevelHeight;
    public boolean hasAltitude;
    public int temperatureTimeCycleTicks;

    public JsonTemperatureDimension(float temperature, int seaLevelHeight, boolean hasAltitude, int temperatureTimeCycleTicks) {
        this.temperature = temperature;
        this.seaLevelHeight = seaLevelHeight;
        this.hasAltitude = hasAltitude;
        this.temperatureTimeCycleTicks = Math.max(temperatureTimeCycleTicks, 0);
    }
}
