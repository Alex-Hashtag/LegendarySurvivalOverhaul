package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.IceFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.SunFernBlock;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ModTemperatureProvider extends TemperatureDataProvider
{

    public ModTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super(LegendarySurvivalOverhaul.MOD_ID, output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        block(BlockRegistry.COOLER.get())
                .addTemperature(temperatureBlock(-15.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));
        block(BlockRegistry.HEATER.get())
                .addTemperature(temperatureBlock(15.0f).addProperty("lit", "true"))
                .addTemperature(temperatureBlock(0.0f).addProperty("lit", "false"));

        block(BlockRegistry.ICE_FERN_CROP.get()).addTemperature(temperatureBlock(-1.5f).addProperty(IceFernBlock.AGE.getName(), String.valueOf(IceFernBlock.MAX_AGE)));
        block(BlockRegistry.SUN_FERN_CROP.get()).addTemperature(temperatureBlock(1.5f).addProperty(SunFernBlock.AGE.getName(), String.valueOf(SunFernBlock.MAX_AGE)));

        block(BlockRegistry.ICE_FERN_GOLD.get()).addTemperature(temperatureBlock(-1.5f).addProperty(IceFernBlock.AGE.getName(), String.valueOf(IceFernBlock.MAX_AGE)));
        block(BlockRegistry.SUN_FERN_GOLD.get()).addTemperature(temperatureBlock(1.5f).addProperty(SunFernBlock.AGE.getName(), String.valueOf(SunFernBlock.MAX_AGE)));

        item(ItemRegistry.SNOW_BOOTS.get()).coldResistance(0.5f);
        item(ItemRegistry.SNOW_LEGGINGS.get()).coldResistance(2.5f);
        item(ItemRegistry.SNOW_CHEST.get()).coldResistance(3.0f);
        item(ItemRegistry.SNOW_HELMET.get()).coldResistance(1.5f);

        item(ItemRegistry.DESERT_BOOTS.get()).heatResistance(0.5f);
        item(ItemRegistry.DESERT_LEGGINGS.get()).heatResistance(2.5f);
        item(ItemRegistry.DESERT_CHEST.get()).heatResistance(3.0f);
        item(ItemRegistry.DESERT_HELMET.get()).heatResistance(1.5f);

        item(ItemRegistry.NETHER_CHALICE.get()).temperature(2.0f);
        item(ItemRegistry.HEAT_RESISTANCE_RING.get()).heatResistance(3.0f);
        item(ItemRegistry.COLD_RESISTANCE_RING.get()).coldResistance(3.0f);
        item(ItemRegistry.THERMAL_RESISTANCE_RING.get()).thermalResistance(3.0f);

        consumable(ItemRegistry.MELON_JUICE.get()).addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-1).duration(1200));
        consumable(ItemRegistry.GLISTERING_MELON_JUICE.get()).addTemperature(temperatureConsumable(TemporaryModifierGroupEnum.DRINK).temperatureLevel(-2).duration(3600));
    }
}
