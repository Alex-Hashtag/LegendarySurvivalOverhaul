package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationBlastingRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationSmeltingRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.RemoveCoatRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.SewingRecipe;


public class RecipeRegistry
{

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SewingRecipe>> SEWING_SERIALIZER = RECIPE_SERIALIZERS.register("sewing", () -> SewingRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PurificationSmeltingRecipe>> PURIFICATION_SMELTING_SERIALIZER = RECIPE_SERIALIZERS.register("purification_smelting", () -> PurificationSmeltingRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PurificationBlastingRecipe>> PURIFICATION_BLASTING_SERIALIZER = RECIPE_SERIALIZERS.register("purification_blasting", () -> PurificationBlastingRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RemoveCoatRecipe>> REMOVE_COAT_SERIALIZER = RECIPE_SERIALIZERS.register("remove_coat", () -> RemoveCoatRecipe.Serializer.INSTANCE);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<SewingRecipe>> SEWING_RECIPE = RECIPE_TYPE.register("sewing", () -> RecipeType.simple(SewingRecipe.Type.ID));

    public static void register(IEventBus eventBus)
    {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPE.register(eventBus);
    }
}
