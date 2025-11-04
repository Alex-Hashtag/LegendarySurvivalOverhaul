package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.blocks.IceFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.SunFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.WaterPlantBlock;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

import static sfiomn.legendarysurvivaloverhaul.common.blocks.ThermalBlock.FACING;

public class ModBlockStateProvider extends BlockStateProvider
{

    public static final ResourceLocation COOLER_OFF = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "block/cooler_off");
    public static final ResourceLocation COOLER_ON = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "block/cooler_on");
    public static final ResourceLocation HEATER_BASE_OFF = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "block/heater_base_off");
    public static final ResourceLocation HEATER_BASE_ON = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "block/heater_base_on");
    public static final ResourceLocation HEATER_TOP = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "block/heater_top");
    public static final ResourceLocation SEWING_TABLE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "block/sewing_table");

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper)
    {
        super(output, LegendarySurvivalOverhaul.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        simpleBlockItem(BlockRegistry.COOLER.get(), new ModelFile.UncheckedModelFile(COOLER_OFF));
        this.getVariantBuilder(BlockRegistry.COOLER.get())
                .forAllStates(state -> {
                    if (state.getValue(BlockStateProperties.LIT))
                        return ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(COOLER_ON))
                                .rotationY((int) state.getValue(FACING).toYRot())
                                .build();
                    else
                        return ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(COOLER_OFF))
                                .rotationY((int) state.getValue(FACING).toYRot())
                                .build();
                });

        simpleBlockItem(BlockRegistry.HEATER.get(), new ModelFile.UncheckedModelFile(this.modLoc("block/heater_off_full_block")));
        this.getVariantBuilder(BlockRegistry.HEATER.get())
                .forAllStates(state -> {
                    if (state.getValue(BlockStateProperties.LIT))
                        return ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(HEATER_BASE_ON))
                                .rotationY((int) state.getValue(FACING).toYRot())
                                .build();
                    else
                        return ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(HEATER_BASE_OFF))
                                .rotationY((int) state.getValue(FACING).toYRot())
                                .build();
                });

        this.getVariantBuilder(BlockRegistry.HEATER_TOP.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(new ModelFile.UncheckedModelFile(HEATER_TOP))
                        .rotationY((int) state.getValue(FACING).toYRot())
                        .build());

        simpleBlockItem(BlockRegistry.SEWING_TABLE.get(), new ModelFile.UncheckedModelFile(SEWING_TABLE));
        this.getVariantBuilder(BlockRegistry.SEWING_TABLE.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(new ModelFile.UncheckedModelFile(SEWING_TABLE))
                        .rotationY((int) state.getValue(FACING).toYRot())
                        .build());

        VariantBlockStateBuilder iceFernBuilder = this.getVariantBuilder(BlockRegistry.ICE_FERN_CROP.get());
        IceFernBlock.AGE.getPossibleValues().forEach((age) -> {
            if (age < IceFernBlock.MAX_AGE)
            {
                iceFernBuilder.partialState()
                        .with(IceFernBlock.AGE, age)
                        .modelForState()
                        .modelFile(models().crop("ice_fern_" + age, this.modLoc("block/ice_fern_" + age)).texture("particle", this.modLoc("block/ice_fern_" + age)).renderType("cutout"))
                        .addModel();
            } else
            {
                iceFernBuilder.partialState()
                        .with(IceFernBlock.AGE, age)
                        .modelForState()
                        .modelFile(new ModelFile.UncheckedModelFile(this.modLoc("block/ice_fern_mature")))
                        .addModel();
            }
        });

        this.simpleBlockWithItem(BlockRegistry.ICE_FERN_GOLD.get(), new ModelFile.UncheckedModelFile(this.modLoc("block/ice_fern_gold")));

        VariantBlockStateBuilder sunFernBuilder = this.getVariantBuilder(BlockRegistry.SUN_FERN_CROP.get());
        SunFernBlock.AGE.getPossibleValues().forEach((age) -> {
            if (age < SunFernBlock.MAX_AGE)
            {
                sunFernBuilder.partialState()
                        .with(SunFernBlock.AGE, age)
                        .modelForState()
                        .modelFile(models().crop("sun_fern_" + age, this.modLoc("block/sun_fern_" + age)).texture("particle", this.modLoc("block/sun_fern_" + age)).renderType("cutout"))
                        .addModel();
            } else
            {
                sunFernBuilder.partialState()
                        .with(SunFernBlock.AGE, age)
                        .modelForState()
                        .modelFile(new ModelFile.UncheckedModelFile(this.modLoc("block/sun_fern_mature")))
                        .addModel();
            }
        });

        this.simpleBlockWithItem(BlockRegistry.SUN_FERN_GOLD.get(), new ModelFile.UncheckedModelFile(this.modLoc("block/sun_fern_gold")));

        VariantBlockStateBuilder waterPlantBuilder = this.getVariantBuilder(BlockRegistry.WATER_PLANT_CROP.get());
        WaterPlantBlock.AGE.getPossibleValues().forEach((age) -> {
            waterPlantBuilder.partialState()
                    .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                    .with(WaterPlantBlock.AGE, age)
                    .modelForState()
                    .modelFile(models().cross("water_plant_bottom_" + age, this.modLoc("block/water_plant_bottom_" + age)).texture("particle", this.modLoc("block/water_plant_bottom_" + age)).renderType("cutout"))
                    .addModel();
            waterPlantBuilder.partialState()
                    .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                    .with(WaterPlantBlock.AGE, age)
                    .modelForState()
                    .modelFile(models().cross("water_plant_top_" + age, this.modLoc("block/water_plant_top_" + age)).texture("particle", this.modLoc("block/water_plant_top_" + age)).renderType("cutout"))
                    .addModel();
        });
    }
}
