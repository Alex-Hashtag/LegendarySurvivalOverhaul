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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureDimension;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureDimensionManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncTemperatureDimensionsPayload;

import java.util.HashMap;
import java.util.Map;

public class TemperatureDimensionListener extends SimpleJsonResourceReloadListener implements ITemperatureDimensionManager
{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonTemperatureDimension> TEMPERATURE_DIMENSIONS = new HashMap<>();

    public TemperatureDimensionListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/dimensions");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncTemperatureDimensionsPayload(TEMPERATURE_DIMENSIONS));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncTemperatureDimensionsPayload(TEMPERATURE_DIMENSIONS));
        }
    }

    public static void acceptServerTemperatureDimensions(Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions)
    {
        TEMPERATURE_DIMENSIONS.clear();
        TEMPERATURE_DIMENSIONS.putAll(temperatureDimensions);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        TEMPERATURE_DIMENSIONS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonTemperatureDimension.CODEC.parse(JsonOps.INSTANCE, json);
                JsonTemperatureDimension temperatureDimension = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing temperature dimension: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    TEMPERATURE_DIMENSIONS.put(key, temperatureDimension);
            } catch (JsonParseException | IllegalStateException error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature dimension json {}", key, error);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature dimensions", TEMPERATURE_DIMENSIONS.size());
    }

    @Override
    public JsonTemperatureDimension get(ResourceLocation dimensionRegistryName)
    {
        return TEMPERATURE_DIMENSIONS.get(dimensionRegistryName);
    }
}
