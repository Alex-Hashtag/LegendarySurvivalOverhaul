package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class VineryThirstProvider extends ThirstDataProvider
{

    public VineryThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("vinery", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("rotten_cherry").addThirst(thirstData(-4, 0.0f));

        IThirstData grape_hydration = thirstData(4, 0.0f);
        IThirstData grape_hydration2 = thirstData(6, 2.0f);
        consumable("cherry").addThirst(grape_hydration);
        consumable("red_grape").addThirst(grape_hydration);
        consumable("white_grape").addThirst(grape_hydration);
        consumable("savanna_grapes_red").addThirst(grape_hydration);
        consumable("savanna_grapes_white").addThirst(grape_hydration);
        consumable("taiga_grapes_red").addThirst(grape_hydration);
        consumable("taiga_grapes_white").addThirst(grape_hydration);
        consumable("jungle_grapes_red").addThirst(grape_hydration2);
        consumable("jungle_grapes_white").addThirst(grape_hydration2);

        IThirstData juice_hydration = thirstData(7, 3.0f);
        IThirstData juice_hydration2 = thirstData(9, 5.0f);
        consumable("red_grapejuice").addThirst(juice_hydration);
        consumable("white_grapejuice").addThirst(juice_hydration);
        consumable("red_savanna_grapejuice").addThirst(juice_hydration);
        consumable("white_savanna_grapejuice").addThirst(juice_hydration);
        consumable("red_taiga_grapejuice").addThirst(juice_hydration);
        consumable("white_taiga_grapejuice").addThirst(juice_hydration);
        consumable("red_jungle_grapejuice").addThirst(juice_hydration2);
        consumable("white_jungle_grapejuice").addThirst(juice_hydration2);
        consumable("apple_juice").addThirst(grape_hydration2);

        IThirstData vinery_hydration1 = thirstData(7, 5.0f).addEffect(MobEffectRegistry.THIRST.get(), 400, 0.3f);
        IThirstData vinery_hydration2 = thirstData(9, 6.5f).addEffect(MobEffectRegistry.THIRST.get(), 500, 0.3f);
        consumable("apple_cider").addThirst(vinery_hydration1);
        consumable("apple_wine").addThirst(vinery_hydration1);
        consumable("noir_wine").addThirst(vinery_hydration1);
        consumable("glowing_wine").addThirst(vinery_hydration1);
        consumable("red_wine").addThirst(vinery_hydration1);
        consumable("cherry_wine").addThirst(vinery_hydration1);
        consumable("creepers_crush").addThirst(vinery_hydration1);
        consumable("kelp_cider").addThirst(vinery_hydration1);
        consumable("jo_special_mixture").addThirst(vinery_hydration1);
        consumable("eiswein").addThirst(vinery_hydration1);
        consumable("villagers_fright").addThirst(vinery_hydration1);
        consumable("clark_wine").addThirst(vinery_hydration1);
        consumable("magnetic_wine").addThirst(vinery_hydration1);

        // Harder craft
        consumable("mead").addThirst(vinery_hydration2);
        consumable("solaris_wine").addThirst(vinery_hydration2);
        consumable("strad_wine").addThirst(vinery_hydration2);
        consumable("lilitu_wine").addThirst(vinery_hydration2);
        consumable("aegis_wine").addThirst(vinery_hydration2);
        consumable("bolvar_wine").addThirst(vinery_hydration2);
        consumable("stal_wine").addThirst(vinery_hydration2);
        consumable("chenet_wine").addThirst(vinery_hydration2);

        consumable("bottle_mojang_noir").addThirst(thirstData(8, 5.0f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.3f));

        IThirstData vinery_hydration3 = thirstData(9, 8.0f).addEffect(MobEffectRegistry.THIRST.get(), 800, 0.4f);
        // Nether / End
        consumable("mellohi_wine").addThirst(vinery_hydration3);
        consumable("cristel_wine").addThirst(vinery_hydration3);
        consumable("chorus_wine").addThirst(vinery_hydration3);

        consumable("jellie_wine").addThirst(thirstData(12, 10.0f).addEffect(MobEffectRegistry.THIRST.get(), 1000, 0.3f));
    }
}
