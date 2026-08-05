package dizzystem.bringthetide.block;

import dizzystem.bringthetide.block.tile.TankEntity;
import dizzystem.bringthetide.util.FluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tank extends HalfTransparentBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final List<List<Double>> TANK_AABB =
            List.of(
                    List.of(1d, 1d, 2d, 15d, 13d, 16d),
                    List.of(2d, 0d, 2d, 14d, 14d, 16d),
                    List.of(4d, 3d, 0d, 12d, 11d, 2d)
            );

    public Tank(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    private VoxelShape makeBoxShape(Direction facing, List<Double> aabb){
        return switch (facing) {
            case SOUTH -> Block.box(
                    16d - aabb.get(3), aabb.get(1), 16d - aabb.get(5),
                    16d - aabb.get(0), aabb.get(4), 16d - aabb.get(2)
            );
            case WEST -> Block.box(aabb.get(2), aabb.get(1), aabb.get(0), aabb.get(5), aabb.get(4), aabb.get(3));
            case EAST -> Block.box(
                    16d - aabb.get(5), aabb.get(1), 16d - aabb.get(3),
                    16d - aabb.get(2), aabb.get(4), 16d - aabb.get(0)
            );
            default -> Block.box(aabb.get(0), aabb.get(1), aabb.get(2), aabb.get(3), aabb.get(4), aabb.get(5));
        };
    }

    private VoxelShape makeTankShape(Direction facing){
        return Shapes.or(
                makeBoxShape(facing, TANK_AABB.get(0)),
                makeBoxShape(facing, TANK_AABB.get(1)),
                makeBoxShape(facing, TANK_AABB.get(2))
        );
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
                               CollisionContext collisionContext){
        return makeTankShape(blockState.getValue(FACING));
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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING);
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new TankEntity(blockPos, blockState);
    }
}
