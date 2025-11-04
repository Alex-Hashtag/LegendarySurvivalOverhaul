package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;

import java.util.*;

public class ThirstData implements IThirstData
{
    private final List<EffectData> effects;
    private final Map<String, String> properties;
    private int hydration;
    private float saturation;

    public ThirstData()
    {
        effects = new ArrayList<>();
        properties = new HashMap<>();
    }

    public ThirstData(ThirstData copy)
    {
        hydration = copy.hydration;
        saturation = copy.saturation;
        effects = new ArrayList<>(copy.effects);
        properties = new HashMap<>(copy.properties);
    }

    @Override
    public IThirstData hydration(int hydrationValue)
    {
        hydration = hydrationValue;
        return this;
    }

    @Override
    public IThirstData saturation(float saturationValue)
    {
        saturation = saturationValue;
        return this;
    }

    @Override
    public IThirstData addEffect(MobEffect effect, int durationInTick, float chance, int amplifier)
    {
        ResourceLocation effectName = Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.getKey(effect));
        effects.add(new EffectData(effectName, Math.max(durationInTick, 0), Math.max(amplifier, 0), Mth.clamp(chance, 0, 1.0f)));
        return this;
    }

    @Override
    public IThirstData addEffect(MobEffect effect, int durationInTick, float chance)
    {
        return this.addEffect(effect, durationInTick, chance, 0);
    }

    @Override
    public IThirstData addEffect(MobEffect effect, int durationInTick, int amplifier)
    {
        return this.addEffect(effect, durationInTick, 0.0f, amplifier);
    }

    @Override
    public IThirstData addEffect(MobEffect effect, int durationInTick)
    {
        return this.addEffect(effect, durationInTick, 1.0f, 0);
    }

    @Override
    public IThirstData addProperty(String propertyName, String propertyValue)
    {
        properties.put(propertyName, propertyValue);
        return this;
    }

    @Override
    public IThirstData copy()
    {
        return new ThirstData(this);
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("hydration", this.hydration);
        json.addProperty("saturation", this.saturation);

        JsonArray jsonEffects = new JsonArray();
        effects.forEach(e -> {
            JsonObject jsonEffect = new JsonObject();
            jsonEffect.addProperty("effect", e.effect.toString());
            jsonEffect.addProperty("duration", e.duration);
            jsonEffect.addProperty("amplifier", e.amplifier);
            jsonEffect.addProperty("chance", e.chance);
            jsonEffects.add(jsonEffect);
        });
        json.add("effects", jsonEffects);

        JsonObject jsonProperties = new JsonObject();
        properties.forEach(jsonProperties::addProperty);
        json.add("properties", jsonProperties);
        return json;
    }

    public record EffectData(ResourceLocation effect, int duration, int amplifier, float chance)
    {
    }
}
