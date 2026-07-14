package dizzystem.bringthetide.block;

import dizzystem.bringthetide.block.tile.VortexCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class VortexCore extends Core {
    UUID placedBy;

    public VortexCore() {
        super(BlockBehaviour.Properties.of().strength(3.5F));
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new VortexCoreEntity(blockPos, blockState, this.placedBy);
    }

    public UUID getPlacedBy(){
        return this.placedBy;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setPlacedBy(Level level, BlockPos blockpos, BlockState blockstate, LivingEntity entity, ItemStack itemstack){
        if (!(entity instanceof Player player)){
            return;
        }

        this.placedBy = player.getUUID();
    }
}
