package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

import java.util.*;
import java.util.stream.Collectors;

public abstract class CoreEntity extends BlockEntity implements IForgeBlockEntity {
    public boolean poolFormed = false;
    public int ticks = 0;
    public boolean emittingParticles = false;
    public ArrayList<Vec3i> missingBlocks = new ArrayList<Vec3i>();
    public HashSet<BlockPos> poolBlocks = new HashSet<BlockPos>(),
            poolFluids = new HashSet<BlockPos>();
    public Map<BlockPos, BlockState[]> missingBlocksAllowed = new HashMap<BlockPos, BlockState[]>();

    public CoreEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }

    public Map<BlockPos, BlockState[]> getMissingBlocksAllowed(){ return missingBlocksAllowed; }
    public ArrayList<Vec3i> getMissingBlocks(){ return missingBlocks; }
    public HashSet<BlockPos> getPoolBlocks(){ return poolBlocks; }
    public HashSet<BlockPos> getPoolFluids(){ return poolFluids; }

    /**
     * The required additional blocks around the core to make it function as part of a ritual.
     *
     * @return a map of Vec3i offsets to the specifications of the block at that offset - this can
     *  be a block, a HolderSet of blocks, or a block tag - when the core is facing north.
     */
    public abstract Map<Vec3i, Object> getRequiredShape();

    public Vec3i rotateOffsetToFacingDirection(Vec3i offset){
        BlockState blockState = getBlockState();
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

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
        return offset;
    }

    /**
     * Checks if this ritual core has all its required blocks.
     *
     * @return the missing blocks as offsets of a north facing core, empty arraylist if none
     */
    public ArrayList<Vec3i> checkRequiredShape(){
        Level level = this.level;
        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = getBlockState();
        ArrayList<Vec3i> missing = new ArrayList<Vec3i>();

        for (Map.Entry<Vec3i, Object> entry : this.getRequiredShape().entrySet()){
            Vec3i offset = entry.getKey();
            Object required = entry.getValue();

            //rotate the offset based on the way we're facing
            Vec3i rotatedOffset = rotateOffsetToFacingDirection(offset);

            if (!PoolHandler.matchBlock(level.getBlockState(blockPos.offset(rotatedOffset)), required)){
                missing.add(offset);
            }
        }

        return missing;
    }

    /**
     * Returns the array of blocks that would match our required block at the given offset.
     *
     * @param offset the offset, as if this core were facing north
     */
    public BlockState[] possibleMatches(Vec3i offset){
        Object requirement = this.getRequiredShape().get(offset);

        return PoolHandler.allBlocksMatching(requirement);
    }

    //updates the client side with our data
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("particleWhirlpooling", this.emittingParticles);
        tag.putLongArray("poolBlocks", this.poolBlocks.stream().
                map(BlockPos::asLong).collect(Collectors.toList()));
        tag.putLongArray("poolFluids", this.poolFluids.stream().
                map(BlockPos::asLong).collect(Collectors.toList()));
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    //is updated by the server side with our data
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();

        this.emittingParticles = tag.getBoolean("particleWhirlpooling");
        this.poolBlocks = Arrays.stream(tag.getLongArray("poolBlocks")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(HashSet::new));
        this.poolFluids = Arrays.stream(tag.getLongArray("poolFluids")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(HashSet::new));
    }

    //This prevents our block render entity from not rendering when it's offscreen, since sometimes you can see
    //  the rest of the pool without seeing the core.
    @Override
    public AABB getRenderBoundingBox(){
        return IForgeBlockEntity.INFINITE_EXTENT_AABB;
    }

    public void tickClient(){
        //do the missing block check clientside as well so our renderer can render the missing blocks
        if (this.ticks++ % 20 == 0){
            Level level = this.level;
            BlockPos pos = this.getBlockPos();
            BlockState blockState = this.getBlockState();

            this.missingBlocks = checkRequiredShape();
            this.missingBlocksAllowed.clear();
            for (var entry : this.missingBlocks){
                this.missingBlocksAllowed.put(
                        pos.offset(rotateOffsetToFacingDirection(entry)), possibleMatches(entry));
            }
        }

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

            //required blocks
            ArrayList<Vec3i> missing = checkRequiredShape();
            if (!missing.isEmpty()){
                this.missingBlocks = missing;
                this.poolBlocks.clear();
                level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
                return;
            }

            //pool is a closed shape
            Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            BlockPos insidePool = pos.relative(facing);
            HashSet<BlockPos> poolBlocks = new HashSet<BlockPos>(),
                    poolFluids = new HashSet<BlockPos>();
            if (PoolHandler.verifyEmptyPool(level, pos, poolBlocks, poolFluids)){
                this.poolBlocks = poolBlocks;

                //todo: add pool filling minigame
                this.poolFluids = poolFluids;
                PoolHandler.registerNewCore(level, pos);
            } else {
                this.poolBlocks.clear();
            }
        } else {
            //todo: check if our pool is still formed

        }

        level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
    }

    //override to make core do stuff when an entity falls into it
    public void entityInPool(Entity entity, Level level, BlockPos pos){}
}