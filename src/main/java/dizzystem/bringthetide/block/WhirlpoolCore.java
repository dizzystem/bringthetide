package dizzystem.bringthetide.block;

import dizzystem.bringthetide.block.tile.ReefCoreEntity;
import dizzystem.bringthetide.block.tile.WhirlpoolCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class WhirlpoolCore extends Core {
    public WhirlpoolCore() {
        super(BlockBehaviour.Properties.of().strength(3.5F));
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new WhirlpoolCoreEntity(blockPos, blockState);
    }
}
