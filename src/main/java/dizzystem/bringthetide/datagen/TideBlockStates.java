package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.block.ExplosionRod;
import dizzystem.bringthetide.block.ShapedPillar;
import dizzystem.bringthetide.block.properties.PillarDirection;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;

public class TideBlockStates extends BlockStateProvider {
    public TideBlockStates(PackOutput output, ExistingFileHelper exFileHelper){
        super (output, BringTheTide.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels(){
        simpleBlock(TideBlocks.BUDDING_PRISMARINE.get());
        createPrismarineClusters();

        shapedPillar(TideBlocks.PRISMARINE_PILLAR, modLoc("block/prismarine_pillar"), modLoc("block/smooth_prismarine"),
                modLoc("block/core_prismarine"), modLoc("block/smooth_prismarine"));
        logBlock(TideBlocks.DRIFTWOOD_LOG.get());

        simpleBlock(TideBlocks.DRIFTWOOD_PLANKS.get());
        stairsBlock(TideBlocks.DRIFTWOOD_STAIRS.get(), modLoc("block/driftwood_planks"));
        slabBlock(TideBlocks.DRIFTWOOD_SLAB.get(), modLoc("block/driftwood_planks"), modLoc("block/driftwood_planks"));
        fenceBlock(TideBlocks.DRIFTWOOD_FENCE.get(), modLoc("block/driftwood_planks"));
        fenceGateBlock(TideBlocks.DRIFTWOOD_FENCE_GATE.get(), modLoc("block/driftwood_planks"));
//        signBlock(TideBlocks.DRIFTWOOD_SIGN.get(), TideBlocks.DRIFTWOOD_WALL_SIGN.get(), modLoc("block/driftwood_planks"));
        pressurePlateBlock(TideBlocks.DRIFTWOOD_PRESSURE_PLATE.get(), modLoc("block/driftwood_planks"));
        buttonBlock(TideBlocks.DRIFTWOOD_BUTTON.get(), modLoc("block/driftwood_planks"));
        shapedPillar(TideBlocks.DRIFTWOOD_COLUMN, modLoc("block/driftwood_pillar"), modLoc("block/driftwood_planks"),
                modLoc("block/driftwood_planks"), modLoc("block/driftwood_planks"));

        simpleBlock(TideBlocks.POOL_BASE.get(),
                models().cubeBottomTop(TideBlocks.POOL_BASE.getId().getPath(), modLoc("block/semitransparent"),
                        modLoc("block/semitransparent"), modLoc("block/semitransparent")).renderType("translucent"));
        simpleBlock(TideBlocks.TANK.get(),
                models().cubeAll(TideBlocks.TANK.getId().getPath(), modLoc("block/tank")).renderType("cutout"));
        simpleBlock(TideBlocks.RITUAL_TNT.get(),
                models().cubeBottomTop(TideBlocks.RITUAL_TNT.getId().getPath(), modLoc("block/tnt_side"),
                        modLoc("block/tnt_bottom"), modLoc("block/tnt_top")));
        explosionRod(TideBlocks.EXPLOSION_ROD, modLoc("block/explosion_rod"));

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

    private void shapedPillar(RegistryObject<ShapedPillar> blockRegistry, ResourceLocation sideTexture,
                              ResourceLocation edgeTexture, ResourceLocation capTexture, ResourceLocation particleTexture) {
        VariantBlockStateBuilder pillarBuilder = getVariantBuilder(blockRegistry.get());
        String path = blockRegistry.getId().getPath();
        BiConsumer<Direction, ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder> body =
                (dir, builder) -> {
                    switch(dir){
                        case NORTH:
                        case SOUTH:
                        case WEST:
                        case EAST:
                            builder.texture("#side");
                            break;
                        case UP:
                        case DOWN:
                            builder.texture("#edge");
                            break;
                    }
                };
        BiConsumer<Direction, ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder> cap =
                (dir, builder) -> {
                    switch(dir){
                        case NORTH:
                        case SOUTH:
                        case WEST:
                        case EAST:
                            builder.texture("#edge");
                            break;
                        case UP:
                        case DOWN:
                            builder.texture("#cap");
                            break;
                    }
                };

        VariantBlockStateBuilder.PartialBlockstate pillarTop = pillarBuilder.partialState()
                .with(ShapedPillar.PILLAR_DIRECTION, PillarDirection.TOP);
        BlockModelBuilder pillarTopModel = models().getBuilder(path+"_top")
                .element().from(2f, 0f, 2f).to(14f, 16f, 14f)
                .allFaces(body).end()
                .element().from(0f, 14f, 0f).to(16f, 16f, 16f)
                .allFaces(cap).end()
                .texture("side", sideTexture)
                .texture("edge", edgeTexture)
                .texture("cap", capTexture)
                .texture("particle", particleTexture);
        pillarTop.setModels(pillarTop.modelForState()
                .modelFile(pillarTopModel).build());

        VariantBlockStateBuilder.PartialBlockstate pillarMiddle = pillarBuilder.partialState()
                .with(ShapedPillar.PILLAR_DIRECTION, PillarDirection.MIDDLE);
        BlockModelBuilder pillarMiddleModel = models().getBuilder(path)
                .element().from(2f, 0f, 2f).to(14f, 16f, 14f)
                .allFaces(body).end()
                .texture("side", sideTexture)
                .texture("edge", edgeTexture)
                .texture("particle", particleTexture);
        pillarMiddle.setModels(pillarMiddle.modelForState()
                .modelFile(pillarMiddleModel).build());

        VariantBlockStateBuilder.PartialBlockstate pillarBase = pillarBuilder.partialState()
                .with(ShapedPillar.PILLAR_DIRECTION, PillarDirection.BASE);
        BlockModelBuilder pillarBaseModel = models().getBuilder(path+"_base")
                .element().from(2f, 0f, 2f).to(14f, 16f, 14f)
                .allFaces(body).end()
                .element().from(0f, 0f, 0f).to(16f, 2f, 16f)
                .allFaces(cap).end()
                .texture("side", sideTexture)
                .texture("edge", edgeTexture)
                .texture("cap", capTexture)
                .texture("particle", particleTexture);
        pillarBase.setModels(pillarBase.modelForState()
                .modelFile(pillarBaseModel).build());
        simpleBlockItem(blockRegistry.get(), pillarMiddleModel);
    }

    private void explosionRod(RegistryObject<ExplosionRod> blockRegistry, ResourceLocation texture) {
        BlockModelBuilder rodModel = models().getBuilder(blockRegistry.getId().getPath())
                .element().from(6f, 12f, 6f).to(10f, 16f, 10f)
                .allFaces((dir, builder) -> {
                    switch(dir){
                        case NORTH:
                        case SOUTH:
                        case WEST:
                        case EAST:
                        case DOWN:
                            builder.uvs(0, 0, 4, 4);
                            break;
                        case UP:
                            builder.uvs(4, 4, 0, 0);
                            break;
                    }
                    builder.texture("#texture");
                }).end()
                .element().from(7f, 0f, 7f).to(9f, 12f, 9f)
                .allFaces((dir, builder) -> {
                    switch(dir){
                        case NORTH:
                        case SOUTH:
                        case WEST:
                        case EAST:
                            builder.uvs(0, 4, 2, 16);
                            break;
                        case DOWN:
                            builder.uvs(0, 4, 2, 6);
                            break;
                    }
                    builder.texture("#texture");
                }).end()
                .texture("texture", texture)
                .texture("particle", texture);

        VariantBlockStateBuilder rodBuilder = getVariantBuilder(blockRegistry.get());
        rodBuilder.forAllStates(state -> {
            ConfiguredModel.Builder<?> model = ConfiguredModel.builder().modelFile(rodModel);
            switch(state.getValue(ExplosionRod.FACING)){
                case NORTH:
                    model.rotationX(90);
                    break;
                case SOUTH:
                    model.rotationX(90);
                    model.rotationY(180);
                    break;
                case WEST:
                    model.rotationX(90);
                    model.rotationY(270);
                    break;
                case EAST:
                    model.rotationX(90);
                    model.rotationY(90);
                    break;
                case DOWN:
                    model.rotationX(180);
            }
            return model.build();
        });
        simpleBlockItem(blockRegistry.get(), rodModel);
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
