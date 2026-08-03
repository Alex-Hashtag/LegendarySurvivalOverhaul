package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class FruitsDelightThirstProvider extends ThirstDataProvider
{

    public FruitsDelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("fruitsdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("bayberry").addThirst(thirstData(2, 1.0f));
        consumable("bayberry_soup").addThirst(thirstData(6, 4.0f));
        consumable("bellini_cocktail").addThirst(thirstData(8, 0.0f));
        consumable("blueberry_custard").addThirst(thirstData(6, 4.0f));
        consumable("hamimelon_juice").addThirst(thirstData(6, 4.0f));
        consumable("hamimelon_shaved_ice").addThirst(thirstData(4, 2.0f));
        consumable("hamimelon_slice").addThirst(thirstData(2, 1.0f));
        consumable("hawberry").addThirst(thirstData(2, 1.0f));
        consumable("hawberry_juice").addThirst(thirstData(6, 4.0f));
        consumable("kiwi").addThirst(thirstData(2, 1.0f));
        consumable("kiwi_juice").addThirst(thirstData(6, 4.0f));
        consumable("lemon").addThirst(thirstData(2, 1.0f));
        consumable("lemon_juice").addThirst(thirstData(6, 4.0f));
        consumable("lychee").addThirst(thirstData(2, 1.0f));
        consumable("lychee_cherry_tea").addThirst(thirstData(8, 6.0f));
        consumable("mango").addThirst(thirstData(2, 1.0f));
        consumable("mango_milkshake").addThirst(thirstData(6, 4.0f));
        consumable("mango_tea").addThirst(thirstData(6, 6.0f));
        consumable("mangosteen").addThirst(thirstData(2, 1.0f));
        consumable("mangosteen_tea").addThirst(thirstData(6, 8.0f));
        consumable("orange").addThirst(thirstData(2, 1.0f));
        consumable("orange_juice").addThirst(thirstData(6, 4.0f));
        consumable("peach").addThirst(thirstData(2, 1.0f));
        consumable("peach_tea").addThirst(thirstData(6, 4.0f));
        consumable("pear").addThirst(thirstData(2, 1.0f));
        consumable("pear_juice").addThirst(thirstData(6, 4.0f));
        consumable("persimmon").addThirst(thirstData(2, 1.0f));
        consumable("pineapple_slice").addThirst(thirstData(2, 1.0f));
    }
}
