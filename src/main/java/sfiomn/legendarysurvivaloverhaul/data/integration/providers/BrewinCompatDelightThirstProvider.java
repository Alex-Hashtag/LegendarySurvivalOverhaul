package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class BrewinCompatDelightThirstProvider extends ThirstDataProvider
{

    public BrewinCompatDelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("brewincompatdelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        consumable("brass_monkey").addThirst(thirstData(4, 0));

        IThirstData beer = thirstData(8, 6.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f);
        consumable("beer_knees").addThirst(beer);
        consumable("brass_monkey").addThirst(beer);

        IThirstData cocktail = thirstData(9, 5.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 500, 0.3f);
        consumable("aqua_velva").addThirst(cocktail);
        consumable("blue_curacao").addThirst(cocktail);
        consumable("china_blue").addThirst(cocktail);
        consumable("gin_and_juice").addThirst(cocktail);
        consumable("gin_and_tonic").addThirst(cocktail);
        consumable("half_and_half").addThirst(cocktail);
        consumable("margarita").addThirst(cocktail);
        consumable("mojito").addThirst(cocktail);
        consumable("pina_colada").addThirst(cocktail);
        consumable("salted_margarita").addThirst(cocktail);
        consumable("singapore_sling").addThirst(cocktail);
        consumable("tequila_sunrise").addThirst(cocktail);

        IThirstData wine = thirstData(7, 5.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 400, 0.3f);
        consumable("mulled_wine").addThirst(wine);
        consumable("peach_wine").addThirst(wine);
        consumable("red_wine").addThirst(wine);
        consumable("sweet_red_wine").addThirst(wine);
        consumable("white_wine").addThirst(wine);

        IThirstData alcohol = thirstData(5, 5.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 800, 0.4f);
        consumable("gin").addThirst(alcohol);
        consumable("kraken_rum").addThirst(alcohol);
        consumable("screwdriver").addThirst(alcohol);
        consumable("tequila").addThirst(alcohol);

        IThirstData soft = thirstData(5, 2.0f);
        consumable("hard_lemonade").addThirst(soft);
        consumable("hard_cider").addThirst(soft);
        consumable("lemon_lime").addThirst(soft);
        consumable("mermaid_lemonade").addThirst(soft);
        consumable("tonic_water").addThirst(soft);
    }
}
