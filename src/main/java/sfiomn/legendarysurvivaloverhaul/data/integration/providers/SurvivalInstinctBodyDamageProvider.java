package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;

import java.util.concurrent.CompletableFuture;

public class SurvivalInstinctBodyDamageProvider extends BodyDamageDataProvider
{

    public SurvivalInstinctBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("survival_instinct", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("adrenaline_injector").healingCharges(1).limbHealing(1).healingTime(10);
        consumable("adrenaline_syringe").healingCharges(1).limbHealing(1).healingTime(10);
        consumable("bandage").healingCharges(4).limbHealing(1).healingTime(30).healsBrokenLimbs().regeneration(200, 0);
        consumable("blood_syringe").healingCharges(1).limbHealing(2).healingTime(10);
        consumable("homemade_bandage").healingCharges(2).limbHealing(1).healingTime(20);
        consumable("medkit_bag").healingCharges(0).limbHealing(1).healsBrokenLimbs().regeneration(200, 2);
        consumable("morphine_injector").healingCharges(1).limbHealing(1).healingTime(10);
        consumable("morphine_syringe").healingCharges(1).limbHealing(1).healingTime(10);
    }
}
