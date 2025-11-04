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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IBodyPartsDamageSourceManager;
import sfiomn.legendarysurvivaloverhaul.network.payloads.SyncBodyPartsDamageSourcesPayload;

import java.util.HashMap;
import java.util.Map;

public class BodyPartsDamageSourceListener extends SimpleJsonResourceReloadListener implements IBodyPartsDamageSourceManager
{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonBodyPartsDamageSource> DAMAGE_SOURCES = new HashMap<>();

    public BodyPartsDamageSourceListener()
    {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/body_damage/damage_sources");
    }

    public static void sendDataToClient(@Nullable ServerPlayer player)
    {
        if (player == null)
        {
            PacketDistributor.sendToAllPlayers(new SyncBodyPartsDamageSourcesPayload(DAMAGE_SOURCES));
        } else
        {
            PacketDistributor.sendToPlayer(player, new SyncBodyPartsDamageSourcesPayload(DAMAGE_SOURCES));
        }
    }

    public static void acceptServerDamageSources(Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources)
    {
        DAMAGE_SOURCES.clear();
        DAMAGE_SOURCES.putAll(damageSources);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
    {
        DAMAGE_SOURCES.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try
            {
                var parsedJson = JsonBodyPartsDamageSource.CODEC.parse(JsonOps.INSTANCE, json);
                JsonBodyPartsDamageSource temperatures = parsedJson.getOrThrow(err -> new IllegalStateException("Failed parsing body parts damage source: " + err));
                if (ModList.get().isLoaded(key.getNamespace()))
                    DAMAGE_SOURCES.put(key, temperatures);
            } catch (JsonParseException | IllegalStateException error)
            {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse body parts damage source json {}", key, error);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} body parts damage sources", DAMAGE_SOURCES.size());
    }

    @Override
    public JsonBodyPartsDamageSource get(String damageSourceName)
    {
        for (Map.Entry<ResourceLocation, JsonBodyPartsDamageSource> entry : DAMAGE_SOURCES.entrySet())
        {
            if (entry.getKey().getPath().equals(damageSourceName))
                return entry.getValue();
        }
        return null;
    }
}
