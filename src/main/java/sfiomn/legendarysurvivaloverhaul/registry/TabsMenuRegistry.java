package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.tabs_menu.TabBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.origins.OriginsDynamicModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.origins.OriginsModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftHeatItemModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftModifier;
import sfiomn.legendarysurvivaloverhaul.common.tabs_menu.BodyDamageTab;
import sfiomn.legendarysurvivaloverhaul.common.tabs_menu.FtbQuestsTab;
import sfiomn.legendarysurvivaloverhaul.common.tabs_menu.InventoryTab;
import sfiomn.legendarysurvivaloverhaul.common.tabs_menu.ReskillableTab;
import sfiomn.legendarysurvivaloverhaul.common.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.CoatModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.ItemModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic.ResistanceAttributeModifier;

import java.util.function.Supplier;

public class TabsMenuRegistry
{
	public static final ResourceLocation TABS_MENU_RESOURCE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "tabs_menu");

	public static final DeferredRegister<TabBase> TABS_MENU = DeferredRegister.create(TABS_MENU_RESOURCE, LegendarySurvivalOverhaul.MOD_ID);

	public static final Supplier<IForgeRegistry<TabBase>> TABS_MENU_REGISTRY = TABS_MENU.makeRegistry(RegistryBuilder::new);

	// Base Tab
	public static final RegistryObject<TabBase> INVENTORY = TABS_MENU.register("inventory", InventoryTab::new);
	public static final RegistryObject<TabBase> BODY_DAMAGE = TABS_MENU.register("body_damage", BodyDamageTab::new);

	public static void register(IEventBus eventBus){
		TABS_MENU.register(eventBus);
	}
}
