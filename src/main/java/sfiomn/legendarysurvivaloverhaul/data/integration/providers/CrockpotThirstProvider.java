package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class CrockpotThirstProvider extends ThirstDataProvider
{

    public CrockpotThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("crockpot", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("asparagus_soup").addThirst(thirstData(4, 2.0f));
        consumable("bone_soup").addThirst(thirstData(4, 2.0f));
        consumable("gazpacho").addThirst(thirstData(4, 2.0f));

        consumable("iced_tea").addThirst(thirstData(8, 5.0f));
        consumable("tea").addThirst(thirstData(6, 3.0f));
        consumable("hot_cocoa").addThirst(thirstData(4, 2.0f));
        consumable("fruit_medley").addThirst(thirstData(6, 3.0f));
        consumable("veg_stinger").addThirst(thirstData(6, 3.0f));
    }
}
