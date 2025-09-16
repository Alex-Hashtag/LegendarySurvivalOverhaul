package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.CoolerBlockEntity;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.HeaterBlockEntity;

public class BlockEntityRegistry {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static RegistryObject<BlockEntityType<HeaterBlockEntity>> HEATER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(LegendarySurvivalOverhaul.MOD_ID + "heater_block_entity", () -> BlockEntityType.Builder
                    .of(HeaterBlockEntity::new, BlockRegistry.HEATER.get()).build(null));

    public static RegistryObject<BlockEntityType<CoolerBlockEntity>> COOLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(LegendarySurvivalOverhaul.MOD_ID + "cooler_block_entity", () -> BlockEntityType.Builder
                    .of(CoolerBlockEntity::new, BlockRegistry.COOLER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
