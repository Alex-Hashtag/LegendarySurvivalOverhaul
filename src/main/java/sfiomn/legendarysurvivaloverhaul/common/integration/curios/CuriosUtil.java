package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.core.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;
import java.util.Optional;

public class CuriosUtil
{
    public static boolean isThermometerEquipped = false;

    public static boolean isCurioItemEquipped(Player player, Item item)
    {
        if (LegendarySurvivalOverhaul.curiosLoaded)
        {
            Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);
            return curiosInventory.map(handler -> handler.isEquipped(item)).orElse(false);
        } else
        {
            return player.getItemInHand(InteractionHand.MAIN_HAND).is(item)
                    || player.getItemInHand(InteractionHand.OFF_HAND).is(item);
        }
    }

    public static boolean isCuriosItem(ItemStack stack)
    {
        if (!LegendarySurvivalOverhaul.curiosLoaded) return false;
        // You can also pass false here; the CLIENT/SERVER distinction isn't needed for most checks.
        boolean isClient = (FMLEnvironment.dist == Dist.CLIENT);
        return !CuriosApi.getItemStackSlots(stack, isClient).isEmpty();
    }

    // Follow the item right click event of curios, necessary to avoid curios hard dependency
    public static boolean equipCurio(Player player, ItemStack stack, InteractionHand hand)
    {
        if (!LegendarySurvivalOverhaul.curiosLoaded || stack.isEmpty()) return false;

        Optional<ICuriosItemHandler> curiosInventoryOpt = CuriosApi.getCuriosInventory(player);
        if (curiosInventoryOpt.isEmpty()) return false;

        ICuriosItemHandler curiosInventory = curiosInventoryOpt.get();
        Map<String, ICurioStacksHandler> curios = curiosInventory.getCurios();

        Tuple<IDynamicStackHandler, SlotContext> firstSlot = null;

        // 1) Try to equip directly into the first empty & valid slot.
        for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet())
        {
            String id = entry.getKey();
            ICurioStacksHandler handler = entry.getValue();
            IDynamicStackHandler slots = handler.getStacks();
            NonNullList<Boolean> renderStates = handler.getRenders();

            for (int ix = 0; ix < slots.getSlots(); ix++)
            {
                SlotContext slotContext = new SlotContext(
                        id, player, ix, false,
                        renderStates.size() > ix && renderStates.get(ix)
                );

                if (!slots.isItemValid(ix, stack)) continue;

                ItemStack present = slots.getStackInSlot(ix);

                // Empty slot → equip immediately.
                if (present.isEmpty())
                {
                    slots.setStackInSlot(ix, stack.copy());
                    if (!player.isCreative())
                    {
                        stack.shrink(stack.getCount());
                    }
                    return true;
                }

                // Otherwise, remember the first valid slot we can swap with (if unequip isn't canceled)
                if (firstSlot == null && canUnequipViaCuriosEvent(present, slotContext))
                {
                    // Make sure the entire stack could fit if we swapped
                    if (slots.extractItem(ix, stack.getMaxStackSize(), true).getCount() == stack.getCount())
                    {
                        firstSlot = new Tuple<>(slots, slotContext);
                    }
                }
            }
        }

        // 2) If no empty slot, swap with the first acceptable occupied slot we found.
        if (firstSlot != null)
        {
            IDynamicStackHandler targetStacks = firstSlot.getA(); // <-- different variable name; no shadowing
            SlotContext slotContext = firstSlot.getB();
            int i = slotContext.index();
            ItemStack present = targetStacks.getStackInSlot(i);

            targetStacks.setStackInSlot(i, stack.copy());
            if (!player.isCreative())
            {
                // Put the replaced curio in the player's hand (the slot they clicked with)
                player.setItemInHand(hand, present.copy());
            }
            // If creative, we leave the hand as-is (typical creative behavior).
            return true;
        }

        return false;
    }

    /**
     * Posts Curios' CurioUnequipEvent(present, slotContext) via reflection and
     * returns false if the event was canceled.
     */
    private static boolean canUnequipViaCuriosEvent(ItemStack present, SlotContext slotContext)
    {
        try
        {
            Class<?> evtClass = Class.forName("top.theillusivec4.curios.api.event.CurioUnequipEvent");
            var ctor = evtClass.getConstructor(ItemStack.class, SlotContext.class);
            Object evt = ctor.newInstance(present, slotContext);

            // Post on NeoForge bus
            Object posted = net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post((net.neoforged.bus.api.Event) evt);

            // Check cancellation if it implements ICancellableEvent
            if (posted instanceof net.neoforged.bus.api.ICancellableEvent c)
            {
                return !c.isCanceled();
            }
            return true;
        } catch (Throwable ignored)
        {
            // If Curios event class isn't present or signature changed, fall back to allowing the swap.
            return true;
        }
    }

}