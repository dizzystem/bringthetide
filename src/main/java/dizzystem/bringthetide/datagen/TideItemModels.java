package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TideItemModels extends ItemModelProvider {
    public TideItemModels(PackOutput output, ExistingFileHelper exFileHelper){
        super (output, BringTheTide.MODID, exFileHelper);
    }

    @Override
    protected void registerModels(){
        withExistingParent(TideBlocks.BUDDING_PRISMARINE.getId().getPath(), modLoc("block/budding_prismarine"));
        withExistingParent(TideBlocks.SMALL_PRISMARINE_BUD.getId().getPath(), modLoc("block/small_prismarine_bud"));
        withExistingParent(TideBlocks.MEDIUM_PRISMARINE_BUD.getId().getPath(), modLoc("block/medium_prismarine_bud"));
        withExistingParent(TideBlocks.LARGE_PRISMARINE_BUD.getId().getPath(), modLoc("block/large_prismarine_bud"));
        withExistingParent(TideBlocks.PRISMARINE_CLUSTER.getId().getPath(), modLoc("block/prismarine_cluster"));

        withExistingParent(TideBlocks.DRIFTWOOD_LOG.getId().getPath(), modLoc("block/driftwood_log"));
        withExistingParent(TideBlocks.CURRENT_CORE.getId().getPath(), modLoc("block/current_core"));
        withExistingParent(TideBlocks.DEPOSITION_CORE.getId().getPath(), modLoc("block/deposition_core"));
        withExistingParent(TideBlocks.EROSION_CORE.getId().getPath(), modLoc("block/erosion_core"));
        withExistingParent(TideBlocks.TRAWL_CORE.getId().getPath(), modLoc("block/trawl_core"));
        withExistingParent(TideBlocks.BASIN_CORE.getId().getPath(), modLoc("block/basin_core"));
        withExistingParent(TideBlocks.SINKHOLE_CORE.getId().getPath(), modLoc("block/sinkhole_core"));
        withExistingParent(TideBlocks.VORTEX_CORE.getId().getPath(), modLoc("block/vortex_core"));

        //basicItem(TideItems.IMBUED_SEAWATER_BUCKET.get());
        basicItem(TideItems.DRIFTWOOD_WAND.get());
        basicItem(TideItems.CREATIVE_WAND.get());
        basicItem(TideItems.SEASHELL_ALLOY_INGOT.get());
        basicItem(TideItems.MASCOT_HELMET.get());
        //for our patchouli book
        basicItem(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "atlas"));
    }
}
