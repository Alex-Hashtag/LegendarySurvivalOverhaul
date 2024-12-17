package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.core.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.loading.FMLLoader;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;

public class CuriosUtil {
    public static boolean isThermometerEquipped = false;

    public static boolean isCurioItemEquipped(Player player, Item item) {
        if (LegendarySurvivalOverhaul.curiosLoaded) {
            LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);

            if (curiosInventory.isPresent() && curiosInventory.resolve().isPresent()) {
                return curiosInventory.resolve().get().isEquipped(item);
            }
        }
        return false;
    }

    public static boolean isCuriosItem(ItemStack stack) {
        return LegendarySurvivalOverhaul.curiosLoaded && !CuriosApi.getItemStackSlots(stack, FMLLoader.getDist() == Dist.CLIENT).values().isEmpty();
    }

    // Follow the item right click event of curios, necessary to avoid curios hard dependency
    public static boolean equipCurio(Player player, ItemStack stack, InteractionHand hand) {
        if (LegendarySurvivalOverhaul.curiosLoaded) {

            LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);

            if (curiosInventory.isPresent() && curiosInventory.resolve().isPresent()) {
                Map<String, ICurioStacksHandler> curios = curiosInventory.resolve().get().getCurios();
                Tuple<IDynamicStackHandler, SlotContext> firstSlot = null;

                for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                    IDynamicStackHandler stackHandlerx = entry.getValue().getStacks();

                    for (int ix = 0; ix < stackHandlerx.getSlots(); ++ix) {
                        String id = entry.getKey();
                        NonNullList<Boolean> renderStates = entry.getValue().getRenders();
                        SlotContext slotContext = new SlotContext(id, player, ix, false, renderStates.size() > ix && renderStates.get(ix));
                        if (stackHandlerx.isItemValid(ix, stack)) {
                            ItemStack present = stackHandlerx.getStackInSlot(ix);
                            if (present.isEmpty()) {
                                stackHandlerx.setStackInSlot(ix, stack.copy());
                                if (!player.isCreative()) {
                                    int count = stack.getCount();
                                    stack.shrink(count);
                                }

                                return true;
                            }

                            if (firstSlot == null) {
                                CurioUnequipEvent unequipEvent = new CurioUnequipEvent(present, slotContext);
                                MinecraftForge.EVENT_BUS.post(unequipEvent);
                                Event.Result result = unequipEvent.getResult();
                                if (result != Event.Result.DENY && stackHandlerx.extractItem(ix, stack.getMaxStackSize(), true).getCount() == stack.getCount()) {
                                    firstSlot = new Tuple(stackHandlerx, slotContext);
                                }
                            }
                        }
                    }
                }

                if (firstSlot != null) {
                    IDynamicStackHandler stackHandler = firstSlot.getA();
                    SlotContext slotContextx = firstSlot.getB();
                    int i = slotContextx.index();
                    ItemStack presentx = stackHandler.getStackInSlot(i);
                    stackHandler.setStackInSlot(i, stack.copy());
                    player.setItemInHand(hand, presentx.copy());
                    return true;
                }
            }
        }
        return false;
    }
}
