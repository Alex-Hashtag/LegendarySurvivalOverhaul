package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;

public class JsonTemperatureFuelItem
{
    public static final Codec<JsonTemperatureFuelItem> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.STRING.fieldOf("thermal_type").forGetter(d -> d.thermalType.name()),
            Codec.INT.fieldOf("duration").forGetter(d -> d.duration)
    ).apply(inst, JsonTemperatureFuelItem::new));

    public ThermalTypeEnum thermalType;
    public int duration;

    public JsonTemperatureFuelItem(String thermalType, int duration)
    {
        this.thermalType = ThermalTypeEnum.get(thermalType);
        this.duration = duration;
    }
}
