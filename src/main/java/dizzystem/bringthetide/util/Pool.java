package dizzystem.bringthetide.util;

import net.minecraft.core.BlockPos;

public class Pool {
    BlockPos[] cores;
    BlockPos[] poolBlocks;
    BlockPos[] poolFluids;

    public Pool(BlockPos[] cores, BlockPos[] poolBlocks, BlockPos[] poolFluids){
        this.cores = cores;
        this.poolBlocks = poolBlocks;
        this.poolFluids = poolFluids;
    }
}
