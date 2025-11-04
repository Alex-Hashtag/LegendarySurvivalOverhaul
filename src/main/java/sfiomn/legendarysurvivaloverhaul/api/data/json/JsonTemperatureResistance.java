package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;


public class JsonTemperatureResistance
{
    public static final Codec<JsonTemperatureResistance> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.FLOAT.optionalFieldOf("temperature", 0.0f).forGetter(c -> c.temperature),
            Codec.FLOAT.optionalFieldOf("heat_resistance", 0.0f).forGetter(c -> c.heatResistance),
            Codec.FLOAT.optionalFieldOf("cold_resistance", 0.0f).forGetter(c -> c.coldResistance),
            Codec.FLOAT.optionalFieldOf("thermal_resistance", 0.0f).forGetter(c -> c.thermalResistance)
    ).apply(inst, JsonTemperatureResistance::new));

    public float temperature;
    public float heatResistance;
    public float coldResistance;
    public float thermalResistance;

    public JsonTemperatureResistance()
    {
        this(0, 0, 0, 0);
    }

    public JsonTemperatureResistance(float temperature)
    {
        this(temperature, 0, 0, 0);
    }

    public JsonTemperatureResistance(float temperature, float heatResistance, float coldResistance, float thermalResistance)
    {
        this.temperature = temperature;
        this.heatResistance = heatResistance;
        this.coldResistance = coldResistance;
        this.thermalResistance = thermalResistance;
    }

    public void add(JsonTemperatureResistance config)
    {
        this.temperature += config.temperature;
        this.heatResistance += config.heatResistance;
        this.coldResistance += config.coldResistance;
        this.thermalResistance += config.thermalResistance;
    }
}
