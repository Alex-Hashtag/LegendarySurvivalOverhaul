package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.PurityEnchantment;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.RefreshingEnchantment;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.ReservoirEnchantment;

public class EnchantmentRegistry {
    
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, LegendarySurvivalOverhaul.MOD_ID);
    
    public static final RegistryObject<Enchantment> REFRESHING = ENCHANTMENTS.register("refreshing", RefreshingEnchantment::new);
    public static final RegistryObject<Enchantment> PURITY = ENCHANTMENTS.register("purity", PurityEnchantment::new);
    public static final RegistryObject<Enchantment> RESERVOIR = ENCHANTMENTS.register("reservoir", ReservoirEnchantment::new);
    
    public static void register(IEventBus modBus) {
        ENCHANTMENTS.register(modBus);
    }
}
