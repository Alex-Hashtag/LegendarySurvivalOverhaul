package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class AtmosphericBodyDamageProvider extends BodyDamageDataProvider
{

    public AtmosphericBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("atmospheric", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        damageSource("atmospheric.yuccaSapling")
                .damageDistribution(DamageDistributionEnum.ONE_OF)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        damageSource("atmospheric.yuccaFlower")
                .damageDistribution(DamageDistributionEnum.ONE_OF)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        damageSource("atmospheric.yuccaBranch")
                .damageDistribution(DamageDistributionEnum.ONE_OF)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        damageSource("atmospheric.yuccaLeaves")
                .damageDistribution(DamageDistributionEnum.ONE_OF)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        damageSource("atmospheric.barrelCactus")
                .damageDistribution(DamageDistributionEnum.ONE_OF)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        damageSource("atmospheric.aloeLeaves")
                .damageDistribution(DamageDistributionEnum.ONE_OF)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
    }
}
