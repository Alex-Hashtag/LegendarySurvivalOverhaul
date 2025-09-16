package sfiomn.legendarysurvivaloverhaul.common.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IThirstConsumableManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncThirstConsumablesPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirstConsumableListener extends SimpleJsonResourceReloadListener implements IThirstConsumableManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, List<JsonThirstConsumable>> THIRST_CONSUMABLES = new HashMap<>();

    public ThirstConsumableListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/thirst/consumables");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        THIRST_CONSUMABLES.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonThirstConsumable.LIST_CODEC.parse(JsonOps.INSTANCE, json);
                List<JsonThirstConsumable> parsedThirstConsumables = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing thirst consumable : {}", error));
                if (ModList.get().isLoaded(key.getNamespace()))
                    THIRST_CONSUMABLES.put(key, parsedThirstConsumables);
            } catch (Exception error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse thirst consumable json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} thirst consumables", THIRST_CONSUMABLES.size());
    }

    public static void sendDataToClient(PacketDistributor.PacketTarget packetTarget) {
        SyncThirstConsumablesPacket.sendTo(packetTarget, THIRST_CONSUMABLES);
    }

    public static void acceptServerThirstConsumables(Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables) {
        THIRST_CONSUMABLES.clear();
        THIRST_CONSUMABLES.putAll(thirstConsumables);
    }

    @Override
    public List<JsonThirstConsumable> get(ResourceLocation resourceLocation) {
        return THIRST_CONSUMABLES.get(resourceLocation);
    }

    @Override
    public JsonThirstConsumable get(ItemStack itemStack) {
        List<JsonThirstConsumable> jsonThirstConsumables = null;
        JsonThirstConsumable defaultJct = null;

        ResourceLocation itemRegistryName = Registries.ITEMS.getKey(itemStack.getItem());

        if (itemRegistryName != null)
            jsonThirstConsumables = THIRST_CONSUMABLES.get(itemRegistryName);

        if (jsonThirstConsumables != null)
            for (JsonThirstConsumable jct: jsonThirstConsumables) {
                if (jct.matchesNbt(itemStack))
                    return jct;
                if (jct.isDefault())
                    defaultJct = jct;
            }

        return defaultJct;
    }
}
