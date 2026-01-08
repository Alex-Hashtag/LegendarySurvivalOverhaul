package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.common.recipe.RemoveCoatRecipe;
import sfiomn.legendarysurvivaloverhaul.data.recipes.PurificationRecipeBuilder;
import sfiomn.legendarysurvivaloverhaul.data.recipes.SewingRecipeBuilder;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.util.internal.ThirstUtilInternal.HYDRATION_ENUM_TAG;

public class ModRecipeProvider extends RecipeProvider
{

    public ModRecipeProvider(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(generator, lookupProvider);
    }

    protected static void smelting(@NotNull RecipeOutput output, Ingredient input, ItemLike result, float experience, int cookingTime, String recipeName)
    {
        SimpleCookingRecipeBuilder
                .smelting(input, RecipeCategory.MISC, result, experience, cookingTime)
                .unlockedBy(getHasName(input.getItems()[0].getItem()), has(input.getItems()[0].getItem()))
                .save(output, LegendarySurvivalOverhaul.MOD_ID + ":" + recipeName + "_from_smelting_" + getItemName(input.getItems()[0].getItem()));
    }

    protected static void purification_smelting(@NotNull RecipeOutput output, Ingredient input, ItemLike result, float experience, int cookingTime, String recipeName)
    {
        PurificationRecipeBuilder
                .smelting(input, RecipeCategory.MISC, result, experience, cookingTime)
                .unlockedBy(getHasName(input.getItems()[0].getItem()), has(input.getItems()[0].getItem()))
                .group("purification")
                .save(output, LegendarySurvivalOverhaul.MOD_ID + ":" + recipeName + "_from_purification_smelting_" + getItemName(input.getItems()[0].getItem()));
    }

    protected static void blasting(@NotNull RecipeOutput output, Ingredient input, ItemLike result, float experience, int cookingTime, String recipeName)
    {
        SimpleCookingRecipeBuilder
                .blasting(input, RecipeCategory.MISC, result, experience, cookingTime)
                .unlockedBy(getHasName(input.getItems()[0].getItem()), has(input.getItems()[0].getItem()))
                .save(output, LegendarySurvivalOverhaul.MOD_ID + ":" + recipeName + "_from_blasting_" + getItemName(input.getItems()[0].getItem()));
    }

    protected static void purification_blasting(@NotNull RecipeOutput output, Ingredient input, ItemLike result, float experience, int cookingTime, String recipeName)
    {
        PurificationRecipeBuilder
                .blasting(input, RecipeCategory.MISC, result, experience, cookingTime)
                .unlockedBy(getHasName(input.getItems()[0].getItem()), has(input.getItems()[0].getItem()))
                .group("purification")
                .save(output, LegendarySurvivalOverhaul.MOD_ID + ":" + recipeName + "_from_purification_blasting_" + getItemName(input.getItems()[0].getItem()));
    }

    protected static void sewing(@NotNull RecipeOutput output, Ingredient input, Ingredient addition, ItemStack result, String recipeName)
    {
        String additionName = getItemName(addition.getItems()[0].getItem());

        SewingRecipeBuilder
                .sewingRecipe(input, addition, result, RecipeCategory.MISC)
                .unlockedBy("has_coat", has(addition.getItems()[0].getItem()))
                .unlockedBy(getHasName(BlockRegistry.SEWING_TABLE.get().asItem()), insideOf(BlockRegistry.SEWING_TABLE.get()))
                .save(output, LegendarySurvivalOverhaul.MOD_ID + ":" + recipeName + "_from_sewing_" + getItemName(input.getItems()[0].getItem()) + "_" + additionName);
    }

