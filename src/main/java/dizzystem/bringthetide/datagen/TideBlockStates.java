package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class TideBlockStates extends BlockStateProvider {
    public TideBlockStates(PackOutput output, ExistingFileHelper exFileHelper){
        super (output, BringTheTide.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels(){
        simpleBlock(TideBlocks.BUDDING_PRISMARINE.get());
        createPrismarineClusters();
        logBlock(TideBlocks.DRIFTWOOD_LOG.get());
        simpleBlock(TideBlocks.DRIFTWOOD_PLANKS.get());
        stairsBlock(TideBlocks.DRIFTWOOD_STAIRS.get(), modLoc("block/driftwood_planks"));
        slabBlock(TideBlocks.DRIFTWOOD_SLAB.get(), modLoc("block/driftwood_planks"), modLoc("block/driftwood_planks"));
        fenceBlock(TideBlocks.DRIFTWOOD_FENCE.get(), modLoc("block/driftwood_planks"));
        fenceGateBlock(TideBlocks.DRIFTWOOD_FENCE_GATE.get(), modLoc("block/driftwood_planks"));
//        signBlock(TideBlocks.DRIFTWOOD_SIGN.get(), TideBlocks.DRIFTWOOD_WALL_SIGN.get(), modLoc("block/driftwood_planks"));
        pressurePlateBlock(TideBlocks.DRIFTWOOD_PRESSURE_PLATE.get(), modLoc("block/driftwood_planks"));
        buttonBlock(TideBlocks.DRIFTWOOD_BUTTON.get(), modLoc("block/driftwood_planks"));

        simpleBlock(TideBlocks.TANK.get(),
                models().cubeAll(TideBlocks.TANK.getId().getPath(), modLoc("block/tank")).renderType("cutout"));
        simpleBlock(TideBlocks.RITUAL_TNT.get(),
                models().cubeBottomTop(TideBlocks.RITUAL_TNT.getId().getPath(), modLoc("block/tnt_side"),
                        modLoc("block/tnt_bottom"), modLoc("block/tnt_top")));
        //sandstone cores
        horizontalBlock(TideBlocks.EROSION_CORE.get(), modLoc("block/core_sandstone"),
                modLoc("block/core_sandstone_face"), modLoc("block/erosion_core"));
        horizontalBlock(TideBlocks.PELAGIC_CORE.get(), modLoc("block/core_sandstone"),
                modLoc("block/core_sandstone_face"), modLoc("block/pelagic_core"));
        horizontalBlock(TideBlocks.SEABED_CORE.get(), modLoc("block/core_sandstone"),
                modLoc("block/core_sandstone_face"), modLoc("block/sinkhole_core"));
        horizontalBlock(TideBlocks.SINKHOLE_CORE.get(), modLoc("block/core_sandstone"),
                modLoc("block/core_sandstone_face"), modLoc("block/sinkhole_core"));
        horizontalBlock(TideBlocks.VORTEX_CORE.get(), modLoc("block/core_sandstone"),
                modLoc("block/core_sandstone_face"), modLoc("block/vortex_core"));

        //prismarine cores
        horizontalBlock(TideBlocks.BASIN_CORE.get(), modLoc("block/core_prismarine"),
                modLoc("block/core_prismarine_face"), modLoc("block/basin_core"));
        horizontalBlock(TideBlocks.CURRENT_CORE.get(), modLoc("block/core_prismarine"),
                modLoc("block/core_prismarine_face"), modLoc("block/current_core"));
        horizontalBlock(TideBlocks.DEPOSITION_CORE.get(), modLoc("block/core_prismarine"),
                modLoc("block/core_prismarine_face"), modLoc("block/deposition_core"));
        horizontalBlock(TideBlocks.REEF_CORE.get(), modLoc("block/core_prismarine"),
                modLoc("block/core_prismarine_face"), modLoc("block/reef_core"));
        horizontalBlock(TideBlocks.TRAWL_CORE.get(), modLoc("block/core_prismarine"),
                modLoc("block/core_prismarine_face"), modLoc("block/trawl_core"));
        horizontalBlock(TideBlocks.WHIRLPOOL_CORE.get(), modLoc("block/core_prismarine"),
                modLoc("block/core_prismarine_face"), modLoc("block/whirlpool_core"));

        //upgrades
        horizontalBlock(TideBlocks.FLOW_UPGRADE.get(), modLoc("block/core_copper"),
                modLoc("block/core_copper_face"), modLoc("block/flow_upgrade"));
        horizontalBlock(TideBlocks.BOUNTY_UPGRADE.get(), modLoc("block/core_copper"),
                modLoc("block/core_copper_face"), modLoc("block/bounty_upgrade"));
        horizontalBlock(TideBlocks.RANGE_UPGRADE.get(), modLoc("block/core_copper"),
                modLoc("block/core_copper_face"), modLoc("block/range_upgrade"));
        horizontalBlock(TideBlocks.ITEM_FILTER.get(), modLoc("block/core_copper"),
                modLoc("block/core_copper_face"), modLoc("block/filter_upgrade"));
        horizontalBlock(TideBlocks.FLUID_FILTER.get(), modLoc("block/core_copper"),
                modLoc("block/core_copper_face"), modLoc("block/filter_upgrade"));
        horizontalBlock(TideBlocks.ENTITY_FILTER.get(), modLoc("block/core_copper"),
                modLoc("block/core_copper_face"), modLoc("block/filter_upgrade"));

        simpleBlock(TideBlocks.BLOCK_IMBUED_SEAWATER.get(),
                models().cubeAll("block_imbued_seawater", modLoc("block/fluids/imbued_seawater")));
    }

    private void createPrismarineClusters(){
        this.createPrismarineCluster(TideBlocks.SMALL_PRISMARINE_BUD);
        this.createPrismarineCluster(TideBlocks.MEDIUM_PRISMARINE_BUD);
        this.createPrismarineCluster(TideBlocks.LARGE_PRISMARINE_BUD);
        this.createPrismarineCluster(TideBlocks.PRISMARINE_CLUSTER);
    }

    private void createPrismarineCluster(RegistryObject<Block> blockRegistry){
        Block block = blockRegistry.get();
        String path = blockRegistry.getId().getPath();
        directionalBlock(block, models().cross(path, modLoc("block/"+path)).renderType("cutout"));
    }
}
