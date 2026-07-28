package dizzystem.bringthetide.fluid;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.registration.TideFluids;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

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
            PoolHandler.entityInPool(entity, level, pos.immutable(), null);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(blockState, level, blockPos, oldState, movedByPiston);

        if (blockState.getFluidState().isSource()){
            PoolHandler.schedulePoolCheck(level, blockPos);
        }
    }

    //Can't be bucketed.
    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack pickupBlock(LevelAccessor p_153772_, BlockPos p_153773_, BlockState p_153774_) {
        return ItemStack.EMPTY;
    }
}