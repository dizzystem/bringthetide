package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ErosionCoreEntity extends CoreEntity {
    public ErosionCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.EROSION_CORE_ENTITY.get(), blockPos, blockState);
    }

    public void tickServer(){
        super.tickServer();
    }
}
