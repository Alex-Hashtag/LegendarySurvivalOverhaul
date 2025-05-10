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
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureMountManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureMountsPacket;

import java.util.HashMap;
import java.util.Map;

public class TemperatureMountListener extends SimpleJsonResourceReloadListener implements ITemperatureMountManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonTemperature> TEMPERATURE_MOUNTS = new HashMap<>();

    public TemperatureMountListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/mounts");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        TEMPERATURE_MOUNTS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonTemperature.CODEC.parse(JsonOps.INSTANCE, json);
                JsonTemperature temperature = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing temperature mount : {}", error));
                if (ModList.get().isLoaded(key.getNamespace()))
                    TEMPERATURE_MOUNTS.put(key, temperature);
            } catch (JsonParseException error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature mount json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature mounts", TEMPERATURE_MOUNTS.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncTemperatureMountsPacket.sendTo(packetTarget, TEMPERATURE_MOUNTS);
    }

    public static void acceptServerTemperatureMounts(Map<ResourceLocation, JsonTemperature> temperatureMounts) {
        TEMPERATURE_MOUNTS.clear();
        TEMPERATURE_MOUNTS.putAll(temperatureMounts);
    }

    @Override
    public JsonTemperature get(ResourceLocation mountRegistryName) {
        return TEMPERATURE_MOUNTS.get(mountRegistryName);
    }
}