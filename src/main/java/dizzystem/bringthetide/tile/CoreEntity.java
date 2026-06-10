package dizzystem.bringthetide.tile;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CoreEntity extends BlockEntity implements IForgeBlockEntity {
    public boolean poolFormed = false;
    public int ticks = 0;
    public boolean particleWhirlpooling = false;
    public ArrayList<BlockPos> poolBlocks, poolFluids;

    public CoreEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("particleWhirlpooling", this.particleWhirlpooling);
        tag.putLongArray("poolBlocks", this.poolBlocks.stream().
                map(BlockPos::asLong).collect(Collectors.toList()));
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();

        this.particleWhirlpooling = tag.getBoolean("particleWhirlpooling");
        this.poolBlocks = Arrays.stream(tag.getLongArray("poolBlocks")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void tickClient(){
        if (this.particleWhirlpooling && this.ticks++ % 10 == 0){
            Level level = this.level;
            BlockPos pos = this.getBlockPos();

            level.addParticle(TideParticles.BUBBLE.get(),
                    pos.getX() + .5,
                    pos.getY() + 1.5,
                    pos.getZ() + .5,
                    0,
                    0,
                    0);
            for (var blockPos : this.poolBlocks){
                level.addParticle(TideParticles.BUBBLE.get(),
                        blockPos.getX() + .5,
                        blockPos.getY() + 1.5,
                        blockPos.getZ() + .5,
                        0,
                        0,
                        0);
            }
        }
    }

    public void tickServer(){
        //once per second
        if (this.ticks++ % 20 != 0){
            return;
        }

        Level level = this.level;
        BlockPos pos = this.getBlockPos();
        BlockState blockState = this.getBlockState();

        if (!this.poolFormed){
            //check if our pool's formed yet
            ArrayList<BlockPos> poolBlocks = new ArrayList<BlockPos>(),
                    poolFluids = new ArrayList<BlockPos>();
            if (PoolHandler.verifyEmptyPool(level, pos, poolBlocks, poolFluids)){
                this.particleWhirlpooling = true;
                this.poolBlocks = poolBlocks;
            } else {
                this.particleWhirlpooling = false;
            }
        }

        level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
    }
}