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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IHealingConsumableManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncBodyDamageHealingConsumablesPayload;

import java.util.HashMap;
import java.util.Map;

public class BodyDamageHealingConsumableListener extends SimpleJsonResourceReloadListener implements IHealingConsumableManager
{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonHealingConsumable> HEALING_CONSUMABLES = new HashMap<>();

    public BodyDamageHealingConsumableListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/body_damage/consumables");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncBodyDamageHealingConsumablesPayload(HEALING_CONSUMABLES));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncBodyDamageHealingConsumablesPayload(HEALING_CONSUMABLES));
        }
    }

    public static void acceptServerHealingConsumables(Map<ResourceLocation, JsonHealingConsumable> healingConsumables)
    {
        HEALING_CONSUMABLES.clear();
        HEALING_CONSUMABLES.putAll(healingConsumables);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        HEALING_CONSUMABLES.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonHealingConsumable.CODEC.parse(JsonOps.INSTANCE, json);
                JsonHealingConsumable temperatures = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing body healing consumable: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    HEALING_CONSUMABLES.put(key, temperatures);
            } catch (JsonParseException | IllegalStateException error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse body healing consumable json {}", key, error);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} body healing consumables", HEALING_CONSUMABLES.size());
    }

    @Override
    public JsonHealingConsumable get(ResourceLocation itemRegistryName)
    {
        return HEALING_CONSUMABLES.get(itemRegistryName);
    }
}
