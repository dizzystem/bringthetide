package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static java.util.Map.entry;

public class ReefCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-2, 0, -1), Blocks.PRISMARINE),
            entry(new Vec3i(-2, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(-1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(2, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(2, 0, -1), Blocks.PRISMARINE)
    );

    public ReefCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.REEF_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when an entity enters our pool
    public void entityInPool(Entity entity, Level level, BlockPos pos) {
        if (!(entity instanceof Animal animal)) {
            return;
        }
        if (!this.isPoolActive()) {
            return;
        }

        if (animal.isBaby()){
            animal.ageUp(4);
        } else if (animal.canFallInLove()){
            animal.setInLove(null);
        }
    }
}
