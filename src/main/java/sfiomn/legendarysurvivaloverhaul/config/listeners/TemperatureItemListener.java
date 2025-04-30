package sfiomn.legendarysurvivaloverhaul.config.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureItemManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureDimensionsPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureItemsPacket;

import java.util.HashMap;
import java.util.Map;

public class TemperatureItemListener extends SimpleJsonResourceReloadListener implements ITemperatureItemManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonTemperatureResistance> TEMPERATURE_ITEMS = new HashMap<>();

    public TemperatureItemListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/items");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        TEMPERATURE_ITEMS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonTemperatureResistance.CODEC.parse(JsonOps.INSTANCE, json);
                JsonTemperatureResistance temperatures = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing temperature item : {}", error));
                TEMPERATURE_ITEMS.put(key, temperatures);
            } catch (JsonParseException error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature item json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature items", TEMPERATURE_ITEMS.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncTemperatureItemsPacket.sendTo(packetTarget, TEMPERATURE_ITEMS);
    }

    public static void acceptServerTemperatureItems(Map<ResourceLocation, JsonTemperatureResistance> temperatureItems) {
        TEMPERATURE_ITEMS.clear();
        TEMPERATURE_ITEMS.putAll(temperatureItems);
    }

    @Override
    public JsonTemperatureResistance get(ResourceLocation itemRegistryName) {
        return TEMPERATURE_ITEMS.get(itemRegistryName);
    }
}