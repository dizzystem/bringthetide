package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static java.util.Map.entry;

public class ErosionCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), TideTags.VALID_POOL_BLOCK),
            entry(new Vec3i(-1, 0, 0), TideTags.VALID_POOL_BLOCK),
            entry(new Vec3i(1, 0, -1), TideTags.VALID_POOL_BLOCK),
            entry(new Vec3i(-1, 0, -1), TideTags.VALID_POOL_BLOCK)
    );

    public ErosionCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.EROSION_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    public void tickServer(){
        super.tickServer();
    }
}