    protected static void juice(RecipeOutput consumer, ItemLike fruit, Item juice)
    {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, juice)
                .requires(ItemRegistry.PURIFIED_WATER_BOTTLE.get())
                .requires(Items.SUGAR)
                .requires(fruit)
                .requires(fruit)
                .requires(fruit)
                .group("drink")
                .unlockedBy(getHasName(fruit), has(fruit))
                .save(consumer);
    }

    private static Ingredient partialNbtIngredient(ItemLike item, CompoundTag nbt)
    {
        // For 1.21.1, we'll use a simple ingredient and handle NBT in the recipe logic
        return Ingredient.of(item);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.DESERT_HELMET.get())
                .pattern("r#r")
                .pattern("r r")
                .define('#', Items.LEATHER)
                .define('r', Items.STRING)
                .group("desert_armor")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.DESERT_BOOTS.get())
                .pattern("r r")
                .pattern("# #")
                .define('#', Items.LEATHER)
                .define('r', Items.STRING)
                .group("desert_armor")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.DESERT_CHEST.get())
                .pattern("r r")
                .pattern("#r#")
                .pattern("###")
                .define('#', Items.LEATHER)
                .define('r', Items.STRING)
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .group("desert_armor")
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.DESERT_LEGGINGS.get())
                .pattern("###")
                .pattern("###")
                .pattern("r r")
                .define('#', Items.LEATHER)
                .define('r', Items.STRING)
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .group("desert_armor")
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SNOW_HELMET.get())
                .pattern("w#w")
                .pattern("w w")
                .define('#', Items.LEATHER)
                .define('w', ItemTags.WOOL)
                .group("snow_Armor")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SNOW_BOOTS.get())
                .pattern("w w")
                .pattern("w w")
                .define('w', ItemTags.WOOL)
                .group("snow_Armor")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SNOW_CHEST.get())
                .pattern("w w")
                .pattern("www")
                .pattern("w#w")
                .define('#', Items.LEATHER)
                .define('w', ItemTags.WOOL)
                .group("snow_Armor")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SNOW_LEGGINGS.get())
                .pattern("www")
                .pattern("w w")
                .pattern("w w")
                .define('w', ItemTags.WOOL)
                .group("snow_Armor")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.COOLING_COAT_1.get())
                .pattern(" is")
                .pattern("isc")
                .pattern("scc")
                .define('i', Items.IRON_INGOT)
                .define('c', ItemRegistry.COLD_STRING.get())
                .define('s', Items.STICK)
                .group("cooling_coat")
                .unlockedBy(getHasName(ItemRegistry.COLD_STRING.get()), has(ItemRegistry.COLD_STRING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.COOLING_COAT_2.get())
                .pattern(" db")
                .pattern("db#")
                .pattern("b##")
                .define('d', Items.DIAMOND)
                .define('b', Items.BLAZE_ROD)
                .define('#', ItemRegistry.COOLING_COAT_1.get())
                .group("cooling_coat")
                .unlockedBy(getHasName(ItemRegistry.COLD_STRING.get()), has(ItemRegistry.COLD_STRING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.COOLING_COAT_3.get())
                .pattern(" ne")
                .pattern("ne#")
                .pattern("e##")
                .define('n', Items.NETHERITE_INGOT)
                .define('e', Items.END_ROD)
                .define('#', ItemRegistry.COOLING_COAT_2.get())
                .group("cooling_coat")
                .unlockedBy(getHasName(ItemRegistry.COLD_STRING.get()), has(ItemRegistry.COLD_STRING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.HEATING_COAT_1.get())
                .pattern(" is")
                .pattern("isw")
                .pattern("sww")
                .define('i', Items.IRON_INGOT)
                .define('w', ItemRegistry.WARM_STRING.get())
                .define('s', Items.STICK)
                .group("heating_coat")
                .unlockedBy(getHasName(ItemRegistry.WARM_STRING.get()), has(ItemRegistry.WARM_STRING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.HEATING_COAT_2.get())
                .pattern(" db")
                .pattern("db#")
                .pattern("b##")
                .define('d', Items.DIAMOND)
                .define('b', Items.BLAZE_ROD)
                .define('#', ItemRegistry.HEATING_COAT_1.get())
                .group("heating_coat")
                .unlockedBy(getHasName(ItemRegistry.WARM_STRING.get()), has(ItemRegistry.WARM_STRING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.HEATING_COAT_3.get())
                .pattern(" ne")
                .pattern("ne#")
                .pattern("e##")
                .define('n', Items.NETHERITE_INGOT)
                .define('e', Items.END_ROD)
                .define('#', ItemRegistry.HEATING_COAT_2.get())
                .group("heating_coat")
                .unlockedBy(getHasName(ItemRegistry.WARM_STRING.get()), has(ItemRegistry.WARM_STRING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.THERMAL_COAT_1.get())
                .pattern(" cs")
                .pattern(" sh")
                .pattern("   ")
                .define('c', ItemRegistry.COOLING_COAT_1.get())
                .define('s', Items.STICK)
                .define('h', ItemRegistry.HEATING_COAT_1.get())
                .group("thermal_coat")
                .unlockedBy(getHasName(ItemRegistry.COOLING_COAT_1.get()), has(ItemRegistry.COOLING_COAT_1.get()))
                .unlockedBy(getHasName(ItemRegistry.HEATING_COAT_1.get()), has(ItemRegistry.HEATING_COAT_1.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.THERMAL_COAT_2.get())
                .pattern(" cb")
                .pattern(" bh")
                .pattern("   ")
                .define('c', ItemRegistry.COOLING_COAT_2.get())
                .define('b', Items.BLAZE_ROD)
                .define('h', ItemRegistry.HEATING_COAT_2.get())
                .group("thermal_coat")
                .unlockedBy(getHasName(ItemRegistry.COOLING_COAT_2.get()), has(ItemRegistry.COOLING_COAT_2.get()))
                .unlockedBy(getHasName(ItemRegistry.HEATING_COAT_2.get()), has(ItemRegistry.HEATING_COAT_2.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.THERMAL_COAT_3.get())
                .pattern(" ce")
                .pattern(" eh")
                .pattern("   ")
                .define('c', ItemRegistry.COOLING_COAT_3.get())
                .define('e', Items.END_ROD)
                .define('h', ItemRegistry.HEATING_COAT_3.get())
                .group("thermal_coat")
                .unlockedBy(getHasName(ItemRegistry.COOLING_COAT_3.get()), has(ItemRegistry.COOLING_COAT_3.get()))
                .unlockedBy(getHasName(ItemRegistry.HEATING_COAT_3.get()), has(ItemRegistry.HEATING_COAT_3.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BlockRegistry.COOLER.get())
                .pattern("psp")
                .pattern("prp")
                .pattern("ppp")
                .define('s', Items.SNOW_BLOCK)
                .define('p', ItemTags.PLANKS)
                .define('r', Items.REDSTONE)
                .unlockedBy(getHasName(Items.STONE), has(Items.STONE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BlockRegistry.HEATER.get())
                .pattern(" i ")
                .pattern("ici")
                .pattern("iwi")
                .define('i', Items.IRON_INGOT)
                .define('c', Items.CAMPFIRE)
                .define('w', Items.COAL_BLOCK)
                .unlockedBy(getHasName(Items.CAMPFIRE), has(Items.CAMPFIRE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.SEASONAL_CALENDAR.get())
                .pattern("ppp")
                .pattern("pcp")
                .pattern("ppp")
                .define('p', Items.PAPER)
                .define('c', Items.CLOCK)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.THERMOMETER.get())
                .pattern("#g#")
                .pattern("#r#")
                .pattern("i#i")
                .define('#', Items.GLASS)
                .define('g', Items.GOLD_INGOT)
                .define('i', Items.IRON_INGOT)
                .define('r', Items.REDSTONE)
                .unlockedBy("has_glass", has(Items.GLASS))
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BlockRegistry.SEWING_TABLE.get())
                .pattern("iii")
                .pattern("r i")
                .pattern("ppp")
                .define('i', Items.IRON_INGOT)
                .define('r', Items.REDSTONE)
                .define('p', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemRegistry.HEART_CONTAINER.get())
                .pattern("hhh")
                .pattern("hhh")
                .pattern("hhh")
                .define('h', ItemRegistry.HEART_FRAGMENT.get())
                .unlockedBy("has_heart_fragment", has(ItemRegistry.HEART_FRAGMENT.get()))
                .save(output);

        CompoundTag nbt = new CompoundTag();
        nbt.putString("Potion", LegendarySurvivalOverhaul.MOD_ID + ":temperature_immunity");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.THERMAL_RESISTANCE_RING.get())
                .pattern(" wh")
                .pattern("stw")
                .pattern("cs ")
                .define('w', ItemRegistry.WARM_STRING.get())
                .define('s', ItemRegistry.COLD_STRING.get())
                .define('t', partialNbtIngredient(Items.POTION, nbt))
                .define('h', ItemRegistry.HEAT_RESISTANCE_RING.get())
                .define('c', ItemRegistry.COLD_RESISTANCE_RING.get())
                .unlockedBy("has_heat_resistance_ring", has(ItemRegistry.HEAT_RESISTANCE_RING.get()))
                .unlockedBy("has_cold_resistance_ring", has(ItemRegistry.COLD_RESISTANCE_RING.get()))
                .save(output);

        juice(output, Items.APPLE, ItemRegistry.APPLE_JUICE.get());
        juice(output, Items.BEETROOT, ItemRegistry.BEETROOT_JUICE.get());
        juice(output, Items.CACTUS, ItemRegistry.CACTUS_JUICE.get());
        juice(output, Items.CARROT, ItemRegistry.CARROT_JUICE.get());
        juice(output, Items.CHORUS_FRUIT, ItemRegistry.CHORUS_FRUIT_JUICE.get());
        juice(output, Items.GLISTERING_MELON_SLICE, ItemRegistry.GLISTERING_MELON_JUICE.get());
        juice(output, Items.GOLDEN_APPLE, ItemRegistry.GOLDEN_APPLE_JUICE.get());
        juice(output, Items.GOLDEN_CARROT, ItemRegistry.GOLDEN_CARROT_JUICE.get());
        juice(output, Items.MELON_SLICE, ItemRegistry.MELON_JUICE.get());
        juice(output, Items.PUMPKIN, ItemRegistry.PUMPKIN_JUICE.get());
        juice(output, Items.SWEET_BERRIES, ItemRegistry.SWEET_BERRIES_JUICE.get());
        juice(output, Items.GLOW_BERRIES, ItemRegistry.GLOW_BERRIES_JUICE.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CANTEEN.get())
                .pattern(" i ")
                .pattern("l l")
                .pattern("lll")
                .define('i', Items.IRON_INGOT)
                .define('l', Items.LEATHER)
                .group("drink")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.LARGE_CANTEEN.get())
                .pattern(" n ")
                .pattern("l l")
                .pattern("lll")
                .define('n', Items.NETHERITE_SCRAP)
                .define('l', Items.LEATHER)
                .group("healing")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemRegistry.HEALING_HERBS.get())
                .pattern("ff ")
                .pattern("ss ")
                .pattern("   ")
                .define('f', ItemTags.FLOWERS)
                .define('s', Tags.Items.SEEDS)
                .group("healing")
                .unlockedBy("has_flower", has(ItemTags.FLOWERS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.PLASTER.get())
                .pattern("ss ")
                .pattern("ww ")
                .pattern("   ")
                .define('s', Items.STRING)
                .define('w', ItemTags.WOOL)
                .group("healing")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.MEDKIT.get())
                .pattern("sis")
                .pattern("gbg")
                .pattern("www")
                .define('s', Items.STRING)
                .define('i', Items.IRON_INGOT)
                .define('g', Items.GOLD_INGOT)
                .define('b', ItemRegistry.BANDAGE.get())
                .define('w', ItemTags.WOOL)
                .group("healing")
                .unlockedBy(getHasName(ItemRegistry.BANDAGE.get()), has(ItemRegistry.BANDAGE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.MORPHINE.get())
                .pattern("  n")
                .pattern(" t ")
                .pattern("g  ")
                .define('n', Items.IRON_NUGGET)
                .define('t', ItemRegistry.TONIC.get())
                .define('g', Items.GLASS)
                .group("healing")
                .unlockedBy(getHasName(ItemRegistry.PLASTER.get()), has(ItemRegistry.PLASTER.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.BANDAGE.get())
                .pattern("  p")
                .pattern(" h ")
                .pattern("p  ")
                .define('p', ItemRegistry.PLASTER.get())
                .define('h', ItemRegistry.HEALING_HERBS.get())
                .group("healing")
                .unlockedBy("has_plaster", has(ItemRegistry.PLASTER.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.TONIC.get())
                .pattern(" h ")
                .pattern("gbg")
                .pattern(" g ")
                .define('h', ItemRegistry.HEALING_HERBS.get())
                .define('b', Items.GLASS_BOTTLE)
                .define('g', Items.GOLD_INGOT)
                .group("healing")
                .unlockedBy(getHasName(ItemRegistry.HEALING_HERBS.get()), has(ItemRegistry.HEALING_HERBS.get()))
                .save(output);

        nbt = new CompoundTag();
        nbt.putString("Potion", "minecraft:water");
        smelting(output, partialNbtIngredient(Items.POTION, nbt), ItemRegistry.PURIFIED_WATER_BOTTLE.get(), 1.0f, 200, "purified_water_bottle");
        blasting(output, partialNbtIngredient(Items.POTION, nbt), ItemRegistry.PURIFIED_WATER_BOTTLE.get(), 1.0f, 60, "purified_water_bottle");

        nbt = new CompoundTag();
        nbt.putString(HYDRATION_ENUM_TAG, HydrationEnum.NORMAL.getName());
        purification_smelting(output, partialNbtIngredient(ItemRegistry.CANTEEN.get(), nbt), ItemRegistry.CANTEEN.get(), 1.0f, 240, "purified_canteen");
        purification_smelting(output, partialNbtIngredient(ItemRegistry.LARGE_CANTEEN.get(), nbt), ItemRegistry.LARGE_CANTEEN.get(), 1.0f, 240, "purified_large_canteen");
        purification_blasting(output, partialNbtIngredient(ItemRegistry.CANTEEN.get(), nbt), ItemRegistry.CANTEEN.get(), 1.0f, 80, "purified_canteen");
        purification_blasting(output, partialNbtIngredient(ItemRegistry.LARGE_CANTEEN.get(), nbt), ItemRegistry.LARGE_CANTEEN.get(), 1.0f, 80, "purified_large_canteen");

        sewing(output, Ingredient.of(Items.STRING), Ingredient.of(ItemRegistry.ICE_FERN.get()), new ItemStack(ItemRegistry.COLD_STRING.get()), "cold_string");
        sewing(output, Ingredient.of(Items.STRING), Ingredient.of(ItemRegistry.SUN_FERN.get()), new ItemStack(ItemRegistry.WARM_STRING.get()), "warm_string");

        SpecialRecipeBuilder.special(RemoveCoatRecipe::new)
                .save(output, LegendarySurvivalOverhaul.MOD_ID + ":remove_coat");
    }
}
