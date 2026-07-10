package dizzystem.bringthetide.tile;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    public boolean poolFormed = false, poolFilled = false, scheduleCheckPool = false, scheduleRegisterCore = false;
    public int ticks = 0, maxCraftingTimer = 0, craftingTimer = 0;
    public float thalassity = 1f, renderThalassity = 0f, speed = 1f, luck = 1f, range = 1f;
    public ItemEntity craftingEntity;
    public ArrayList<Vec3i> missingBlocks = new ArrayList<>();
    public ArrayList<BlockPos> poolCores = new ArrayList<>();
    public HashSet<BlockPos> poolBlocks = new HashSet<>(), poolFluids = new HashSet<>();
    public BlockPos poolCentre = getBlockPos();
    public Map<BlockPos, BlockState[]> missingBlocksAllowed = new HashMap<>();
    public Map<BlockPos, Integer> renderOverlayData = new HashMap<>();

    public CoreEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }

    public Map<BlockPos, BlockState[]> getMissingBlocksAllowed(){ return missingBlocksAllowed; }
    public ArrayList<Vec3i> getMissingBlocks(){ return missingBlocks; }
    public ArrayList<BlockPos> getPoolCores(){ return poolCores; }
    public HashSet<BlockPos> getPoolBlocks(){ return poolBlocks; }
    public HashSet<BlockPos> getPoolFluids(){ return poolFluids; }
    public BlockPos getPoolCentre(){ return poolCentre; }
    public boolean isPoolActive(){ return poolFilled; }
    public Map<BlockPos, Integer> getRenderOverlayData(){ return renderOverlayData; }

    public void setSpeed(float speed){ this.speed = speed; }
    public void setLuck(float luck){ this.luck = luck; }
    public void setRange(float range){ this.range = range; }
    public float getSpeed(){ return speed; }
    public int getTicksPerAction(){ return (int) (80 / speed); }
    public float getLuck() { return luck; }
    public float getRange() { return range; }

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
                offset = new Vec3i(-offset.getX(), offset.getY(), -offset.getZ());
                break;
            case WEST:
                offset = new Vec3i(offset.getZ(), offset.getY(), -offset.getX());
                break;
            case EAST:
                offset = new Vec3i(-offset.getZ(), offset.getY(), offset.getX());
                break;
        }
        return offset;
    }

    /**
     * Returns the block positions of the required shape of this core in its current place and rotation.
     */
    public HashSet<BlockPos> getRequiredBlockPositions(){
        BlockPos blockPos = this.getBlockPos();
        HashSet<BlockPos> blocks = new HashSet<>();

        for (Map.Entry<Vec3i, Object> entry : this.getRequiredShape().entrySet()) {
            Vec3i offset = entry.getKey();

            //rotate the offset based on the way we're facing
            Vec3i rotatedOffset = rotateOffsetToFacingDirection(offset);

            blocks.add(blockPos.offset(rotatedOffset));
        }
        return blocks;
    }

    /**
     * Checks if this ritual core has all its required blocks.
     *
     * @return the missing blocks as offsets of a north facing core, empty arraylist if none
     */
    public ArrayList<Vec3i> checkRequiredShape(){
        Level level = this.level;
        BlockPos blockPos = this.getBlockPos();
        ArrayList<Vec3i> missing = new ArrayList<>();

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
        tag.putLongArray("poolCores", this.poolCores.stream().
                map(BlockPos::asLong).collect(Collectors.toList()));
        tag.putLongArray("poolBlocks", this.poolBlocks.stream().
                map(BlockPos::asLong).collect(Collectors.toList()));
        tag.putLongArray("poolFluids", this.poolFluids.stream().
                map(BlockPos::asLong).collect(Collectors.toList()));
        tag.putLong("poolCentre", this.poolCentre.asLong());
        tag.putInt("maxCraftingTimer", this.maxCraftingTimer);
        tag.putInt("craftingTimer", this.craftingTimer);
        if (this.craftingEntity != null){
            tag.putInt("craftingEntity", this.craftingEntity.getId());
        } else {
            tag.putInt("craftingEntity", -1);
        }
        tag.putFloat("fillQuota", this.thalassity);
        tag.putBoolean("poolFormed", this.poolFormed);
        tag.putBoolean("poolFilled", this.poolFilled);
    }

    //This is used for both saving and updating clients with our data.
    protected void loadClientData(CompoundTag tag) {
        this.poolCores = Arrays.stream(tag.getLongArray("poolCores")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(ArrayList::new));
        this.poolBlocks = Arrays.stream(tag.getLongArray("poolBlocks")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(HashSet::new));
        this.poolFluids = Arrays.stream(tag.getLongArray("poolFluids")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(HashSet::new));
        this.poolCentre = BlockPos.of(tag.getLong("poolCentre"));
        this.maxCraftingTimer = tag.getInt("maxCraftingTimer");
        this.craftingTimer = tag.getInt("craftingTimer");
        int entityId = tag.getInt("craftingEntity");
        if (entityId == -1) {
            this.craftingEntity = null;
        } else {
            this.craftingEntity = (ItemEntity) level.getEntity(entityId);
        }
        this.thalassity = tag.getInt("fillQuota");
        this.poolFormed = tag.getBoolean("poolFormed");
        this.poolFilled = tag.getBoolean("poolFilled");
    }

    //saving
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveClientData(tag);
    }

    //loading
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadClientData(tag);

        this.scheduleRegisterCore();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            loadClientData(tag);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveClientData(tag);
        return tag;
    }

    //syncs to client
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    //is synced from server
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
        return this.craftingTimer;
    }

    public void setMaxCraftingTimer(int maxCraftingTimer) {
        this.maxCraftingTimer = maxCraftingTimer;
    }

    public int getMaxCraftingTimer() {
        return this.maxCraftingTimer;
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

    public void scheduleRegisterCore(){
        this.scheduleRegisterCore = true;
    }

    public void clearPoolData(Level level, BlockPos pos) {
        this.poolCores.clear();
        this.poolBlocks.clear();
        this.poolFluids.clear();
        this.renderOverlayData.clear();
        this.poolFormed = false;
        this.poolFilled = false;
        PoolHandler.deleteCore(level, pos);
    }

    public void makeSparkles() {
        Level level = getLevel();
        BlockPos[] poolFluids = this.poolFluids.toArray(new BlockPos[this.poolFluids.size()]);
        RandomSource random = level.getRandom();

        for (int i=0;i<4;i++){
            if (random.nextInt(256) < this.poolFluids.size()){
                BlockPos poolBlock = poolFluids[random.nextInt(0, poolFluids.length)];
                level.addParticle(TideParticles.SPARKLE.get(),
                        poolBlock.getX() + Math.random(),
                        poolBlock.getY() + 1,
                        poolBlock.getZ() + Math.random(),
                        0,
                        0,
                        0);
            }
        }
    }

    public void tickClient(){
        if (this.maxCraftingTimer > 0) {
            this.craftingTimer--;
        }

        if (!this.poolFormed){
            this.renderThalassity = 0f;
        } else if (this.renderThalassity != this.thalassity){
            this.renderThalassity += 0.1f * (this.thalassity - this.renderThalassity);
            if (Math.abs(this.thalassity - this.renderThalassity) < 0.02){
                this.renderThalassity = this.thalassity;
            }
        }

        if (this.poolFilled && this.getBlockPos().equals(this.poolCores.get(0))){
            makeSparkles();
        }

        if (this.scheduleRegisterCore){
            this.scheduleRegisterCore = false;

            if (this.poolFormed){
                PoolHandler.registerCore(getLevel(), getBlockPos());
            }
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

    public void resetQuota() {
        this.thalassity = 1f;
    }

    public void tickServer(){
        if (this.maxCraftingTimer > 0){
            this.craftingTimer --;
            PoolHandler.endCrafts(this.craftingEntity);
        }

        if (this.scheduleCheckPool){
            this.scheduleCheckPool = false;
            //check if the pool's filled
            Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            BlockPos insidePool = getBlockPos().relative(facing);
            HashSet<BlockPos> poolBlocks = new HashSet<>(), poolFluids = new HashSet<>();
            if (PoolHandler.verifyPoolFilled(level, insidePool, poolBlocks, poolFluids)){
                this.poolBlocks = poolBlocks;
                this.poolFluids = poolFluids;
                this.poolFormed = true;
                this.poolFilled = true;
            } else if (this.thalassity <= 0.00001){ //check if we have any thalassity left
                //if not turn all our seawater back into water
                clearImbuedWater();
                resetQuota();
            }
        }

        if (this.scheduleRegisterCore){
            this.scheduleRegisterCore = false;

            if (this.poolFormed){
                PoolHandler.registerCore(getLevel(), getBlockPos());
            }
        }

        //once per second
        if (this.ticks++ % 20 != 0){
            return;
        }

        Level level = this.level;
        BlockPos pos = this.getBlockPos();
        BlockState blockState = this.getBlockState();

        //check if our pool's formed yet
        if (!this.poolFormed){
            //required blocks
            ArrayList<Vec3i> missing = checkRequiredShape();
            if (!missing.isEmpty()){
                this.missingBlocks = missing;
                clearPoolData(level, pos);
                level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
                return;
            }

            //pool is a closed shape
            HashSet<BlockPos> poolBlocks = new HashSet<>(),
                    poolFluids = new HashSet<>();
            if (PoolHandler.verifyEmptyPool(level, pos, poolBlocks, poolFluids)){
                //yup this is an empty pool
                ArrayList<BlockPos> poolCores = poolBlocks.stream().filter(
                        blockPos -> level.getBlockEntity(blockPos) instanceof CoreEntity
                ).collect(Collectors.toCollection(ArrayList::new));
                this.poolCores = poolCores;
                this.poolBlocks = poolBlocks;
                this.poolFluids = poolFluids;

                //apply upgrades
                this.speed = 1;
                this.luck = 1;
                this.range = 1;
                for (var core : poolCores){
                    if (level.getBlockEntity(core) instanceof UpgradeCoreEntity upgradeCore){
                        upgradeCore.applyUpgradeto(this);
                    }
                }

                float totalX = 0f, totalZ = 0f;
                for (var fluidPos : poolFluids){
                    totalX += fluidPos.getX();
                    totalZ += fluidPos.getZ();
                }
                this.poolCentre = new BlockPos(
                        Math.round(totalX / poolFluids.size()),
                        pos.getY(),
                        Math.round(totalZ / poolFluids.size()));

                //tell the other cores we already checked to save them from checking again
                this.poolCores.forEach(core -> {
                    CoreEntity coreEntity = (CoreEntity) level.getBlockEntity(core);

                    coreEntity.poolCores = this.poolCores;
                    coreEntity.poolBlocks = this.poolBlocks;
                    coreEntity.poolFluids = this.poolFluids;
                    coreEntity.poolCentre = this.poolCentre;
                    coreEntity.poolFormed = true;
                    PoolHandler.registerCore(level, core);

                    coreEntity.setSpeed(this.getSpeed());
                    coreEntity.setLuck(this.getLuck());
                    coreEntity.setRange(this.getRange());

                    BlockState state = level.getBlockState(core);
                    level.sendBlockUpdated(core, state, state, Block.UPDATE_CLIENTS);
                });
            } else {
                this.poolBlocks.clear();
            }
        } else { //check if our pool is still formed
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
                resetQuota();
                clearPoolData(level, pos);
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
                resetQuota();
                clearPoolData(level, pos);
                level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
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

    /* ===pool filling minigame=== */
    public void schedulePoolCheck(){
        this.scheduleCheckPool = true;
    }

    public boolean placeFluid(Player player, ItemStack wand, Level level, BlockPos pos){
        if (!this.poolFormed){
            return false;
        }
        if (!this.poolFluids.contains(pos)){
            return false;
        }

        thalassity -= PoolHandler.getThalassityCost(player, wand, level);

        schedulePoolCheck();
        return true;
    }
    /* ===end pool filling minigame=== */
}