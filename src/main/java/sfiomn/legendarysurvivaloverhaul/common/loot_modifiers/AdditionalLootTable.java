package sfiomn.legendarysurvivaloverhaul.common.loot_modifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.function.Supplier;

import static net.minecraft.world.level.storage.loot.LootTable.createStackSplitter;

public class AdditionalLootTable extends LootModifier {

    public static final Supplier<Codec<AdditionalLootTable>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(instance -> codecStart(instance)
                    .and(ResourceLocation.CODEC.fieldOf("lootTable").forGetter(m -> m.lootTable))
                    .and(Codec.BOOL.optionalFieldOf("replace", false).forGetter(m -> m.replace))
                    .apply(instance, AdditionalLootTable::new)
            )
    );

    private final ResourceLocation lootTable;
    private final boolean replace;

    public AdditionalLootTable(LootItemCondition[] conditions, ResourceLocation lootTable, boolean replace) {
        super(conditions);
        this.lootTable = lootTable;
        this.replace = replace;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (replace) {
            generatedLoot.clear();
        }

        ServerLevel level = context.getLevel();
        // noinspection deprecation
        context.getResolver().getLootTable(lootTable).getRandomItemsRaw(context, createStackSplitter(level, generatedLoot::add));
        // noinspection deprecation
        //context.getResolver().getLootTable(lootTable).getRandomItemsRaw(context, generatedLoot::add);
        LegendarySurvivalOverhaul.LOGGER.debug(context.getResolver().getLootTable(lootTable).getLootTableId());
        LegendarySurvivalOverhaul.LOGGER.debug("gen loot : " + generatedLoot);

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
