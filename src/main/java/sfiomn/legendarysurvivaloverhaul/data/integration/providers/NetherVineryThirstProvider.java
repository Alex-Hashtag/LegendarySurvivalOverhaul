package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class NetherVineryThirstProvider extends ThirstDataProvider
{

    public NetherVineryThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("nethervinery", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("crimson_grape").addThirst(thirstData(2, 0.0f));
        consumable("warped_grape").addThirst(thirstData(2, 0.0f));

        IThirstData vinery_hydration = thirstData(5, 4.0f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f);
        IThirstData vinery2_hydration = thirstData(7, 5.0f).addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f);
        consumable("blazewine_pinot").addThirst(vinery_hydration);
        consumable("ghastly_grenache").addThirst(vinery_hydration);
        consumable("netherite_nectar").addThirst(vinery2_hydration);
        consumable("lava_fizz").addThirst(vinery_hydration);
        consumable("nether_fizz").addThirst(vinery_hydration);
        consumable("improved_lava_fizz").addThirst(vinery2_hydration);
        consumable("improved_nether_fizz").addThirst(vinery2_hydration);
    }
}
