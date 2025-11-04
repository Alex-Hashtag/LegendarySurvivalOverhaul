package sfiomn.legendarysurvivaloverhaul.data.integration;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import sfiomn.legendarysurvivaloverhaul.data.integration.providers.*;

import java.util.concurrent.CompletableFuture;

public final class IntegrationDataGenerators
{
    public static void addIntegrationProviders(GatherDataEvent event, DataGenerator gen, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {

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
        gen.addProvider(event.includeServer(), new BreweryTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BreweryThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BrewinAndChewinTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BrewinAndChewinThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BrewinCompatDelightTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BrewinCompatDelightThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new BygTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CallOfYucatanTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CampingTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CandlelightTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CandlelightThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CataclysmTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new ConfectioneryThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CornExpansionThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CrabbersDelightTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CrabbersDelightThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CreateTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CreateThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CrockpotTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new CrockpotThirstProvider(packOutput, lookupProvider, existingFileHelper));

        if (ModList.get().isLoaded("curios"))
        {
            gen.addProvider(event.includeServer(), new CuriosProvider(packOutput, existingFileHelper, lookupProvider));
            CuriosBlockTagProvider blockTagProvider = gen.addProvider(event.includeServer(), new CuriosBlockTagProvider(packOutput, lookupProvider, existingFileHelper));
            gen.addProvider(event.includeServer(), new CuriosItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));
        }

        gen.addProvider(event.includeServer(), new DecorativeBlocksTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new EcologicsThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new EndergeticTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new EndermanOverhaulTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new FarmAndCharmTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new FarmAndCharmThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new FarmersDelightTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new FarmersDelightThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new FarmersRespiteTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new FarmersRespiteThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new GraveyardTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new HardcoreTorchesTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new HearthAndHarvestTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new HearthAndHarvestThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new HerbalBrewsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new HerbalBrewsThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new IceAndFireBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new IceAndFireTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new InfernalExpansionTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new IronsSpellbooksTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new LegendaryAdditionsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new LegendaryCreaturesBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new LegendaryCreaturesTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new MeadowTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new MeadowThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new MedsAndHerbsBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new NeapolitanTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new NeapolitanThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new NetherVineryTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new NetherVineryThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new OriginsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new PeculiarsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new PeculiarsThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new QuarkTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new RealisticTorchesTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new RusticDelightTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new RusticDelightThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SeasonalsTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SeasonalsThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SupplementariesBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SupplementariesTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SupplementariesThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SurvivalInstinctBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new SurvivalInstinctThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new TerraFirmaCraftTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new UpgradeAquaticThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new ValhelsiaStructuresTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new VineryTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new VineryThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new WardrobeTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new WilderNatureTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
    }
}
