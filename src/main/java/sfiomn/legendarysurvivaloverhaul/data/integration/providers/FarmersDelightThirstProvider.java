package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class FarmersDelightThirstProvider extends ThirstDataProvider
{

    public FarmersDelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("farmersdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("tomato").addThirst(thirstData(4, 0.0f));
        consumable("milk_bottle").addThirst(thirstData(6, 1.0f));
        consumable("apple_cider").addThirst(thirstData(5, 2.0f));
        consumable("hot_cocoa").addThirst(thirstData(6, 3.0f));
        consumable("melon_juice").addThirst(thirstData(8, 4.0f));
        consumable("tomato_sauce").addThirst(thirstData(6, 0.0f));

        consumable("melon_popsicle").addThirst(thirstData(4, 1.0f));
        consumable("fruit_salad").addThirst(thirstData(6, 2.0f));

        consumable("chicken_soup").addThirst(thirstData(4, 2.0f));
        consumable("vegetable_soup").addThirst(thirstData(4, 2.0f));
        consumable("pumpkin_soup").addThirst(thirstData(7, 3.0f));
    }
}
