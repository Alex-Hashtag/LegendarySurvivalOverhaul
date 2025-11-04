package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;
import net.minecraft.world.effect.MobEffect;

public interface IThirstData
{

    IThirstData hydration(int hydrationValue);

    IThirstData saturation(float saturationValue);

    IThirstData addEffect(MobEffect effect, int durationInTick, float chance, int amplifier);

    IThirstData addEffect(MobEffect effect, int durationInTick, int amplifier);

    IThirstData addEffect(MobEffect effect, int durationInTick, float chance);

    IThirstData addEffect(MobEffect effect, int durationInTick);

    IThirstData addProperty(String propertyName, String propertyValue);

    IThirstData copy();

    JsonObject build();
}
