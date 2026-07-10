package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static java.util.Map.entry;

public class RangeUpgradeEntity extends CoreEntity implements UpgradeCoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE_BRICKS),
            entry(new Vec3i(1, 0, 1), Blocks.PRISMARINE_BRICKS)
    );

    public RangeUpgradeEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.RANGE_UPGRADE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    @Override
    public void applyUpgradeto(CoreEntity coreEntity) {
        coreEntity.setRange(coreEntity.getRange() * 2);
    }
}

