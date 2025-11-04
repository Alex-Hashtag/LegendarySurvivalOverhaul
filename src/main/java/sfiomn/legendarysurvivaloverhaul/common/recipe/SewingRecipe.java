package sfiomn.legendarysurvivaloverhaul.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class SewingRecipe implements Recipe<RecipeInput>
{
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;

    public SewingRecipe(Ingredient base, Ingredient addition, ItemStack result)
    {
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public Ingredient getBase()
    {
        return base;
    }

    public Ingredient getAddition()
    {
        return addition;
    }

    @Override
    public boolean matches(@NotNull RecipeInput input, Level level)
    {
        if (level.isClientSide) return false;
        return base.test(input.getItem(0))
                && addition.test(input.getItem(1));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, @NotNull HolderLookup.Provider provider)
    {
        ItemStack itemstack = this.result.copy();
        ItemStack base = input.getItem(0);
        CustomData custom = base.get(DataComponents.CUSTOM_DATA);
        if (custom != null)
        {
            itemstack.set(DataComponents.CUSTOM_DATA, CustomData.of(custom.copyTag()));
        }
        return itemstack;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1)
    {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider)
    {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType()
    {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SewingRecipe>
    {
        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sewing");
    }

    public static class Serializer implements RecipeSerializer<SewingRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sewing");

        private static final MapCodec<SewingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("base").forGetter(SewingRecipe::getBase),
                Ingredient.CODEC.fieldOf("addition").forGetter(SewingRecipe::getAddition),
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result)
        ).apply(instance, SewingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, SewingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.base,
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.addition,
                ItemStack.STREAM_CODEC, r -> r.result,
                SewingRecipe::new
        );

        @Override
        public MapCodec<SewingRecipe> codec()
        {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SewingRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }
    }

}
