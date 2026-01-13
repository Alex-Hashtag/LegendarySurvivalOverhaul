package sfiomn.legendarysurvivaloverhaul.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;

public class RemoveCoatRecipe extends CustomRecipe
{
    private final CraftingBookCategory category;

    public RemoveCoatRecipe(CraftingBookCategory category)
    {
        super(category);
        this.category = category;
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level)
    {
        if (level.isClientSide) return false;

        ItemStack armorStack = ItemStack.EMPTY;
        ItemStack shearsStack = ItemStack.EMPTY;
        int itemCount = 0;

        for (int i = 0; i < input.size(); i++)
        {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty())
            {
                itemCount++;
                if (stack.getItem() instanceof ShearsItem)
                {
                    shearsStack = stack;
                }
                else if (!TemperatureUtil.getArmorCoatTag(stack).isEmpty())
                {
                    armorStack = stack;
                }
            }
        }

        return itemCount == 2 && !armorStack.isEmpty() && !shearsStack.isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, @NotNull HolderLookup.Provider provider)
    {
        ItemStack armorStack = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++)
        {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && !(stack.getItem() instanceof ShearsItem) && !TemperatureUtil.getArmorCoatTag(stack).isEmpty())
            {
                armorStack = stack;
                break;
            }
        }

        if (!armorStack.isEmpty())
        {
            ItemStack result = armorStack.copy();
            TemperatureUtil.removeArmorCoatTag(result);
            return result;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    public CraftingBookCategory category()
    {
        return this.category;
    }

    public static class Serializer implements RecipeSerializer<RemoveCoatRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "remove_coat");

        private static final MapCodec<RemoveCoatRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(RemoveCoatRecipe::category)
        ).apply(instance, RemoveCoatRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, RemoveCoatRecipe> STREAM_CODEC = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC, RemoveCoatRecipe::category,
                RemoveCoatRecipe::new
        );

        @Override
        public MapCodec<RemoveCoatRecipe> codec()
        {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RemoveCoatRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }
    }
}
