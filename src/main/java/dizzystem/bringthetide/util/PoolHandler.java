package dizzystem.bringthetide.util;

import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.recipe.DepositionRecipe;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideFluids;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.registration.TideRecipes;
import dizzystem.bringthetide.tile.CoreEntity;
import dizzystem.bringthetide.tile.DepositionCoreEntity;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PoolHandler {
    static HashSet<PoolCore> cores = new HashSet<>();
    static Map<ItemEntity, ArrayList<CraftingAttempt>> craftingAttempts = new HashMap<>();
    static Map<ItemEntity, CraftingOngoing> craftingOngoings = new HashMap<>();

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

        beginCrafts();
    }

    /**
     * Called from cores that do crafting recipes. This collects the cores that are attempting to craft with
     *  this ItemEntity into a Map so we can do crafting with them in one go.
     */
    public static void attemptCraft(ItemEntity entity, Level level, BlockPos pos, RecipeType<?> recipeType) {
        if (!craftingAttempts.containsKey(entity)) {
            craftingAttempts.put(entity, new ArrayList<>());
        }
        craftingAttempts.get(entity).add(new CraftingAttempt(pos, recipeType));
    }

    public static void beginCrafts(){
        //todo: add cooldown for how often we check the same entity
        for (var entry : craftingAttempts.entrySet()){
            ItemEntity entity = entry.getKey();
            ArrayList<CraftingAttempt> attempts = entry.getValue();

            Level level = entity.level();

            //do the deposition ones first
            ArrayList<BlockPos> depositionCores = attempts.stream()
                    .filter(attempt -> attempt.recipeType() == TideRecipes.DEPOSITION.get() &&
                            level.getBlockEntity(attempt.corePos()) instanceof DepositionCoreEntity core)
                    .map(CraftingAttempt::corePos)
                    .collect(Collectors.toCollection(ArrayList::new));

            ItemStack mainIngredient = entity.getItem();
            ItemStack[] depositionCatalysts = depositionCores.stream()
                    .map(pos -> ((DepositionCoreEntity) level.getBlockEntity(pos)).getItemStack())
                    .filter(itemStack -> itemStack != null && !itemStack.isEmpty())
                    .toArray(ItemStack[]::new);

            //maybe we should put this code in the crafting core
            IItemHandlerModifiable inputs = new ItemStackHandler(depositionCatalysts.length + 1);
            inputs.setStackInSlot(0, mainIngredient);
            for (int i=0;i<depositionCatalysts.length;i++){
                inputs.setStackInSlot(i+1, depositionCatalysts[i]);
            }
            RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

            Optional<DepositionRecipe> maybeRecipe =
                    level.getRecipeManager().getRecipeFor(
                            TideRecipes.DEPOSITION.get(),
                            inputWrapper,
                            level);
            if (!maybeRecipe.isPresent()) {
                continue;
            }

            craftingOngoings.put(entity, new CraftingOngoing(depositionCores, maybeRecipe.get()));
        }

    }

    public void endCrafts(){
        for (var entry : craftingOngoings.entrySet()){
            ItemEntity entity = entry.getKey();
            CraftingOngoing ongoing = entry.getValue();

            if (entity == null || entity.getItem().isEmpty()){
                //item entity was picked up or otherwise destroyed
                craftingOngoings.remove(entity);
                continue;
            }
            Level level = entity.level();

            int timeLeft = 0;
            boolean invalid = false;
            for (var blockPos : ongoing.corePosses()){
                if (!(level.getBlockEntity(blockPos) instanceof CoreEntity coreEntity)){
                    invalid = true;
                    break;
                }
                timeLeft += coreEntity.getCraftingTimer();
            }
            if (invalid){
                //one of the cores involved was broken
                craftingOngoings.remove(entity);
                continue;
            }

            if (timeLeft > 0){
                continue;
            }

            ItemStack mainIngredient = entity.getItem();
            ItemStack[] depositionCatalysts = ongoing.corePosses().stream()
                    .map(pos -> ((DepositionCoreEntity) level.getBlockEntity(pos)).getItemStack())
                    .filter(itemStack -> itemStack != null && !itemStack.isEmpty())
                    .toArray(ItemStack[]::new);

            IItemHandlerModifiable inputs = new ItemStackHandler(depositionCatalysts.length + 1);
            inputs.setStackInSlot(0, mainIngredient);
            for (int i=0;i<depositionCatalysts.length;i++){
                inputs.setStackInSlot(i+1, depositionCatalysts[i]);
            }
            RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

            Recipe<?> recipe = ongoing.recipe();
            ItemStack output;
            if (recipe.getType() == TideRecipes.DEPOSITION.get()){
                output = ((DepositionRecipe) recipe).assemble(inputWrapper, level.registryAccess());
            } else {
                continue;
            }

            mainIngredient.split(1);
            ItemEntity outputEntity = new ItemEntity(level, pos.getX()+0.5,
                    this.getBlockPos().getY() + 1, pos.getZ()+0.5, output);
            outputEntity.setDeltaMovement(0, 0, 0);
            outputEntity.setNoGravity(true);
            outputEntity.setPickUpDelay(20);
            level.addFreshEntity(outputEntity);

            ((ServerLevel) level).sendParticles(TideParticles.SPLASH.get(),
                    pos.getX() + .5,
                    pos.getY() + 1.5,
                    pos.getZ() + .5,
                    10,
                    0,
                    0,
                    0,
                    0.25);
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
