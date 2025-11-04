package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.util.internal.ThirstUtilInternal.HYDRATION_ENUM_TAG;

public class ModThirstProvider extends ThirstDataProvider
{

    public ModThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super(LegendarySurvivalOverhaul.MOD_ID, output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable(ItemRegistry.APPLE_JUICE.get()).addThirst(thirstData(6, 3.0f));
        consumable(ItemRegistry.BEETROOT_JUICE.get()).addThirst(thirstData(8, 4.0f));
        consumable(ItemRegistry.CACTUS_JUICE.get()).addThirst(thirstData(9, 3.0f));
        consumable(ItemRegistry.CARROT_JUICE.get()).addThirst(thirstData(4, 2.0f));
        consumable(ItemRegistry.CHORUS_FRUIT_JUICE.get()).addThirst(thirstData(12, 14.0f));
        consumable(ItemRegistry.GOLDEN_APPLE_JUICE.get()).addThirst(thirstData(10, 12.0f));
        consumable(ItemRegistry.GOLDEN_CARROT_JUICE.get()).addThirst(thirstData(8, 6.0f));
        consumable(ItemRegistry.GLISTERING_MELON_JUICE.get()).addThirst(thirstData(6, 6.0f));
        consumable(ItemRegistry.MELON_JUICE.get()).addThirst(thirstData(7, 4.0f));
        consumable(ItemRegistry.PUMPKIN_JUICE.get()).addThirst(thirstData(6, 3.5f));
        consumable(ItemRegistry.GLOW_BERRIES_JUICE.get()).addThirst(thirstData(4, 6.0f));
        consumable(ItemRegistry.SWEET_BERRIES_JUICE.get()).addThirst(thirstData(9, 5.0f));
        consumable(ItemRegistry.PURIFIED_WATER_BOTTLE.get()).addThirst(thirstData(6, 1.5f));
        consumable(ItemRegistry.WATER_PLANT_BAG.get()).addThirst(thirstData(3, 0.0f));

        IThirstData dirtyWaterCanteen = thirstData(3, 0.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 300, 0.75f)
                .addProperty(HYDRATION_ENUM_TAG, HydrationEnum.NORMAL.getName());
        IThirstData purifiedWaterCanteen = thirstData(6, 1.5f)
                .addProperty(HYDRATION_ENUM_TAG, HydrationEnum.PURIFIED.getName());
        consumable(ItemRegistry.CANTEEN.get()).addThirst(dirtyWaterCanteen).addThirst(purifiedWaterCanteen);
        consumable(ItemRegistry.LARGE_CANTEEN.get()).addThirst(dirtyWaterCanteen).addThirst(purifiedWaterCanteen);
    }
}
