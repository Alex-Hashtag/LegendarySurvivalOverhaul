package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class PeculiarsThirstProvider extends ThirstDataProvider
{

    public PeculiarsThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("peculiars", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("yucca_milkshake").addThirst(thirstData(6, 2.0f));
        consumable("aloe_milkshake").addThirst(thirstData(6, 2.0f));
        consumable("passionfruit_milkshake").addThirst(thirstData(6, 2.0f));
    }
}
