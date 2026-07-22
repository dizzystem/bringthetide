package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class FlowUpgradeEntity extends CoreEntity implements UpgradeCoreEntity {
    private final List<Block> VALID_BLOCKS = List.of(Blocks.CUT_SANDSTONE, Blocks.PRISMARINE);
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-1, 0, 0), VALID_BLOCKS),
            entry(new Vec3i(1, 0, 0), VALID_BLOCKS)
    );

    public FlowUpgradeEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.FLOW_UPGRADE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    @Override
    public void applyUpgradeto(CoreEntity coreEntity) {
        coreEntity.setSpeed(coreEntity.getSpeed() * 2);
    }
}
