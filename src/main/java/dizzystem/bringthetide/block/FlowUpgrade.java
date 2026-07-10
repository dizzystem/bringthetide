package dizzystem.bringthetide.block;

import dizzystem.bringthetide.tile.FlowUpgradeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class FlowUpgrade extends Core {
    public FlowUpgrade() {
        super(BlockBehaviour.Properties.of().strength(3.5F));
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new FlowUpgradeEntity(blockPos, blockState);
    }
}
