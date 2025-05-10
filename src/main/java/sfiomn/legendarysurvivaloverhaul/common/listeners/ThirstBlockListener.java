package sfiomn.legendarysurvivaloverhaul.common.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IThirstBlockManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncThirstBlocksPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirstBlockListener extends SimpleJsonResourceReloadListener implements IThirstBlockManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, List<JsonThirstBlock>> THIRST_BLOCKS = new HashMap<>();

    public ThirstBlockListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/thirst/blocks");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        THIRST_BLOCKS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonThirstBlock.LIST_CODEC.parse(JsonOps.INSTANCE, json);
                List<JsonThirstBlock> parsedThirstBlocks = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing thirst block : {}", error));
                if (ModList.get().isLoaded(key.getNamespace()))
                    THIRST_BLOCKS.put(key, parsedThirstBlocks);
            } catch (Exception error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse thirst block json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} thirst blocks", THIRST_BLOCKS.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncThirstBlocksPacket.sendTo(packetTarget, THIRST_BLOCKS);
    }

    public static void acceptServerThirstBlocks(Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks) {
        THIRST_BLOCKS.clear();
        THIRST_BLOCKS.putAll(thirstBlocks);
    }

    @Override
    public List<JsonThirstBlock> get(ResourceLocation resourceLocation) {
        return THIRST_BLOCKS.get(resourceLocation);
    }
}
