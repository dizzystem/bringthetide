package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static java.util.Map.entry;

public class FluidFilterEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
    );

    public FluidFilterEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.FLUID_FILTER_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }
}
