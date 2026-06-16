package dizzystem.bringthetide.fluid;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.registration.TideFluids;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockImbuedSeawater extends LiquidBlock {
    public BlockImbuedSeawater(){
        super(TideFluids.IMBUED_SEAWATER, BlockBehaviour.Properties.copy(Blocks.WATER));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity){
        if (level.isClientSide()){
            return;
        }
        if (blockState.getFluidState().isSource()){
            PoolHandler.entityInPool(entity, level, pos);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(blockState, level, blockPos, oldState, movedByPiston);
        if (blockState.getFluidState().isSource()){
            LogUtils.getLogger().info("Seawater placed at {}", blockPos);
            if (PoolHandler.verifyPoolFilled(level, blockPos, null, null)){
                LogUtils.getLogger().info("Valid pool");
            } else {
                LogUtils.getLogger().info("Invalid pool");
            }
        }
    }
}