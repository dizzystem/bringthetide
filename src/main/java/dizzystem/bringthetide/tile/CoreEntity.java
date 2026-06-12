package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CoreEntity extends BlockEntity implements IForgeBlockEntity {
    public boolean poolFormed = false;
    public int ticks = 0;
    public boolean emittingParticles = false;
    public ArrayList<BlockPos> poolBlocks = new ArrayList<BlockPos>(), poolFluids = new ArrayList<BlockPos>();

    public CoreEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }

    public ArrayList<BlockPos> getPoolBlocks(){ return poolBlocks; }
    public ArrayList<BlockPos> getPoolFluids(){ return poolFluids; }

    /**
     * The required additional blocks around the core to make it function as part of a ritual.
     *
     * @return a map of Vec3i offsets to the specifications of the block at that offset - this can
     *  be a block, a HolderSet of blocks, or a block tag - when the core is facing north.
     */
    public abstract Map<Vec3i, Object> getRequiredShape();

    @SuppressWarnings("unchecked")
    public boolean matchBlock(BlockState blockState, Object requirement){
        if (requirement instanceof Block){
            return blockState.is((Block) requirement);
        } else if (requirement instanceof HolderSet<?>){
            return blockState.is((HolderSet<Block>) requirement);
        } else if (requirement instanceof TagKey<?>){
            return blockState.is((TagKey<Block>) requirement);
        }

        return false;
    }

    /**
     * Checks if this ritual core has all its required blocks.
     *
     * @return the missing blocks, empty arraylist if none
     */
    public ArrayList<Vec3i> checkRequiredShape(){
        Level level = this.level;
        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = getBlockState();
        ArrayList<Vec3i> missing = new ArrayList<Vec3i>();
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        for (Map.Entry<Vec3i, Object> entry : this.getRequiredShape().entrySet()){
            Vec3i offset = entry.getKey();
            Object required = entry.getValue();

            //rotate the offset based on the way we're facing
            switch (facing){
                case NORTH:
                    break;
                case SOUTH:
                    offset = new Vec3i(offset.getX(), offset.getY(), -offset.getZ());
                    break;
                case WEST:
                    offset = new Vec3i(offset.getZ(), offset.getY(), offset.getX());
                    break;
                case EAST:
                    offset = new Vec3i(-offset.getZ(), offset.getY(), offset.getX());
                    break;
            }

            if (!matchBlock(level.getBlockState(blockPos.offset(offset)), required)){
                missing.add(offset);
            }
        }

        return missing;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("particleWhirlpooling", this.emittingParticles);
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

        this.emittingParticles = tag.getBoolean("particleWhirlpooling");
        this.poolBlocks = Arrays.stream(tag.getLongArray("poolBlocks")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //This prevents our block render entity from not rendering when it's offscreen, since sometimes you can see
    //  the rest of the pool without seeing the core.
    @Override
    public AABB getRenderBoundingBox(){
        return IForgeBlockEntity.INFINITE_EXTENT_AABB;
    }

    public void tickClient(){
        if (this.emittingParticles && this.ticks++ % 10 == 0){
            Level level = this.level;
            BlockPos pos = this.getBlockPos();
/*
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
            }*/
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
                this.emittingParticles = true;
                this.poolBlocks = poolBlocks;
            } else {
                this.emittingParticles = false;
            }
        }

        level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
    }
}