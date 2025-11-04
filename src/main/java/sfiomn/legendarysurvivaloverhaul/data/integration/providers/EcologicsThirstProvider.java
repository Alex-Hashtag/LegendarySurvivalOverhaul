package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class EcologicsThirstProvider extends ThirstDataProvider
{

    public EcologicsThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("ecologics", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("coconut_slice").addThirst(thirstData(3, 0.5f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));
        consumable("prickly_pear").addThirst(thirstData(2, 0.5f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f));
        consumable("tropical_stew").addThirst(thirstData(3, 0.5f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.4f));
    }
}
