package sfiomn.legendarysurvivaloverhaul.common.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.attachments.temperature.TemperatureItemAttachment;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ThermometerItem extends Item
{
    public ThermometerItem(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        if (level.isClientSide())
        {
            TemperatureItemAttachment tempCap = AttachmentUtil.getTempItemAttachment(player.getMainHandItem());

            float temperature = tempCap.getWorldTemperatureLevel();
            Component temperatureComponent;
            if (Config.Baked.renderTemperatureInFahrenheit)
            {
                temperatureComponent = Component.literal(WorldUtil.toFahrenheit(temperature) + "\u00B0F");
            } else
            {
                temperatureComponent = Component.literal(temperature + "\u00B0C");
            }
            player.displayClientMessage(temperatureComponent, (true));
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced)
    {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        List<MutableComponent> text = new ArrayList<>();

        // Tooltips can be built off the render thread (e.g., creative search trees). Avoid GLFW calls off-thread.
        boolean showDetails = false;
        if (RenderSystem.isOnRenderThread())
        {
            long windowHandle = Minecraft.getInstance().getWindow().getWindow();
            int key = KeyMappingRegistry.showAddedDesc.getKey().getValue();
            if (windowHandle != 0L && key != -1)
            {
                showDetails = InputConstants.isKeyDown(windowHandle, key);
            }
        }

        if (showDetails)
        {
            text.add(Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".thermometer.description"));
            if (LegendarySurvivalOverhaul.curiosLoaded)
                text.add(Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".thermometer.bauble_description"));

        } else
        {
            text.add(Component.literal(ChatFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".added_desc.activate", ChatFormatting.LIGHT_PURPLE, I18n.get(KeyMappingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), ChatFormatting.GRAY)));
        }

        tooltipComponents.addAll(text);
    }
}
