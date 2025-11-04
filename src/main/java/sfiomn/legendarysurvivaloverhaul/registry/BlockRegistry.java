package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.*;

import java.util.function.Supplier;

public class BlockRegistry
{

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, LegendarySurvivalOverhaul.MOD_ID);

    // Blocks that should also get a BlockItem -> use registerBlock(...)
    public static final DeferredHolder<Block, HeaterBaseBlock> HEATER =
            registerBlock("heater", () -> new HeaterBaseBlock(ThermalTypeEnum.HEATING));

    public static final DeferredHolder<Block, HeaterTopBlock> HEATER_TOP =
            BLOCKS.register("heater_top", () -> new HeaterTopBlock(Block.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .noOcclusion())); // no BlockItem

    public static final DeferredHolder<Block, CoolerBlock> COOLER =
            registerBlock("cooler", () -> new CoolerBlock(ThermalTypeEnum.COOLING));

    public static final DeferredHolder<Block, SewingTableBlock> SEWING_TABLE =
            registerBlock("sewing_table", () -> new SewingTableBlock(SewingTableBlock.getProperties()));

    // Crops – usually no BlockItem
    public static final DeferredHolder<Block, SunFernBlock> SUN_FERN_CROP =
            BLOCKS.register("sun_fern_crop", SunFernBlock::new);

    public static final DeferredHolder<Block, SunFernGoldBlock> SUN_FERN_GOLD =
            registerBlock("sun_fern_gold", () -> new SunFernGoldBlock(SunFernGoldBlock.getProperties()));

    public static final DeferredHolder<Block, IceFernBlock> ICE_FERN_CROP =
            BLOCKS.register("ice_fern_crop", IceFernBlock::new);

    public static final DeferredHolder<Block, IceFernGoldBlock> ICE_FERN_GOLD =
            registerBlock("ice_fern_gold", () -> new IceFernGoldBlock(IceFernGoldBlock.getProperties()));

    public static final DeferredHolder<Block, WaterPlantBlock> WATER_PLANT_CROP =
            BLOCKS.register("water_plant_crop", WaterPlantBlock::new);

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block)
    {
        DeferredHolder<Block, T> holder = BLOCKS.register(name, block);
        registerBlockItem(name, holder);
        return holder;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredHolder<Block, T> block)
    {
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
