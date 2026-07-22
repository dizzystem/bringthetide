package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class RangeUpgradeEntity extends CoreEntity implements UpgradeCoreEntity {
    private final List<Block> VALID_BLOCKS = List.of(Blocks.CUT_SANDSTONE, Blocks.PRISMARINE);
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), VALID_BLOCKS),
            entry(new Vec3i(1, 0, 1), VALID_BLOCKS)
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

