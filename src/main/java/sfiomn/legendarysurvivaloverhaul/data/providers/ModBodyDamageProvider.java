package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ModBodyDamageProvider extends BodyDamageDataProvider
{

    public ModBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super(LegendarySurvivalOverhaul.MOD_ID, output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable(ItemRegistry.HEALING_HERBS.get()).healingCharges(1).limbHealing(1).healingTime(10);
        consumable(ItemRegistry.PLASTER.get()).healingCharges(2).limbHealing(1).healingTime(20);
        consumable(ItemRegistry.BANDAGE.get()).healingCharges(4).limbHealing(1).healingTime(30).healsBrokenLimbs()
                .regeneration(200, 0);
        consumable(ItemRegistry.TONIC.get()).healingCharges(0).limbHealing(0).limbShields(1);
        consumable(ItemRegistry.MEDKIT.get()).healingCharges(0).limbHealing(1).healsBrokenLimbs()
                .regeneration(200, 2);

        damageSource(LegendarySurvivalOverhaul.MOD_ID + ".hypothermia")
                .damageDistribution(DamageDistributionEnum.NONE);
        damageSource(LegendarySurvivalOverhaul.MOD_ID + ".hyperthermia")
                .damageDistribution(DamageDistributionEnum.NONE);
        damageSource(LegendarySurvivalOverhaul.MOD_ID + ".dehydration")
                .damageDistribution(DamageDistributionEnum.NONE);
    }
}
