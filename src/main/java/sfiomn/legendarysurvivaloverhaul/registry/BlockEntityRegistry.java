package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.CoolerBlockEntity;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.HeaterBlockEntity;

public class BlockEntityRegistry
{

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LegendarySurvivalOverhaul.MOD_ID);

    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HeaterBlockEntity>> HEATER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("heater", () ->
                    BlockEntityType.Builder.of(HeaterBlockEntity::new, BlockRegistry.HEATER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoolerBlockEntity>> COOLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("cooler", () ->
                    BlockEntityType.Builder.of(CoolerBlockEntity::new, BlockRegistry.COOLER.get()).build(null));


}
