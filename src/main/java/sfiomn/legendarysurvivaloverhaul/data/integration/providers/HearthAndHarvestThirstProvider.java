package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class HearthAndHarvestThirstProvider extends ThirstDataProvider
{

    public HearthAndHarvestThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("hearthandharvest", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("goat_milk_bottle").addThirst(thirstData(6, 1.0f));

        consumable("mead")
                .addThirst(thirstData(7, 4.0f)
                        .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));

        consumable("blueberries").addThirst(thirstData(4, 0.0f));
        consumable("blueberry_juice").addThirst(thirstData(6, 3.0f));
        consumable("blueberry_wine")
                .addThirst(thirstData(7, 4.0f)
                        .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));

        consumable("cherry").addThirst(thirstData(4, 0.0f));
        consumable("cherry_juice").addThirst(thirstData(6, 3.0f));
        consumable("cherry_wine")
                .addThirst(thirstData(7, 4.0f)
                        .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));

        consumable("green_grapes").addThirst(thirstData(4, 0.0f));
        consumable("green_grape_juice").addThirst(thirstData(6, 3.0f));
        consumable("green_grape_wine")
                .addThirst(thirstData(7, 4.0f)
                        .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));

        consumable("raspberry").addThirst(thirstData(4, 0.0f));
        consumable("raspberry_juice").addThirst(thirstData(6, 3.0f));
        consumable("raspberry_wine")
                .addThirst(thirstData(7, 4.0f)
                        .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));

        consumable("red_grapes").addThirst(thirstData(4, 0.0f));
        consumable("red_grape_juice").addThirst(thirstData(6, 3.0f));
        consumable("red_grape_wine")
                .addThirst(thirstData(7, 4.0f)
                        .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));

        consumable("sweet_berry_wine")
                .addThirst(thirstData(7, 4.0f)
                        .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));
    }
}
