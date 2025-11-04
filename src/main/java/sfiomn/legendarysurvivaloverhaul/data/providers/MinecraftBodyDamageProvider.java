package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class MinecraftBodyDamageProvider extends BodyDamageDataProvider
{

    public MinecraftBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("minecraft", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable(Items.ENCHANTED_GOLDEN_APPLE).healingCharges(0).healingValue(3).duration(600);

        item(Items.DIAMOND_HELMET).headResistance(0.1f);
        item(Items.DIAMOND_CHESTPLATE).chestResistance(0.1f);
        item(Items.DIAMOND_LEGGINGS).legsResistance(0.1f);
        item(Items.DIAMOND_BOOTS).feetResistance(0.1f);

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
        damageSource("indirectMagic")
                .damageDistribution(DamageDistributionEnum.NONE);
    }
}
