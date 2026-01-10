package sfiomn.legendarysurvivaloverhaul;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.wetness.WetnessUtil;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.CanteenProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.SeasonalCalendarSeasonTypeProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.SeasonalCalendarTimeProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.ThermometerProperty;
import sfiomn.legendarysurvivaloverhaul.client.screens.SewingTableScreen;
import sfiomn.legendarysurvivaloverhaul.client.screens.ThermalScreen;
import sfiomn.legendarysurvivaloverhaul.common.attachments.ModAttachments;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosEvents;
import sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons.EclipticSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.jsonConfig.JsonIntegrationConfigRegistration;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.vampirism.VampirismEvents;
import sfiomn.legendarysurvivaloverhaul.common.items.armor.ArmorMaterialBase;
import sfiomn.legendarysurvivaloverhaul.common.listeners.*;
import sfiomn.legendarysurvivaloverhaul.config.Config;
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

    /**
     * Serene Seasons and Better Weather both add their own seasons system,
     * so we'll probably want to integrate those with the temperature,
     * i.e. making it so that winter is colder, summer is hotter.
     */
    public static boolean betterWeatherLoaded = false;
    public static boolean sereneSeasonsLoaded = false;
    public static boolean eclipticSeasonsLoaded = false;

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
    public static boolean artifactsLoaded = false;
    public static boolean beachpartyLoaded = false;
    public static boolean meadowLoaded = false;
    public static boolean overflowingbarsLoaded = false;
    public static boolean weather2Loaded = false;
    public static boolean medsandherbsLoaded = false;
    public static boolean crayfishFurnitureLoaded = false;

    public static Path configPath = FMLPaths.CONFIGDIR.get();
    public static Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "legendarysurvivaloverhaul");
    public static Path modConfigJsons = Paths.get(modConfigPath.toString(), "json");
    public static Path modIntegrationConfigJsons = Paths.get(modConfigJsons.toString(), "integration");

    public LegendarySurvivalOverhaul(IEventBus modBus, ModContainer modContainer)
    {

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::onModConfigLoadEvent);
        modBus.addListener(this::onModConfigReloadEvent);
        modBus.addListener(this::onLoadComplete);

        // Register attachments
        ModAttachments.init(modBus);

        // Register data components
        DataComponentRegistry.init(modBus);

        IEventBus forgeBus = NeoForge.EVENT_BUS;

        forgeBus.addListener(this::addReloadListenerEvent);

        Config.register();

        AttributeRegistry.register(modBus);
        ArmorMaterialBase.register(modBus);
        ItemRegistry.register(modBus);
        MobEffectRegistry.register(modBus);
        LootModifierRegistry.register(modBus);
        BlockRegistry.register(modBus);
        ContainerRegistry.register(modBus);
        ParticleTypeRegistry.register(modBus);
        RecipeRegistry.register(modBus);
        SoundRegistry.register(modBus);
        TemperatureModifierRegistry.register(modBus);
        BlockEntityRegistry.register(modBus);
        FeatureRegistry.register(modBus);
        CreativeTabRegistry.register(modBus);

        forgeBus.addListener(CommandRegistry::registerCommandsEvent);

        modIntegration(forgeBus);
    }

    private void modIntegration(IEventBus forgeBus)
    {
        sereneSeasonsLoaded = ModList.get().isLoaded("sereneseasons");
        eclipticSeasonsLoaded = ModList.get().isLoaded("eclipticseasons");
        curiosLoaded = ModList.get().isLoaded("curios");
        terraFirmaCraftLoaded = ModList.get().isLoaded("tfc");
        vampirismLoaded = ModList.get().isLoaded("vampirism");
        originsLoaded = ModList.get().isLoaded("origins");
        mutantMonstersLoaded = ModList.get().isLoaded("mutantmonsters");
        supplementariesLoaded = ModList.get().isLoaded("supplementaries");
        artifactsLoaded = ModList.get().isLoaded("artifacts");
        beachpartyLoaded = ModList.get().isLoaded("beachparty");
        meadowLoaded = ModList.get().isLoaded("meadow");
        overflowingbarsLoaded = ModList.get().isLoaded("overflowingbars");
        medsandherbsLoaded = ModList.get().isLoaded("meds_and_herbs");
        crayfishFurnitureLoaded = ModList.get().isLoaded("refurbished_furniture");

        weather2Loaded = ModList.get().isLoaded("weather2");
        surviveLoaded = ModList.get().isLoaded("survive");

        if (sereneSeasonsLoaded)
            LOGGER.debug("Serene Seasons is loaded, enabling compatibility");

        if (eclipticSeasonsLoaded)
        {
            if (sereneSeasonsLoaded)
                LOGGER.debug("Ecliptic Seasons and Serene Seasons are loaded, please choose one over the other");
            else
                LOGGER.debug("Ecliptic Seasons is loaded, enabling compatibility");
        }

        if (terraFirmaCraftLoaded)
            LOGGER.debug("TerraFirmaCraft is loaded, enabling compatibility");

        if (mutantMonstersLoaded)
            LOGGER.debug("Mutant Monsters is loaded, enabling compatibility");

        if (curiosLoaded)
        {
            LOGGER.debug("Curios is loaded, enabling compatibility");
            forgeBus.register(CuriosEvents.class);
        }

        if (vampirismLoaded)
        {
            LOGGER.debug("Vampirism is loaded, enabling compatibility");
            forgeBus.register(VampirismEvents.class);
        }

//		if (originsLoaded) {
//			LOGGER.debug("Origins is loaded, enabling compatibility");
//			forgeBus.register(OriginsEvents.class);
//		}

        if (supplementariesLoaded)
            LOGGER.debug("Supplementarie is loaded, enabling compatibility");

        if (artifactsLoaded)
            LOGGER.debug("Artifacts is loaded, enabling compatibility");

        if (beachpartyLoaded)
            LOGGER.debug("Let's do Beachparty is loaded, enabling compatibility");

        if (meadowLoaded)
            LOGGER.debug("Let's do Meadow is loaded, enabling compatibility");

        if (overflowingbarsLoaded)
            LOGGER.debug("Overflowing Bars is loaded, enabling compatibility");

        if (medsandherbsLoaded)
            LOGGER.debug("Meds And Herbs is loaded, enabling compatibility");

        if (crayfishFurnitureLoaded)
            LOGGER.debug("MrCrayfish's Furniture Mod: Refurbished is loaded, enabling compatibility");

        if (weather2Loaded)
            LOGGER.debug("Weather2 is loaded, no compatibility for now");

        if (surviveLoaded)
            LOGGER.debug("Survive is loaded, I hope you know what you're doing");

        JsonIntegrationConfigRegistration.init(LegendarySurvivalOverhaul.modIntegrationConfigJsons.toFile());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {

            TemperatureUtil.internal = new TemperatureUtilInternal();
            ThirstUtil.internal = new ThirstUtilInternal();
            BodyDamageUtil.internal = new BodyDamageUtilInternal();
            WetnessUtil.internal = new WetnessUtilInternal();
            HealthUtil.internal = new HealthUtilInternal();

            TemperatureDataManager.internalConsumable = new TemperatureConsumableListener();
            TemperatureDataManager.internalConsumableBlock = new TemperatureConsumableBlockListener();
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
            BodyDamageDataManager.internalBodyResistanceItem = new BodyPartResistanceItemListener();
        });
    }

    private void onLoadComplete(final FMLLoadCompleteEvent event)
    {
        event.enqueueWork(() ->
        {
            BodyDamageUtilInternal.initMalusConfig();
            BodyDamageUtilInternal.initLimbEffects();

            if (sereneSeasonsLoaded)
                SereneSeasonsUtil.initAverageTemperatures();

            if (eclipticSeasonsLoaded)
                EclipticSeasonsUtil.initAverageTemperatures();
        });
    }

    private void onModConfigLoadEvent(ModConfigEvent.Loading event)
    {

        if (event.getConfig().getSpec() == Config.CLIENT_SPEC)
            Config.Baked.bakeClient();

        if (event.getConfig().getSpec() == Config.COMMON_SPEC)
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
        event.addListener((PreparableReloadListener) TemperatureDataManager.internalConsumableBlock);
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
        event.addListener((PreparableReloadListener) BodyDamageDataManager.internalBodyResistanceItem);
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            Config.Baked.bakeClient();

            ItemProperties.register(ItemRegistry.THERMOMETER.get(), ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "temperature"), new ThermometerProperty());
            ItemProperties.register(ItemRegistry.CANTEEN.get(), ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "thirstenum"), new CanteenProperty());
            ItemProperties.register(ItemRegistry.LARGE_CANTEEN.get(), ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "thirstenum"), new CanteenProperty());
            if (LegendarySurvivalOverhaul.sereneSeasonsLoaded || LegendarySurvivalOverhaul.eclipticSeasonsLoaded)
            {
                ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "time"), new SeasonalCalendarTimeProperty());
                ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "seasontype"), new SeasonalCalendarSeasonTypeProperty());
            }
        }

        @SubscribeEvent
        public static void onRegisterMenuScreens(RegisterMenuScreensEvent event)
        {
            event.register(ContainerRegistry.COOLER_CONTAINER.get(), ThermalScreen::new);
            event.register(ContainerRegistry.HEATER_CONTAINER.get(), ThermalScreen::new);
            event.register(ContainerRegistry.SEWING_TABLE_CONTAINER.get(), SewingTableScreen::new);
        }
    }
}
