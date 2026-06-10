package dizzystem.bringthetide.util;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class PoolHandler {
    static Map<BlockPos, BlockPos> poolBlocksToCore = new HashMap<>();

    private static short getCacheKey(BlockPos origin, BlockPos blockPos) {
        int i = blockPos.getX() - origin.getX();
        int j = blockPos.getZ() - origin.getZ();
        return (short)((i + 128 & 255) << 8 | j + 128 & 255);
    }

    private static boolean isValidPoolBlock(BlockState blockState){
        return blockState.is(TideTags.VALID_POOL_BLOCK);
    }

    private static boolean isValidPoolFluid(BlockState blockState){
        return blockState.is(TideBlocks.BLOCK_IMBUED_SEAWATER.get());
    }

    /**
     * Floods through the blocks inside a pool checking if they meet the given condition. If any does not,
     *  returns false, otherwise true.
     *
     * @param pos The starting block.
     * @param condition A condition that takes a BlockState and returns true or false.
     */
    private static boolean horizontalFlood(Level level, BlockPos pos, Predicate<BlockState> condition){
        Map<BlockPos, Boolean> validityMap = new HashMap<BlockPos, Boolean>();
        ArrayList<BlockPos> toCheck = new ArrayList<BlockPos>();
        BlockPos currentBlock = pos;

        //Iterate through the adjacent blocks, stopping when we find valid pool blocks.
        for (int i=0;i<1000;i++){
            //Max pool size 256 blocks (internal).
            if (i >= 256){
                return false;
            }

            for (Direction direction1 : Direction.Plane.HORIZONTAL){
                Direction[] diagonalMoves = new Direction[]{ direction1, direction1.getClockWise() };
                for (Direction direction2 : diagonalMoves){
                    BlockPos adjBlock;

                    if (direction1 == direction2){
                        adjBlock = currentBlock.relative(direction1);
                    } else {
                        adjBlock = currentBlock.relative(direction1).relative(direction2);
                    }

                    if (validityMap.get(adjBlock) != null) {
                        continue;
                    }

                    BlockState adjBlockState = level.getBlockState(adjBlock);

                    if (isValidPoolBlock(adjBlockState)) {
                        validityMap.put(pos, true);
                    } else if (condition.test(adjBlockState)) {
                        validityMap.put(pos, true);
                        toCheck.add(pos);
                    } else {
                        return false;
                    }
                }
            }

            if (!toCheck.isEmpty()){
                currentBlock = toCheck.remove(toCheck.size()-1);
            } else {
                break;
            }
        }

        return true;

    }

    /**
     * Checks whether the pool is now filled (all spaces enclosed by the pool filled by valid pool fluid
     *  source blocks).
     *
     * @param pos A valid pool fluid blockpos in the pool.
     */
    public static Boolean verifyPoolFilled(Level level, BlockPos pos){
        return horizontalFlood(level, pos, PoolHandler::isValidPoolFluid);
    }

    /**
     * Checks if this ritual core is part of a valid empty pool (edges composed of valid pool blocks, the rest filled in
     *  with air).
     *
     * @param level the dimension
     * @param pos a ritual core block (may not be the primary)
     */
    public static boolean verifyEmptyPool(Level level, BlockPos pos) {
        ArrayList<BlockPos> possiblePools = new ArrayList<BlockPos>();

        //Any line of blocks has two sides, so we have to figure out which are the two and try both.
        ArrayList<Direction> adjBlockDirs = new ArrayList<Direction>();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos adjBlock = pos.relative(direction);
            BlockState adjBlockState = level.getBlockState(adjBlock);
            if (isValidPoolBlock(adjBlockState)) {
                adjBlockDirs.add(direction);
            }
        }
        if (adjBlockDirs.size() != 2) {
            return false;
        }
        //Either they're opposite from each other (a straight line) or at a right angle.
        if (adjBlockDirs.get(0) == adjBlockDirs.get(1).getOpposite()) {
            //In a straight line we should expect the pool to be on the two remaining sides.
            for (var adjBlockDir : adjBlockDirs) {
                Direction clockwise = adjBlockDir.getClockWise();
                possiblePools.add(pos.relative(clockwise));
            }
        } else {
            //In a right angle we should expect the pool to be in the diagonal between the two blocks
            //  and its opposite direction.
            BlockPos diagonal1 = pos, diagonal2 = pos;
            for (var adjBlockDir : adjBlockDirs) {
                diagonal1 = pos.relative(adjBlockDir);
                diagonal2 = pos.relative(adjBlockDir.getOpposite());
            }
            possiblePools.add(diagonal1);
            possiblePools.add(diagonal2);
        }

        Predicate<BlockState> replaceableByPoolFluid =
                blockState -> blockState.canBeReplaced(TideFluids.IMBUED_SEAWATER.get());
        if (horizontalFlood(level, possiblePools.get(0), replaceableByPoolFluid)){
            return true;
        } else if (horizontalFlood(level, possiblePools.get(1), replaceableByPoolFluid)){
            return true;
        }

        return false;
    }

    /**
     * Adds the given pool to the pool map so that it can start doing its thing.
     *
     * @param pos The primary core of the pool (first core rotating clockwise from due north).
     */
    public static void registerNewPool(BlockPos pos){

        //poolBlocksToCore.put(pos);

        new Pool(new BlockPos[]{}, new BlockPos[]{}, new BlockPos[]{});
    }

    public static void entityInPool(ItemEntity entity, BlockPos pos){
        ItemStack item = entity.getItem();

        if (item.is(ItemTags.LOGS)){
            //ItemStack result = new ItemStack()
        }
    }
}
