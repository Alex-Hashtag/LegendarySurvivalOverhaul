package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class NeapolitanThirstProvider extends ThirstDataProvider
{

    public NeapolitanThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("neapolitan", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("chocolate_milkshake").addThirst(thirstData(4, 1.0f));
        consumable("vanilla_milkshake").addThirst(thirstData(4, 1.0f));
        consumable("strawberry_milkshake").addThirst(thirstData(4, 1.0f));
        consumable("banana_milkshake").addThirst(thirstData(4, 1.0f));
    }
}
