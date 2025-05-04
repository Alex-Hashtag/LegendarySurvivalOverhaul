package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class LegendaryCreaturesTemperatureProvider extends TemperatureDataProvider {

    public LegendaryCreaturesTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("legendarycreatures", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        item("straw_hat").heatResistance(3.0f);
    }
}
