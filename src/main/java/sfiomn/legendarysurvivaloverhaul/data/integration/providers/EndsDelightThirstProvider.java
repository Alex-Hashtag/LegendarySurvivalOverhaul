package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class EndsDelightThirstProvider extends ThirstDataProvider
{

    public EndsDelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("ends_delight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("bubble_tea").addThirst(thirstData(15, 18.0f));
        consumable("chorus_flower_tea").addThirst(thirstData(4, 18.0f));
        consumable("chorus_fruit_milk_tea").addThirst(thirstData(10, 15.0f));
        consumable("chorus_fruit_wine").addThirst(thirstData(10, 8.0f));
        consumable("dragon_breath_soda").addThirst(thirstData(19, 2.0f));
    }
}
