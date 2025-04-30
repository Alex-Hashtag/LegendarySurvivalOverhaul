package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.util.internal.ThirstUtilInternal.HYDRATION_ENUM_TAG;

public class ModThirstProvider extends ThirstDataProvider {

    public ModThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(LegendarySurvivalOverhaul.MOD_ID, output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        consumable("apple_juice").addThirst(thirstData(6, 3.0f));
        consumable("beetroot_juice").addThirst(thirstData(9, 4.0f));
        consumable("cactus_juice").addThirst(thirstData(9, 3.0f));
        consumable("carrot_juice").addThirst(thirstData(4, 2.0f));
        consumable("chorus_fruit_juice").addThirst(thirstData(12, 8.0f));
        consumable("golden_apple_juice").addThirst(thirstData(20, 20.0f));
        consumable("golden_carrot_juice").addThirst(thirstData(12, 12.0f));
        consumable("glistering_melon_juice").addThirst(thirstData(16, 16.0f));
        consumable("melon_juice").addThirst(thirstData(8, 4.0f));
        consumable("pumpkin_juice").addThirst(thirstData(6, 1.5f));
        consumable("purified_water_bottle").addThirst(thirstData(3, 0.0f));
        consumable("water_plant_bag").addThirst(thirstData(3, 0.0f));

        IThirstData dirtyWaterCanteen = thirstData(3, 0.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 300, 0.75f)
                .addProperty(HYDRATION_ENUM_TAG, HydrationEnum.NORMAL.getName());
        IThirstData purifiedWaterCanteen = thirstData(6, 1.5f)
                .addProperty(HYDRATION_ENUM_TAG, HydrationEnum.PURIFIED.getName());
        consumable("canteen").addThirst(dirtyWaterCanteen).addThirst(purifiedWaterCanteen);
        consumable("large_canteen").addThirst(dirtyWaterCanteen).addThirst(purifiedWaterCanteen);
    }
}
