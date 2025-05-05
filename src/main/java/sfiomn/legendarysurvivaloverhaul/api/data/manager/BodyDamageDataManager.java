package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;

public class BodyDamageDataManager {

    public static IBodyPartsDamageSourceManager internalBodyPartsDamageSource;
    public static IHealingConsumableManager internalHealingConsumable;

    /**
     * Retrieves the Body Parts Information related to the provided damage source name
     * @param damageSourceName resource location of the damage source
     * @return Body Parts Information
     */
    @Nullable
    public static JsonBodyPartsDamageSource getBodyParts(String damageSourceName) {
        return internalBodyPartsDamageSource.get(damageSourceName.toLowerCase());
    }

    /**
     * Retrieves the Healing data related to the provided item registry name
     * @param itemRegistryName resource location of the healing item
     * @return Healing Information
     */
    @Nullable
    public static JsonHealingConsumable getHealingItem(ResourceLocation itemRegistryName) {
        return internalHealingConsumable.get(itemRegistryName);
    }
}
