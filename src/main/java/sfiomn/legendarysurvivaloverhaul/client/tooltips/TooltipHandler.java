package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonMobEffect;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.ModEnchantments;
import sfiomn.legendarysurvivaloverhaul.common.integration.artifacts.ArtifactsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.beachparty.BeachpartyUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EventBusSubscriber(
        modid = LegendarySurvivalOverhaul.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME,
        value = Dist.CLIENT
)
public class TooltipHandler
{

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(stack.getItem());

        if (!stack.isEmpty() && itemRegistryName != null)
        {
            List<Component> tooltips = event.getToolTip();

            for (Component component : tooltips)
            {
                if (component instanceof MutableComponent mutableComponent)
                {
                    if (componentHasOneOfKeys(component,
                            AttributeRegistry.HEATING_TEMPERATURE.get().getDescriptionId(),
                            AttributeRegistry.HEAT_RESISTANCE.get().getDescriptionId()))
                        mutableComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16420407)));
                    if (componentHasOneOfKeys(component,
                            AttributeRegistry.COOLING_TEMPERATURE.get().getDescriptionId(),
                            AttributeRegistry.COLD_RESISTANCE.get().getDescriptionId()))
                        mutableComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303)));
                    if (componentHasOneOfKeys(component, AttributeRegistry.THERMAL_RESISTANCE.get().getDescriptionId()))
                        mutableComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(10040319)));
                }
            }

            if (Config.Baked.temperatureEnabled)
            {

                if (stack.getItem() instanceof ArmorItem)
                {
                    addCoatTemperatureOnArmorText(stack, tooltips);
                }

                addFoodEffectText(stack, tooltips);
            }

            if (Config.Baked.localizedBodyDamageEnabled)
                addHealingText(stack, tooltips);

            if (LegendarySurvivalOverhaul.beachpartyLoaded)
                addShadeText(stack, tooltips);
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.GatherComponents event)
    {
        if (event.isCanceled())
            return;

        Minecraft mc = Minecraft.getInstance();
        Screen gui = mc.screen;
        if (gui == null)
            return;

        if (Config.Baked.thirstEnabled && Config.Baked.showHydrationTooltip)
            addHydrationTooltip(event.getItemStack(), event.getTooltipElements());
    }

    private static boolean componentHasOneOfKeys(Component component, String... keys)
    {
        if (component != null && component.getContents() instanceof TranslatableContents translatableContents &&
                translatableContents.getArgs() != null)
        {
            return Arrays.stream(translatableContents.getArgs()).anyMatch(s -> {
                if (s instanceof MutableComponent mutableComponent &&
                        mutableComponent.getContents() instanceof TranslatableContents translatableContents1)
                {
                    for (String key : keys)
                    {
                        if (translatableContents1.getKey().equals(key))
                            return true;
                    }
                    return false;
                } else
                    return false;
            });
        }
        return false;
    }

    private static void addCoatTemperatureOnArmorText(ItemStack stack, List<Component> tooltips)
    {
        String coatId = TemperatureUtil.getArmorCoatTag(stack);
        CoatEnum coat = CoatEnum.getFromId(coatId);

        Component text;

        if (coat != null)
        {
            text = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor_coat." + coatId);
        } else
        {
            return;
        }

        text = Component.literal("")
                .withStyle(ChatFormatting.BLUE)
                .append(text);

        tooltips.add(text);
    }

    private static void addFoodEffectText(ItemStack stack, List<Component> tooltips)
    {
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(stack.getItem());
        List<JsonTemperatureConsumable> jtcs = TemperatureDataManager.getConsumable(itemRegistryName);

        if (jtcs != null)
        {
            for (JsonTemperatureConsumable jtc : jtcs)
            {
                MobEffectInstance effectInstance = new MobEffectInstance(net.minecraft.core.Holder.direct(jtc.getEffect()), jtc.duration, Math.abs(jtc.temperatureLevel));
                MutableComponent mutableComponent = Component.translatable(effectInstance.getDescriptionId());

                if (Math.abs(jtc.temperatureLevel) > 1)
                {
                    mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + (Math.abs(jtc.temperatureLevel) - 1)));
                }

                if (jtc.duration > 20)
                {
                    mutableComponent = Component.translatable("potion.withDuration", mutableComponent, MobEffectUtil.formatDuration(effectInstance, 1.0f, 20.0f));
                }

                if (jtc.getEffect() == MobEffectRegistry.COLD_FOOD.get() || jtc.getEffect() == MobEffectRegistry.COLD_DRINK.get())
                    tooltips.add(mutableComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303))));

                if (jtc.getEffect() == MobEffectRegistry.HOT_FOOD.get())
                    tooltips.add(mutableComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16420407))));
            }
        }
    }

    private static void addHealingText(ItemStack stack, List<Component> tooltips)
    {

        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(stack.getItem());
        JsonHealingConsumable jsonConsumableHeal = BodyDamageDataManager.getHealingItem(itemRegistryName);

        if (jsonConsumableHeal != null)
        {
            if (jsonConsumableHeal.healingCharges > 0)
            {
                tooltips.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.body_part", jsonConsumableHeal.healingCharges).withStyle(ChatFormatting.BLUE));
            } else if (jsonConsumableHeal.healingCharges == 0)
            {
                tooltips.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.whole_body").withStyle(ChatFormatting.BLUE));
            }
            tooltips.add(
                    Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.healing_value",
                                    jsonConsumableHeal.healingValue, MathUtil.round(jsonConsumableHeal.healingTime / 20.0f, 1))
                            .withStyle(ChatFormatting.BLUE));

            MutableComponent mutableComponent = Component.translatable(MobEffectRegistry.RECOVERY.get().getDescriptionId());

            if (jsonConsumableHeal.recoveryEffectDuration > 0)
            {
                if (jsonConsumableHeal.recoveryEffectAmplifier > 0)
                {
                    mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + jsonConsumableHeal.recoveryEffectAmplifier));
                }

                MobEffectInstance mei = new MobEffectInstance(MobEffectRegistry.RECOVERY, jsonConsumableHeal.recoveryEffectDuration, jsonConsumableHeal.recoveryEffectAmplifier);
                mutableComponent = Component.translatable("potion.withDuration", mutableComponent, MobEffectUtil.formatDuration(mei, 1.0f, 20.0f));

                tooltips.add(mutableComponent.withStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
            }
        }
    }

    private static void addShadeText(ItemStack stack, List<Component> tooltips)
    {

        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(stack.getItem());

        if (itemRegistryName != null && (BeachpartyUtil.canProvideShade(itemRegistryName) || ArtifactsUtil.canProvideShade(itemRegistryName)))
        {
            tooltips.add(
                    Component.translatable("tooltip.legendarysurvivaloverhaul.provides_shade")
                            .withStyle(ChatFormatting.WHITE));
        }
    }

    private static void addHydrationTooltip(ItemStack stack, List<Either<FormattedText, TooltipComponent>> tooltips)
    {

        JsonThirstConsumable jsonThirstConsumable = ThirstDataManager.getConsumable(stack);

        HydrationTooltipComponent hydrationTooltipComponent = null;
        List<MutableComponent> hydrationEffectComponents = new ArrayList<>();

        if (jsonThirstConsumable != null)
        {
            int hydration = jsonThirstConsumable.hydration;
            float saturation = jsonThirstConsumable.saturation;
            
            // Add Refreshing enchantment bonus for canteens
            if (stack.getItem() instanceof CanteenItem && CanteenItem.canDrink(stack))
            {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null && mc.player.level() != null)
                {
                    try {
                        // Safely lookup the enchantment - will throw if not available
                        var enchantmentRegistry = mc.player.level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
                        var refreshingEnchOpt = enchantmentRegistry.get(ModEnchantments.REFRESHING);
                        if (refreshingEnchOpt.isPresent()) {
                            int refreshingLevel = stack.getEnchantmentLevel(refreshingEnchOpt.get());
                            if (refreshingLevel > 0)
                            {
                                hydration += refreshingLevel;
                                saturation += Math.max(0, refreshingLevel - 1);
                            }
                        }
                    } catch (IllegalStateException e) {
                        // Enchantment registry not available yet, skip bonus calculation
                    }
                }
            }
            
            hydrationTooltipComponent = new HydrationTooltipComponent(hydration, saturation);
            for (JsonMobEffect effect : jsonThirstConsumable.effects)
            {
                if (effect.chance > 0 && effect.duration > 0 && !effect.name.isEmpty())
                {
                    hydrationEffectComponents.add(getHydrationEffectTooltip(effect.chance, effect.name, effect.amplifier, effect.duration));
                }
            }
        }

        if (hydrationTooltipComponent != null)
        {
            tooltips.add(Either.right(hydrationTooltipComponent));
        }

        for (MutableComponent hydrationEffectComponent : hydrationEffectComponents)
        {
            if (hydrationEffectComponent != null)
                tooltips.add(Either.left(hydrationEffectComponent));
        }
    }

    private static MutableComponent getHydrationEffectTooltip(double effectChance, String effectName, int amplifier, int duration)
    {
        MobEffect effect = null;
        if (effectName != null && !effectName.isEmpty() && effectChance > 0)
            effect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(effectName));

        if (effect == null)
            return null;

        MobEffectInstance effectInstance = new MobEffectInstance(net.minecraft.core.Holder.direct(effect), duration, amplifier, false, true);
        MutableComponent mutableComponent = Component.translatable(effectInstance.getDescriptionId());

        if (effectInstance.getAmplifier() > 1)
        {
            mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + effectInstance.getAmplifier()));
        }

        if (effectInstance.getDuration() > 20)
        {
            mutableComponent = Component.translatable("potion.withDuration", mutableComponent, MobEffectUtil.formatDuration(effectInstance, 1.0f, 20.0f));
        }

        if (effectChance < 1)
            mutableComponent = Component.translatable("tooltip.legendarysurvivaloverhaul.potion_with_effectChance", (int) (effectChance * 100), mutableComponent);

        return mutableComponent.withStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
    }
}
