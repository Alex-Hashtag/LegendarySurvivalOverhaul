package sfiomn.legendarysurvivaloverhaul.data.integration.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class CuriosProvider extends CuriosDataProvider
{
    public CuriosProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(LegendarySurvivalOverhaul.MOD_ID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper)
    {
        String beltSlotId = "belt";
        String necklaceSlotId = "necklace";
        String headSlotId = "head";
        String bodySlotId = "body";
        String charmSlotId = "charm";
        String ringSlotId = "ring";
        this.createSlot(beltSlotId).icon(ResourceLocation.parse("curios:slot/empty_belt_slot"));
        this.createSlot(necklaceSlotId).icon(ResourceLocation.parse("curios:slot/empty_necklace_slot"));
        this.createSlot(headSlotId).icon(ResourceLocation.parse("curios:slot/empty_head_slot"));
        this.createSlot(bodySlotId).icon(ResourceLocation.parse("curios:slot/empty_body_slot"));
        this.createSlot(charmSlotId).icon(ResourceLocation.parse("curios:slot/empty_charm_slot"));
        this.createSlot(ringSlotId).icon(ResourceLocation.parse("curios:slot/empty_ring_slot"));
        this.createEntities("player").addPlayer().addSlots(beltSlotId, necklaceSlotId, headSlotId, bodySlotId, charmSlotId, ringSlotId);
    }
}
