package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class FarmersRespiteThirstProvider extends ThirstDataProvider
{

    public FarmersRespiteThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("farmersrespite", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("coffee").addThirst(thirstData(5, 3.5f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));
        consumable("long_coffee").addThirst(thirstData(7, 5f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));
        consumable("strong_coffee").addThirst(thirstData(5, 3.5f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.4f));

        consumable("long_apple_cider").addThirst(thirstData(10, 4.0f));
        consumable("strong_apple_cider").addThirst(thirstData(5, 2.0f));
        consumable("strong_hot_cocoa").addThirst(thirstData(6, 3.0f));
        consumable("strong_melon_juice").addThirst(thirstData(8, 4.0f));

        IThirstData teaTemperature = thirstData(5, 3.0f);
        consumable("black_tea").addThirst(teaTemperature);
        consumable("dandelion_tea").addThirst(teaTemperature);
        consumable("gamblers_tea").addThirst(teaTemperature.copy().addEffect(MobEffectRegistry.THIRST.get(), 600, 0.5f));
        consumable("green_tea").addThirst(teaTemperature);
        consumable("yellow_tea").addThirst(teaTemperature);
        consumable("rose_hip_tea").addThirst(teaTemperature);
        consumable("purulent_tea").addThirst(teaTemperature);

        IThirstData longTeaTemperature = thirstData(7, 4.0f);
        consumable("long_black_tea").addThirst(longTeaTemperature);
        consumable("long_dandelion_tea").addThirst(longTeaTemperature);
        consumable("long_gamblers_tea").addThirst(longTeaTemperature.copy().addEffect(MobEffectRegistry.THIRST.get(), 600, 0.5f));
        consumable("long_green_tea").addThirst(longTeaTemperature);
        consumable("long_yellow_tea").addThirst(longTeaTemperature);
        consumable("long_rose_hip_tea").addThirst(longTeaTemperature);
        consumable("long_purulent_tea").addThirst(longTeaTemperature);

        IThirstData strongTeaTemperature = thirstData(5, 3.0f);
        consumable("strong_black_tea").addThirst(strongTeaTemperature);
        consumable("long_dandelion_tea").addThirst(strongTeaTemperature);
        consumable("strong_gamblers_tea").addThirst(strongTeaTemperature.copy().addEffect(MobEffectRegistry.THIRST.get(), 600, 0.5f));
        consumable("strong_green_tea").addThirst(strongTeaTemperature);
        consumable("strong_yellow_tea").addThirst(strongTeaTemperature);
        consumable("strong_rose_hip_tea").addThirst(strongTeaTemperature);
        consumable("strong_purulent_tea").addThirst(strongTeaTemperature);

    }
}
