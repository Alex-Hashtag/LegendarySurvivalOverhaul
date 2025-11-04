package sfiomn.legendarysurvivaloverhaul.common.containers;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;
import sfiomn.legendarysurvivaloverhaul.common.recipe.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ContainerRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static sfiomn.legendarysurvivaloverhaul.common.integration.mutantmonsters.MutantMonstersUtil.isMutantMonstersArmor;
import static sfiomn.legendarysurvivaloverhaul.data.providers.ModAdvancementProvider.SEW_A_COAT_ADVANCEMENT;

public class SewingTableContainer extends ItemCombinerMenu
{
    public static final int INPUT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;

    @Nullable
    private RecipeHolder<SewingRecipe> selectedRecipe; // holder, not the raw recipe

    public SewingTableContainer(int windowId, Inventory playerInventory, FriendlyByteBuf data)
    {
        this(windowId, playerInventory, ContainerLevelAccess.NULL);
    }

    public SewingTableContainer(int windowId, Inventory playerInventory, ContainerLevelAccess access)
    {
        super(ContainerRegistry.SEWING_TABLE_CONTAINER.get(), windowId, playerInventory, access);
    }

    public static boolean isItemArmor(ItemStack itemStack)
    {
        return itemStack.getItem() instanceof ArmorItem || isMutantMonstersArmor(itemStack.getItem());
    }

    public static boolean isItemCoat(ItemStack itemStack)
    {
        return itemStack.getItem() instanceof CoatItem;
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions()
    {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(INPUT_SLOT, 18, 39, (itemStack) -> true)
                .withSlot(ADDITIONAL_SLOT, 65, 39, (itemStack) -> true)
                .withResultSlot(RESULT_SLOT, 134, 39)
                .build();
    }

    @Override
    public void createResult()
    {
        // Adapt menu input slots to RecipeInput (1.21 API)
        net.minecraft.world.item.crafting.RecipeInput recipeInput = new net.minecraft.world.item.crafting.RecipeInput()
        {
            @Override
            public ItemStack getItem(int index)
            {
                return inputSlots.getItem(index);
            }

            @Override
            public int size()
            {
                return inputSlots.getContainerSize();
            }
        };

        // Now returns RecipeHolder<SewingRecipe>
        List<RecipeHolder<SewingRecipe>> sewingRecipes =
                this.player.level().getRecipeManager()
                        .getRecipesFor(SewingRecipe.Type.INSTANCE, recipeInput, this.player.level());

        ItemStack itemStack = ItemStack.EMPTY;

        // If not doing coat-on-armor, try normal recipe
        if (!isItemArmor(inputSlots.getItem(INPUT_SLOT)) || !isItemCoat(inputSlots.getItem(ADDITIONAL_SLOT)))
        {
            if (!sewingRecipes.isEmpty())
            {
                this.selectedRecipe = sewingRecipes.get(0);
                itemStack = this.selectedRecipe.value().assemble(
                        recipeInput, this.player.level().registryAccess());
                // set the holder, not the recipe
                this.resultSlots.setRecipeUsed(this.selectedRecipe);
            }
        } else
        {
            // Coat not already applied
            if (!Objects.equals(
                    TemperatureUtil.getArmorCoatTag(inputSlots.getItem(INPUT_SLOT)),
                    ((CoatItem) (inputSlots.getItem(ADDITIONAL_SLOT).getItem())).coat.id()))
            {

                if (!sewingRecipes.isEmpty())
                {
                    this.selectedRecipe = sewingRecipes.get(0);
                    itemStack = this.selectedRecipe.value().assemble(
                            recipeInput, this.player.level().registryAccess());
                    this.resultSlots.setRecipeUsed(this.selectedRecipe);
                } else
                {
                    // Fallback: apply coat tag directly
                    itemStack = this.inputSlots.getItem(INPUT_SLOT).copy();
                    CoatItem coatItem = (CoatItem) inputSlots.getItem(ADDITIONAL_SLOT).getItem();
                    TemperatureUtil.setArmorCoatTag(itemStack, coatItem.coat.id());

                    if (player instanceof ServerPlayer serverPlayer)
                    {
                        // Advancements now use AdvancementHolder
                        AdvancementHolder sewCoatAdvancement =
                                serverPlayer.server.getAdvancements().get(ResourceLocation.parse(SEW_A_COAT_ADVANCEMENT));
                        if (sewCoatAdvancement != null)
                        {
                            for (String criteria : serverPlayer.getAdvancements()
                                    .getOrStartProgress(sewCoatAdvancement)
                                    .getRemainingCriteria())
                            {
                                serverPlayer.getAdvancements().award(sewCoatAdvancement, criteria);
                            }
                        }
                    }
                }
            }
        }

        this.resultSlots.setItem(0, itemStack);
        this.broadcastChanges();
    }

    private void shrinkStackInSlot(int index)
    {
        this.inputSlots.removeItem(index, 1);
    }

    @Override
    protected boolean mayPickup(Player player, boolean b)
    {
        return true;
    }

    @Override
    protected void onTake(Player player, ItemStack itemStack)
    {
        itemStack.onCraftedBy(player.level(), player, itemStack.getCount());
        this.resultSlots.awardUsedRecipes(player, Collections.singletonList(itemStack));

        this.shrinkStackInSlot(INPUT_SLOT);
        this.shrinkStackInSlot(ADDITIONAL_SLOT);

        this.player.level().playSound(null, player.blockPosition(), SoundRegistry.SEWING_TABLE.get(), SoundSource.BLOCKS);
    }

    @Override
    protected boolean isValidBlock(BlockState blockState)
    {
        return blockState.is(BlockRegistry.SEWING_TABLE.get());
    }
}
