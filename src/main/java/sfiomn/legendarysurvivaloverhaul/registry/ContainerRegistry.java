package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.AbstractThermalContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.CoolerContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.HeaterContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;


public class ContainerRegistry
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(Registries.MENU_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<MenuType<AbstractThermalContainer>, ? extends MenuType<AbstractThermalContainer>> COOLER_CONTAINER = CONTAINERS.register("cooler_container", () -> IForgeMenuType.create(CoolerContainer::new));

    public static final DeferredHolder<MenuType<AbstractThermalContainer>, ? extends MenuType<AbstractThermalContainer>> HEATER_CONTAINER = CONTAINERS.register("heater_container", () -> IForgeMenuType.create(HeaterContainer::new));

    public static final DeferredHolder<MenuType<SewingTableContainer>, ? extends MenuType<SewingTableContainer>> SEWING_TABLE_CONTAINER = CONTAINERS.register("sewing_table_container", () -> IForgeMenuType.create(SewingTableContainer::new));

    public static void register(IEventBus eventBus)
    {
        CONTAINERS.register(eventBus);
    }
}
