package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.loot_modifiers.AdditionalLootTable;


public class LootModifierRegistry
{

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AdditionalLootTable>> ADDITIONAL_LOOT_TABLE_MODIFIER = LOOT_MODIFIERS.register("additional_loot_table", AdditionalLootTable.CODEC);

    public static void register(IEventBus eventBus)
    {
        LOOT_MODIFIERS.register(eventBus);
    }
}
