package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class SeasonalsThirstProvider extends ThirstDataProvider {

    public SeasonalsThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("seasonals", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        consumable("pumpkin_milkshake").addThirst(thirstData(4, 1.0f));
        consumable("sweet_berry_milkshake").addThirst(thirstData(4, 1.0f));
    }
}
