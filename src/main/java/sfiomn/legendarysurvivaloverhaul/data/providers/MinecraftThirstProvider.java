package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class MinecraftThirstProvider extends ThirstDataProvider
{

    public MinecraftThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("minecraft", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        block("rain").addThirst(thirstData(1, 0));

        IThirstData dirtyWater = thirstData(3, 0)
                .addEffect(MobEffectRegistry.THIRST.get(), 300, 0.75f);
        block("flowing_water").addThirst(dirtyWater);
        block(Blocks.WATER).addThirst(dirtyWater);
        block(Blocks.WATER_CAULDRON).addThirst(dirtyWater);

        consumable(Items.POTION)
                .addThirst(dirtyWater.addProperty("Potion", "minecraft:water"))
                .addThirst(dirtyWater.addProperty("Potion", "minecraft:mundane"))
                .addThirst(dirtyWater.addProperty("Potion", "minecraft:thick"))
                .addThirst(dirtyWater.addProperty("Potion", "minecraft:awkward"))
                .addThirst(thirstData(0, 0.0f).addProperty("Potion", "minecraft:empty"))
                .addThirst(thirstData(6, 1.5f));

        consumable(Items.MELON_SLICE).addThirst(thirstData(2, 1.0f));
        consumable(Items.APPLE).addThirst(thirstData(2, 0.5f));
        consumable(Items.GLOW_BERRIES).addThirst(thirstData(2, 0.5f));
        consumable(Items.SWEET_BERRIES).addThirst(thirstData(2, 0.5f));
        consumable(Items.ROTTEN_FLESH).addThirst(thirstData(-4, 0.0f).addEffect(MobEffectRegistry.THIRST.get(), 600));
        consumable(Items.SPIDER_EYE).addThirst(thirstData(-4, 0.0f).addEffect(MobEffectRegistry.THIRST.get(), 600));
        consumable(Items.MILK_BUCKET).addThirst(thirstData(5, 1.0f));
    }
}
