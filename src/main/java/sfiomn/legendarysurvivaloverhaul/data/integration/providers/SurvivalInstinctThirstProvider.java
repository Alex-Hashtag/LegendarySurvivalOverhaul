package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.concurrent.CompletableFuture;

public class SurvivalInstinctThirstProvider extends ThirstDataProvider
{

    public SurvivalInstinctThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("survival_instinct", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {

        IThirstData beer_hydration = thirstData(8, 6.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 600, 0.2f);
        consumable("beer").addThirst(beer_hydration);

        IThirstData alcohol = thirstData(5, 5.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 800, 0.4f);
        consumable("tequila").addThirst(alcohol);
        consumable("whiskey").addThirst(alcohol);

        IThirstData wine = thirstData(7, 5.0f)
                .addEffect(MobEffectRegistry.THIRST.get(), 400, 0.3f);
        consumable("wine").addThirst(wine);

        consumable("bean_can").addThirst(thirstData(3, 1.0f));
        consumable("cod_can").addThirst(thirstData(4, 1.0f));
        consumable("sardine_can").addThirst(thirstData(4, 1.0f));
        consumable("tuna_can").addThirst(thirstData(4, 1.0f));
        consumable("fruit_can").addThirst(thirstData(6, 2.0f));
        consumable("tomato_soup").addThirst(thirstData(6, 2.0f));
        consumable("military_can").addThirst(thirstData(4, 2.0f));
        consumable("milk").addThirst(thirstData(9, 4.0f));
        consumable("orange_juice").addThirst(thirstData(9, 4.0f));

        consumable("blue_soda").addThirst(thirstData(8, 3.5f));
        consumable("red_soda").addThirst(thirstData(8, 3.5f));
        consumable("lemon_soda").addThirst(thirstData(8, 3.5f));
        consumable("energy_can").addThirst(thirstData(8, 3.5f));
        consumable("monster_can").addThirst(thirstData(11, 4.0f));

        consumable("gallon_of_milk").addThirst(thirstData(10, 5.0f));
        consumable("gallon_of_water").addThirst(thirstData(10, 5.0f));

    }
}
