package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class CandlelightThirstProvider extends ThirstDataProvider
{

    public CandlelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("candlelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("mozzarella").addThirst(thirstData(2, 0.0f));

        IThirstData soupThirstData = thirstData(4, 2.0f);
        consumable("tomato_soup").addThirst(soupThirstData);
        consumable("mushroom_soup").addThirst(soupThirstData);

        IThirstData sinkThirst = thirstData(3, 0)
                .addEffect(MobEffectRegistry.THIRST.get(), 300, 0.75f)
                .addProperty("filled", "true");
        block("cobblestone_kitchen_sink").addThirst(sinkThirst);
        block("sandstone_kitchen_sink").addThirst(sinkThirst);
        block("stone_bricks_kitchen_sink").addThirst(sinkThirst);
        block("deepslate_kitchen_sink").addThirst(sinkThirst);
        block("granite_kitchen_sink").addThirst(sinkThirst);
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
