package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class MinersDelightThirstProvider extends ThirstDataProvider
{

    public MinersDelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("miners_delight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("milk_cup").addThirst(thirstData(4, 1.0f));

        IThirstData lowThirst = thirstData(4, 1.0f);
        consumable("beetroot_soup_cup").addThirst(lowThirst);
        consumable("mushroom_stew_cup").addThirst(lowThirst);
        consumable("cave_soup_cup").addThirst(lowThirst);
        consumable("bat_soup_cup").addThirst(lowThirst);
        consumable("insect_stew_cup").addThirst(lowThirst);

        IThirstData midThirst = thirstData(5, 2.0f);
        consumable("fish_stew_cup").addThirst(midThirst);
        consumable("rabbit_stew_cup").addThirst(midThirst);
        consumable("beef_stew_cup").addThirst(midThirst);
        consumable("vegetable_soup_cup").addThirst(lowThirst);

        IThirstData highThirst = thirstData(5, 3.0f);
        consumable("baked_cod_stew_cup").addThirst(highThirst);
        consumable("noodle_soup_cup").addThirst(highThirst);
        consumable("chicken_soup_cup").addThirst(highThirst);
        consumable("pumpkin_soup_cup").addThirst(highThirst);
        consumable("cave_soup").addThirst(highThirst);
        consumable("bat_soup").addThirst(highThirst);
        consumable("insect_stew").addThirst(highThirst);

    }
}
