package sfiomn.legendarysurvivaloverhaul.data.recipes;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

import javax.annotation.Nullable;

public class SewingRecipeBuilder {
    private final RecipeCategory category;
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final RecipeSerializer<?> type;

    public SewingRecipeBuilder(RecipeSerializer<?> type, RecipeCategory category, Ingredient base, Ingredient addition, ItemStack result) {
        this.category = category;
        this.type = type;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public static SewingRecipeBuilder sewingRecipe(Ingredient base, Ingredient addition, ItemStack result, RecipeCategory category) {
        return new SewingRecipeBuilder(RecipeRegistry.SEWING_SERIALIZER.get(), category, base, addition, result);
    }

    public SewingRecipeBuilder unlockedBy(String name, CriterionTriggerInstance advancement) {
        this.advancement.addCriterion(name, advancement);
        return this;
    }

    public void save(RecipeOutput output, String id) {
        this.save(output, new ResourceLocation(id));
    }

    public void save(RecipeOutput output, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id));

        JsonObject json = new JsonObject();
        json.add("base", this.base.toJson());
        json.add("addition", this.addition.toJson());

        ResourceLocation resultRegistryName = Registries.ITEM.getKey(this.result.getItem());
        if (resultRegistryName != null) {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", resultRegistryName.toString());
            if (this.result.hasTag() && this.result.getTag() != null) {
                jsonobject.addProperty("type", "forge:partial_nbt");
                jsonobject.addProperty("nbt", this.result.getTag().toString());
            }
            json.add("result", jsonobject);
        }

        output.accept(id, this.type, json, this.advancement, id.withPrefix("recipes/" + category.getFolderName() + "/"));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    // 1.20.4 RecipeOutput pathway used; no FinishedRecipe implementation required.
}
