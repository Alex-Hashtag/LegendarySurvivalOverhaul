package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class HerbalBrewsThirstProvider extends ThirstDataProvider
{

    public HerbalBrewsThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("herbalbrews", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("green_tea").addThirst(thirstData(5, 3.0f));
        consumable("black_tea").addThirst(thirstData(5, 3.0f));
        consumable("hibiscus_tea").addThirst(thirstData(6, 4.0f));
        consumable("lavender_tea").addThirst(thirstData(7, 5.0f));
        consumable("coffee").addThirst(thirstData(5, 3.5f));
        consumable("milk_coffee").addThirst(thirstData(6, 4.0f));
        consumable("rooibos_tea").addThirst(thirstData(9, 7.0f));
        consumable("oolong_tea").addThirst(thirstData(10, 10.0f));
        consumable("yerba_mate_tea").addThirst(thirstData(12, 12.0f));
        consumable("flask").addThirst(thirstData(7, 4.5f));
    }
}
