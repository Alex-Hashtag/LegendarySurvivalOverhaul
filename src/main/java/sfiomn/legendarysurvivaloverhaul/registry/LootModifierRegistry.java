package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.loot_modifiers.AdditionalLootTable;


public class LootModifierRegistry
{

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(Registries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<Codec<AdditionalLootTable>, ? extends Codec<AdditionalLootTable>> ADDITIONAL_LOOT_TABLE_MODIFIER = LOOT_MODIFIERS.register("additional_loot_table", AdditionalLootTable.CODEC);

    public static void register(IEventBus eventBus)
    {
        LOOT_MODIFIERS.register(eventBus);
    }
}
