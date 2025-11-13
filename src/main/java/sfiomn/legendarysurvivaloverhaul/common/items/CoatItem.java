package sfiomn.legendarysurvivaloverhaul.common.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;

import java.util.List;
import java.util.Objects;

public class CoatItem extends Item
{
    public CoatEnum coat;

    public CoatItem(CoatEnum coat, Item.Properties properties)
    {
        super(properties);
        this.coat = coat;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
    {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);

        MutableComponent text;

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
            if (this.coat != null)
            {
                MutableComponent effectComponent = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".coat_item." + this.coat.type() + ".effect").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303)));
                MutableComponent temperatureComponent = Component.literal(" " + this.coat.modifier());

                if (Objects.equals(coat.type(), "heating"))
                {
                    effectComponent = effectComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303)));
                    temperatureComponent = temperatureComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303)));
                } else if (Objects.equals(coat.type(), "cooling"))
                {
                    effectComponent = effectComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16420407)));
                    temperatureComponent = temperatureComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16420407)));
                } else
                {
                    effectComponent = effectComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(10040319)));
                    temperatureComponent = temperatureComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(10040319)));
                }

                text = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".coat_item.desc", effectComponent).append(temperatureComponent);
            } else
            {
                return;
            }

        } else
        {
            text = Component.literal(ChatFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".added_desc.activate", ChatFormatting.LIGHT_PURPLE, I18n.get(KeyMappingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), ChatFormatting.GRAY));
        }

        tooltipComponents.add(text);
    }
}
