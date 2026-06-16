package dizzystem.bringthetide.util;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideFluids;
import dizzystem.bringthetide.tile.CoreEntity;
import net.minecraft.core.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jline.utils.Log;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PoolHandler {
    static HashSet<PoolCore> cores = new HashSet<PoolCore>();

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
     * @param poolBlocks If an array is supplied, the pool edge blocks will be added to it for use in the calling function.
     * @param poolFluids If an array is supplied, the pool interior blocks will be added to it for use in the calling function.
     */
    private static boolean horizontalFlood(Level level, BlockPos pos, Predicate<BlockState> condition,
                                           HashSet<BlockPos> poolBlocks, HashSet<BlockPos> poolFluids){
        Map<BlockPos, Boolean> validityMap = new HashMap<BlockPos, Boolean>();
        ArrayList<BlockPos> toCheck = new ArrayList<BlockPos>();
        BlockPos currentBlock = pos;

        if (poolBlocks == null){ poolBlocks = new HashSet<BlockPos>(); }
        if (poolFluids == null){ poolFluids = new HashSet<BlockPos>(); }

        //Iterate through the adjacent blocks, stopping when we find valid pool blocks.
        for (int i=0;i<1000;i++){
            //Max pool size 256 blocks (internal).
            if (i >= 256){
                return false;
            }

            BlockState currentBlockState = level.getBlockState(currentBlock);

            if (isValidPoolBlock(currentBlockState)) { //pool edge, stop here
                poolBlocks.add(currentBlock);
                validityMap.put(currentBlock, true);
            } else if (condition.test(currentBlockState)) { //pool fluid, keep checking
                poolFluids.add(currentBlock);
                validityMap.put(currentBlock, true);

                for (Direction direction1 : Direction.Plane.HORIZONTAL){
                    Direction[] diagonalMoves = new Direction[]{ direction1, direction1.getClockWise() };
                    for (Direction direction2 : diagonalMoves){
                        BlockPos adjBlock;

                        if (direction1 == direction2){
                            adjBlock = currentBlock.relative(direction1);
                        } else {
                            adjBlock = currentBlock.relative(direction1).relative(direction2);
                        }

                        if (validityMap.get(adjBlock) == null) {
                            toCheck.add(adjBlock);
                        }
                    }
                }
            } else {
                return false;
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
    public static Boolean verifyPoolFilled(Level level, BlockPos pos, HashSet<BlockPos> poolBlocks,
                                           HashSet<BlockPos> poolFluids){
        return horizontalFlood(level, pos, PoolHandler::isValidPoolFluid, poolBlocks, poolFluids);
    }

    /**
     * Checks if this ritual core is part of a valid empty pool (edges composed of valid pool blocks, the rest filled in
     *  with air).
     *
     * @param level the dimension
     * @param pos a ritual core block (may not be the primary)
     * @param poolBlocks If an array is supplied, the pool edge blocks will be added to it for use in the calling function.
     * @param poolFluids If an array is supplied, the pool interior blocks will be added to it for use in the calling function.
     */
    public static boolean verifyEmptyPool(Level level, BlockPos pos, HashSet<BlockPos> poolBlocks,
                                          HashSet<BlockPos> poolFluids) {
        ArrayList<BlockPos> possiblePools = new ArrayList<BlockPos>();
        BlockPos pool;

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
        //We use new blank arrays to avoid adding to our return arrays until we're sure which pool is the real one.
        HashSet<BlockPos> poolBlocks1 = new HashSet<BlockPos>(), poolBlocks2 = new HashSet<BlockPos>(),
                poolFluids1 = new HashSet<BlockPos>(), poolFluids2 = new HashSet<BlockPos>();
        if (horizontalFlood(level, possiblePools.get(0), replaceableByPoolFluid, poolBlocks1, poolFluids1)){
            poolBlocks.addAll(poolBlocks1);
            poolFluids.addAll(poolFluids1);
        } else if (horizontalFlood(level, possiblePools.get(1), replaceableByPoolFluid, poolBlocks2, poolFluids2)) {
            poolBlocks.addAll(poolBlocks2);
            poolFluids.addAll(poolFluids2);
        } else {
            return false;
        }

        //Finally, check the cores to see if they have their required blocks.
        for (var block : poolBlocks){
            BlockEntity coreEntity = level.getBlockEntity(block);
            if (coreEntity instanceof CoreEntity){ //this also covers if the block has no tile entity
                ArrayList<Vec3i> missing = ((CoreEntity) coreEntity).checkRequiredShape();
                if (!missing.isEmpty()){
                    //for (var m : missing){
                    //    LogUtils.getLogger().info("missing {}", m);
                    //}
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Adds the given ritual core to the pool map so that it can start doing its thing.
     *
     * @param pos The BlockPos of the core.
     */
    public static void registerNewCore(Level level, BlockPos pos){
        cores.add(new PoolCore(level, pos));
    }

    /**
     * Called when an entity enters an imbued seawater block, which may or may not be part of a pool.
     *
     * @param pos The BlockPos of the core.
     */
    public static void entityInPool(Entity entity, Level level, BlockPos pos){
        for (PoolCore core : cores){
            if (level != core.level()){
                continue;
            }

            BlockEntity blockEntity = level.getBlockEntity(core.corePos());
            if (blockEntity instanceof CoreEntity){
                HashSet<BlockPos> poolFluids = ((CoreEntity) blockEntity).getPoolFluids();
                if (poolFluids.contains(pos)){
                    ((CoreEntity) blockEntity).entityInPool(entity, level, pos);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean matchBlock(BlockState blockState, Object requirement){
        if (requirement instanceof Block){
            return blockState.is((Block) requirement);
        } else if (requirement instanceof HolderSet<?>){
            return blockState.is((HolderSet<Block>) requirement);
        } else if (requirement instanceof TagKey<?>){
            return blockState.is((TagKey<Block>) requirement);
        }

        return false;
    }

    public static BlockState[] allBlocksMatching(Object requirement){
        if (requirement instanceof Block){
            return new BlockState[]{ ((Block) requirement).defaultBlockState() };
        } else if (requirement instanceof HolderSet<?>){
            return ((HolderSet<Block>) requirement).stream().map(Holder::value)
                    .map(Block::defaultBlockState).toArray(BlockState[]::new);
        } else if (requirement instanceof TagKey<?>){
            return ForgeRegistries.BLOCKS.tags().getTag((TagKey<Block>) requirement).stream()
                    .map(Block::defaultBlockState).toArray(BlockState[]::new);
        }

        return new BlockState[]{};
    }
}
