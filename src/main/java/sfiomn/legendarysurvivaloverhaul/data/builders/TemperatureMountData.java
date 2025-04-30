package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.ITemperatureData;

public class TemperatureMountData implements ITemperatureData {
    private float temperature;

    public TemperatureMountData() {
    }

    @Override
    public ITemperatureData temperature(float temperatureValue) {
        temperature = temperatureValue;
        return this;
    }

    @Override
    public JsonObject build() {
        JsonObject json = new JsonObject();
        json.addProperty("temperature", this.temperature);
        return json;
    }
}
