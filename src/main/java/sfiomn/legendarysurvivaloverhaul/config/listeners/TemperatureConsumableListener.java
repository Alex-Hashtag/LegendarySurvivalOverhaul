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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureConsumableManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureConsumablesPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemperatureConsumableListener extends SimpleJsonResourceReloadListener implements ITemperatureConsumableManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, List<JsonTemperatureConsumable>> TEMPERATURE_CONSUMABLES = new HashMap<>();

    public TemperatureConsumableListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/consumables");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        TEMPERATURE_CONSUMABLES.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonTemperatureConsumable.LIST_CODEC.parse(JsonOps.INSTANCE, json);
                List<JsonTemperatureConsumable> temperatures = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing temperature consumable : {}", error));
                TEMPERATURE_CONSUMABLES.put(key, temperatures);
            } catch (JsonParseException error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature consumable json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature consumables", TEMPERATURE_CONSUMABLES.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncTemperatureConsumablesPacket.sendTo(packetTarget, TEMPERATURE_CONSUMABLES);
    }

    public static void acceptServerTemperatureConsumables(Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables) {
        TEMPERATURE_CONSUMABLES.clear();
        TEMPERATURE_CONSUMABLES.putAll(temperatureConsumables);
    }

    @Override
    public List<JsonTemperatureConsumable> get(ResourceLocation itemRegistryName) {
        return TEMPERATURE_CONSUMABLES.get(itemRegistryName);
    }
}