package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;
import sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider
{

    public static final String SEW_A_COAT_ADVANCEMENT =
            LegendarySurvivalOverhaul.MOD_ID + ":main/sew_a_coat";

    public ModAdvancementProvider(PackOutput packOutput,
                                  CompletableFuture<HolderLookup.Provider> lookupProvider,
                                  ExistingFileHelper existingFileHelper)
    {
        super(packOutput, lookupProvider, existingFileHelper, List.of(new LegendarySurvivalAdvancements()));
    }

    private static class LegendarySurvivalAdvancements implements AdvancementProvider.AdvancementGenerator
    {
        private LegendarySurvivalAdvancements()
        {
        }

        @Override
        public void generate(HolderLookup.@NotNull Provider registries,
                             @NotNull Consumer<AdvancementHolder> consumer,
                             @NotNull ExistingFileHelper existingFileHelper)
        {

            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(
                            ItemRegistry.THERMOMETER.get(),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID + ".description"),
                            ResourceLocation.parse("textures/block/ice.png"),
                            AdvancementType.TASK,
                            false, false, false
                    )
                    .addCriterion("main", PlayerTrigger.TriggerInstance.tick())
                    .save(consumer, LegendarySurvivalOverhaul.MOD_ID + ":main/root");

            AdvancementHolder get_a_thermometer = Advancement.Builder.advancement()
                    .display(
                            ItemRegistry.THERMOMETER.get(),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID + ".get_a_thermometer"),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID + ".get_a_thermometer.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .parent(root)
                    .addCriterion("inventory_changed",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.THERMOMETER.get()))
                    .save(consumer, LegendarySurvivalOverhaul.MOD_ID + ":main/get_a_thermometer");

            AdvancementHolder get_a_calendar = Advancement.Builder.advancement()
                    .display(
                            ItemRegistry.SEASONAL_CALENDAR.get(),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID + ".get_a_calendar"),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID + ".get_a_calendar.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .parent(root)
                    .addCriterion("inventory_changed",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SEASONAL_CALENDAR.get()))
                    .save(consumer, LegendarySurvivalOverhaul.MOD_ID + ":main/get_a_calendar");

            AdvancementHolder use_a_sewing_table = Advancement.Builder.advancement()
                    .display(
                            BlockRegistry.SEWING_TABLE.get(),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID + ".get_a_sewing_table"),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID + ".get_a_sewing_table.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .parent(root)
                    .addCriterion("inventory_changed",
                            InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegistry.SEWING_TABLE.get()))
                    .save(consumer, LegendarySurvivalOverhaul.MOD_ID + ":main/get_a_sewing_table");

            // The craftedItem trigger now expects List<ItemPredicate.Builder>
            List<ItemPredicate.Builder> itemPredicates = new ArrayList<>();
            for (CoatEnum coatEnum : CoatEnum.values())
            {
                CompoundTag coatTag = new CompoundTag();
                coatTag.putString(TemperatureUtilInternal.COAT_TAG, coatEnum.id());
                itemPredicates.add(
                        ItemPredicate.Builder.item()
                                .of(Tags.Items.ARMORS)
                );
            }

            AdvancementHolder sew_a_coat = Advancement.Builder.advancement()
                    .display(
                            ItemRegistry.COOLING_COAT_1.get(),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID + ".sew_a_coat"),
                            Component.translatable("advancement." + LegendarySurvivalOverhaul.MOD_ID + ".sew_a_coat.description"),
                            null, AdvancementType.TASK, true, true, false
                    )
                    .parent(use_a_sewing_table)
                    .addCriterion(
                            "recipe_crafted",
                            RecipeCraftedTrigger.TriggerInstance.craftedItem(
                                    RecipeRegistry.SEWING_RECIPE.getId(), itemPredicates
                            )
                    )
                    .save(consumer, SEW_A_COAT_ADVANCEMENT);
        }
    }
}
