package sfiomn.legendarysurvivaloverhaul.common.listeners;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.registry.EnchantmentRegistry;
import net.minecraftforge.registries.ForgeRegistries;

import static sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul.LOGGER;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EnchantmentListener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        int leftPurity = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PURITY.get(), left);
        int rightPurity = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PURITY.get(), right);
        boolean leftHasPurity = leftPurity > 0 || stackHasPurityOnBook(left);
        boolean rightHasPurity = rightPurity > 0 || stackHasPurityOnBook(right);

        LOGGER.debug("AnvilUpdateEvent: side={}, left={}, right={}, leftPurity={}, rightPurity={}, leftHasPurityBook={}, rightHasPurityBook={}",
                event.getPlayer() != null ? (event.getPlayer().level().isClientSide ? "client" : "server") : "unknown",
                left.isEmpty() ? "empty" : left.getItem().getDescriptionId(),
                right.isEmpty() ? "empty" : right.getItem().getDescriptionId(),
                leftPurity, rightPurity,
                stackHasPurityOnBook(left), stackHasPurityOnBook(right));

        // Only mutate stacks on the server side to persist changes
        if (event.getPlayer() == null || event.getPlayer().level().isClientSide) {
            return;
        }

        // Case 1: Left is canteen (usual target), right provides Purity (book or another item with Purity)
        if (!left.isEmpty() && left.getItem() instanceof CanteenItem && leftPurity == 0 && rightHasPurity) {
            CanteenItem.onPurityApplied(left);
            LOGGER.debug("Purified LEFT canteen via anvil (book on right)");
        }

        // Case 2: Right is canteen (if user swaps), left provides Purity
        if (!right.isEmpty() && right.getItem() instanceof CanteenItem && rightPurity == 0 && leftHasPurity) {
            CanteenItem.onPurityApplied(right);
            LOGGER.debug("Purified RIGHT canteen via anvil (book on left)");
        }
    }

    private static boolean stackHasPurityOnBook(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != Items.ENCHANTED_BOOK)
            return false;
        ListTag listtag = EnchantedBookItem.getEnchantments(stack);
        if (listtag == null)
            return false;
        ResourceLocation purityId = ForgeRegistries.ENCHANTMENTS.getKey(EnchantmentRegistry.PURITY.get());
        if (purityId == null)
            return false;
        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag tag = listtag.getCompound(i);
            String id = tag.getString("id");
            if (purityId.toString().equals(id)) {
                return tag.getShort("lvl") > 0;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onGrindstone(GrindstoneEvent.OnPlaceItem event) {
        ItemStack topItem = event.getTopItem();
        ItemStack bottomItem = event.getBottomItem();
        
        // Check if either item is a canteen with Purity
        if (topItem.getItem() instanceof CanteenItem) {
            int purityLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PURITY.get(), topItem);
            if (purityLevel > 0) {
                // Grindstone removes enchantments, so we don't need to do anything special here
                // The water will remain purified even after enchantment removal
            }
        }
        
        if (bottomItem.getItem() instanceof CanteenItem) {
            int purityLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PURITY.get(), bottomItem);
            if (purityLevel > 0) {
                // Same as above
            }
        }
    }
}
