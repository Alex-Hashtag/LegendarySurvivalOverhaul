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
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureItemManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncTemperatureItemsPayload;

import java.util.HashMap;
import java.util.Map;

public class TemperatureItemListener extends SimpleJsonResourceReloadListener implements ITemperatureItemManager
{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonTemperatureResistance> TEMPERATURE_ITEMS = new HashMap<>();

    public TemperatureItemListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/items");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncTemperatureItemsPayload(TEMPERATURE_ITEMS));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncTemperatureItemsPayload(TEMPERATURE_ITEMS));
        }
    }

    public static void acceptServerTemperatureItems(Map<ResourceLocation, JsonTemperatureResistance> temperatureItems)
    {
        TEMPERATURE_ITEMS.clear();
        TEMPERATURE_ITEMS.putAll(temperatureItems);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        TEMPERATURE_ITEMS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonTemperatureResistance.CODEC.parse(JsonOps.INSTANCE, json);
                JsonTemperatureResistance temperatures = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing temperature item: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    TEMPERATURE_ITEMS.put(key, temperatures);
            } catch (JsonParseException | IllegalStateException error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature item json {}", key, error);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature items", TEMPERATURE_ITEMS.size());
    }

    @Override
    public JsonTemperatureResistance get(ResourceLocation itemRegistryName)
    {
        return TEMPERATURE_ITEMS.get(itemRegistryName);
    }
}
