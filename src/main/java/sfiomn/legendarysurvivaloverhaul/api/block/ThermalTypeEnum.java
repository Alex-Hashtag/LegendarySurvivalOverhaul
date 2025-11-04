package sfiomn.legendarysurvivaloverhaul.api.block;

public enum ThermalTypeEnum
{
    COOLING(-1.0f),
    HEATING(1.0f),
    BROKEN(0f);

    private final float temperature;

    ThermalTypeEnum(float temperature)
    {
        this.temperature = temperature;
    }

    public static ThermalTypeEnum get(String name)
    {
        for (ThermalTypeEnum t : values())
            if (t.name().equalsIgnoreCase(name)) return t;
        throw new IllegalArgumentException();
    }

    public float getTemperatureLevel()
    {
        return temperature;
    }
}
