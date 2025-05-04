package sfiomn.legendarysurvivaloverhaul.data.integration;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.data.integration.providers.*;
import sfiomn.legendarysurvivaloverhaul.data.providers.ModBlockTagProvider;
import sfiomn.legendarysurvivaloverhaul.data.providers.ModGlobalLootModifierProvider;
import sfiomn.legendarysurvivaloverhaul.data.providers.ModItemTagProvider;

import java.util.concurrent.CompletableFuture;

public final class IntegrationDataGenerators {
    public static void addIntegrationProviders(GatherDataEvent event, DataGenerator gen, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {

        gen.addProvider(event.includeServer(), new AlexsMobsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new AquamiraeTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new ArtifactsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new AtmosphericBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BeachPartyTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BeachPartyThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BetterEndForgeTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BetterEndTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BopTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BornInChaosTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BygTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CallOfYucatanTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CataclysmTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new ConfectioneryThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CreateTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CrockpotTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CrockpotThirstProvider(packOutput, lookupProvider, existingFileHelper));

        gen.addProvider(event.includeServer(), new CuriosProvider(packOutput, existingFileHelper, lookupProvider));
        CuriosBlockTagProvider blockTagProvider = gen.addProvider(event.includeServer(), new CuriosBlockTagProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CuriosItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));

        gen.addProvider(event.includeServer(), new EndergeticTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new EndermanOverhaulTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new FarmersdelightTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new FarmersdelightThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new HardcoreTorchesTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new IceAndFireTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new InfernalExpansionTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new IronsSpellbooksTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new LegendaryAdditionsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new LegendaryCreaturesBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new LegendaryCreaturesTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new NeapolitanTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new NeapolitanThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new OriginsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new PeculiarsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new PeculiarsThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new QuarkTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new RealisticTorchesTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SeasonalsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SeasonalsThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SupplementariesBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new TerraFirmaCraftTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new UpgradeAquaticThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new WardrobeTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
    }
}
