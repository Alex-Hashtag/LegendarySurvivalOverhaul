package sfiomn.legendarysurvivaloverhaul.common.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureDimensionManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureDimensionsPacket;

import java.util.HashMap;
import java.util.Map;

public class TemperatureDimensionListener extends SimpleJsonResourceReloadListener implements ITemperatureDimensionManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonTemperature> TEMPERATURE_DIMENSIONS = new HashMap<>();

    public TemperatureDimensionListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/dimensions");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        TEMPERATURE_DIMENSIONS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonTemperature.CODEC.parse(JsonOps.INSTANCE, json);
                JsonTemperature temperature = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing temperature dimension : {}", error));
                if (ModList.get().isLoaded(key.getNamespace()))
                    TEMPERATURE_DIMENSIONS.put(key, temperature);
            } catch (JsonParseException error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature dimension json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature dimensions", TEMPERATURE_DIMENSIONS.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncTemperatureDimensionsPacket.sendTo(packetTarget, TEMPERATURE_DIMENSIONS);
    }

    public static void acceptServerTemperatureDimensions(Map<ResourceLocation, JsonTemperature> temperatureDimensions) {
        TEMPERATURE_DIMENSIONS.clear();
        TEMPERATURE_DIMENSIONS.putAll(temperatureDimensions);
    }

    @Override
    public JsonTemperature get(ResourceLocation dimensionRegistryName) {
        return TEMPERATURE_DIMENSIONS.get(dimensionRegistryName);
    }
}