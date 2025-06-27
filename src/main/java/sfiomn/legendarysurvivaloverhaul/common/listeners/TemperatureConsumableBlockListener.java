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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumableBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureConsumableBlockManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureConsumableBlocksPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemperatureConsumableBlockListener extends SimpleJsonResourceReloadListener implements ITemperatureConsumableBlockManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> TEMPERATURE_CONSUMABLE_BLOCKS = new HashMap<>();

    public TemperatureConsumableBlockListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/consumable_blocks");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        TEMPERATURE_CONSUMABLE_BLOCKS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonTemperatureConsumableBlock.LIST_CODEC.parse(JsonOps.INSTANCE, json);
                List<JsonTemperatureConsumableBlock> temperatures = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing temperature consumable block : {}", error));
                if (ModList.get().isLoaded(key.getNamespace()))
                    TEMPERATURE_CONSUMABLE_BLOCKS.put(key, temperatures);
            } catch (JsonParseException error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature consumable block json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature consumable blocks", TEMPERATURE_CONSUMABLE_BLOCKS.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncTemperatureConsumableBlocksPacket.sendTo(packetTarget, TEMPERATURE_CONSUMABLE_BLOCKS);
    }

    public static void acceptServerTemperatureConsumableBlocks(Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumableBlocks) {
        TEMPERATURE_CONSUMABLE_BLOCKS.clear();
        TEMPERATURE_CONSUMABLE_BLOCKS.putAll(temperatureConsumableBlocks);
    }

    @Override
    public List<JsonTemperatureConsumableBlock> get(ResourceLocation itemRegistryName) {
        return TEMPERATURE_CONSUMABLE_BLOCKS.get(itemRegistryName);
    }
}