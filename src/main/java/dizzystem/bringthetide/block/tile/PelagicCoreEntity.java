package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static java.util.Map.entry;

public class PelagicCoreEntity extends ItemCoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-2, 0, -1), Blocks.SANDSTONE),
            entry(new Vec3i(-2, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(-1, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(1, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(2, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(2, 0, -1), Blocks.SANDSTONE)
    );

    public PelagicCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.PELAGIC_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    public void tickServer() {
        super.tickServer();

        if (!this.isPoolActive()) {
            return;
        }


    }
}
