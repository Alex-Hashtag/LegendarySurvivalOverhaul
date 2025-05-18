package sfiomn.legendarysurvivaloverhaul;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.wetness.WetnessUtil;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.CanteenProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.SeasonalCalendarTimeProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.SeasonalCalendarSeasonTypeProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.ThermometerProperty;
import sfiomn.legendarysurvivaloverhaul.client.screens.SewingTableScreen;
import sfiomn.legendarysurvivaloverhaul.client.screens.ThermalScreen;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosEvents;
import sfiomn.legendarysurvivaloverhaul.common.integration.jsonConfig.JsonIntegrationConfigRegistration;
import sfiomn.legendarysurvivaloverhaul.common.integration.origins.OriginsEvents;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.vampirism.VampirismEvents;
import sfiomn.legendarysurvivaloverhaul.common.listeners.*;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.registry.*;
import sfiomn.legendarysurvivaloverhaul.util.internal.*;

import java.nio.file.Path;
import java.nio.file.Paths;


@SuppressWarnings("unused")
@Mod(LegendarySurvivalOverhaul.MOD_ID)
public class LegendarySurvivalOverhaul
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "legendarysurvivaloverhaul";

	/** Serene Seasons and Better Weather both add their own seasons system,
	 *  so we'll probably want to integrate those with the temperature,
	 *  i.e. making it so that winter is colder, summer is hotter.
	 */
	public static boolean betterWeatherLoaded = false;
	public static boolean sereneSeasonsLoaded = false;

	/**
	 * TerraFirmaCraft temperature calculation already takes into account
	 * Biome, Season, daily time & elevation. The TerraFirmaCraft modifier will
	 * take directly this calculation, and all similar modifiers will be disabled
	 */
	public static boolean terraFirmaCraftLoaded = false;

	/**
	 * Since my mod and Survive both do very similar things, it might be
	 * a good idea to let the user know that should probably only choose
	 * one or the other unless they know what they're doing,
	 * Also it should only show this type of warning once so that we don't
	 * annoy the player if they decide to go through with it.
	 */
	public static boolean surviveLoaded = false;
	public static boolean curiosLoaded = false;

	public static boolean vampirismLoaded = false;
	public static boolean originsLoaded = false;
	public static boolean mutantMonstersLoaded = false;
	public static boolean supplementariesLoaded = false;
	public static boolean beachpartyLoaded = false;
	public static boolean meadowLoaded = false;
	public static boolean overflowingbarsLoaded = false;

	public static Path configPath = FMLPaths.CONFIGDIR.get();
	public static Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "legendarysurvivaloverhaul");
	public static Path modConfigJsons = Paths.get(modConfigPath.toString(), "json");
	public static Path modIntegrationConfigJsons = Paths.get(modConfigJsons.toString(), "integration");
	
	public LegendarySurvivalOverhaul()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		
		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onModConfigLoadEvent);
		modBus.addListener(this::onLoadComplete);

		forgeBus.addListener(this::addReloadListenerEvent);

		Config.register();

		AttributeRegistry.register(modBus);
		ItemRegistry.register(modBus);
		LootModifierRegistry.register(modBus);
		BlockRegistry.register(modBus);
		ContainerRegistry.register(modBus);
		MobEffectRegistry.register(modBus);
		ParticleTypeRegistry.register(modBus);
		RecipeRegistry.register(modBus);
		SoundRegistry.register(modBus);
		TemperatureModifierRegistry.register(modBus);
		BlockEntityRegistry.register(modBus);
		FeatureRegistry.register(modBus);
		CreativeTabRegistry.register(modBus);

		forgeBus.addListener(CommandRegistry::registerCommandsEvent);
		forgeBus.addListener(this::registerCapabilities);

		forgeBus.register(this);

		modIntegration(forgeBus);
	}
	
	private void modIntegration(IEventBus forgeBus)
	{
		sereneSeasonsLoaded = ModList.get().isLoaded("sereneseasons");
		curiosLoaded = ModList.get().isLoaded("curios");
		surviveLoaded = ModList.get().isLoaded("survive");
		terraFirmaCraftLoaded = ModList.get().isLoaded("tfc");
		vampirismLoaded = ModList.get().isLoaded("vampirism");
		originsLoaded = ModList.get().isLoaded("origins");
		mutantMonstersLoaded = ModList.get().isLoaded("mutantmonsters");
		supplementariesLoaded = ModList.get().isLoaded("supplementaries");
		beachpartyLoaded = ModList.get().isLoaded("beachparty");
		meadowLoaded = ModList.get().isLoaded("meadow");
		overflowingbarsLoaded = ModList.get().isLoaded("overflowingbars");

		if (sereneSeasonsLoaded)
			LOGGER.debug("Serene Seasons is loaded, enabling compatibility");

		if (terraFirmaCraftLoaded)
			LOGGER.debug("TerraFirmaCraft is loaded, enabling compatibility");

		if (mutantMonstersLoaded)
			LOGGER.debug("Mutant Monsters is loaded, enabling compatibility");

		if (curiosLoaded) {
			LOGGER.debug("Curios is loaded, enabling compatibility");
			forgeBus.register(CuriosEvents.class);
		}

		if (vampirismLoaded) {
			LOGGER.debug("Vampirism is loaded, enabling compatibility");
			forgeBus.register(VampirismEvents.class);
		}

		if (originsLoaded) {
			LOGGER.debug("Origins is loaded, enabling compatibility");
			forgeBus.register(OriginsEvents.class);
		}

		if (supplementariesLoaded)
			LOGGER.debug("Supplementarie is loaded, enabling compatibility");

		if (beachpartyLoaded)
			LOGGER.debug("Let's do Beachparty is loaded, enabling compatibility");

		if (meadowLoaded)
			LOGGER.debug("Let's do Meadow is loaded, enabling compatibility");

		if (overflowingbarsLoaded)
			LOGGER.debug("Overflowing Bars is loaded, enabling compatibility");

		if (surviveLoaded)
			LOGGER.debug("Survive is loaded, I hope you know what you're doing");

		JsonIntegrationConfigRegistration.init(LegendarySurvivalOverhaul.modIntegrationConfigJsons.toFile());
	}
	
	private void commonSetup(final FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {
			NetworkHandler.register();

			TemperatureUtil.internal = new TemperatureUtilInternal();
			ThirstUtil.internal = new ThirstUtilInternal();
			BodyDamageUtil.internal = new BodyDamageUtilInternal();
			WetnessUtil.internal = new WetnessUtilInternal();
			HealthUtil.internal = new HealthUtilInternal();

			TemperatureDataManager.internalConsumable = new TemperatureConsumableListener();
			TemperatureDataManager.internalBlock = new TemperatureBlockListener();
			TemperatureDataManager.internalItem = new TemperatureItemListener();
			TemperatureDataManager.internalBiome = new TemperatureBiomeListener();
			TemperatureDataManager.internalFuelItem = new TemperatureFuelItemListener();
			TemperatureDataManager.internalDimension = new TemperatureDimensionListener();
			TemperatureDataManager.internalMount = new TemperatureMountListener();
			TemperatureDataManager.internalOrigin = new TemperatureOriginListener();

			ThirstDataManager.internalConsumable = new ThirstConsumableListener();
			ThirstDataManager.internalBlock = new ThirstBlockListener();

			BodyDamageDataManager.internalBodyPartsDamageSource = new BodyPartsDamageSourceListener();
			BodyDamageDataManager.internalHealingConsumable = new BodyDamageHealingConsumableListener();
		});
	}

	private void onLoadComplete(final FMLLoadCompleteEvent event)
	{
		event.enqueueWork(() ->
		{
			BodyDamageUtilInternal.initMalusConfig();
			BodyDamageUtilInternal.initFirstAidSuppliesBoostingEffects();

			if (sereneSeasonsLoaded)
				SereneSeasonsUtil.initAverageTemperatures();

			MobEffectRegistry.registerBrewingRecipes();
		});
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(TemperatureCapability.class);
		event.register(WetnessCapability.class);
		event.register(ThirstCapability.class);
		event.register(HealthCapability.class);
		event.register(TemperatureItemCapability.class);
		event.register(FoodCapability.class);
		event.register(BodyDamageCapability.class);
	}

	private void onModConfigLoadEvent(ModConfigEvent.Loading event)
	{
		final ModConfig config = event.getConfig();

		if (config.getSpec() == Config.CLIENT_SPEC)
			Config.Baked.bakeClient();

		if (config.getSpec() == Config.COMMON_SPEC)
			Config.Baked.bakeCommon();
	}

	private void onModConfigReloadEvent(ModConfigEvent.Reloading event)
	{
		final ModConfig config = event.getConfig();

		// Since client config is not shared, we want it to update instantly whenever it's saved
		if (config.getSpec() == Config.CLIENT_SPEC)
			Config.Baked.bakeClient();
	}

	private void addReloadListenerEvent(final AddReloadListenerEvent event)
	{
		event.addListener((PreparableReloadListener) TemperatureDataManager.internalConsumable);
		event.addListener((PreparableReloadListener) TemperatureDataManager.internalBlock);
		event.addListener((PreparableReloadListener) TemperatureDataManager.internalItem);
		event.addListener((PreparableReloadListener) TemperatureDataManager.internalBiome);
		event.addListener((PreparableReloadListener) TemperatureDataManager.internalFuelItem);
		event.addListener((PreparableReloadListener) TemperatureDataManager.internalDimension);
		event.addListener((PreparableReloadListener) TemperatureDataManager.internalMount);
		event.addListener((PreparableReloadListener) TemperatureDataManager.internalOrigin);
		event.addListener((PreparableReloadListener) ThirstDataManager.internalConsumable);
		event.addListener((PreparableReloadListener) ThirstDataManager.internalBlock);
		event.addListener((PreparableReloadListener) BodyDamageDataManager.internalBodyPartsDamageSource);
		event.addListener((PreparableReloadListener) BodyDamageDataManager.internalHealingConsumable);
	}

	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			Config.Baked.bakeClient();

			MenuScreens.register(ContainerRegistry.COOLER_CONTAINER.get(), ThermalScreen::new);
			MenuScreens.register(ContainerRegistry.HEATER_CONTAINER.get(), ThermalScreen::new);
			MenuScreens.register(ContainerRegistry.SEWING_TABLE_CONTAINER.get(), SewingTableScreen::new);

			ItemProperties.register(ItemRegistry.THERMOMETER.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature"), new ThermometerProperty());
			ItemProperties.register(ItemRegistry.CANTEEN.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirstenum"), new CanteenProperty());
			ItemProperties.register(ItemRegistry.LARGE_CANTEEN.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirstenum"), new CanteenProperty());
			if (LegendarySurvivalOverhaul.sereneSeasonsLoaded) {
				ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "time"), new SeasonalCalendarTimeProperty());
				ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "seasontype"), new SeasonalCalendarSeasonTypeProperty());
			}
		}
	}
}
