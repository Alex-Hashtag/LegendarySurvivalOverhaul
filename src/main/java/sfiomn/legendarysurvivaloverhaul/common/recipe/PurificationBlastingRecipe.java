package sfiomn.legendarysurvivaloverhaul.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;

public class PurificationBlastingRecipe extends BlastingRecipe {
    public PurificationBlastingRecipe(ResourceLocation id, String group, CookingBookCategory cookingBookCategory, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(id, group, cookingBookCategory, ingredient, result, experience, cookingTime);
    }

    @Override
    public boolean matches(Container inventory, @NotNull Level level) {
        ItemStack input = inventory.getItem(0);
        // Check if item type matches (ignoring NBT/enchantments) and has water
        return input.getItem() instanceof CanteenItem && ThirstUtil.getCapacityTag(input) > 0;
    }

    @Override
    public @NotNull ItemStack assemble(Container inventory, @NotNull RegistryAccess access) {
        ItemStack input = inventory.getItem(0);
        int hydrationCapacity = ThirstUtil.getCapacityTag(input);
        
        // Create result with the SAME item type as input (preserves large vs regular canteen)
        ItemStack result = new ItemStack(input.getItem());
        
        // Copy enchantments from input to result
        if (input.hasTag() && input.getTag().contains("Enchantments")) {
            result.getOrCreateTag().put("Enchantments", input.getTag().get("Enchantments").copy());
        }
        
        // Set the purified hydration values
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, hydrationCapacity);
        
        return result;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess access) {
        ItemStack result = this.result.copy();
        int maxHydrationCapacity = 0;
        if (this.result.getItem() instanceof CanteenItem resultItem) {
            maxHydrationCapacity = resultItem.getMaxCapacity();
        }
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, maxHydrationCapacity);
        return result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeType.BLASTING;
    }

    public static class Serializer implements RecipeSerializer<PurificationBlastingRecipe> {
        public static final Serializer INSTANCE = new Serializer(100);
        private final int defaultCookingTime;

        public Serializer(int cookingTime) {
            this.defaultCookingTime = cookingTime;
        }

        public PurificationBlastingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            String s = GsonHelper.getAsString(pJson, "group", "");
            CookingBookCategory cookingbookcategory = (CookingBookCategory)CookingBookCategory.CODEC.byName(GsonHelper.getAsString(pJson, "category", (String)null), CookingBookCategory.MISC);
            JsonElement jsonelement = GsonHelper.isArrayNode(pJson, "ingredient") ? GsonHelper.getAsJsonArray(pJson, "ingredient") : GsonHelper.getAsJsonObject(pJson, "ingredient");
            Ingredient ingredient = Ingredient.fromJson((JsonElement)jsonelement, false);
            if (!pJson.has("result")) {
                throw new JsonSyntaxException("Missing result, expected to find a string or object");
            } else {
                ItemStack itemstack;
                if (pJson.get("result").isJsonObject()) {
                    itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
                } else {
                    String s1 = GsonHelper.getAsString(pJson, "result");
                    ResourceLocation resourcelocation = new ResourceLocation(s1);
                    itemstack = new ItemStack((ItemLike) BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                        return new IllegalStateException("Item: " + s1 + " does not exist");
                    }));
                }

                float f = GsonHelper.getAsFloat(pJson, "experience", 0.0F);
                int i = GsonHelper.getAsInt(pJson, "cookingtime", this.defaultCookingTime);
                return new PurificationBlastingRecipe(pRecipeId, s, cookingbookcategory, ingredient, itemstack, f, i);
            }
        }

        public PurificationBlastingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            String s = pBuffer.readUtf();
            CookingBookCategory cookingbookcategory = (CookingBookCategory)pBuffer.readEnum(CookingBookCategory.class);
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack itemstack = pBuffer.readItem();
            float f = pBuffer.readFloat();
            int i = pBuffer.readVarInt();
            return new PurificationBlastingRecipe(pRecipeId, s, cookingbookcategory, ingredient, itemstack, f, i);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, PurificationBlastingRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.getGroup());
            pBuffer.writeEnum(pRecipe.category());
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeFloat(pRecipe.experience);
            pBuffer.writeVarInt(pRecipe.cookingTime);
        }
    }
}
