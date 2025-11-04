package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class RusticDelightThirstProvider extends ThirstDataProvider
{

    public RusticDelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("rusticdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("bell_pepper_green").addThirst(thirstData(4, 0.0f));
        consumable("bell_pepper_yellow").addThirst(thirstData(4, 0.0f));
        consumable("bell_pepper_red").addThirst(thirstData(4, 0.0f));

        consumable("syrup").addThirst(thirstData(2, 1.0f));

        consumable("dark_coffee").addThirst(thirstData(4, 3.5f));
        consumable("coffee").addThirst(thirstData(5, 3.5f));
        consumable("milk_coffee").addThirst(thirstData(6, 4.0f));
        consumable("syrup_coffee").addThirst(thirstData(6, 4.0f));
        consumable("chocolate_coffee").addThirst(thirstData(7, 4.5f));
        consumable("honey_coffee").addThirst(thirstData(7, 4.5f));
    }
}
