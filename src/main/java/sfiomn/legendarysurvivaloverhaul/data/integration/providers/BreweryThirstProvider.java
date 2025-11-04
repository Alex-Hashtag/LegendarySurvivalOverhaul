package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class BreweryThirstProvider extends ThirstDataProvider
{

    public BreweryThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("brewery", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        IThirstData whiskey_hydration = thirstData(5, 5.0f).addEffect(MobEffectRegistry.THIRST.get(), 800, 0.4f);
        consumableAndBlock("whiskey_jojannik", whiskey_hydration);
        consumableAndBlock("whiskey_lilitusinglemalt", whiskey_hydration);
        consumableAndBlock("whiskey_cristelwalker", whiskey_hydration);
        consumableAndBlock("whiskey_maggoallan", whiskey_hydration);
        consumableAndBlock("whiskey_carrasconlabel", whiskey_hydration);
        consumableAndBlock("whiskey_ak", whiskey_hydration);
        consumableAndBlock("whiskey_highland_hearth", whiskey_hydration);
        consumableAndBlock("whiskey_smokey_reverie", whiskey_hydration);
        consumableAndBlock("whiskey_jamesons_malt", whiskey_hydration);

        IThirstData beer_hydration = thirstData(8, 6.0f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f);
        consumableAndBlock("beer_wheat", beer_hydration);
        consumableAndBlock("beer_barley", beer_hydration);
        consumableAndBlock("beer_hops", beer_hydration);
        consumableAndBlock("beer_nettle", beer_hydration);
        consumableAndBlock("beer_oat", beer_hydration);
        consumableAndBlock("beer_haley", beer_hydration);
    }
}
