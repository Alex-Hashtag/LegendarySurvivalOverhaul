package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum.DRINK;

public class FarmAndCharmThirstProvider extends ThirstDataProvider {

    public FarmAndCharmThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("farm_and_charm", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {

        consumable("tomato").addThirst(thirstData(4, 0.0f));

        IThirstData teaThirstData = thirstData(7, 3.0f);
        consumable("strawberry_tea").addThirst(teaThirstData);
        consumable("nettle_tea").addThirst(teaThirstData);
        consumable("ribwort_tea").addThirst(teaThirstData);
        consumable("strawberry_tea_cup").addThirst(teaThirstData);
        consumable("nettle_tea_cup").addThirst(teaThirstData);
        consumable("ribwort_tea_cup").addThirst(teaThirstData);
    }
}
