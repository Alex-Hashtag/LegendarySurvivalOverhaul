package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class SupplementariesThirstProvider extends ThirstDataProvider
{

    public SupplementariesThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("supplementaries", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        IThirstData sinkThirst = thirstData(3, 0)
                .addEffect(MobEffectRegistry.THIRST.get(), 300, 0.75f)
                .addProperty("has_water", "true")
                .addProperty("enabled", "true");
        block("faucet").addThirst(sinkThirst);

        consumable("lumisene_bottle").addThirst(thirstData(4, 1.0f));
    }
}
