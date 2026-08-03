package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class PamHc2FoodCoreThirstProvider extends ThirstDataProvider
{

    public PamHc2FoodCoreThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("pamhc2foodcore", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("applejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("applepopsicleitem").addThirst(thirstData(3, 2.0f));
        consumable("applesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("carrotjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("carrotsoupitem").addThirst(thirstData(8, 5.0f));
        consumable("chocolatemilkitem").addThirst(thirstData(9, 10.0f));
        consumable("chorusjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("choruspopsicleitem").addThirst(thirstData(3, 2.0f));
        consumable("chorussmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("cookiesandmilkitem").addThirst(thirstData(9, 4.0f));
        consumable("fruitpunchitem").addThirst(thirstData(4, 4.0f));
        consumable("fudgesicleitem").addThirst(thirstData(3, 2.0f));
        consumable("glowberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("glowberrypopsicleitem").addThirst(thirstData(3, 2.0f));
        consumable("glowberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("hotchocolateitem").addThirst(thirstData(9, 4.0f));
        consumable("melonjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("melonpopsicleitem").addThirst(thirstData(3, 2.0f));
        consumable("melonsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("noodlesoupitem").addThirst(thirstData(8, 5.0f));
        consumable("p8juiceitem").addThirst(thirstData(4, 4.0f));
        consumable("potatosoupitem").addThirst(thirstData(8, 5.0f));
        consumable("pumpkinsoupitem").addThirst(thirstData(8, 5.0f));
        consumable("stockitem").addThirst(thirstData(4, 2.0f));
        consumable("sweetberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("sweetberrypopsicleitem").addThirst(thirstData(3, 2.0f));
        consumable("sweetberrysmoothieitem").addThirst(thirstData(10, 12.0f));
    }
}
