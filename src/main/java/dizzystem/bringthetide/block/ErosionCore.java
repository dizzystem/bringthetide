package dizzystem.bringthetide.block;

import dizzystem.bringthetide.tile.ErosionCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ErosionCore extends Block implements EntityBlock {
    public ErosionCore() {
        super(Properties.of().strength(3.5F));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new ErosionCoreEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
        if (level.isClientSide()){
            return null;
        }
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof ErosionCoreEntity be){
                be.tickServer();
            }
        };
    }
}
