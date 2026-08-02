package dizzystem.bringthetide.block;

import dizzystem.bringthetide.block.tile.TankEntity;
import dizzystem.bringthetide.util.FluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

public class Tank extends HalfTransparentBlock implements EntityBlock {
    protected static final VoxelShape TANK_SHAPE =
            Shapes.or(
                    Block.box(1d, 0d, 2d, 15d, 14d, 16d),
                    Block.box(4d, 3d, 0d, 12d, 11d, 16d)
            );

    public Tank(BlockBehaviour.Properties props) {
        super(props);
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new TankEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
                               CollisionContext collisionContext){
        return TANK_SHAPE;
    }

    @ParametersAreNonnullByDefault
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult blockHitResult){
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null){
            return InteractionResult.PASS;
        }

        //right clicking fluids in or out
        boolean rightClickFluid = FluidHandler.tryRightClickFluidIntoTank(player, hand, be);
        if (rightClickFluid){
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

}
