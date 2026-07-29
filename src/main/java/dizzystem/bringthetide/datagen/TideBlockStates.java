package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.block.ShapedPillar;
import dizzystem.bringthetide.block.properties.PillarDirection;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
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

        VariantBlockStateBuilder prismarinePillarBuilder = getVariantBuilder(TideBlocks.PRISMARINE_PILLAR.get());

        VariantBlockStateBuilder.PartialBlockstate prismarinePillarTop = prismarinePillarBuilder.partialState()
                .with(ShapedPillar.PILLAR_DIRECTION, PillarDirection.TOP);
        BlockModelBuilder pillarTopBuilder = models().getBuilder("prismarine_pillar_top")
                .element().from(2f, 0f, 2f).to(14f, 16f, 14f)
                .allFaces((dir, builder) -> builder.texture("#bricks")).end()
                .element().from(0f, 14f, 0f).to(16f, 16f, 16f)
                .allFaces((dir, builder) -> builder.texture("#bricks")).end()
                .texture("bricks", modLoc("block/prismarine_bricks"));
        prismarinePillarTop.setModels(prismarinePillarTop.modelForState()
                .modelFile(pillarTopBuilder).build());

        VariantBlockStateBuilder.PartialBlockstate prismarinePillarMiddle = prismarinePillarBuilder.partialState()
                .with(ShapedPillar.PILLAR_DIRECTION, PillarDirection.MIDDLE);
        BlockModelBuilder pillarMiddleBuilder = models().getBuilder("prismarine_pillar")
                .element().from(2f, 0f, 2f).to(14f, 16f, 14f)
                .allFaces((dir, builder) -> builder.texture("#bricks")).end()
                .texture("bricks", modLoc("block/prismarine_bricks"));
        prismarinePillarMiddle.addModels(prismarinePillarMiddle.modelForState()
                .modelFile(pillarMiddleBuilder).buildLast());

        VariantBlockStateBuilder.PartialBlockstate prismarinePillarBase = prismarinePillarBuilder.partialState()
                .with(ShapedPillar.PILLAR_DIRECTION, PillarDirection.BASE);
        BlockModelBuilder pillarBaseBuilder = models().getBuilder("prismarine_pillar_base")
                .element().from(2f, 0f, 2f).to(14f, 16f, 14f)
                .allFaces((dir, builder) -> builder.texture("#bricks")).end()
                .element().from(0f, 0f, 0f).to(16f, 2f, 16f)
                .allFaces((dir, builder) -> builder.texture("#bricks")).end()
                .texture("bricks", modLoc("block/prismarine_bricks"));
        prismarinePillarBase.addModels(prismarinePillarBase.modelForState()
                .modelFile(pillarBaseBuilder).buildLast());

//        prismarinePillarBuilder.forAllStates(state -> {
//            switch (state.getValue(ShapedPillar.PILLAR_DIRECTION)){
//                case TOP: return ConfiguredModel.builder().modelFile(pillarTopBuilder).build();
//                case BASE: return ConfiguredModel.builder().modelFile(pillarBaseBuilder).build();
//                default: return ConfiguredModel.builder().modelFile(pillarMiddleBuilder).build();
//            }
//        });

        logBlock(TideBlocks.DRIFTWOOD_LOG.get());

        simpleBlock(TideBlocks.DRIFTWOOD_PLANKS.get());
        stairsBlock(TideBlocks.DRIFTWOOD_STAIRS.get(), modLoc("block/driftwood_planks"));
        slabBlock(TideBlocks.DRIFTWOOD_SLAB.get(), modLoc("block/driftwood_planks"), modLoc("block/driftwood_planks"));
        fenceBlock(TideBlocks.DRIFTWOOD_FENCE.get(), modLoc("block/driftwood_planks"));
        fenceGateBlock(TideBlocks.DRIFTWOOD_FENCE_GATE.get(), modLoc("block/driftwood_planks"));
//        signBlock(TideBlocks.DRIFTWOOD_SIGN.get(), TideBlocks.DRIFTWOOD_WALL_SIGN.get(), modLoc("block/driftwood_planks"));
        pressurePlateBlock(TideBlocks.DRIFTWOOD_PRESSURE_PLATE.get(), modLoc("block/driftwood_planks"));
        buttonBlock(TideBlocks.DRIFTWOOD_BUTTON.get(), modLoc("block/driftwood_planks"));

        simpleBlock(TideBlocks.POOL_BASE.get(),
                models().cubeBottomTop(TideBlocks.POOL_BASE.getId().getPath(), modLoc("block/semitransparent"),
                        modLoc("block/semitransparent"), modLoc("block/semitransparent")).renderType("translucent"));
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
