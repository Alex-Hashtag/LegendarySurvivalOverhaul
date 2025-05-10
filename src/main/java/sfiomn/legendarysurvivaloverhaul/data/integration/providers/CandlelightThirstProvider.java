package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class CandlelightThirstProvider extends ThirstDataProvider {

    public CandlelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("candlelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {

        IThirstData sinkThirst = thirstData(4, 0.0f).addProperty("filled", "true");
        block("cobblestone_kitchen_sink").addThirst(sinkThirst);
        block("sandstone_kitchen_sink").addThirst(sinkThirst);
        block("stone_bricks_kitchen_sink").addThirst(sinkThirst);
        block("deepslate_kitchen_sink").addThirst(sinkThirst);
        block("granit_kitchen_sink").addThirst(sinkThirst);
        block("end_kitchen_sink").addThirst(sinkThirst);
        block("mud_kitchen_sink").addThirst(sinkThirst);
        block("quartz_kitchen_sink").addThirst(sinkThirst);
        block("mud_kitchen_sink").addThirst(sinkThirst);
        block("mud_kitchen_sink").addThirst(sinkThirst);
        block("quartz_kitchen_sink").addThirst(sinkThirst);
        block("red_nether_bricks_kitchen_sink").addThirst(sinkThirst);
        block("basalt_kitchen_sink").addThirst(sinkThirst);
        block("bamboo_kitchen_sink").addThirst(sinkThirst);
    }
}
