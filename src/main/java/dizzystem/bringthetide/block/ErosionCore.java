package dizzystem.bringthetide.block;

import dizzystem.bringthetide.block.tile.ErosionCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class ErosionCore extends Core {
    public ErosionCore() {
        super(Properties.of().strength(3.5F));
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new ErosionCoreEntity(blockPos, blockState);
    }
}
