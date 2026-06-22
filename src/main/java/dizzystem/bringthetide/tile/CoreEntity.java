package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

import java.util.*;
import java.util.stream.Collectors;

public abstract class CoreEntity extends BlockEntity implements IForgeBlockEntity {
    public boolean poolFormed = false, poolFilled = false, checkPool = false;
    public int ticks = 0, fillQuota = MAX_FILL_QUOTA, maxCraftingTimer = 0, craftingTimer = 0;
    public ItemEntity craftingEntity;
    public ArrayList<Vec3i> missingBlocks = new ArrayList<Vec3i>();
    public HashSet<BlockPos> poolBlocks = new HashSet<BlockPos>(),
            poolFluids = new HashSet<BlockPos>();
    public Map<BlockPos, BlockState[]> missingBlocksAllowed = new HashMap<>();

    public static int MAX_FILL_QUOTA = 4;

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

    //This is used for both saving and updating clients with our data.
    protected void saveClientData(CompoundTag tag) {
        tag.putLongArray("poolBlocks", this.poolBlocks.stream().
                map(BlockPos::asLong).collect(Collectors.toList()));
        tag.putLongArray("poolFluids", this.poolFluids.stream().
                map(BlockPos::asLong).collect(Collectors.toList()));
        tag.putInt("maxCraftingTimer", this.maxCraftingTimer);
        tag.putInt("craftingTimer", this.craftingTimer);
        if (this.craftingEntity != null){
            tag.putInt("craftingEntity", this.craftingEntity.getId());
        }
        tag.putInt("fillQuota", this.fillQuota);
        tag.putBoolean("poolFormed", this.poolFormed);
        tag.putBoolean("poolFilled", this.poolFilled);
    }

    //This is used for both saving and updating clients with our data.
    protected void loadClientData(CompoundTag tag) {
        this.poolBlocks = Arrays.stream(tag.getLongArray("poolBlocks")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(HashSet::new));
        this.poolFluids = Arrays.stream(tag.getLongArray("poolFluids")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(HashSet::new));
        this.maxCraftingTimer = tag.getInt("maxCraftingTimer");
        this.craftingTimer = tag.getInt("craftingTimer");
        int entityId = tag.getInt("craftingEntity");
        if (entityId != 0){
            this.craftingEntity = (ItemEntity) level.getEntity(tag.getInt("craftingEntity"));
        }
        this.fillQuota = tag.getInt("fillQuota");
        this.poolFormed = tag.getBoolean("poolFormed");
        this.poolFilled = tag.getBoolean("poolFilled");
        if (poolFormed){
            PoolHandler.registerCore(getLevel(), getBlockPos());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveClientData(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadClientData(tag);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            loadClientData(tag);
        }
    }

    //updates the client side with our data
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveClientData(tag);
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

        if (tag != null){
            handleUpdateTag(tag);
        }
    }

    //This prevents our block render entity from not rendering when it's offscreen, since sometimes you can see
    //  the rest of the pool without seeing the core.
    @Override
    public AABB getRenderBoundingBox(){
        return IForgeBlockEntity.INFINITE_EXTENT_AABB;
    }

    public void setCraftingTimer(int craftingTimer) {
        this.craftingTimer = craftingTimer;
    }

    public int getCraftingTimer() {
        return craftingTimer;
    }

    public void setMaxCraftingTimer(int maxCraftingTimer) {
        this.maxCraftingTimer = maxCraftingTimer;
    }

    public int getMaxCraftingTimer() {
        return maxCraftingTimer;
    }

    public void setCraftingEntity(ItemEntity craftingEntity) {
        this.craftingEntity = craftingEntity;
    }

    public ItemEntity getCraftingEntity(){
        return this.craftingEntity;
    }

    public void clearImbuedWater(){
        for (var fluidPos : this.poolFluids){
            if (level.getBlockState(fluidPos).is(TideBlocks.BLOCK_IMBUED_SEAWATER.get())){
                level.setBlockAndUpdate(fluidPos, Blocks.WATER.defaultBlockState());
                ((ServerLevel) level).sendParticles(TideParticles.BUBBLE.get(),
                        fluidPos.getX() + 0.5,
                        fluidPos.getY() + 1,
                        fluidPos.getZ() + 0.5,
                        5,
                        0,
                        0,
                        0,
                        0.1);
            }
        }
    }

