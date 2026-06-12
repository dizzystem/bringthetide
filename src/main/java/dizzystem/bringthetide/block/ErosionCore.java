package dizzystem.bringthetide.block;

import dizzystem.bringthetide.tile.ErosionCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

    @ParametersAreNonnullByDefault
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof ErosionCoreEntity be){
                if (lvl.isClientSide()){
                    be.tickClient();
                } else {
                    be.tickServer();
                }
            }
        };
    }
}
