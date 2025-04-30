package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ModBodyDamageProvider extends BodyDamageDataProvider {

    public ModBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(LegendarySurvivalOverhaul.MOD_ID, output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        consumable("healing_herbs").healingCharge(1).healingValue(2).duration(600);
        consumable("plaster").healingCharge(1).healingValue(3).duration(400);
        consumable("bandage").healingCharge(3).healingValue(3).duration(300);
        consumable("tonic").healingCharge(0).healingValue(5).duration(600);
        consumable("medikit").healingCharge(0).healingValue(8).duration(400);

        damageSource("fall")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
        damageSource("hotFloor")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
        damageSource("fallingBlock")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyPart(BodyPartEnum.HEAD);
        damageSource("flyIntoWall")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyPart(BodyPartEnum.HEAD);
        damageSource("anvil")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyPart(BodyPartEnum.HEAD);
        damageSource("lightningBolt")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyParts(Arrays.asList(BodyPartEnum.values()));
        damageSource("onFire")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyParts(Arrays.asList(BodyPartEnum.values()));
        damageSource("explosion")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyParts(Arrays.asList(BodyPartEnum.values()));
        damageSource("bad_respawn_point")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyParts(Arrays.asList(BodyPartEnum.values()));
        damageSource("dragonBreath")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyParts(Arrays.asList(BodyPartEnum.values()));
        damageSource("inFire")
                .damageDistribution(DamageDistributionEnum.ALL)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        damageSource("cactus")
                .damageDistribution(DamageDistributionEnum.ONE_OF)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        damageSource("sweetBerryBush")
                .damageDistribution(DamageDistributionEnum.ONE_OF)
                .addBodyParts(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
        damageSource("in_wall")
                .damageDistribution(DamageDistributionEnum.NONE);
        damageSource("drown")
                .damageDistribution(DamageDistributionEnum.NONE);
        damageSource("starve")
                .damageDistribution(DamageDistributionEnum.NONE);
        damageSource("magic")
                .damageDistribution(DamageDistributionEnum.NONE);
        damageSource("wither")
                .damageDistribution(DamageDistributionEnum.NONE);
        damageSource(LegendarySurvivalOverhaul.MOD_ID + ".hypothermia")
                .damageDistribution(DamageDistributionEnum.NONE);
        damageSource(LegendarySurvivalOverhaul.MOD_ID + ".hyperthermia")
                .damageDistribution(DamageDistributionEnum.NONE);
        damageSource(LegendarySurvivalOverhaul.MOD_ID + ".dehydration")
                .damageDistribution(DamageDistributionEnum.NONE);
    }
}
