package sfiomn.legendarysurvivaloverhaul.common.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureOriginManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncTemperatureOriginsPayload;

import java.util.HashMap;
import java.util.Map;

public class TemperatureOriginListener extends SimpleJsonResourceReloadListener implements ITemperatureOriginManager
{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonTemperatureResistance> TEMPERATURE_ORIGINS = new HashMap<>();

    public TemperatureOriginListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/origins");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncTemperatureOriginsPayload(TEMPERATURE_ORIGINS));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncTemperatureOriginsPayload(TEMPERATURE_ORIGINS));
        }
    }

    public static void acceptServerTemperatureOrigins(Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins)
    {
        TEMPERATURE_ORIGINS.clear();
        TEMPERATURE_ORIGINS.putAll(temperatureOrigins);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        TEMPERATURE_ORIGINS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonTemperatureResistance.CODEC.parse(JsonOps.INSTANCE, json);
                JsonTemperatureResistance temperatures = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing temperature origin: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    TEMPERATURE_ORIGINS.put(key, temperatures);
            } catch (JsonParseException | IllegalStateException error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature origin json {}", key, error);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature origins", TEMPERATURE_ORIGINS.size());
    }

    @Override
    public JsonTemperatureResistance get(ResourceLocation originRegistryName)
    {
        return TEMPERATURE_ORIGINS.get(originRegistryName);
    }
}