    public void tickClient(){
        if (this.maxCraftingTimer > 0) {
            this.craftingTimer--;
        }

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
    }

    public void tickServer(){
        if (this.maxCraftingTimer > 0){
            this.craftingTimer --;
            PoolHandler.endCrafts(this.craftingEntity);
        }

        if (this.checkPool){
            checkPool = false;
            //check if the pool's filled
            Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            BlockPos insidePool = getBlockPos().relative(facing);
            HashSet<BlockPos> poolBlocks = new HashSet<>(), poolFluids = new HashSet<>();
            if (PoolHandler.verifyPoolFilled(level, insidePool, poolBlocks, poolFluids)){
                this.poolBlocks = poolBlocks;
                this.poolFluids = poolFluids;
                this.poolFormed = true;
                this.poolFilled = true;
            } else if (fillQuota <= 0){ //check if we have any quota left
                //if not turn all our seawater back into water
                clearImbuedWater();
                this.fillQuota = MAX_FILL_QUOTA;
            }
        }

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
                this.poolFluids.clear();
                this.poolFormed = false;
                this.poolFilled = false;
                level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
                return;
            }

            //pool is a closed shape
            Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            BlockPos insidePool = pos.relative(facing);
            HashSet<BlockPos> poolBlocks = new HashSet<>(),
                    poolFluids = new HashSet<>();
            if (PoolHandler.verifyEmptyPool(level, pos, poolBlocks, poolFluids)){
                this.poolBlocks = poolBlocks;
                this.poolFluids = poolFluids;
                this.poolFormed = true;
                PoolHandler.registerCore(level, pos);
                level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
            } else {
                this.poolBlocks.clear();
            }
        } else {
            //check if our pool is still formed
            boolean formed = true;
            for (var blockPos : this.poolBlocks){
                if (!level.getBlockState(blockPos).is(TideTags.VALID_POOL_BLOCK)){
                    formed = false;
                    break;
                }

                if (level.getBlockEntity(blockPos) instanceof CoreEntity coreEntity){
                    ArrayList<Vec3i> missing = coreEntity.checkRequiredShape();
                    if (!missing.isEmpty()){
                        formed = false;
                        break;
                    }
                }
            }
            if (!formed){
                clearImbuedWater();
                this.fillQuota = MAX_FILL_QUOTA;
                this.poolBlocks.clear();
                this.poolFluids.clear();
                this.poolFormed = false;
                this.poolFilled = false;
                PoolHandler.deleteCore(level, pos);
                level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
            }
        }

        if (this.poolFilled){
            //check if our pool is still filled
            boolean filled = true;
            for (var blockPos : this.poolFluids){
                if (!level.getBlockState(blockPos).is(TideBlocks.BLOCK_IMBUED_SEAWATER.get())){
                    filled = false;
                    break;
                }
            }
            if (!filled){
                clearImbuedWater();
                this.fillQuota = MAX_FILL_QUOTA;
                this.poolFilled = false;
            }
        }

        level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
    }

    //override to make core do stuff when an entity falls into it
    public void entityInPool(Entity entity, Level level, BlockPos pos){}

    //override to make core do crafting
    public void beginCraft(ItemEntity entity, ArrayList<BlockPos> cores){}

    //override to make core do crafting
    public void endCraft(ItemEntity entity, ArrayList<BlockPos> cores, Recipe<?> recipe){}

    //pool filling minigame
    public void schedulePoolCheck(){
        this.checkPool = true;
    }

    public boolean placeFluid(Level level, BlockPos pos){
        if (!this.poolFormed){
            return false;
        }
        if (!this.poolFluids.contains(pos)){
            return false;
        }

        level.addParticle(TideParticles.BUBBLE.get(),
                pos.getX() + .5,
                pos.getY() + 1,
                pos.getZ() + .5,
                1,
                1,
                1);

        fillQuota --;

        schedulePoolCheck();
        return true;
    }
}