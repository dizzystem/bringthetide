package dizzystem.bringthetide.block;

import dizzystem.bringthetide.block.properties.PillarDirection;
import dizzystem.bringthetide.block.properties.TideBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class ShapedPillar extends Block {
    public static final EnumProperty<PillarDirection> PILLAR_DIRECTION = TideBlockStateProperties.PILLAR_DIRECTION;

    protected static final VoxelShape MIDDLE_SHAPE =
            Block.box(2d, 0d, 2d, 14d, 16d, 14d);
    protected static final VoxelShape BASE_SHAPE =
            Shapes.or(MIDDLE_SHAPE,
                    Block.box(0d, 0d, 0d, 16d, 2d, 16d));
    protected static final VoxelShape TOP_SHAPE =
            Shapes.or(MIDDLE_SHAPE,
                    Block.box(0d, 14d, 0d, 16d, 16d, 16d));

    public ShapedPillar(BlockBehaviour.Properties props){
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(PILLAR_DIRECTION, PillarDirection.MIDDLE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(PILLAR_DIRECTION);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
                               CollisionContext collisionContext){
        return switch (blockState.getValue(PILLAR_DIRECTION)) {
            default -> MIDDLE_SHAPE;
            case TOP -> TOP_SHAPE;
            case BASE -> BASE_SHAPE;
        };
    }

    //Returns whether we should be a top pillar, a middle pillar or a base pillar.
    private BlockState getState(LevelAccessor level, BlockPos pos){
        boolean pillarAbove = level.getBlockState(pos.relative(Direction.UP)).is(this);
        boolean pillarBelow = level.getBlockState(pos.relative(Direction.DOWN)).is(this);
        if ((pillarAbove && pillarBelow) || (!pillarAbove && !pillarBelow)){
            return this.defaultBlockState().setValue(PILLAR_DIRECTION, PillarDirection.MIDDLE);
        }
        if (pillarAbove){
            return this.defaultBlockState().setValue(PILLAR_DIRECTION, PillarDirection.BASE);
        }
        return this.defaultBlockState().setValue(PILLAR_DIRECTION, PillarDirection.TOP);
    }

    @Override
    //This is called when we're placed.
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return getState(level, pos);
    }

    @Override
    @ParametersAreNonnullByDefault
    //This is called when we're updated.
    public @NotNull BlockState updateShape(BlockState blockState, Direction side, BlockState otherBlockState,
                                           LevelAccessor levelAccessor, BlockPos blockPos, BlockPos otherBlockPos) {
        return getState(levelAccessor, blockPos);
    }
}
