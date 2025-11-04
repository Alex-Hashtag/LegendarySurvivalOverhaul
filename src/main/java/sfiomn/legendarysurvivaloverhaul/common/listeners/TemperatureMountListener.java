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
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureMountManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncTemperatureMountsPayload;

import java.util.HashMap;
import java.util.Map;

public class TemperatureMountListener extends SimpleJsonResourceReloadListener implements ITemperatureMountManager
{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonTemperatureResistance> TEMPERATURE_MOUNTS = new HashMap<>();

    public TemperatureMountListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/mounts");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncTemperatureMountsPayload(TEMPERATURE_MOUNTS));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncTemperatureMountsPayload(TEMPERATURE_MOUNTS));
        }
    }

    public static void acceptServerTemperatureMounts(Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts)
    {
        TEMPERATURE_MOUNTS.clear();
        TEMPERATURE_MOUNTS.putAll(temperatureMounts);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        TEMPERATURE_MOUNTS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonTemperatureResistance.CODEC.parse(JsonOps.INSTANCE, json);
                JsonTemperatureResistance temperature = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing temperature mount: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    TEMPERATURE_MOUNTS.put(key, temperature);
            } catch (JsonParseException | IllegalStateException error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature mount json {}", key, error);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature mounts", TEMPERATURE_MOUNTS.size());
    }

    @Override
    public JsonTemperatureResistance get(ResourceLocation mountRegistryName)
    {
        return TEMPERATURE_MOUNTS.get(mountRegistryName);
    }
}
