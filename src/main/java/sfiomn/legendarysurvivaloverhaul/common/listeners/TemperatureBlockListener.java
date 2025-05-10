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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureBlockManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureBlocksPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemperatureBlockListener extends SimpleJsonResourceReloadListener implements ITemperatureBlockManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, List<JsonTemperatureBlock>> TEMPERATURE_BLOCKS = new HashMap<>();

    public TemperatureBlockListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/blocks");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        TEMPERATURE_BLOCKS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonTemperatureBlock.LIST_CODEC.parse(JsonOps.INSTANCE, json);
                List<JsonTemperatureBlock> temperatures = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing temperature block : {}", error));
                if (ModList.get().isLoaded(key.getNamespace()))
                    TEMPERATURE_BLOCKS.put(key, temperatures);
            } catch (JsonParseException error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse temperature block json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature blocks", TEMPERATURE_BLOCKS.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncTemperatureBlocksPacket.sendTo(packetTarget, TEMPERATURE_BLOCKS);
    }

    public static void acceptServerTemperatureBlocks(Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks) {
        TEMPERATURE_BLOCKS.clear();
        TEMPERATURE_BLOCKS.putAll(temperatureBlocks);
    }

    @Override
    public List<JsonTemperatureBlock> get(ResourceLocation itemRegistryName) {
        return TEMPERATURE_BLOCKS.get(itemRegistryName);
    }
}