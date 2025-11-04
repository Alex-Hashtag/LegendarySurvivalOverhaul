package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class MeadowThirstProvider extends ThirstDataProvider
{

    public MeadowThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("meadow", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        IThirstData dirtyWater = thirstData(3, 0)
                .addEffect(MobEffectRegistry.THIRST.get(), 300, 0.75f);
        block("wooden_water_cauldron").addThirst(dirtyWater);

        IThirstData milkThirstData = thirstData(5, 2.0f);
        consumable("wooden_milk_bucket").addThirst(milkThirstData);
        consumable("wooden_sheep_milk_bucket").addThirst(milkThirstData);
        consumable("wooden_buffalo_milk_bucket").addThirst(milkThirstData);
        consumable("wooden_goat_milk_bucket").addThirst(milkThirstData);
        consumable("wooden_warped_milk_bucket").addThirst(milkThirstData);
        consumable("wooden_grain_milk_bucket").addThirst(milkThirstData);
        consumable("wooden_amethyst_milk_bucket").addThirst(milkThirstData);
    }
}
