package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

public class AttributeBuilder
{

    protected final Holder<Attribute> attribute;
    protected final ResourceLocation descriptionId;

    public AttributeBuilder(Holder<Attribute> attribute, ResourceLocation descriptionId)
    {
        this.attribute = attribute;
        this.descriptionId = descriptionId;
    }

    public void addModifier(ItemAttributeModifierEvent event, UUID uuid, double value)
    {
        if (uuid == null || attribute == null || descriptionId == null)
        {
            // Skip if any required parameter is null
            return;
        }
        try
        {
            EquipmentSlot slot = ItemUtil.getEquippableSlot(event.getItemStack());
            if (slot == null)
            {
                return; // Can't add modifier without a valid slot
            }
            // Create a unique ResourceLocation using the UUID to avoid conflicts
            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                    descriptionId.getNamespace(),
                    descriptionId.getPath() + "_" + uuid.toString().substring(0, 8)
            );
            event.addModifier(
                    attribute,
                    new AttributeModifier(modifierId, value, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.bySlot(slot)
            );
        } catch (Exception e)
        {
            // Log and skip on any error to prevent crashes
            System.err.println("Error adding attribute modifier: " + e.getMessage());
        }
    }

    public void addModifier(Player player, UUID uuid, double value)
    {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null)
        {
            // Create a unique ResourceLocation using the UUID to avoid conflicts
            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                    descriptionId.getNamespace(),
                    descriptionId.getPath() + "_" + uuid.toString().substring(0, 8)
            );
            
            // Remove existing modifier with this ID if present
            // Wrap in try-catch to handle NeoForge 1.21.1 getModifier issues
            try {
                AttributeModifier existingModifier = instance.getModifier(modifierId);
                if (existingModifier != null)
                {
                    instance.removeModifier(modifierId);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // NeoForge 1.21.1 can throw this when modifier doesn't exist
                // Safe to continue - modifier doesn't exist, so no need to remove
            } catch (Exception e) {
                // Log unexpected errors but continue
                System.err.println("Error checking for existing attribute modifier: " + e.getMessage());
            }
            
            instance.addPermanentModifier(new AttributeModifier(modifierId, value, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    public AttributeInstance getAttribute(Player player)
    {
        return player.getAttribute(attribute);
    }
}
