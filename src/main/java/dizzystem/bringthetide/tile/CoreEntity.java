package dizzystem.bringthetide.tile;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CoreEntity extends BlockEntity {
    public boolean poolFormed = false;
    public int ticks = 0;

    public CoreEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }

    public void tickServer(){
        //once per second
        if (ticks++ % 20 != 0){
            return;
        }

        if (!this.poolFormed){
            Level level = this.level;
            BlockPos pos = this.getBlockPos();
            //check if our pool's formed yet
            if (PoolHandler.verifyEmptyPool(level, pos)){
                for (int i=0;i<4;i++){
                    ((ServerLevel) level).addParticle(TideParticles.WHIRLPOOL.get(),
                            pos.getX() + .5,
                            pos.getY() + 1.5,
                            pos.getZ() + .5,
                            4,
                            2,
                            1);
                }
            }
        }
    }
}