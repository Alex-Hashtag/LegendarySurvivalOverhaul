package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.Optional;

public class ItemUtil {

    private ItemUtil() {}

    public static boolean canBeEquippedInSlot(ItemStack stack, EquipmentSlot slot) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getEquipmentSlot() == slot;
        }

        if (stack.getItem() instanceof ShieldItem) {
            return slot == EquipmentSlot.OFFHAND;
        }

        if (CuriosUtil.isCuriosItem(stack))
            return false;

        return slot == EquipmentSlot.MAINHAND;
    }

    public static EquipmentSlot getEquippableSlot(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getEquipmentSlot();
        }

        if (stack.getItem() instanceof ShieldItem) {
            return EquipmentSlot.OFFHAND;
        }

        return EquipmentSlot.MAINHAND;
    }

    public static String compassLocation(Entity entity) {
        return switch (Config.Baked.compassInfoMode) {
            case FULL -> "XYZ: " + entity.blockPosition().getX() +
                    " / " + entity.blockPosition().getY() + " / " + entity.blockPosition().getZ();
            case HORIZONTAL -> "XZ: " + entity.blockPosition().getX() + " / " + entity.blockPosition().getZ();
            case NONE -> "";
        };
    }

    public static String compassDeathLocation(Player player) {
        Optional<GlobalPos> globalDeathPos = player.getLastDeathLocation();
        if (globalDeathPos.isPresent()) {
            GlobalPos pos = globalDeathPos.get();
            return Component.translatable("message.legendarysurvivaloverhaul.compass_death_location",
                    pos.dimension().location().getPath().replace("_", " ").replace("-", " "), pos.pos().getX(), pos.pos().getY(), pos.pos().getZ()).getString();
        }
        return "";
    }
}