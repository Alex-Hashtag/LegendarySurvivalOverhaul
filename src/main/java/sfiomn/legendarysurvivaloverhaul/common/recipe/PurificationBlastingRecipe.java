package sfiomn.legendarysurvivaloverhaul.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;

public class PurificationBlastingRecipe extends BlastingRecipe
{
    public PurificationBlastingRecipe(String group, CookingBookCategory cookingBookCategory, Ingredient ingredient, ItemStack result, float experience, int cookingTime)
    {
        super(group, cookingBookCategory, ingredient, result, experience, cookingTime);
    }

    @Override
    public boolean matches(SingleRecipeInput input, @NotNull Level level)
    {
        ItemStack inputStack = input.item();
        // Check if item type matches (ignoring NBT/enchantments) and has water
        return inputStack.getItem() instanceof CanteenItem && ThirstUtil.getCapacityTag(inputStack) > 0;
    }

    @Override
    public @NotNull ItemStack assemble(SingleRecipeInput input, @NotNull HolderLookup.Provider provider)
    {
        ItemStack inputStack = input.item();
        int hydrationCapacity = ThirstUtil.getCapacityTag(inputStack);
        
        // Create result with the SAME item type as input (preserves large vs regular canteen)
        ItemStack result = new ItemStack(inputStack.getItem());
        
        // Copy enchantments from input to result using DataComponents
        ItemEnchantments enchantments = inputStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (!enchantments.isEmpty()) {
            result.set(DataComponents.ENCHANTMENTS, enchantments);
        }
        
        // Set the purified hydration values
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, hydrationCapacity);
        
        return result;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider)
    {
        ItemStack result = this.result.copy();
        int maxHydrationCapacity = 0;
        if (this.result.getItem() instanceof CanteenItem resultItem)
        {
            maxHydrationCapacity = resultItem.getMaxCapacity();
        }
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, maxHydrationCapacity);
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType()
    {
        return RecipeType.BLASTING;
    }

    public static class Serializer implements RecipeSerializer<PurificationBlastingRecipe>
    {
        public static final Serializer INSTANCE = new Serializer(100);
        private static final StreamCodec<RegistryFriendlyByteBuf, PurificationBlastingRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, AbstractCookingRecipe::getGroup,
                ByteBufCodecs.fromCodec(CookingBookCategory.CODEC), AbstractCookingRecipe::category,
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
                ItemStack.STREAM_CODEC, r -> r.result,
                ByteBufCodecs.FLOAT, r -> r.experience,
                ByteBufCodecs.VAR_INT, r -> r.cookingTime,
                PurificationBlastingRecipe::new
        );
        private final int defaultCookingTime;

        public Serializer(int cookingTime)
        {
            this.defaultCookingTime = cookingTime;
        }

        @Override
        public MapCodec<PurificationBlastingRecipe> codec()
        {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(AbstractCookingRecipe::getGroup),
                    CookingBookCategory.CODEC.optionalFieldOf("category", CookingBookCategory.MISC).forGetter(AbstractCookingRecipe::category),
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.ingredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(r -> r.experience),
                    Codec.INT.optionalFieldOf("cookingtime", this.defaultCookingTime).forGetter(r -> r.cookingTime)
            ).apply(instance, (group, category, ingredient, result, exp, time) -> new PurificationBlastingRecipe(group, category, ingredient, result, exp, time)));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PurificationBlastingRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }
    }
}
