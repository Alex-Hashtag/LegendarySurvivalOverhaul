package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.TemperatureDataProvider;

import java.util.concurrent.CompletableFuture;

public class IronsSpellbooksTemperatureProvider extends TemperatureDataProvider
{

    public IronsSpellbooksTemperatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("irons_spellbooks", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        item("pyromancer_helmet").coldResistance(1.5f);
        item("pyromancer_chestplate").coldResistance(2.5f);
        item("pyromancer_leggings").coldResistance(2.0f);
        item("pyromancer_boots").coldResistance(1.5f);
        item("cryomancer_helmet").heatResistance(1.5f);
        item("cryomancer_chestplate").heatResistance(2.5f);
        item("cryomancer_leggings").heatResistance(2.0f);
        item("cryomancer_boots").heatResistance(1.5f);
        item("netherite_mage_helmet").coldResistance(2.0f);
        item("netherite_mage_chestplate").coldResistance(3.5f);
        item("netherite_mage_leggings").coldResistance(2.5f);
        item("netherite_mage_boots").coldResistance(2.0f);
    }
}
