package sfiomn.legendarysurvivaloverhaul.common.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

public class RemoveCoatRecipe extends CustomRecipe {
    public RemoveCoatRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer craftingContainer, @NotNull Level level) {
        int armorSlot = -1;
        int shearsSlot = -1;

        for (int i = 0; i < craftingContainer.getContainerSize(); i++) {
            ItemStack stack = craftingContainer.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.getItem() instanceof ShearsItem) {
                if (shearsSlot != -1) {
                    return false;
                }
                shearsSlot = i;
                continue;
            }

            if (SewingTableContainer.isItemArmor(stack) && !TemperatureUtil.getArmorCoatTag(stack).isEmpty()) {
                if (armorSlot != -1) {
                    return false;
                }
                armorSlot = i;
                continue;
            }

            return false;
        }

        return armorSlot != -1 && shearsSlot != -1;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer craftingContainer, @NotNull RegistryAccess registryAccess) {
        for (int i = 0; i < craftingContainer.getContainerSize(); i++) {
            ItemStack stack = craftingContainer.getItem(i);
            if (!stack.isEmpty() && SewingTableContainer.isItemArmor(stack) && !TemperatureUtil.getArmorCoatTag(stack).isEmpty()) {
                ItemStack result = stack.copy();
                result.setCount(1);
                TemperatureUtil.removeArmorCoatTag(result);
                return result;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingContainer craftingContainer) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(craftingContainer.getContainerSize(), ItemStack.EMPTY);
        RandomSource random = RandomSource.create();

        for (int i = 0; i < craftingContainer.getContainerSize(); i++) {
            ItemStack stack = craftingContainer.getItem(i);
            if (stack.getItem() instanceof ShearsItem) {
                ItemStack shears = stack.copy();
                if (shears.hurt(1, random, null)) {
                    shears = ItemStack.EMPTY;
                }
                remaining.set(i, shears);
            }
        }

        return remaining;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.REMOVE_COAT_SERIALIZER.get();
    }
}
