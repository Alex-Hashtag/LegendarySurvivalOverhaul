package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class RusticDelightAddonThirstProvider extends ThirstDataProvider
{

    public RusticDelightAddonThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("rusticdelight_addon", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("chocolate_coffee").addThirst(thirstData(6, 8.0f));
        consumable("coffee").addThirst(thirstData(4, 6.0f));
        consumable("cooking_oil").addThirst(thirstData(0, 1.0f));
        consumable("dark_coffee").addThirst(thirstData(6, 2.0f));
        consumable("milk_coffee").addThirst(thirstData(6, 8.0f));
        consumable("syrup_coffee").addThirst(thirstData(8, 4.0f));
    }
}