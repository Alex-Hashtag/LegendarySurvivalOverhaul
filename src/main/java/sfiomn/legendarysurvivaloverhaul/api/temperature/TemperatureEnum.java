package sfiomn.legendarysurvivaloverhaul.api.temperature;

// Stolen shamelessly from Charles445's SimpleDifficulty mod
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/api/temperature/TemperatureEnum.java
/// Every value uses the avarage of the 2 values for the bound!!!
public enum TemperatureEnum
{
    FROSTBITE(5),
    COLD(13),
    NORMAL(20),
    HOT(27),
    HEAT_STROKE(35);

    private final float value;

    TemperatureEnum(float value)
    {
        this.value = value;
    }

    public static TemperatureEnum get(float temperature) {
        if (temperature < (FROSTBITE.value + COLD.value) / 2)
            return FROSTBITE;
        else if (temperature < (COLD.value + NORMAL.value) / 2)
            return COLD;
        else if (temperature < (NORMAL.value + HOT.value) / 2)
            return NORMAL;
        else if (temperature < (HOT.value + HEAT_STROKE.value) / 2)
            return HOT;
        else
            return HEAT_STROKE;
    }

    public float getValue()
    {
        return this.value;
    }
}
