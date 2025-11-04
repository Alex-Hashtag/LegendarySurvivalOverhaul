package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class BeachPartyThirstProvider extends ThirstDataProvider
{

    public BeachPartyThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("beachparty", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("coconut_open").addThirst(thirstData(3, 0.0f));
        consumable("coconut_cocktail").addThirst(thirstData(4, 2.0f));
        consumable("sweetberries_cocktail").addThirst(thirstData(4, 4.0f));
        consumable("cocoa_cocktail").addThirst(thirstData(6, 3.0f));
        consumable("pumpkin_cocktail").addThirst(thirstData(6, 3.0f));
        consumable("melon_cocktail").addThirst(thirstData(8, 4.0f));
        consumable("honey_cocktail").addThirst(thirstData(9, 6.0f));
        consumable("refreshing_drink").addThirst(thirstData(10, 8.0f));
        consumable("sweetberry_milkshake").addThirst(thirstData(6, 4.0f));
        consumable("coconut_milkshake").addThirst(thirstData(6, 4.0f));
        consumable("chocolate_milkshake").addThirst(thirstData(8, 6.0f));
    }
}
