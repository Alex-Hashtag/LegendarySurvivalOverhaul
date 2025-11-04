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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IBodyResistanceItemManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncBodyPartResistanceItemsPayload;

import java.util.HashMap;
import java.util.Map;

public class BodyPartResistanceItemListener extends SimpleJsonResourceReloadListener implements IBodyResistanceItemManager
{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonBodyPartResistance> BODY_PART_RESISTANCE_ITEMS = new HashMap<>();

    public BodyPartResistanceItemListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/body_damage/items");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncBodyPartResistanceItemsPayload(BODY_PART_RESISTANCE_ITEMS));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncBodyPartResistanceItemsPayload(BODY_PART_RESISTANCE_ITEMS));
        }
    }

    public static void acceptServerBodyPartResistanceItems(Map<ResourceLocation, JsonBodyPartResistance> temperatureItems)
    {
        BODY_PART_RESISTANCE_ITEMS.clear();
        BODY_PART_RESISTANCE_ITEMS.putAll(temperatureItems);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        BODY_PART_RESISTANCE_ITEMS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonBodyPartResistance.CODEC.parse(JsonOps.INSTANCE, json);
                JsonBodyPartResistance bodyPartResistance = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing body part resistance item: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    BODY_PART_RESISTANCE_ITEMS.put(key, bodyPartResistance);
            } catch (JsonParseException | IllegalStateException error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse body part resistance item json {}", key, error);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} body part resistance items", BODY_PART_RESISTANCE_ITEMS.size());
    }

    @Override
    public JsonBodyPartResistance get(ResourceLocation itemRegistryName)
    {
        return BODY_PART_RESISTANCE_ITEMS.get(itemRegistryName);
    }
}
