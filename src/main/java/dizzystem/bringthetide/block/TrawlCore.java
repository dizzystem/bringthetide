package dizzystem.bringthetide.block;

import dizzystem.bringthetide.tile.TrawlCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class TrawlCore extends Core {
    public TrawlCore() {
        super(BlockBehaviour.Properties.of().strength(3.5F));
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new TrawlCoreEntity(blockPos, blockState);
    }
}
