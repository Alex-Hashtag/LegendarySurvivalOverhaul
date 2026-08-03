package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class ExpandedDelightThirstProvider extends ThirstDataProvider
{

    public ExpandedDelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("expandeddelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("apple_juice").addThirst(thirstData(8, 6.0f));
        consumable("cranberry_juice").addThirst(thirstData(8, 6.0f));
        consumable("glow_berry_juice").addThirst(thirstData(4, 10.0f));
        consumable("goat_milk_bottle").addThirst(thirstData(4, 5.0f));
        consumable("goat_milk_bucket").addThirst(thirstData(10, 6.0f));
        consumable("sweet_berry_juice").addThirst(thirstData(8, 8.0f));
    }
}
