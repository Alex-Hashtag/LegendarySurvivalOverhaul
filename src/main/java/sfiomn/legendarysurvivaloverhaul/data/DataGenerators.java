package sfiomn.legendarysurvivaloverhaul.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.data.integration.IntegrationDataGenerators;
import sfiomn.legendarysurvivaloverhaul.data.providers.*;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        gen.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        //gen.addProvider(event.includeClient(), new ModParticleProvider(packOutput, existingFileHelper));

        ModDatapackBuiltinEntriesProvider datapackProvider = gen.addProvider(event.includeServer(), new ModDatapackBuiltinEntriesProvider(packOutput, lookupProvider));
        CompletableFuture<HolderLookup.Provider> lookupWithCustom = datapackProvider.getRegistryProvider();
        
        gen.addProvider(event.includeServer(), new ModEntityTypesTagProvider(packOutput, lookupWithCustom, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModDamageTypeTagsProvider(packOutput, lookupWithCustom, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModEnchantmentTagProvider(packOutput, lookupWithCustom, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupWithCustom));
        gen.addProvider(event.includeServer(), ModLootTableProvider.createLootTables(packOutput, lookupWithCustom));
        gen.addProvider(event.includeServer(), new ModAdvancementProvider(packOutput, lookupWithCustom, existingFileHelper));

        ModBlockTagProvider blockTagProvider = gen.addProvider(event.includeServer(),
                new ModBlockTagProvider(packOutput, lookupWithCustom, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModItemTagProvider(packOutput, lookupWithCustom, blockTagProvider.contentsGetter(), existingFileHelper));

        gen.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(packOutput, lookupWithCustom));
        gen.addProvider(event.includeServer(), new ModTemperatureProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new MinecraftBodyDamageProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new MinecraftThirstProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new MinecraftTemperatureProvider(packOutput, lookupProvider, existingFileHelper));

        IntegrationDataGenerators.addIntegrationProviders(event, gen, packOutput, lookupProvider, existingFileHelper);
    }
}
