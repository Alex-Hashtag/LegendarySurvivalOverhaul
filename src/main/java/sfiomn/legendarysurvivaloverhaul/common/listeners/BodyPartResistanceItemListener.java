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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IBodyResistanceItemManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncBodyPartResistanceItemsPacket;

import java.util.HashMap;
import java.util.Map;

public class BodyPartResistanceItemListener extends SimpleJsonResourceReloadListener implements IBodyResistanceItemManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonBodyPartResistance> BODY_PART_RESISTANCE_ITEMS = new HashMap<>();

    public BodyPartResistanceItemListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/body_damage/items");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        BODY_PART_RESISTANCE_ITEMS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonBodyPartResistance.CODEC.parse(JsonOps.INSTANCE, json);
                JsonBodyPartResistance bodyPartResistance = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing body part resistance item : {}", error));
                if (ModList.get().isLoaded(key.getNamespace()))
                    BODY_PART_RESISTANCE_ITEMS.put(key, bodyPartResistance);
            } catch (JsonParseException error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse body part resistance item json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} body part resistance items", BODY_PART_RESISTANCE_ITEMS.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncBodyPartResistanceItemsPacket.sendTo(packetTarget, BODY_PART_RESISTANCE_ITEMS);
    }

    public static void acceptServerBodyPartResistanceItems(Map<ResourceLocation, JsonBodyPartResistance> temperatureItems) {
        BODY_PART_RESISTANCE_ITEMS.clear();
        BODY_PART_RESISTANCE_ITEMS.putAll(temperatureItems);
    }

    @Override
    public JsonBodyPartResistance get(ResourceLocation itemRegistryName) {
        return BODY_PART_RESISTANCE_ITEMS.get(itemRegistryName);
    }
}