package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.mcreator.medsandherbs.init.MedsAndHerbsModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;

import java.util.concurrent.CompletableFuture;

public class MedsAndHerbsBodyDamageProvider extends BodyDamageDataProvider {

    public MedsAndHerbsBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("meds_and_herbs", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        consumable(MedsAndHerbsModItems.MEDKIT_NOVICE.get()).healingCharges(0).healingValue(3).duration(400);
        consumable(MedsAndHerbsModItems.MEDKIT_ADVANCED.get()).healingCharges(0).healingValue(5).duration(800);
        consumable(MedsAndHerbsModItems.MEDKIT_EXPERT.get()).healingCharges(0).healingValue(8).duration(600);
    }
}
