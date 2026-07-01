package dizzystem.bringthetide.util;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideFluids;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.tile.CoreEntity;
import net.minecraft.core.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PoolHandler {
    static HashSet<PoolCore> cores = new HashSet<>();
    static Map<ItemEntity, ArrayList<CraftingAttempt>> craftingAttempts = new HashMap<>();
    static Map<ItemEntity, OngoingCraft> ongoingCrafts = new HashMap<>();
    static Map<Entity, Integer> entityCooldowns = new HashMap<>();

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
        Map<BlockPos, Boolean> validityMap = new HashMap<>();
        ArrayList<BlockPos> toCheck = new ArrayList<>();
        BlockPos currentBlock = pos;

        if (poolBlocks == null){ poolBlocks = new HashSet<>(); }
        if (poolFluids == null){ poolFluids = new HashSet<>(); }

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
        Direction facing = level.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos poolBlock =  pos.relative(facing);

        //Check that the pool is enclosed and contains no blocks that can't be washed away by seawater.
        Predicate<BlockState> replaceableByPoolFluid =
                blockState -> blockState.canBeReplaced(TideFluids.IMBUED_SEAWATER.get());
        if (!horizontalFlood(level, poolBlock, replaceableByPoolFluid, poolBlocks, poolFluids)){
            return false;
        }

        //Finally, check the cores to see if they have their required blocks.
        for (var block : poolBlocks){
            if (level.getBlockEntity(block) instanceof CoreEntity coreEntity){ //this also covers if the block has no tile entity
                ArrayList<Vec3i> missing = coreEntity.checkRequiredShape();
                if (!missing.isEmpty()){
                    return false;
                }

                //also check that they're actually part of the pool
                HashSet<BlockPos> reqs = coreEntity.getRequiredBlockPositions();
                for (var req : reqs){
                    if (!poolBlocks.contains(req)){
                        return false;
                    }
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
    public static void registerCore(Level level, BlockPos pos){
        cores.add(new PoolCore(level, pos));
    }

    public static void deleteCore(Level level, BlockPos pos){
        cores.remove(new PoolCore(level, pos));
    }

    /**
     * Called when an entity enters an imbued seawater block, which may or may not be part of a pool.
     *
     * @param pos The BlockPos of the core.
     */
    public static void entityInPool(Entity entity, Level level, BlockPos pos){
        //only check the same entity once every 10 ticks
        Integer cooldown = entityCooldowns.get(entity);
        if (cooldown != null && cooldown > 0){
            entityCooldowns.put(entity, cooldown-1);
            return;
        }
        entityCooldowns.put(entity, 10);

        for (PoolCore core : cores){
            if (level != core.level()){
                continue;
            }

            BlockEntity blockEntity = level.getBlockEntity(core.corePos());
            if (blockEntity instanceof CoreEntity coreEntity){
                if (!coreEntity.poolFilled){
                    continue;
                }
                HashSet<BlockPos> poolFluids = coreEntity.getPoolFluids();
                if (poolFluids.contains(pos)){
                    ((CoreEntity) blockEntity).entityInPool(entity, level, pos);
                }
            }
        }

        beginCrafts();
    }

    /**
     * Called from cores that do crafting recipes. This collects the cores that are attempting to craft with
     *  this ItemEntity into a Map so we can do crafting with them in one go.
     */
    public static void attemptCraft(ItemEntity entity, Level level, BlockPos pos, RecipeType<?> recipeType) {
        if (ongoingCrafts.get(entity) != null){
            //we're already crafting with this
            return;
        }

        if (!craftingAttempts.containsKey(entity)) {
            craftingAttempts.put(entity, new ArrayList<>());
        }
        craftingAttempts.get(entity).add(new CraftingAttempt(pos, recipeType));
    }

    public static void beginCrafts(){
        for (var entry : craftingAttempts.entrySet()){
            ItemEntity entity = entry.getKey();
            ArrayList<CraftingAttempt> attempts = entry.getValue();

            Level level = entity.level();

            //Send the collected crafting attempts back to one of the cores that have the same recipeType so it
            // can figure out the recipe.
            while (!attempts.isEmpty()){
                CraftingAttempt attempt = attempts.get(0);
                ArrayList<BlockPos> cores = attempts.stream()
                        .filter(a -> a.recipeType() == attempt.recipeType())
                        .map(CraftingAttempt::corePos)
                        .collect(Collectors.toCollection(ArrayList::new));
                if (level.getBlockEntity(attempt.corePos()) instanceof CoreEntity core){
                    try {
                        core.beginCraft(entity, cores);
                    } catch (Exception e) {
                        LogUtils.getLogger().error("Error when attempting to craft item at {}", attempt.corePos(), e);
                    }
                    attempts = attempts.stream()
                            .filter(a -> a.recipeType() != attempt.recipeType())
                            .collect(Collectors.toCollection(ArrayList::new));
                } else {
                    attempts.remove(0);
                }
            }
        }
        craftingAttempts.clear();
    }

    public static void addOngoingCraft(ItemEntity entity, OngoingCraft craft){
        ongoingCrafts.put(entity, craft);
    }

    public static void endCrafts(ItemEntity entity){
        if (entity == null || entity.isRemoved() || entity.getItem().isEmpty()){
            //item entity was picked up or otherwise destroyed
            ongoingCrafts.remove(entity);
            return;
        }

        OngoingCraft ongoing = ongoingCrafts.get(entity);
        Level level = entity.level();
        ArrayList<BlockPos> cores = ongoing.corePosses();

        int timeLeft = 0;
        boolean invalid = false;
        for (var blockPos : cores){
            if (!(level.getBlockEntity(blockPos) instanceof CoreEntity coreEntity)){
                invalid = true;
                break;
            }
            timeLeft += coreEntity.getCraftingTimer();
        }
        if (invalid){
            //one of the cores involved was broken
            ongoingCrafts.remove(entity);
            return;
        }

        //not done cooking yet
        if (timeLeft > 0){
            return;
        }

        try {
            ((CoreEntity) level.getBlockEntity(cores.get(0))).endCraft(entity, cores, ongoing.recipe());
        } catch (Exception e) {
            LogUtils.getLogger().error("Error when attempting to craft item at {}", cores.get(0), e);
        }
        ongoingCrafts.remove(entity);
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
        if (requirement instanceof Block blockRequirement){
            return new BlockState[]{ blockRequirement.defaultBlockState() };
        } else if (requirement instanceof HolderSet<?> setRequirement){
            return ((HolderSet<Block>) setRequirement).stream().map(Holder::value)
                    .map(Block::defaultBlockState).toArray(BlockState[]::new);
        } else if (requirement instanceof TagKey<?> tagRequirement){
            return ForgeRegistries.BLOCKS.tags().getTag((TagKey<Block>) tagRequirement).stream()
                    .map(Block::defaultBlockState).toArray(BlockState[]::new);
        }

        return new BlockState[]{};
    }

    //pool filling minigame
    public static boolean wandUse(Player player, ItemStack wand, Level level, BlockPos pos){
        BlockState turnInto = null;
        boolean inPool = false;

        //we already checked it's a fluid and a source block in the wand
        if (level.dimensionType().ultraWarm()){ //lava in the nether instead of water
            if (level.getBlockState(pos).is(Blocks.LAVA)){
                turnInto = TideBlocks.BLOCK_IMBUED_SEAWATER.get().defaultBlockState();
            } else {
                return false;
            }
        } else {
            if (level.getBlockState(pos).is(Blocks.WATER)){
                turnInto = TideBlocks.BLOCK_IMBUED_SEAWATER.get().defaultBlockState();
            } else {
                return false;
            }
        }

        for (PoolCore core : cores){
            if (level != core.level()){
                continue;
            }

            BlockEntity blockEntity = level.getBlockEntity(core.corePos());
            if (blockEntity instanceof CoreEntity coreEntity){
                if (coreEntity.placeFluid(player, wand, level, pos)){
                    inPool = true;
                }
            }
        }

        if (inPool){
            level.setBlockAndUpdate(pos, turnInto);
            for (int i=0;i<4;i++){
                level.addParticle(TideParticles.BUBBLE.get(),
                        pos.getX() + Math.random(),
                        pos.getY() + 1,
                        pos.getZ() + Math.random(),
                        0,
                        0,
                        0);
            }
            return true;
        }

        return false;
    }
}
