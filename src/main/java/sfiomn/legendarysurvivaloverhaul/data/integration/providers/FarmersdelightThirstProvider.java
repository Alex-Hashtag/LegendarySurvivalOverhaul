package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class FarmersdelightThirstProvider extends ThirstDataProvider {

    public FarmersdelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("farmersdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {

        consumable("chicken_soup").addThirst(thirstData(4, 2.0f));
        consumable("vegetable_soup").addThirst(thirstData(4, 2.0f));
        consumable("pumpkin_soup").addThirst(thirstData(4, 2.0f));
        consumable("hot_cocoa").addThirst(thirstData(4, 1.0f));
        consumable("melon_juice").addThirst(thirstData(8, 4.0f));
    }
}
