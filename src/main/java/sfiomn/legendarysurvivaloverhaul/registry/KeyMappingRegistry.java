package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;


@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyMappingRegistry
{
    public static KeyMapping showAddedDesc;
    public static KeyMapping showBodyHealth;

    @SubscribeEvent
    public static void registerKeyMappingsEvent(RegisterKeyMappingsEvent event)
    {
        showAddedDesc = create("added_desc", GLFW.GLFW_KEY_LEFT_SHIFT, KeyConflictContext.GUI);
        event.register(showAddedDesc);
        showBodyHealth = create("body_health", GLFW.GLFW_KEY_H, KeyConflictContext.UNIVERSAL);
        event.register(showBodyHealth);
    }

    private static KeyMapping create(String name, int key, KeyConflictContext context)
    {
        return new KeyMapping("key." + LegendarySurvivalOverhaul.MOD_ID + "." + name, context, InputConstants.Type.KEYSYM, key, "key." + LegendarySurvivalOverhaul.MOD_ID + ".title");
    }
}
