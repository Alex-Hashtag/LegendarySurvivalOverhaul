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
        consumable("adrenaline_injector").healingCharges(1).healingValue(1).duration(100);
        consumable("adrenaline_syringe").healingCharges(1).healingValue(1).duration(100);
        consumable("bandage").healingCharges(3).healingValue(3).duration(300);
        consumable("blood_syringe").healingCharges(1).healingValue(4).duration(375);
        consumable("homemade_bandage").healingCharges(3).healingValue(3).duration(300);
        consumable("medkit_bag").healingCharges(6).healingValue(8).duration(400);
        consumable("medkit_bag").healingCharges(6).healingValue(8).duration(400);
        consumable("morphine_injector").healingCharges(1).healingValue(2).duration(150);
        consumable("morphine_syringe").healingCharges(1).healingValue(2).duration(150);
    }
}
