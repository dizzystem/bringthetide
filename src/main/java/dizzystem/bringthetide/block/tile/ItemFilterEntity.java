package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class ItemFilterEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
    );

    public ItemFilterEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.ITEM_FILTER_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }
}

