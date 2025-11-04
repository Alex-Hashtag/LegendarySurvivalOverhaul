package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.neoforged.bus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;


public class CuriosEvents
{

    @SubscribeEvent
    public static void onCurioAttributeModifierEvent(CurioAttributeModifierEvent event)
    {
        CuriosModifier.addAttribute(event);
    }
}
