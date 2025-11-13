package sfiomn.legendarysurvivaloverhaul.data.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

import javax.annotation.Nullable;

public class PurificationRecipeBuilder
{
    private final RecipeCategory category;
    private final CookingBookCategory bookCategory;
    private final Item result;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final RecipeSerializer<? extends AbstractCookingRecipe> serializer;
    private final Kind kind;
    private boolean hasCriterion = false;
    @Nullable
    private String group;

    private PurificationRecipeBuilder(RecipeCategory pCategory, CookingBookCategory pBookCategory, ItemLike pResult, Ingredient pIngredient, float pExperience, int pCookingTime, RecipeSerializer<? extends AbstractCookingRecipe> pSerializer, Kind kind)
    {
        this.category = pCategory;
        this.bookCategory = pBookCategory;
        this.result = pResult.asItem();
        this.ingredient = pIngredient;
        this.experience = pExperience;
        this.cookingTime = pCookingTime;
        this.serializer = pSerializer;
        this.kind = kind;
    }

    public static PurificationRecipeBuilder blasting(Ingredient pIngredient, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime)
    {
        return new PurificationRecipeBuilder(pCategory, determineBlastingRecipeCategory(pResult), pResult, pIngredient, pExperience, pCookingTime, RecipeRegistry.PURIFICATION_BLASTING_SERIALIZER.get(), Kind.BLASTING);
    }

    public static PurificationRecipeBuilder smelting(Ingredient pIngredient, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime)
    {
        return new PurificationRecipeBuilder(pCategory, determineSmeltingRecipeCategory(pResult), pResult, pIngredient, pExperience, pCookingTime, RecipeRegistry.PURIFICATION_SMELTING_SERIALIZER.get(), Kind.SMELTING);
    }

    private static CookingBookCategory determineSmeltingRecipeCategory(ItemLike pResult)
    {
        if (pResult.asItem().components().has(net.minecraft.core.component.DataComponents.FOOD))
        {
            return CookingBookCategory.FOOD;
        } else
        {
            return pResult.asItem() instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
        }
    }

    private static CookingBookCategory determineBlastingRecipeCategory(ItemLike pResult)
    {
        return pResult.asItem() instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
    }

    public PurificationRecipeBuilder unlockedBy(String pCriterionName, Criterion<?> pCriterionTrigger)
    {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        this.hasCriterion = true;
        return this;
    }

    public PurificationRecipeBuilder group(@Nullable String pGroupName)
    {
        this.group = pGroupName;
        return this;
    }

    public Item getResult()
    {
        return this.result;
    }

    public void save(@NotNull RecipeOutput output, @NotNull String id)
    {
        this.save(output, ResourceLocation.parse(id));
    }

    public void save(RecipeOutput output, @NotNull ResourceLocation recipeId)
    {
        this.ensureValid(recipeId);
        AdvancementHolder advancementHolder = this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .build(recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/"));

        AbstractCookingRecipe recipe = (this.kind == Kind.SMELTING)
                ? new sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationSmeltingRecipe(
                this.group == null ? "" : this.group, this.bookCategory, this.ingredient,
                new net.minecraft.world.item.ItemStack(this.result), this.experience, this.cookingTime)
                : new sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationBlastingRecipe(
                this.group == null ? "" : this.group, this.bookCategory, this.ingredient,
                new net.minecraft.world.item.ItemStack(this.result), this.experience, this.cookingTime);

        output.accept(recipeId, recipe, advancementHolder);
    }

    private void ensureValid(ResourceLocation pId)
    {
        if (!this.hasCriterion)
        {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }

    // Note: 1.20.4 RecipeOutput path used; no FinishedRecipe implementation needed.

    private enum Kind { SMELTING, BLASTING }
}
