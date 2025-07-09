package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.BodyDamageDataProvider;

import java.util.concurrent.CompletableFuture;

public class MinecraftBodyDamageProvider extends BodyDamageDataProvider {

    public MinecraftBodyDamageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super("minecraft", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        consumable(Items.ENCHANTED_GOLDEN_APPLE).healingCharges(0).healingValue(3).duration(600);

        item(Items.DIAMOND_HELMET).headResistance(0.1f);
        item(Items.DIAMOND_CHESTPLATE).chestResistance(0.1f);
        item(Items.DIAMOND_LEGGINGS).legsResistance(0.1f);
        item(Items.DIAMOND_BOOTS).feetResistance(0.1f);
    }
}
