package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class BrewinAndChewinThirstProvider extends ThirstDataProvider
{

    public BrewinAndChewinThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("brewinandchewin", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        IThirstData drink0 = thirstData(3, 2.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.3f);
        consumable("saccharine_rum").addThirst(drink0);

        IThirstData drink1 = thirstData(8, 6.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f);
        consumable("beer").addThirst(drink1);
        consumable("bloody_mary").addThirst(drink1);
        consumable("dread_nog").addThirst(drink1);
        consumable("egg_grog").addThirst(drink1);
        consumable("mead").addThirst(drink1);
        consumable("steel_toe_stout").addThirst(drink1);
        consumable("strongroot_ale").addThirst(drink1);

        IThirstData drink2 = thirstData(10, 6.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f);
        consumable("glittering_grenadine").addThirst(drink2);
        consumable("kombucha").addThirst(drink2);

        IThirstData drink3 = thirstData(5, 4.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 1000, 0.4f);
        consumable("pale_jane").addThirst(drink3);
        consumable("vodka").addThirst(drink3);
        consumable("red_rum").addThirst(drink3);
        consumable("rice_wine").addThirst(drink3);
        consumable("salty_folly").addThirst(drink3);

        consumable("withering_dross").addThirst(thirstData(12, 7.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 1200));
    }
}
