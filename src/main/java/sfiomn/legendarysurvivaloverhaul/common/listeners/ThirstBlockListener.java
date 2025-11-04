package sfiomn.legendarysurvivaloverhaul.common.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IThirstBlockManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncThirstBlocksPayload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirstBlockListener extends SimpleJsonResourceReloadListener implements IThirstBlockManager
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, List<JsonThirstBlock>> THIRST_BLOCKS = new HashMap<>();

    public ThirstBlockListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/thirst/blocks");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncThirstBlocksPayload(THIRST_BLOCKS));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncThirstBlocksPayload(THIRST_BLOCKS));
        }
    }

    public static void acceptServerThirstBlocks(Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks)
    {
        THIRST_BLOCKS.clear();
        THIRST_BLOCKS.putAll(thirstBlocks);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        THIRST_BLOCKS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonThirstBlock.LIST_CODEC.parse(JsonOps.INSTANCE, json);
                List<JsonThirstBlock> parsedThirstBlocks = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing thirst block: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    THIRST_BLOCKS.put(key, parsedThirstBlocks);
            } catch (Exception error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse thirst block json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} thirst blocks", THIRST_BLOCKS.size());
    }

    @Override
    public List<JsonThirstBlock> get(ResourceLocation resourceLocation)
    {
        return THIRST_BLOCKS.get(resourceLocation);
    }

    @Override
    public JsonThirstBlock get(BlockState block)
    {
        List<JsonThirstBlock> jsonThirstBlocks = null;
        JsonThirstBlock defaultJct = null;

        ResourceLocation blockRegistryName = BuiltInRegistries.BLOCK.getKey(block.getBlock());

        if (blockRegistryName != null)
            jsonThirstBlocks = THIRST_BLOCKS.get(blockRegistryName);

        if (jsonThirstBlocks != null)
            for (JsonThirstBlock jtb : jsonThirstBlocks)
            {
                if (jtb.matchesState(block))
                    return jtb;
                if (jtb.isDefault())
                    defaultJct = jtb;
            }

        return defaultJct;
    }

    @Override
    public JsonThirstBlock get(FluidState fluid)
    {
        List<JsonThirstBlock> jsonThirstBlocks = null;
        JsonThirstBlock defaultJct = null;

        ResourceLocation fluidRegistryName = BuiltInRegistries.FLUID.getKey(fluid.getType());

        if (fluidRegistryName != null)
            jsonThirstBlocks = THIRST_BLOCKS.get(fluidRegistryName);

        if (jsonThirstBlocks != null)
            for (JsonThirstBlock jtb : jsonThirstBlocks)
            {
                if (jtb.matchesState(fluid))
                    return jtb;
                if (jtb.isDefault())
                    defaultJct = jtb;
            }

        return defaultJct;
    }
}
