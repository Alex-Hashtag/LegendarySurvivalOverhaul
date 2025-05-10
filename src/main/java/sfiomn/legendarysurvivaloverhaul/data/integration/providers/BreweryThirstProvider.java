package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.data.builders.ThirstData;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;

public class BreweryThirstProvider extends ThirstDataProvider {

    public BreweryThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("brewery", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {

        IThirstData whiskey_hydration = thirstData(3, 2.0f).addEffect(MobEffectRegistry.THIRST.get(), 800, 0.4f);
        consumable("whiskey_jojannik").addThirst(whiskey_hydration);
        consumable("whiskey_lilitusinglemalt").addThirst(whiskey_hydration);
        consumable("whiskey_cristelwalker").addThirst(whiskey_hydration);
        consumable("whiskey_maggoallan").addThirst(whiskey_hydration);
        consumable("whiskey_carrasconlabel").addThirst(whiskey_hydration);
        consumable("whiskey_ak").addThirst(whiskey_hydration);
        consumable("whiskey_highland_hearth").addThirst(whiskey_hydration);
        consumable("whiskey_smokey_reverie").addThirst(whiskey_hydration);
        consumable("whiskey_jamesons_malt").addThirst(whiskey_hydration);

        IThirstData beer_hydration = thirstData(8, 4.0f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f);
        consumable("beer_wheat").addThirst(beer_hydration);
        consumable("beer_barley").addThirst(beer_hydration);
        consumable("beer_hops").addThirst(beer_hydration);
        consumable("beer_nettle").addThirst(beer_hydration);
        consumable("beer_oat").addThirst(beer_hydration);
        consumable("beer_haley").addThirst(beer_hydration);
    }
}
