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
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IThirstConsumableManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncThirstConsumablesPaload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirstConsumableListener extends SimpleJsonResourceReloadListener implements IThirstConsumableManager
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, List<JsonThirstConsumable>> THIRST_CONSUMABLES = new HashMap<>();

    public ThirstConsumableListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/thirst/consumables");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncThirstConsumablesPaload(THIRST_CONSUMABLES));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncThirstConsumablesPaload(THIRST_CONSUMABLES));
        }
    }

    public static void acceptServerThirstConsumables(Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables)
    {
        THIRST_CONSUMABLES.clear();
        THIRST_CONSUMABLES.putAll(thirstConsumables);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        THIRST_CONSUMABLES.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonThirstConsumable.LIST_CODEC.parse(JsonOps.INSTANCE, json);
                List<JsonThirstConsumable> parsedThirstConsumables = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing thirst consumable: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    THIRST_CONSUMABLES.put(key, parsedThirstConsumables);
            } catch (Exception error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse thirst consumable json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} thirst consumables", THIRST_CONSUMABLES.size());
    }

    @Override
    public List<JsonThirstConsumable> get(ResourceLocation resourceLocation)
    {
        return THIRST_CONSUMABLES.get(resourceLocation);
    }

    @Override
    public JsonThirstConsumable get(ItemStack itemStack)
    {
        List<JsonThirstConsumable> jsonThirstConsumables = null;
        JsonThirstConsumable defaultJct = null;

        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

        if (itemRegistryName != null)
            jsonThirstConsumables = THIRST_CONSUMABLES.get(itemRegistryName);

        if (jsonThirstConsumables != null)
            for (JsonThirstConsumable jct : jsonThirstConsumables)
            {
                if (jct.matchesNbt(itemStack))
                    return jct;
                if (jct.isDefault())
                    defaultJct = jct;
            }

        return defaultJct;
    }
}
