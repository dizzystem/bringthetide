package dizzystem.bringthetide.mixin;

import com.google.common.collect.ImmutableSet;
import dizzystem.bringthetide.util.BlockEntityTypeAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashSet;
import java.util.Set;

//Are you serious. We have to use a mixin to add a custom sign?
@Mixin(BlockEntityType.class)
public abstract class MixinBlockEntityType<T extends BlockEntity> implements BlockEntityTypeAccessor {
    @Shadow @Mutable
    private Set<Block> validBlocks;

    @Override
    public void addTideBlock(Block block){
        HashSet<Block> blocks = new HashSet(this.validBlocks);
        blocks.add(block);
        this.validBlocks = ImmutableSet.copyOf(blocks); //Take that, "immutable" set.
    }
}
