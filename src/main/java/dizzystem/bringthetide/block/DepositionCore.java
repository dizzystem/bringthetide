package dizzystem.bringthetide.block;

import dizzystem.bringthetide.tile.DepositionCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class DepositionCore extends Core {
    public DepositionCore() {
        super(BlockBehaviour.Properties.of().strength(3.5F));
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new DepositionCoreEntity(blockPos, blockState);
    }

    @ParametersAreNonnullByDefault
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof DepositionCoreEntity be){
                if (lvl.isClientSide()){
                    be.tickClient();
                } else {
                    be.tickServer();
                }
            }
        };
    }
}
