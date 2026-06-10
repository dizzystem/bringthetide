package dizzystem.bringthetide.block;

import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class PrismarineClusterBlock extends AmethystClusterBlock {
    public PrismarineClusterBlock(int i, int j, Properties properties){
        super(i, j, properties);
    }

    public PushReaction getPistonPushReaction(BlockState state){
        return PushReaction.DESTROY;
    }
}
