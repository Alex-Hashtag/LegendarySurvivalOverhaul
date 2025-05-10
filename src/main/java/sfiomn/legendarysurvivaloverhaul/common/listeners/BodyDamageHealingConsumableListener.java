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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IHealingConsumableManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncBodyDamageHealingConsumablesPacket;

import java.util.HashMap;
import java.util.Map;

public class BodyDamageHealingConsumableListener extends SimpleJsonResourceReloadListener implements IHealingConsumableManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonHealingConsumable> HEALING_CONSUMABLES = new HashMap<>();

    public BodyDamageHealingConsumableListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/body_damage/consumables");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        HEALING_CONSUMABLES.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonHealingConsumable.CODEC.parse(JsonOps.INSTANCE, json);
                JsonHealingConsumable temperatures = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing body healing consumable : {}", error));
                if (ModList.get().isLoaded(key.getNamespace()))
                    HEALING_CONSUMABLES.put(key, temperatures);
            } catch (JsonParseException error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse body healing consumable json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} body healing consumables", HEALING_CONSUMABLES.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncBodyDamageHealingConsumablesPacket.sendTo(packetTarget, HEALING_CONSUMABLES);
    }

    public static void acceptServerHealingConsumables(Map<ResourceLocation, JsonHealingConsumable> healingConsumables) {
        HEALING_CONSUMABLES.clear();
        HEALING_CONSUMABLES.putAll(healingConsumables);
    }

    @Override
    public JsonHealingConsumable get(ResourceLocation itemRegistryName) {
        return HEALING_CONSUMABLES.get(itemRegistryName);
    }
}