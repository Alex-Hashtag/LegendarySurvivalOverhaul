package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class FarmAndCharmThirstProvider extends ThirstDataProvider
{

    public FarmAndCharmThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("farm_and_charm", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("tomato").addThirst(thirstData(4, 0.0f));
        consumable("strawberry").addThirst(thirstData(2, 0.0f));

        IThirstData teaThirstData = thirstData(7, 5.0f);
        consumable("strawberry_tea").addThirst(teaThirstData);
        consumable("nettle_tea").addThirst(teaThirstData);
        consumable("ribwort_tea").addThirst(teaThirstData);
        consumable("strawberry_tea_cup").addThirst(teaThirstData);
        consumable("nettle_tea_cup").addThirst(teaThirstData);
        consumable("ribwort_tea_cup").addThirst(teaThirstData);

        IThirstData soupThirstData = thirstData(4, 2.0f);
        consumable("simple_tomato_soup").addThirst(soupThirstData);
        consumable("barley_soup").addThirst(soupThirstData);
        consumable("onion_soup").addThirst(soupThirstData);
        consumable("potato_soup").addThirst(soupThirstData);
        consumable("goulash").addThirst(soupThirstData);
    }
}
