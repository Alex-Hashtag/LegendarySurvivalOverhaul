package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;

import java.util.concurrent.CompletableFuture;

public class MedsAndHerbsBodyDamageProvider extends BodyDamageDataProvider
{

    public MedsAndHerbsBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("meds_and_herbs", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("medkit_novice").healingCharges(0).healingValue(3).duration(400);
        consumable("medkit_advanced").healingCharges(0).healingValue(5).duration(800);
        consumable("medkit_expert").healingCharges(0).healingValue(8).duration(600);
    }
}
