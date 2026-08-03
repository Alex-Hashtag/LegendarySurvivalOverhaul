package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class PamHc2CropsThirstProvider extends ThirstDataProvider
{

    public PamHc2CropsThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("pamhc2crops", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("grapeitem").addThirst(thirstData(2, 1.0f));
        consumable("greengrapeitem").addThirst(thirstData(2, 1.0f));
        consumable("hotcoffeeitem").addThirst(thirstData(3, 10.0f));
        consumable("hotnettleteaitem").addThirst(thirstData(5, 10.0f));
        consumable("hotteaitem").addThirst(thirstData(5, 10.0f));
        consumable("huckleberry").addThirst(thirstData(2, 1.0f));
        consumable("kiwiitem").addThirst(thirstData(2, 1.0f));
        consumable("tomatoitem").addThirst(thirstData(2, 1.0f));
    }
}
