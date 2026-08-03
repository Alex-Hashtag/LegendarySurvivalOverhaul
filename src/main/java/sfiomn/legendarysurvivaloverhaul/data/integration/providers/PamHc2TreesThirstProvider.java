package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class PamHc2TreesThirstProvider extends ThirstDataProvider
{

    public PamHc2TreesThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("pamhc2trees", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("grapefruititem").addThirst(thirstData(2, 1.0f));
        consumable("guavaitem").addThirst(thirstData(2, 1.0f));
        consumable("lemonitem").addThirst(thirstData(2, 1.0f));
        consumable("mangoitem").addThirst(thirstData(2, 1.0f));
        consumable("orangeitem").addThirst(thirstData(2, 1.0f));
        consumable("papayaitem").addThirst(thirstData(2, 1.0f));
        consumable("peachitem").addThirst(thirstData(2, 1.0f));
        consumable("pearitem").addThirst(thirstData(2, 1.0f));
        consumable("plumitem").addThirst(thirstData(2, 1.0f));
        consumable("starfruititem").addThirst(thirstData(2, 1.0f));
    }
}
