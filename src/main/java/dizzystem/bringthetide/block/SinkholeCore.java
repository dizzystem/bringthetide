package dizzystem.bringthetide.block;

import dizzystem.bringthetide.tile.SinkholeCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class SinkholeCore extends Core {
    public SinkholeCore() {
        super(Properties.of().strength(3.5F));
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new SinkholeCoreEntity(blockPos, blockState);
    }
}
