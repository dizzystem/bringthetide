package dizzystem.bringthetide.util;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Map.entry;

public class MultiblockChecker {
    protected static List<Multiblock> multiblocks;

    static {
        BlockState prismarine = Blocks.PRISMARINE.defaultBlockState();
        BlockState oxidizedCopper = Blocks.OXIDIZED_COPPER.defaultBlockState();
        BlockState waxedOxidizedCopper = Blocks.WAXED_OXIDIZED_COPPER.defaultBlockState();
        BlockState sand = Blocks.SAND.defaultBlockState();

        multiblocks = new ArrayList<Multiblock>();

        Multiblock MULTI_BUDDING_PRISMARINE = new Multiblock(
                Map.ofEntries(
                        entry(new int[]{0, 0, 0}, List.of(prismarine, oxidizedCopper, waxedOxidizedCopper)),
                        entry(new int[]{1, 0, 0}, sand),
                        entry(new int[]{-1, 0, 0}, sand),
                        entry(new int[]{0, 1, 0}, sand),
                        entry(new int[]{0, -1, 0}, sand),
                        entry(new int[]{0, 0, 1}, sand),
                        entry(new int[]{0, 0, -1}, sand)
                ),
                (Level level, BlockPos centre) -> {
                    level.setBlockAndUpdate(centre, TideBlocks.BUDDING_PRISMARINE.get().defaultBlockState());
                    for (var direction : Direction.values()){
                        BlockPos side = centre.relative(direction);
                        level.setBlockAndUpdate(side, TideBlocks.PRISMARINE_CLUSTER.get()
                                .defaultBlockState().setValue(AmethystClusterBlock.FACING, direction));
                        for (int i=0;i<4;i++){
                            level.addParticle(TideParticles.WHIRLPOOL.get(),
                                    side.getX() + .5,
                                    side.getY() + 1.5,
                                    side.getZ() + .5,
                                    1,
                                    1,
                                    1);
                        }
                    }
                    return true;
                });
        multiblocks.add(MULTI_BUDDING_PRISMARINE);
/*
        Multiblock MULTI_DRIFTWOOD = new Multiblock(
                Map.ofEntries(
                        entry(new int[]{0, 0, 0}, imbued_seawater),
                        entry(new int[]{1, 0, 0}, any_log),
                        entry(new int[]{-1, 0, 0}, any_log),
                        entry(new int[]{0, 0, 1}, any_log),
                        entry(new int[]{0, 0, -1}, any_log)
                ),
                (Level level, BlockPos centre) -> {
                    for (var direction : Direction.values()){
                        BlockPos side = centre.relative(direction);
                        level.setBlockAndUpdate(side, Registration.IMBUED_SEAWATER.get()
                                .defaultBlockState());
                        if (!level.isClientSide()){
                            ((ServerLevel) level).sendParticles(
                                    ParticleTypes.FISHING,
                                    side.getX() + .5,
                                    side.getY() + .5,
                                    side.getZ() + .5,
                                    10,
                                    0,
                                    0,
                                    0,
                                    1);
                        }
                    }
                    return true;
                });
        multiblocks.add(MULTI_BUDDING_PRISMARINE);

 */
    }

    public static boolean assembleMultiblock(Level level, BlockPos pos){
        for (var multi : multiblocks){
            BlockPos centre = multi.check(level, pos);
            if (centre != null){
                multi.form(level, centre);
                return true;
            }
        }

        return false;
    }

    static class Multiblock {
        private Map<int[], Object> blocks;
        private BiFunction<Level, BlockPos, Boolean> formAction;

        public Multiblock(Map<int[], Object> blocks, BiFunction<Level, BlockPos, Boolean> formAction) {
            for (var block : blocks.entrySet()){
                if (!(block.getValue() instanceof List<?>) && !(block instanceof BlockState)){
                    //how do i throw an exception here?
                    //throw new Exception("Invalid block in multiblock");
                    LogUtils.getLogger().info("Invalid block in multiblock");
                }
            }
            this.blocks = blocks;
            this.formAction = formAction;
        }

        /**
         * Checks if the given blockstate matches the given template.
         *
         * @param real The blockstate to check.
         * @param template Either a List of blockstates or a blockstate.
         */
        private boolean compareBlockState(BlockState real, Object template){
            if (template instanceof List<?>){
                for (var blockState : (List<BlockState>) template){
                    if (real == blockState){
                        return true;
                    }
                }
                return false;
            }

            return real == (BlockState) template;
        }

        /**
         * Checks if this multiblock is present here in the world.
         *
         * @param pos The block position of a block that could be part of this multiblock.
         * @return The centre block position (0,0,0).
         */
        public BlockPos check(Level level, BlockPos pos){
            for (var startBlock : blocks.entrySet()){
                int[] startOffset = startBlock.getKey();
                if (!compareBlockState(level.getBlockState(pos), startBlock.getValue())){
                    continue;
                }

                boolean mismatch = false;

                for (var checkBlock : blocks.entrySet()){
                    int[] checkOffset = checkBlock.getKey().clone();
                    checkOffset[0] += startOffset[0];
                    checkOffset[1] += startOffset[1];
                    checkOffset[2] += startOffset[2];
                    if (!compareBlockState(level.getBlockState(pos.offset(checkOffset[0], checkOffset[1], checkOffset[2])),
                            checkBlock.getValue())){
                        mismatch = true;
                        break;
                    }
                }

                if (mismatch){
                    continue;
                }

                return pos.offset(startOffset[0], startOffset[1], startOffset[2]);
            }
            return null;
        }

        /**
         * Forms the multiblock, whatever that may mean.
         */
        public boolean form(Level level, BlockPos centre){
            return formAction.apply(level, centre);
        }
    }
}
