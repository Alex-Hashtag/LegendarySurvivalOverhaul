package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class MinecraftThirstProvider extends ThirstDataProvider {

    public MinecraftThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("minecraft", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        block("rain").addThirst(thirstData(1, 0));
        block("flowing_water").addThirst(thirstData(3, 0));

        IThirstData dirtyWater = thirstData(3, 0)
                .addEffect(MobEffectRegistry.THIRST.get(), 300, 0.75f);
        block("water").addThirst(dirtyWater);
        block("water_cauldron").addThirst(dirtyWater);

        consumable("potion")
                .addThirst(dirtyWater.addProperty("Potion", "minecraft:water"))
                .addThirst(dirtyWater.addProperty("Potion", "minecraft:mundane"))
                .addThirst(dirtyWater.addProperty("Potion", "minecraft:thick"))
                .addThirst(dirtyWater.addProperty("Potion", "minecraft:awkward"))
                .addThirst(thirstData(0, 0.0f).addProperty("Potion", "minecraft:empty"))
                .addThirst(thirstData(6, 1.5f));

        consumable("melon_slice").addThirst(thirstData(2, 1.0f));
        consumable("apple").addThirst(thirstData(2, 0.5f));
        consumable("beetroot_soup").addThirst(thirstData(4, 2.0f));
        consumable("rabbit_stew").addThirst(thirstData(6, 2.0f));
        consumable("mushroom_stew").addThirst(thirstData(4, 2.0f));
        consumable("suspicious_stew").addThirst(thirstData(4, 2.0f));
        consumable("rotten_flesh").addThirst(thirstData(-2, -1.0f).addEffect(MobEffectRegistry.THIRST.get(), 600));
    }
}
