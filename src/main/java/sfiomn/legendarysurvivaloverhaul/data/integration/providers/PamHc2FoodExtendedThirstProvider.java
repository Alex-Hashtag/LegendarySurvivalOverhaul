package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.api.data.providers.ThirstDataProvider;

import java.util.concurrent.CompletableFuture;

public class PamHc2FoodExtendedThirstProvider extends ThirstDataProvider
{

    public PamHc2FoodExtendedThirstProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper)
    {
        super("pamhc2foodextended", output, lookupProvider, fileHelper);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        consumable("applecideritem").addThirst(thirstData(7, 2.0f));
        consumable("apricotjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("apricotsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("bananajuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("bananasmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("bbqsaucetem").addThirst(thirstData(1, 0.0f));
        consumable("blackberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("blackberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("blueberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("blueberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("breadfruitjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("breadfruitsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("cactusfruitjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("cactusfruitsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("candleberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("candleberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("cantaloupejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("cantaloupesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("carrotjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("chaiteaitem").addThirst(thirstData(10, 12.0f));
        consumable("cherryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("cherrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("cherrysodaitem").addThirst(thirstData(8, 0.0f));
        consumable("chickentendersmealitem").addThirst(thirstData(8, 0.0f));
        consumable("chocolatemilkitem").addThirst(thirstData(8, 2.0f));
        consumable("coffeeconlecheitem").addThirst(thirstData(4, 2.0f));
        consumable("colasodaitem").addThirst(thirstData(8, 0.0f));
        consumable("cranberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("cranberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("creamedbroccolisoupitem").addThirst(thirstData(10, 6.0f));
        consumable("creamedcornitem").addThirst(thirstData(10, 6.0f));
        consumable("creamofavocadosoupitem").addThirst(thirstData(10, 6.0f));
        consumable("dandelionteaitem").addThirst(thirstData(14, 6.0f));
        consumable("datejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("datesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("dragonfruitjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("dragonfruitsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("durianjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("duriansmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("earlgreyteaitem").addThirst(thirstData(4, 10.0f));
        consumable("eggnogitem").addThirst(thirstData(6, 8.0f));
        consumable("elderberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("elderberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("energydrinkitem").addThirst(thirstData(6, 1.0f));
        consumable("figjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("figsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("gardensoupitem").addThirst(thirstData(10, 2.0f));
        consumable("gingersodaitem").addThirst(thirstData(8, 0.0f));
        consumable("gooseberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("grapefruitjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("grapefruitsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("grapefruitsodaitem").addThirst(thirstData(8, 0.0f));
        consumable("grapejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("grapesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("grapesodaitem").addThirst(thirstData(8, 0.0f));
        consumable("gravyitem").addThirst(thirstData(1, 0.0f));
        consumable("greengrapejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("greengrapesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("greenteaitem").addThirst(thirstData(4, 10.0f));
        consumable("grooseberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("guavajuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("guavasmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("huckleberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("huckleberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("jackfruitjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("jackfruitsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("juniperberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("juniperberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("kiwijuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("kiwismoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("lambbarleysoupitem").addThirst(thirstData(10, 2.0f));
        consumable("leekbaconsoupitem").addThirst(thirstData(10, 2.0f));
        consumable("lemonaideitem").addThirst(thirstData(14, 8.0f));
        consumable("lemonjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("lemonlimesodaitem").addThirst(thirstData(10, 2.0f));
        consumable("lemonsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("limejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("limesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("lycheejuicejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("lycheesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("mangojuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("mangosmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("misosoupitem").addThirst(thirstData(12, 6.0f));
        consumable("mulberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("mulberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("oldworldveggiesoupitem").addThirst(thirstData(12, 5.0f));
        consumable("orangejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("orangesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("orangesodaitem").addThirst(thirstData(8, 0.0f));
        consumable("oystersauceitem").addThirst(thirstData(2, 1.0f));
        consumable("papayajuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("papayasmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("passonfruitjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("passonfruitsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("pawpawjuicejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("pawpawsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("peaandhamsoupitem").addThirst(thirstData(4, 8.0f));
        consumable("peachjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("peachsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("pearjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("pearsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("persimmonjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("persimmonsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("pinacoladaitem").addThirst(thirstData(4, 8.0f));
        consumable("pineapplejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("pineapplesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("plumjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("plumsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("pomegranatejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("pomegranatesmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("potatoandleeksoupitem").addThirst(thirstData(4, 6.0f));
        consumable("pumpkinspicelatteitem").addThirst(thirstData(8, 14.0f));
        consumable("rambutanjuicejuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("rambutansmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("ramenitem").addThirst(thirstData(2, 2.0f));
        consumable("raspberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("raspberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("rootbeerfloatitem").addThirst(thirstData(8, 4.0f));
        consumable("rootbeersodaitem").addThirst(thirstData(8, 0.0f));
        consumable("rosepetalteaitem").addThirst(thirstData(6, 10.0f));
        consumable("seedsoupitem").addThirst(thirstData(4, 2.0f));
        consumable("soursopjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("soursopsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("soymilkitem").addThirst(thirstData(4, 3.0f));
        consumable("starfruitjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("starfruitsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("strawberryjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("strawberrysmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("strawberrysodaitem").addThirst(thirstData(8, 0.0f));
        consumable("sundayhighteaitem").addThirst(thirstData(14, 12.0f));
        consumable("sweetteaitem").addThirst(thirstData(10, 14.0f));
        consumable("tamarindjuiceitem").addThirst(thirstData(4, 4.0f));
        consumable("tamarindsmoothieitem").addThirst(thirstData(10, 12.0f));
        consumable("vegetablesoupitem").addThirst(thirstData(8, 4.0f));
        consumable("wontonsoupitem").addThirst(thirstData(10, 5.0f));
    }
}
