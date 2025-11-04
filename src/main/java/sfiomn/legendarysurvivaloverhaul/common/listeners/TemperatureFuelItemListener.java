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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureFuelItem;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureFuelItemManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncTemperatureFuelItemsPayload;

import java.util.HashMap;
import java.util.Map;

public class TemperatureFuelItemListener extends SimpleJsonResourceReloadListener implements ITemperatureFuelItemManager
{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonTemperatureFuelItem> TEMPERATURE_FUEL_ITEMS = new HashMap<>();

    public TemperatureFuelItemListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/fuel_items");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncTemperatureFuelItemsPayload(TEMPERATURE_FUEL_ITEMS));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncTemperatureFuelItemsPayload(TEMPERATURE_FUEL_ITEMS));
        }
    }

    public static void acceptServerTemperatureFuelItems(Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems)
    {
        TEMPERATURE_FUEL_ITEMS.clear();
        TEMPERATURE_FUEL_ITEMS.putAll(temperatureFuelItems);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        TEMPERATURE_FUEL_ITEMS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonTemperatureFuelItem.CODEC.parse(JsonOps.INSTANCE, json);
                JsonTemperatureFuelItem temperatures = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing temperature fuel item: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    TEMPERATURE_FUEL_ITEMS.put(key, temperatures);
            } catch (JsonParseException | IllegalStateException error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature fuel item json {}", key, error);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature fuel items", TEMPERATURE_FUEL_ITEMS.size());
    }

    @Override
    public JsonTemperatureFuelItem get(ResourceLocation itemRegistryName)
    {
        return TEMPERATURE_FUEL_ITEMS.get(itemRegistryName);
    }
}
