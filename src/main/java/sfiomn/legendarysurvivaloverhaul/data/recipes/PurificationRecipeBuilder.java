package sfiomn.legendarysurvivaloverhaul.data.recipes;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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

public class PurificationRecipeBuilder {
    private final RecipeCategory category;
    private final CookingBookCategory bookCategory;
    private final Item result;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;
    private final RecipeSerializer<? extends AbstractCookingRecipe> serializer;

    private PurificationRecipeBuilder(RecipeCategory pCategory, CookingBookCategory pBookCategory, ItemLike pResult, Ingredient pIngredient, float pExperience, int pCookingTime, RecipeSerializer<? extends AbstractCookingRecipe> pSerializer) {
        this.category = pCategory;
        this.bookCategory = pBookCategory;
        this.result = pResult.asItem();
        this.ingredient = pIngredient;
        this.experience = pExperience;
        this.cookingTime = pCookingTime;
        this.serializer = pSerializer;
    }

    public static PurificationRecipeBuilder blasting(Ingredient pIngredient, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime) {
        return new PurificationRecipeBuilder(pCategory, determineBlastingRecipeCategory(pResult), pResult, pIngredient, pExperience, pCookingTime, RecipeRegistry.PURIFICATION_BLASTING_SERIALIZER.get());
    }

    public static PurificationRecipeBuilder smelting(Ingredient pIngredient, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime) {
        return new PurificationRecipeBuilder(pCategory, determineSmeltingRecipeCategory(pResult), pResult, pIngredient, pExperience, pCookingTime, RecipeRegistry.PURIFICATION_SMELTING_SERIALIZER.get());
    }

    public PurificationRecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    public PurificationRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    public Item getResult() {
        return this.result;
    }

    public void save(@NotNull RecipeOutput output, @NotNull String id) {
        this.save(output, new ResourceLocation(id));
    }

    public void save(RecipeOutput output, @NotNull ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId));

        JsonObject json = new JsonObject();
        if (this.group != null && !this.group.isEmpty()) {
            json.addProperty("group", this.group);
        }
        var resultRegistryName = BuiltInRegistries.ITEM.getKey(this.result);
        json.addProperty("category", this.bookCategory.getSerializedName());
        json.add("ingredient", this.ingredient.toJson());
        if (resultRegistryName != null) {
            json.addProperty("result", resultRegistryName.toString());
        }
        json.addProperty("experience", this.experience);
        json.addProperty("cookingtime", this.cookingTime);

        output.accept(recipeId, this.serializer, json, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/"));
    }

    private static CookingBookCategory determineSmeltingRecipeCategory(ItemLike pResult) {
        if (pResult.asItem().isEdible()) {
            return CookingBookCategory.FOOD;
        } else {
            return pResult.asItem() instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
        }
    }

    private static CookingBookCategory determineBlastingRecipeCategory(ItemLike pResult) {
        return pResult.asItem() instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }

    // Note: 1.20.4 RecipeOutput path used; no FinishedRecipe implementation needed.
}
