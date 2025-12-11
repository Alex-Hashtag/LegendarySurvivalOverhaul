package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.ModEnchantments;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends EnchantmentTagsProvider {

    public ModEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, LegendarySurvivalOverhaul.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Add canteen enchantments to the enchanting table tag
        tag(EnchantmentTags.IN_ENCHANTING_TABLE)
                .addOptional(ModEnchantments.REFRESHING.location())
                .addOptional(ModEnchantments.PURITY.location())
                .addOptional(ModEnchantments.RESERVOIR.location());
        
        // Add to treasure tag if you want them to appear in loot (optional)
        // Commenting out as these should be obtainable via enchanting table
        // tag(EnchantmentTags.TREASURE)
        //         .addOptional(ModEnchantments.PURITY.location());
        
        // Add to non-treasure tag (enchantments that can be obtained normally)
        tag(EnchantmentTags.NON_TREASURE)
                .addOptional(ModEnchantments.REFRESHING.location())
                .addOptional(ModEnchantments.PURITY.location())
                .addOptional(ModEnchantments.RESERVOIR.location());
        
        // Add to tradeable tag (can be obtained from villagers)
        tag(EnchantmentTags.TRADEABLE)
                .addOptional(ModEnchantments.REFRESHING.location())
                .addOptional(ModEnchantments.PURITY.location())
                .addOptional(ModEnchantments.RESERVOIR.location());
    }
}
