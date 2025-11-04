package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.common.items.*;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.*;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.*;

import java.util.List;

import static sfiomn.legendarysurvivaloverhaul.common.items.armor.ArmorMaterialBase.DESERT;
import static sfiomn.legendarysurvivaloverhaul.common.items.armor.ArmorMaterialBase.SNOW;


public class ItemRegistry
{

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, LegendarySurvivalOverhaul.MOD_ID);

    public static final DeferredHolder<Item, ? extends Item> THERMOMETER = ITEMS.register("thermometer", () -> new ThermometerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, ? extends Item> SEASONAL_CALENDAR = ITEMS.register("seasonal_calendar", () -> new SeasonalCalendarItem(new Item.Properties()));

    public static final DeferredHolder<Item, ? extends Item> NETHER_CHALICE = ITEMS.register("nether_chalice", () -> new NetherChaliceItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, ? extends Item> SPONGE = ITEMS.register("sponge", () -> new WearableCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE))
    {

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
        {
            super.appendHoverText(stack, context, tooltipComponents, isAdvanced);

            tooltipComponents.add(Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".sponge.description"));
        }
    });
    public static final DeferredHolder<Item, ? extends Item> HEAT_RESISTANCE_RING = ITEMS.register("heat_resistance_ring", () -> new WearableCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ? extends Item> COLD_RESISTANCE_RING = ITEMS.register("cold_resistance_ring", () -> new WearableCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ? extends Item> THERMAL_RESISTANCE_RING = ITEMS.register("thermal_resistance_ring", () -> new WearableCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, ? extends Item> FIRST_AID_SUPPLIES = ITEMS.register("first_aid_supplies", () -> new WearableCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE))
    {

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
        {
            super.appendHoverText(stack, context, tooltipComponents, isAdvanced);

            tooltipComponents.add(Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".first_aid_supplies.description"));
        }
    });

    public static final DeferredHolder<Item, ? extends Item> SNOW_HELMET = ITEMS.register("snow_helmet", () -> new ArmorItem(SNOW, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(6))));
    public static final DeferredHolder<Item, ? extends Item> SNOW_CHEST = ITEMS.register("snow_chestplate", () -> new ArmorItem(SNOW, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(6))));
    public static final DeferredHolder<Item, ? extends Item> SNOW_LEGGINGS = ITEMS.register("snow_leggings", () -> new ArmorItem(SNOW, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(6))));
    public static final DeferredHolder<Item, ? extends Item> SNOW_BOOTS = ITEMS.register("snow_boots", () -> new ArmorItem(SNOW, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(6)))
    {
        @Override
        public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer)
        {
            return true;
        }
    });

    public static final DeferredHolder<Item, ? extends Item> DESERT_HELMET = ITEMS.register("desert_helmet", () -> new ArmorItem(DESERT, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(6))));
    public static final DeferredHolder<Item, ? extends Item> DESERT_CHEST = ITEMS.register("desert_chestplate", () -> new ArmorItem(DESERT, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(6))));
    public static final DeferredHolder<Item, ? extends Item> DESERT_LEGGINGS = ITEMS.register("desert_leggings", () -> new ArmorItem(DESERT, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(6))));
    public static final DeferredHolder<Item, ? extends Item> DESERT_BOOTS = ITEMS.register("desert_boots", () -> new ArmorItem(DESERT, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(6))));

    public static final DeferredHolder<Item, ? extends Item> COOLING_COAT_1 = ITEMS.register("cooling_coat_1", () -> new CoatItem(CoatEnum.COOLING_1, new Item.Properties().rarity(Rarity.COMMON)));
    public static final DeferredHolder<Item, ? extends Item> COOLING_COAT_2 = ITEMS.register("cooling_coat_2", () -> new CoatItem(CoatEnum.COOLING_2, new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ? extends Item> COOLING_COAT_3 = ITEMS.register("cooling_coat_3", () -> new CoatItem(CoatEnum.COOLING_3, new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, ? extends Item> HEATING_COAT_1 = ITEMS.register("heating_coat_1", () -> new CoatItem(CoatEnum.HEATING_1, new Item.Properties().rarity(Rarity.COMMON)));
    public static final DeferredHolder<Item, ? extends Item> HEATING_COAT_2 = ITEMS.register("heating_coat_2", () -> new CoatItem(CoatEnum.HEATING_2, new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ? extends Item> HEATING_COAT_3 = ITEMS.register("heating_coat_3", () -> new CoatItem(CoatEnum.HEATING_3, new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, ? extends Item> THERMAL_COAT_1 = ITEMS.register("thermal_coat_1", () -> new CoatItem(CoatEnum.THERMAL_1, new Item.Properties().rarity(Rarity.COMMON)));
    public static final DeferredHolder<Item, ? extends Item> THERMAL_COAT_2 = ITEMS.register("thermal_coat_2", () -> new CoatItem(CoatEnum.THERMAL_2, new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ? extends Item> THERMAL_COAT_3 = ITEMS.register("thermal_coat_3", () -> new CoatItem(CoatEnum.THERMAL_3, new Item.Properties().rarity(Rarity.EPIC)));

    public static final DeferredHolder<Item, ? extends Item> COLD_STRING = ITEMS.register("cold_string", () -> new Item(new Item.Properties().rarity(Rarity.COMMON)));
    public static final DeferredHolder<Item, ? extends Item> WARM_STRING = ITEMS.register("warm_string", () -> new Item(new Item.Properties().rarity(Rarity.COMMON)));

    public static final DeferredHolder<Item, ? extends Item> SUN_FERN_SEEDS = ITEMS.register("sun_fern_seeds", () -> new ItemNameBlockItem(BlockRegistry.SUN_FERN_CROP.get(), new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> SUN_FERN = ITEMS.register("sun_fern_leaf", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> SUN_FERN_GOLD = ITEMS.register("sun_fern_gold_leaf", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> ICE_FERN_SEEDS = ITEMS.register("ice_fern_seeds", () -> new ItemNameBlockItem(BlockRegistry.ICE_FERN_CROP.get(), new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> ICE_FERN = ITEMS.register("ice_fern_leaf", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> ICE_FERN_GOLD = ITEMS.register("ice_fern_gold_leaf", () -> new Item(new Item.Properties()));

    // Thirst
    public static final DeferredHolder<Item, ? extends Item> WATER_PURIFIER = ITEMS.register("water_purifier", () -> new WearableCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE))
    {

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
        {
            super.appendHoverText(stack, context, tooltipComponents, isAdvanced);

            tooltipComponents.add(Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".water_purifier.description"));
        }
    });

    public static final DeferredHolder<Item, ? extends Item> CANTEEN = ITEMS.register("canteen", () -> new CanteenItem(new Item.Properties().rarity(Rarity.COMMON)));
    public static final DeferredHolder<Item, ? extends Item> LARGE_CANTEEN = ITEMS.register("large_canteen", () -> new LargeCanteenItem(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final DeferredHolder<Item, ? extends Item> APPLE_JUICE = ITEMS.register("apple_juice", () -> new JuiceItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> BEETROOT_JUICE = ITEMS.register("beetroot_juice", () -> new JuiceItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> CACTUS_JUICE = ITEMS.register("cactus_juice", () -> new JuiceItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> CARROT_JUICE = ITEMS.register("carrot_juice", () -> new JuiceItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> CHORUS_FRUIT_JUICE = ITEMS.register("chorus_fruit_juice", () -> new JuiceItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> GOLDEN_APPLE_JUICE = ITEMS.register("golden_apple_juice", () -> new JuiceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, ? extends Item> GOLDEN_CARROT_JUICE = ITEMS.register("golden_carrot_juice", () -> new JuiceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, ? extends Item> GLISTERING_MELON_JUICE = ITEMS.register("glistering_melon_juice", () -> new JuiceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, ? extends Item> MELON_JUICE = ITEMS.register("melon_juice", () -> new JuiceItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> PUMPKIN_JUICE = ITEMS.register("pumpkin_juice", () -> new JuiceItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> SWEET_BERRIES_JUICE = ITEMS.register("sweet_berries_juice", () -> new JuiceItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> GLOW_BERRIES_JUICE = ITEMS.register("glow_berries_juice", () -> new JuiceItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> PURIFIED_WATER_BOTTLE = ITEMS.register("purified_water_bottle", () -> new PurifiedWaterBottleItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> WATER_PLANT_BAG = ITEMS.register("water_plant_bag", () -> new DrinkItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> WATER_PLANT_SEEDS = ITEMS.register("water_plant_seeds", () -> new ItemNameBlockItem(BlockRegistry.WATER_PLANT_CROP.get(), new Item.Properties()));

    // Health Overhaul
    public static final DeferredHolder<Item, ? extends Item> HEART_CONTAINER = ITEMS.register("heart_container", () -> new HeartContainerItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, ? extends Item> HEART_FRAGMENT = ITEMS.register("heart_fragment", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ? extends Item> SHIELD_CONTAINER = ITEMS.register("shield_container", () -> new ShieldContainerItem(new Item.Properties().rarity(Rarity.EPIC)));

    // Body Healing
    public static final DeferredHolder<Item, ? extends Item> HEALING_HERBS = ITEMS.register("healing_herbs", () -> new HealingHerbsItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> PLASTER = ITEMS.register("plaster", () -> new PlasterItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> BANDAGE = ITEMS.register("bandage", () -> new BandageItem(new Item.Properties()));
    public static final DeferredHolder<Item, ? extends Item> TONIC = ITEMS.register("tonic", () -> new TonicItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ? extends Item> MEDKIT = ITEMS.register("medkit", () -> new Medkit(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, ? extends Item> MORPHINE = ITEMS.register("morphine", () -> new MorphineItem(new Item.Properties()));

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
