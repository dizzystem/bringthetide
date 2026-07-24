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
        withExistingParent(TideBlocks.TANK.getId().getPath(), modLoc("block/fluid_tank"));

        withExistingParent(TideBlocks.DRIFTWOOD_LOG.getId().getPath(), modLoc("block/driftwood_log"));
        withExistingParent(TideBlocks.DRIFTWOOD_PLANKS.getId().getPath(), modLoc("block/driftwood_planks"));
        withExistingParent(TideBlocks.DRIFTWOOD_STAIRS.getId().getPath(), modLoc("block/driftwood_stairs"));
        withExistingParent(TideBlocks.DRIFTWOOD_SLAB.getId().getPath(), modLoc("block/driftwood_slab"));
        singleTexture(TideBlocks.DRIFTWOOD_FENCE.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/fence_inventory"),
                modLoc("block/driftwood_planks"));
        withExistingParent(TideBlocks.DRIFTWOOD_FENCE_GATE.getId().getPath(), modLoc("block/driftwood_fence_gate"));
//        withExistingParent(TideBlocks.DRIFTWOOD_SIGN.getId().getPath(), modLoc("block/driftwood_sign"));
//        withExistingParent(TideBlocks.DRIFTWOOD_WALL_SIGN.getId().getPath(), modLoc("block/driftwood_sign"));
        withExistingParent(TideBlocks.DRIFTWOOD_PRESSURE_PLATE.getId().getPath(), modLoc("block/driftwood_pressure_plate"));
        singleTexture(TideBlocks.DRIFTWOOD_BUTTON.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/button_inventory"),
                modLoc("block/driftwood_planks"));

        withExistingParent(TideBlocks.BASIN_CORE.getId().getPath(), modLoc("block/basin_core"));
        withExistingParent(TideBlocks.CURRENT_CORE.getId().getPath(), modLoc("block/current_core"));
        withExistingParent(TideBlocks.DEPOSITION_CORE.getId().getPath(), modLoc("block/deposition_core"));
        withExistingParent(TideBlocks.EROSION_CORE.getId().getPath(), modLoc("block/erosion_core"));
        withExistingParent(TideBlocks.PELAGIC_CORE.getId().getPath(), modLoc("block/pelagic_core"));
        withExistingParent(TideBlocks.REEF_CORE.getId().getPath(), modLoc("block/reef_core"));
        withExistingParent(TideBlocks.TRAWL_CORE.getId().getPath(), modLoc("block/trawl_core"));
        withExistingParent(TideBlocks.SINKHOLE_CORE.getId().getPath(), modLoc("block/sinkhole_core"));
        withExistingParent(TideBlocks.VORTEX_CORE.getId().getPath(), modLoc("block/vortex_core"));
        withExistingParent(TideBlocks.WHIRLPOOL_CORE.getId().getPath(), modLoc("block/whirlpool_core"));
        withExistingParent(TideBlocks.FLOW_UPGRADE.getId().getPath(), modLoc("block/flow_upgrade"));
        withExistingParent(TideBlocks.BOUNTY_UPGRADE.getId().getPath(), modLoc("block/bounty_upgrade"));
        withExistingParent(TideBlocks.RANGE_UPGRADE.getId().getPath(), modLoc("block/range_upgrade"));
        withExistingParent(TideBlocks.ITEM_FILTER.getId().getPath(), modLoc("block/item_filter"));
        withExistingParent(TideBlocks.FLUID_FILTER.getId().getPath(), modLoc("block/fluid_filter"));
        withExistingParent(TideBlocks.ENTITY_FILTER.getId().getPath(), modLoc("block/entity_filter"));

        //basicItem(TideItems.IMBUED_SEAWATER_BUCKET.get());
        basicItem(TideItems.DRIFTWOOD_WAND.get());
        basicItem(TideItems.CREATIVE_WAND.get());
        basicItem(TideItems.TURTLE_ALLOY_INGOT.get());
        basicItem(TideItems.FISH_ALLOY_INGOT.get());
        basicItem(TideItems.SEASHELL_ALLOY_INGOT.get());
        basicItem(TideItems.SEABOUND_SKIN.get());
        basicItem(TideItems.COSTUME_HELMET.get());
        basicItem(TideItems.COSTUME_CHESTPLATE.get());
        basicItem(TideItems.COSTUME_LEGGINGS.get());
        basicItem(TideItems.COSTUME_BOOTS.get());
        //for our patchouli book
        basicItem(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "atlas"));
    }
}
