package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public class SeabedCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-1, 0, 0), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(1, 0, 0), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(0, 0, -3), Blocks.CUT_SANDSTONE)
    );

    public SeabedCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.SEABED_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when a block enters our pool
    @Override
    public void blocksInPool(ServerLevel level, Set<BlockPos> blocks, Vec3 origin){
        for (BlockPos block : blocks){
            this.blockInPool(level, block, origin);
        }
    }

    private void blockInPool(ServerLevel level, BlockPos block, Vec3 origin){
        BlockState blockState = level.getBlockState(block);
        if (blockState.isAir() || !blockState.getFluidState().isEmpty()){
            //is air or a fluid
            return;
        }

        //soft harvest

        //first check if it's a plant
        if (!(blockState.getBlock() instanceof IPlantable plant)){
            return;
        }

        //then, check if breaking it would drop a seed that can be used to replant it
        List<ItemStack> drops = Block.getDrops(blockState, level, block, level.getBlockEntity(block));
        ItemStack seed = null;
        BlockItem seedType = null;

        for (ItemStack drop : drops){
            if (drop.getItem() instanceof BlockItem blockDrop && blockDrop.getBlock() == blockState.getBlock()){
                seed = drop;
                seedType = blockDrop;
                break;
            }
        }
        if (seed == null){
            return;
        }

        //finally, actually break and replant it
        level.removeBlock(block, false);
        seedType.place(new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, seed,
                new BlockHitResult(origin, Direction.UP, block, false)));
        seed.shrink(1);

        Vec3 poolCentre = this.getPoolCentre().getCenter();
        for (ItemStack drop : drops){
            ItemEntity entity = new ItemEntity(level, poolCentre.x, poolCentre.y, poolCentre.z, drop);
            level.addFreshEntity(entity);
        }
    }
}
