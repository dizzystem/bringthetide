package dizzystem.bringthetide.block;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;

public class BuddingPrismarine extends Block {
    public int GROWTH_CHANCE = 5;
    private static final Direction[] DIRECTIONS = Direction.values();

    public BuddingPrismarine(){
        super(Properties.of()
                .strength(3.5F));
    }

    public PushReaction getPistonPushReaction(BlockState state){
        return PushReaction.DESTROY;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random){
        if (random.nextInt(GROWTH_CHANCE) != 0){
            return;
        }

        Direction direction = Util.getRandom(DIRECTIONS, random);
        BlockPos targetPos = pos.relative(direction);
        BlockState targetState = level.getBlockState(targetPos);
        Block newCluster = null;

        if (canClusterGrowAtState(targetState)){
            newCluster = TideBlocks.SMALL_PRISMARINE_BUD.get();
        } else if (targetState.is(TideBlocks.SMALL_PRISMARINE_BUD.get()) && targetState.getValue(AmethystClusterBlock.FACING) == direction){
            newCluster = TideBlocks.MEDIUM_PRISMARINE_BUD.get();
        } else if (targetState.is(TideBlocks.MEDIUM_PRISMARINE_BUD.get()) && targetState.getValue(AmethystClusterBlock.FACING) == direction){
            newCluster = TideBlocks.LARGE_PRISMARINE_BUD.get();
        } else if (targetState.is(TideBlocks.LARGE_PRISMARINE_BUD.get()) && targetState.getValue(AmethystClusterBlock.FACING) == direction){
            newCluster = TideBlocks.PRISMARINE_CLUSTER.get();
        }

        if (newCluster != null){
            BlockState newClusterState = newCluster.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, Boolean.valueOf(targetState.getFluidState().getType() == Fluids.WATER));
            level.setBlockAndUpdate(targetPos, newClusterState);
        }
    }

    public static boolean canClusterGrowAtState(BlockState state){
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }
}
