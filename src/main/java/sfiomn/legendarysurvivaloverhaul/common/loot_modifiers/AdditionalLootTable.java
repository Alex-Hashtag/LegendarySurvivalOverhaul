package sfiomn.legendarysurvivaloverhaul.common.loot_modifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.function.Supplier;

import static net.minecraft.world.level.storage.loot.LootTable.createStackSplitter;

public class AdditionalLootTable extends LootModifier
{

    public static final Supplier<MapCodec<AdditionalLootTable>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.mapCodec(instance -> codecStart(instance)
                    .and(ResourceLocation.CODEC.fieldOf("lootTable").forGetter(m -> m.lootTable))
                    .and(com.mojang.serialization.Codec.BOOL.optionalFieldOf("replace", false).forGetter(m -> m.replace))
                    .apply(instance, AdditionalLootTable::new)
            )
    );

    private final ResourceLocation lootTable;
    private final boolean replace;

    public AdditionalLootTable(LootItemCondition[] conditions, ResourceLocation lootTable, boolean replace)
    {
        super(conditions);
        this.lootTable = lootTable;
        this.replace = replace;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
    {
        if (replace)
        {
            generatedLoot.clear();
        }

        ServerLevel level = context.getLevel();
        ResourceKey<LootTable> lootTableKey = ResourceKey.create(Registries.LOOT_TABLE, lootTable);
        context.getResolver().get(Registries.LOOT_TABLE, lootTableKey).ifPresent(table -> {
            // noinspection deprecation
            table.value().getRandomItemsRaw(context, createStackSplitter(level, generatedLoot::add));
            LegendarySurvivalOverhaul.LOGGER.debug(lootTableKey);
            LegendarySurvivalOverhaul.LOGGER.debug("gen loot : " + generatedLoot);
        });

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec()
    {
        return CODEC.get();
    }
}
