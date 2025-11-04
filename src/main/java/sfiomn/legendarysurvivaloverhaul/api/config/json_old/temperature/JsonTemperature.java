package sfiomn.legendarysurvivaloverhaul.api.config.json_old.temperature;

import com.google.gson.annotations.SerializedName;

/**
 * Code taken and adapted from Charles445's SimpleDifficulty mod
 *
 * @author Charles445
 * @author Icey
 * @see <a href="https://github.com/Charles445/SimpleDifficulty/tree/master/src/main/java/com/charles445/simpledifficulty/api/config/json">Github Link</a>
 */

public class JsonTemperature
{
    @SerializedName("temperature")
    public float temperature;

    public JsonTemperature(float temperature)
    {
        this.temperature = temperature;
    }
}
