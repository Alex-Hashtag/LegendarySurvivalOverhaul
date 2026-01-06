package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.level.feature.DoubleBlockFeature;
import sfiomn.legendarysurvivaloverhaul.common.level.feature.GoldFernFeature;

public class FeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<DoubleBlockFeature> DOUBLE_BLOCK = FEATURES.register("double_block", () -> new DoubleBlockFeature(SimpleBlockConfiguration.CODEC));
    public static final RegistryObject<GoldFernFeature> GOLD_FERN = FEATURES.register("gold_fern", () -> new GoldFernFeature(SimpleBlockConfiguration.CODEC));

    public static void register(IEventBus eventBus){
        FEATURES.register(eventBus);
    }
}
