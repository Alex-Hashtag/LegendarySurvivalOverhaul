package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class ExtraDelightThirstProvider extends ThirstDataProvider
{

    public ExtraDelightThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("extradelight", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("apple_milkshake").addThirst(thirstData(16, 12.0f));
        consumable("bbq_jar_item").addThirst(thirstData(2, 1.0f));
        consumable("cactus_juice").addThirst(thirstData(6, 5.0f));
        consumable("caramel_sauce").addThirst(thirstData(2, 1.0f));
        consumable("chocolate_milk").addThirst(thirstData(10, 12.0f));
        consumable("chocolate_milkshake").addThirst(thirstData(16, 12.0f));
        consumable("coffee").addThirst(thirstData(6, 4.0f));
        consumable("cookie_dough_milkshake").addThirst(thirstData(16, 12.0f));
        consumable("dalgona_coffee").addThirst(thirstData(5, 7.0f));
        consumable("dark_chocolate_syrup_bottle").addThirst(thirstData(2, 4.0f));
        consumable("eggnog").addThirst(thirstData(12, 9.0f));
        consumable("fish_sauce_item").addThirst(thirstData(2, 1.0f));
        consumable("ginger_beer").addThirst(thirstData(10, 2.0f));
        consumable("glow_berry_juice").addThirst(thirstData(6, 5.0f));
        consumable("glow_berry_milkshake").addThirst(thirstData(16, 12.0f));
        consumable("gourmet_hot_chocolate").addThirst(thirstData(18, 8.0f));
        consumable("grapefruit_juice").addThirst(thirstData(8, 4.0f));
        consumable("honey_milkshake").addThirst(thirstData(16, 12.0f));
        consumable("horchata").addThirst(thirstData(10, 2.0f));
        consumable("hot_sauce_item").addThirst(thirstData(1, 0.0f));
        consumable("ketchup_jar_item").addThirst(thirstData(2, 1.0f));
        consumable("lemon_juice").addThirst(thirstData(8, 4.0f));
        consumable("lemonade").addThirst(thirstData(16, 12.0f));
        consumable("lime_juice").addThirst(thirstData(8, 4.0f));
        consumable("limeade").addThirst(thirstData(16, 12.0f));
        consumable("mayo_jar_item").addThirst(thirstData(2, 1.0f));
        consumable("milk_chocolate_syrup_bottle").addThirst(thirstData(2, 4.0f));
        consumable("milkshake").addThirst(thirstData(12, 12.0f));
        consumable("mint_chip_milkshake").addThirst(thirstData(16, 12.0f));
        consumable("nut_butter_milkshake").addThirst(thirstData(16, 12.0f));
        consumable("orange_juice").addThirst(thirstData(8, 4.0f));
        consumable("orangeade").addThirst(thirstData(16, 12.0f));
        consumable("pumkin_milkshake").addThirst(thirstData(16, 12.0f));
        consumable("punch").addThirst(thirstData(11, 9.0f));
        consumable("soy_milk").addThirst(thirstData(9, 7.0f));
        consumable("soy_sauce_item").addThirst(thirstData(1, 1.0f));
        consumable("sweet_berry_juice").addThirst(thirstData(6, 5.0f));
        consumable("sweet_berry_milkshake").addThirst(thirstData(16, 12.0f));
        consumable("tea").addThirst(thirstData(10, 10.0f));
        consumable("tomato_juice").addThirst(thirstData(6, 5.0f));
        consumable("white_chocolate_syrup_bottle").addThirst(thirstData(2, 4.0f));
        consumable("xocolati").addThirst(thirstData(10, 6.0f));
    }
}
