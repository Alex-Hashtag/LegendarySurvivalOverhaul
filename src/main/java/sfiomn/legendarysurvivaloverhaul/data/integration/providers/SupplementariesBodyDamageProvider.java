package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SupplementariesBodyDamageProvider extends BodyDamageDataProvider
{

    public SupplementariesBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("supplementaries", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        damageSource("supplementaries.bamboo_spikes")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
    }
}
