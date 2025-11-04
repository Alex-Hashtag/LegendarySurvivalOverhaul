package sfiomn.legendarysurvivaloverhaul.data.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import sfiomn.legendarysurvivaloverhaul.common.recipe.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

public class SewingRecipeBuilder
{
    private final RecipeCategory category;
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final RecipeSerializer<SewingRecipe> type;
    private boolean hasCriterion = false;

    public SewingRecipeBuilder(RecipeSerializer<SewingRecipe> type, RecipeCategory category, Ingredient base, Ingredient addition, ItemStack result)
    {
        this.category = category;
        this.type = type;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public static SewingRecipeBuilder sewingRecipe(Ingredient base, Ingredient addition, ItemStack result, RecipeCategory category)
    {
        return new SewingRecipeBuilder(RecipeRegistry.SEWING_SERIALIZER.get(), category, base, addition, result);
    }

    public SewingRecipeBuilder unlockedBy(String name, Criterion<?> trigger)
    {
        this.advancement.addCriterion(name, trigger);
        this.hasCriterion = true;
        return this;
    }

    public void save(RecipeOutput output, String id)
    {
        this.save(output, ResourceLocation.parse(id));
    }

    public void save(RecipeOutput output, ResourceLocation id)
    {
        this.ensureValid(id);
        AdvancementHolder advancementHolder = this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .build(id.withPrefix("recipes/" + this.category.getFolderName() + "/"));

        SewingRecipe recipe = new SewingRecipe(this.base, this.addition, this.result);
        output.accept(id, recipe, advancementHolder);
    }

    private void ensureValid(ResourceLocation id)
    {
        if (!this.hasCriterion)
        {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    // 1.20.4 RecipeOutput pathway used; no FinishedRecipe implementation required.
}
