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
        horizontalBlock(TideBlocks.EROSION_CORE.get(), modLoc("block/core_sandstone"),
                modLoc("block/core_sandstone_face"), modLoc("block/erosion_core"));
        horizontalBlock(TideBlocks.DEPOSITION_CORE.get(), modLoc("block/core_prismarine"),
                modLoc("block/core_prismarine_face"), modLoc("block/deposition_core"));
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
