package dizzystem.bringthetide.block;

import dizzystem.bringthetide.util.ExplosionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;

public class ExplosionRod extends RodBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ExplosionRod(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, ctx.getClickedFace()).setValue(WATERLOGGED, Boolean.valueOf(flag));
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockState updateShape(BlockState blockState, Direction face, BlockState otherBlockState,
                                  LevelAccessor levelAccessor, BlockPos blockPos, BlockPos otherBlockPos) {
        if (blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        return super.updateShape(blockState, face, otherBlockState, levelAccessor, blockPos, otherBlockPos);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, WATERLOGGED);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (!level.isClientSide()){
            //Let the handler know that there's an explosion rod here.
            ExplosionHandler.registerExplosionRod((ServerLevel) level, pos);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onRemove(BlockState oldState, Level level, BlockPos blockPos, BlockState newState, boolean pistonMoved){
        if (!newState.is(oldState.getBlock())){
            if (!level.isClientSide()){
                //Let the handler know that there's no longer an explosion rod here.
                ExplosionHandler.deregisterExplosionRod((ServerLevel) level, blockPos);
            }
        }

        super.onRemove(oldState, level, blockPos, newState, pistonMoved);
    }
}
