package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.CoolerContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.HeaterContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;


public class ContainerRegistry
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(Registries.MENU, LegendarySurvivalOverhaul.MOD_ID);

    public static void register(IEventBus eventBus)
    {
        CONTAINERS.register(eventBus);
    }    public static final DeferredHolder<MenuType<?>, MenuType<CoolerContainer>> COOLER_CONTAINER =
            CONTAINERS.register("cooler_container", () -> IMenuTypeExtension.create(CoolerContainer::new));

    public static final DeferredHolder<MenuType<?>, MenuType<HeaterContainer>> HEATER_CONTAINER =
            CONTAINERS.register("heater_container", () -> IMenuTypeExtension.create(HeaterContainer::new));

    public static final DeferredHolder<MenuType<?>, MenuType<SewingTableContainer>> SEWING_TABLE_CONTAINER =
            CONTAINERS.register("sewing_table_container", () -> IMenuTypeExtension.create(SewingTableContainer::new));


}
